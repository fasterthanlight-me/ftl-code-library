package com.livecast.common.util;

import com.livecast.common.integrations.S3Client;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public class AttachmentsUtils {

    public static <T extends WithAttachments<T>> Page<T> updatePresignUrl(S3Client s3Client, Page<T> page) {
        page.getContent().forEach(t -> t.signAttachments(s3Client));
        return page;
    }

    public static <T extends WithAttachments<T>> List<T> updatePresignUrl(S3Client s3Client, List<T> list) {
        list.forEach(t -> t.signAttachments(s3Client));
        return list;
    }

    public static <T extends WithAttachments<T>> Optional<T> updatePresignUrl(S3Client s3Client, Optional<T> optional) {
        optional.ifPresent(t -> t.signAttachments(s3Client));
        return optional;
    }

    public static <T extends WithAttachments<T>> ResponseEntity<T> updatePresignUrl(S3Client s3Client, ResponseEntity<T> response) {
        T t = response.getBody();
        if (t == null) {
            return response;
        }

        t.signAttachments(s3Client);
        return response;
    }

    public static <T extends WithAttachments<T>> T updatePresignUrl(S3Client s3Client, T t) {
        return t.signAttachments(s3Client);
    }
}