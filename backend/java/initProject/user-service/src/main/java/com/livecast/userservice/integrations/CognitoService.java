package com.livecast.userservice.integrations;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class CognitoService {
    @Value("${amazonProperties.region}")
    private String region;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @Value("${amazonProperties.userPoolId}")
    private String userPoolId;

    private AWSCognitoIdentityProvider client;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.client = AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    public void addUserToGroup(String username, String group) {
        AdminAddUserToGroupRequest request = new AdminAddUserToGroupRequest();
        request.setUserPoolId(userPoolId);
        request.setUsername(username);
        request.setGroupName(group);
        client.adminAddUserToGroup(request);
    }

    public void removeUserFromGroup(String username, String group) {
        AdminRemoveUserFromGroupRequest request = new AdminRemoveUserFromGroupRequest();
        request.setUserPoolId(userPoolId);
        request.setUsername(username);
        request.setGroupName(group);
        client.adminRemoveUserFromGroup(request);
    }

    public void adminDeleteUser(String username) {
        AdminDeleteUserRequest adminDeleteUserRequest = new AdminDeleteUserRequest();
        adminDeleteUserRequest.setUserPoolId(userPoolId);
        adminDeleteUserRequest.setUsername(username);
        client.adminDeleteUser(adminDeleteUserRequest);
    }
}
