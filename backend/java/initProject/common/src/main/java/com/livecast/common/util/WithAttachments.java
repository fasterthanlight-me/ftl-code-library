package com.livecast.common.util;


import com.livecast.common.integrations.S3Client;

public interface WithAttachments<T> {
    T signAttachments(S3Client s3Client);
}
