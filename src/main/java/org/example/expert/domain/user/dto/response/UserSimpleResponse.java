package org.example.expert.domain.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSimpleResponse {
    private Long id;
    private String email;
    private String nickname;

    @QueryProjection
    public UserSimpleResponse(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }
}
