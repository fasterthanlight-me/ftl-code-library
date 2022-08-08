package com.livecast.userservice.repository.spec;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.*;
import java.util.List;

import static java.util.Objects.isNull;

public class Specifications {

    public static <T> Specification<T> idsSpec(List<Long> ids) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                if (ids == null) {
                    return null;
                }

                return cb.or(root.get("id").in(ids));
            }
        };
    }

    public static <T> Specification<T> nullableAnd(@Nullable Specification<T> spec,
                                                   @Nullable Specification<T> anotherSpec) {
        if (isNull(spec)){
            return anotherSpec;
        }
        return spec.and(anotherSpec);
    }

}
