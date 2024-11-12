package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoDetailResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TodoDslRepository {
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    List<TodoDetailResponse> findAllOrderByCreatedAtDesc(TodoSearchRequest requestDto);
}
