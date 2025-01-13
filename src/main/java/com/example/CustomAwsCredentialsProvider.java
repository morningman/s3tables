package com.example;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;

import java.util.Map;

public class CustomAwsCredentialsProvider implements AwsCredentialsProvider {
    private final String accessKeyId;
    private final String secretAccessKey;

    public CustomAwsCredentialsProvider(String accessKeyId, String secretAccessKey) {
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
    }

    @Override
    public AwsCredentials resolveCredentials() {
        return AwsBasicCredentials.create(accessKeyId, secretAccessKey);
    }

    public static CustomAwsCredentialsProvider create(Map<String, String> props) {
        return new CustomAwsCredentialsProvider(props.get("s3.access-key-id"), props.get("s3.secret-access-key"));
    }
}