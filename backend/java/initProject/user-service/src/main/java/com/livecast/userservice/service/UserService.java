package com.livecast.userservice.service;

import com.livecast.common.integrations.S3Client;
import com.livecast.userservice.dto.UserDTO;
import com.livecast.userservice.entity.User;
import com.livecast.userservice.entity.enums.UserType;
import com.livecast.common.exception.CustomException;
import com.livecast.userservice.integrations.CognitoService;
import com.livecast.userservice.mapper.UserMapper;
import com.livecast.userservice.repository.FollowRepository;
import com.livecast.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.livecast.common.util.AttachmentsUtils.updatePresignUrl;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CognitoService cognitoService;
    private final FollowRepository followRepository;
    private final S3Client s3Client;

    @Autowired
    public UserService(UserRepository userRepository,
                       CognitoService cognitoService,
                       FollowRepository repository,
                       S3Client s3Client) {
        this.userRepository = userRepository;
        this.cognitoService = cognitoService;
        this.followRepository = repository;
        this.s3Client = s3Client;
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, CustomException.NOT_FOUND));

        cognitoService.adminDeleteUser(user.getUsername());
        s3Client.delete(S3Client.S3SubPaths.USER_PROFILE_PICTURES_SUB_PATH, user.getPicture().getId());
        followRepository.deleteAllByUserId(userId);
        followRepository.deleteAllByFollowerId(userId);
        userRepository.deleteById(userId);
    }

    public void upgradeToCreator(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, CustomException.NOT_FOUND));

        cognitoService.addUserToGroup(user.getUsername(), UserType.CREATOR.name());
        user.setType(UserType.CREATOR);
        userRepository.save(user);
    }

    public void downgradeToRegular(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, CustomException.NOT_FOUND));

        cognitoService.removeUserFromGroup(user.getUsername(), UserType.CREATOR.name());
        user.setType(UserType.REGULAR);
        userRepository.save(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.findOneByUsername(userDTO.getUsername()).isPresent()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, CustomException.ALREADY_EXIST);
        }
        User user = UserMapper.INSTANCE.toEntity(userDTO);
        user.setId(null);
        user.setType(UserType.REGULAR);
        return UserMapper.INSTANCE.toDTO(updatePresignUrl(s3Client, userRepository.save(user)));
    }

    public UserDTO updateUser(UserDTO userDTO, Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND, CustomException.NOT_FOUND));
        UserMapper.INSTANCE.update(user, userDTO);
        deleteAttachment(userDTO, user);
        return UserMapper.INSTANCE.toDTO(userRepository.save(user));
    }

    public UserDTO updateCurrentUser(UserDTO userDTO, String username) {
        User user = userRepository.findOneByUsername(username).orElseThrow(() ->
                new CustomException(HttpStatus.BAD_REQUEST, CustomException.CURRENT_USER_DOES_NOT_EXIST));
        deleteAttachment(userDTO, user);
        UserMapper.INSTANCE.update(user, userDTO);
        return UserMapper.INSTANCE.toDTO(updatePresignUrl(s3Client, userRepository.save(user)));
    }

    private void deleteAttachment(UserDTO userDTO, User user) {
        if (nonNull(user.getPicture()) && isNull(userDTO.getPicture())) {
            s3Client.delete(S3Client.S3SubPaths.USER_PROFILE_PICTURES_SUB_PATH, user.getPicture().getId());
        }
        if (nonNull(user.getPicture()) && nonNull(userDTO.getPicture()) &&
                !user.getPicture().getId().equals(userDTO.getPicture().getId())) {
            s3Client.delete(S3Client.S3SubPaths.USER_PROFILE_PICTURES_SUB_PATH, user.getPicture().getId());
        }
    }

    public Long getIdByUsername(String username) {
        return userRepository.findOneByUsername(username)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST,
                        CustomException.CURRENT_USER_DOES_NOT_EXIST)).getId();
    }
}
