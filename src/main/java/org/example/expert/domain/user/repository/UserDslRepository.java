package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.entity.User;
import org.springframework.data.repository.query.Param;

public interface UserDslRepository {
    User findByNickName(@Param("nickname") String nickname);
}
