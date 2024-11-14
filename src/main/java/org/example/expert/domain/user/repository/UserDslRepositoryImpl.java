package org.example.expert.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.entity.QUser;
import org.example.expert.domain.user.entity.User;

@RequiredArgsConstructor
public class UserDslRepositoryImpl implements UserDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public User findByNickName(String nickname) {
        QUser user = QUser.user;
        return queryFactory.selectFrom(user)
                .where(user.nickname.eq(nickname))
                .fetchOne();
    }
}
