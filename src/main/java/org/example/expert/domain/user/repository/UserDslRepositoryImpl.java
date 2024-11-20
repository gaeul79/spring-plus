package org.example.expert.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.dto.response.QUserSimpleResponse;
import org.example.expert.domain.user.dto.response.UserSimpleResponse;
import org.example.expert.domain.user.entity.QUser;

@RequiredArgsConstructor
public class UserDslRepositoryImpl implements UserDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public UserSimpleResponse findByMinimumColumnNickName(String nickname) {
        QUser user = QUser.user;
        return queryFactory
                .select(new QUserSimpleResponse(user.id, user.email, user.nickname))
                .from(user)
                .where(user.nickname.eq(nickname))
                .fetchOne();
    }
}
