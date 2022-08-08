package com.livecast.userservice.controller;

import com.amazonaws.HttpMethod;
import com.livecast.userservice.dto.UserDTO;
import com.livecast.common.entity.S3PresignedUrl;
import com.livecast.userservice.entity.Follower;
import com.livecast.userservice.entity.User;
import com.livecast.userservice.entity.User_;
import com.livecast.common.exception.CustomException;
import com.livecast.common.integrations.S3Client;
import com.livecast.userservice.mapper.UserMapper;
import com.livecast.userservice.repository.FollowRepository;
import com.livecast.userservice.repository.UserRepository;
import com.livecast.userservice.repository.spec.FollowSpec;
import com.livecast.userservice.repository.spec.Specifications;
import com.livecast.userservice.service.UserService;
import com.livecast.common.util.AuthUtil;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.List;

import static com.livecast.common.util.AttachmentsUtils.updatePresignUrl;

@RestController
@RequestMapping(path = "v1/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final S3Client s3Client;
    private final FollowRepository followRepository;

    @Autowired
    public UserController(UserRepository userRepository,
                          UserService userService,
                          S3Client s3Client, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.s3Client = s3Client;
        this.followRepository = followRepository;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public Page<User> list(@Conjunction(
            value = {
                    @Or({
                            @Spec(path = User_.USERNAME, params = "search", spec = LikeIgnoreCase.class),
                            @Spec(path = User_.EMAIL, params = "search", spec = LikeIgnoreCase.class),
                    })
            }
    ) Specification<User> spec, Pageable pageRequest,
                           @RequestParam(name = "ids", required = false)
                                   List<Long> ids) {
        return updatePresignUrl(s3Client,
                userRepository.findAll(Specifications.nullableAnd(spec, Specifications.idsSpec(ids)),
                pageRequest));
    }

    @RequestMapping(path = "/me", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> get(Authentication authentication) {
        return ResponseEntity.ok(UserMapper.INSTANCE
                .toDTO(updatePresignUrl(s3Client, userRepository.findOneByUsername(AuthUtil.getUsername(authentication)))
                        .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, CustomException.CURRENT_USER_DOES_NOT_EXIST))));
    }

    //This method will be used for first log in. When you signed up in cognito but did not have user in our database.
    //That's why we do not require any permissions here.
    @RequestMapping(path = "/me", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> create(Authentication authentication, @RequestBody @Valid UserDTO userDTO) {
        //todo: email
        if (!AuthUtil.getUsername(authentication).equals(userDTO.getUsername())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, CustomException.FORBIDDEN_OTHER_USER);
        }
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @RequestMapping(path = "/me", method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> update(@RequestBody @Valid UserDTO userDTO,
                                          Authentication authentication) {
        return ResponseEntity.ok(userService.updateCurrentUser(userDTO, AuthUtil.getUsername(authentication)));
    }

    @RequestMapping(path = "/me/followers", method = RequestMethod.GET)
    public Page<Follower> getMyFollowers(Authentication authentication, Pageable pageRequest) {
        Long myId = userService.getIdByUsername(AuthUtil.getUsername(authentication));
        Specification<Follower> spec = FollowSpec.userId(myId);
        return followRepository.findAll(spec, pageRequest);
    }

    @RequestMapping(path = "/me/following", method = RequestMethod.GET)
    public Page<Follower> getMyFollowing(Authentication authentication, Pageable pageRequest) {
        Long myId = userService.getIdByUsername(AuthUtil.getUsername(authentication));
        Specification<Follower> spec = FollowSpec.followerId(myId);
        return followRepository.findAll(spec, pageRequest);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok(UserMapper.INSTANCE
                .toDTO(userRepository.findById(id).orElseThrow(() ->
                        new CustomException(HttpStatus.NOT_FOUND, CustomException.NOT_FOUND))));
    }

    @RequestMapping(path = "/{id}/followers", method = RequestMethod.GET)
    public Page<Follower> getFollowers(@PathVariable("id") Long id, Pageable pageRequest) {
        Specification<Follower> spec = FollowSpec.userId(id);
        return followRepository.findAll(spec, pageRequest);
    }

    @RequestMapping(path = "/{id}/following", method = RequestMethod.GET)
    public Page<Follower> getFollowing(@PathVariable("id") Long id, Pageable pageRequest) {
        Specification<Follower> spec = FollowSpec.followerId(id);
        return followRepository.findAll(spec, pageRequest);
    }

    @RequestMapping(path = "/me/pictures/presign_put_url", method = RequestMethod.GET)
    @Transactional
    public S3PresignedUrl getUploadPictureUrl() {
        return s3Client.generateSignedUrl(S3Client.S3SubPaths.USER_PROFILE_PICTURES_SUB_PATH);
    }

    @RequestMapping(path = "/me/pictures/presign_get_url/{object_id}", method = RequestMethod.GET)
    @Transactional
    public S3PresignedUrl getDownloadPicturedUrl(@PathVariable("object_id") String objectId, @RequestParam(value = "file_name", required = false) String fileName) {
        return s3Client.generateSignedUrl(S3Client.S3SubPaths.USER_PROFILE_PICTURES_SUB_PATH, objectId, HttpMethod.GET, fileName);
    }
}
