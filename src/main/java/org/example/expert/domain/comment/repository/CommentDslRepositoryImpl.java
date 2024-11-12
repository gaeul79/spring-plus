package org.example.expert.domain.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentDslRepositoryImpl implements CommentDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Long countByTodoId(Long todoId) {
        return 1L;
    }
}
