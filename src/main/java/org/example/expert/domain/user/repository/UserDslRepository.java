package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.dto.response.UserSimpleResponse;
import org.springframework.data.repository.query.Param;

public interface UserDslRepository {
    UserSimpleResponse findByMinimumColumnNickName(@Param("nickname") String nickname);
}
