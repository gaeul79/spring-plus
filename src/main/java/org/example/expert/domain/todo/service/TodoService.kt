package org.example.expert.domain.todo.service

import org.example.expert.api.WeatherClient
import org.example.expert.domain.common.exception.InvalidRequestException
import org.example.expert.domain.todo.dto.request.TodoSaveRequest
import org.example.expert.domain.todo.dto.request.TodoSearchRequest
import org.example.expert.domain.todo.dto.response.TodoDetailResponse
import org.example.expert.domain.todo.dto.response.TodoResponse
import org.example.expert.domain.todo.dto.response.TodoSaveResponse
import org.example.expert.domain.todo.entity.Todo
import org.example.expert.domain.todo.repository.TodoRepository
import org.example.expert.domain.user.dto.response.UserResponse
import org.example.expert.domain.user.entity.User
import org.example.expert.security.UserDetailsImpl
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class TodoService(
    private val todoRepository: TodoRepository,
    private val weatherClient: WeatherClient
) {

    fun saveTodo(userDetail: UserDetailsImpl, todoSaveRequest: TodoSaveRequest): TodoSaveResponse {
        val user = User.fromAuthUser(userDetail)
        val weather = weatherClient.getTodayWeather()
        val newTodo = Todo.create(
            title = todoSaveRequest.title,
            contents = todoSaveRequest.contents,
            weather = weather,
            user = user
        )
        val savedTodo = todoRepository.save(newTodo)
        return TodoSaveResponse(
            savedTodo.id!!,
            savedTodo.title,
            savedTodo.contents,
            weather,
            UserResponse(user.id!!, user.email)
        )
    }

    fun getTodos(weather: String, startDate: LocalDateTime, endDate: LocalDateTime, page: Int, size: Int): Page<TodoResponse> {
        val pageable: Pageable = PageRequest.of(page - 1, size)
        val todos = todoRepository.findAllByOrderByModifiedAtDesc(weather, startDate, endDate, pageable)
        return todos.map { todo ->
            TodoResponse(
                todo.id!!,
                todo.title,
                todo.contents,
                todo.weather,
                UserResponse(todo.user.id!!, todo.user.email),
                todo.createdAt,
                todo.modifiedAt
            )
        }
    }

    fun getQueryDslTodos(requestDto: TodoSearchRequest): Page<TodoDetailResponse> {
        val pageable: Pageable = PageRequest.of(requestDto.page - 1, requestDto.size)
        return todoRepository.findAllOrderByCreatedAtDesc(requestDto, pageable)
    }

    fun getTodo(todoId: Long): TodoResponse {
        val todo = todoRepository.findByIdWithUser(todoId)
            .orElseThrow { InvalidRequestException("Todo not found") }
        val user = todo.user
        return TodoResponse(
            todo.id!!,
            todo.title,
            todo.contents,
            todo.weather,
            UserResponse(user.id!!, user.email),
            todo.createdAt,
            todo.modifiedAt
        )
    }
}