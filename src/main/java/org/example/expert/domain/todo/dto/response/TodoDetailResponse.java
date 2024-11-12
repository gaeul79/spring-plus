package org.example.expert.domain.todo.dto.response;

import lombok.Getter;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.dto.response.UserResponse;

import java.time.LocalDateTime;

@Getter
public class TodoDetailResponse {
    private Long id;
    private String title;
    private String contents;
    private String weather;
    private UserResponse user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int cntManager;
    private int cntComment;

    public static TodoDetailResponse from(Todo todo) {
        TodoDetailResponse dto = new TodoDetailResponse();
        dto.id = todo.getId();
        dto.title = todo.getTitle();
        dto.contents = todo.getContents();
        dto.weather = todo.getWeather();
        dto.user = UserResponse.from(todo.getUser());
        dto.createdAt = todo.getCreatedAt();
        dto.modifiedAt = todo.getModifiedAt();
        dto.cntManager = todo.getManagers().size();
        dto.cntComment = todo.getComments().size();
        return dto;
    }
}
