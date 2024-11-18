package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.entity.User;
import org.springframework.data.repository.query.Param;

public interface UserDslRepository {
    User findByMinimumColumnNickName(@Param("nickname") String nickname);

    User findByIndexingNickName(@Param("nickname") String nickname);
}
