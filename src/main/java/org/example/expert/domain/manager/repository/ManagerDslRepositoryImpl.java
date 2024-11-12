package org.example.expert.domain.manager.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ManagerDslRepositoryImpl implements ManagerDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Long countByUserId(Long userId) {
        return 1L;
    }
}
