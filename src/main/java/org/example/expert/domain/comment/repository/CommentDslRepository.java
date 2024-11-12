package org.example.expert.domain.comment.repository;

public interface CommentDslRepository {
    Long countByTodoId(Long todoId);
}
