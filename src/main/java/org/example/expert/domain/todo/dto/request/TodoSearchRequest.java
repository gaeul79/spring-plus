package org.example.expert.domain.todo.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TodoSearchRequest {
    private String title;
    private String nickname;
    private String weather;
    private LocalDateTime startCreateDate;
    private LocalDateTime endCreateDate;
    private int page;
    private int size;
}
