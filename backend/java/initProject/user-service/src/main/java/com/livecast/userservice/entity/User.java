package com.livecast.userservice.entity;

import com.livecast.common.entity.Attachment;
import com.livecast.common.integrations.S3Client;
import com.livecast.common.util.WithAttachments;
import com.livecast.userservice.entity.enums.UserType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "users")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class User implements WithAttachments<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String bio;
    private String phoneNumber;
    private Short age;
    @Enumerated(EnumType.STRING)
    private UserType type;

    @org.hibernate.annotations.Type(type = "jsonb")
    private Attachment picture;

    @Override
    public User signAttachments(S3Client s3Client) {
        if (picture != null) {
            picture = s3Client.generateSignedUrl(picture);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
