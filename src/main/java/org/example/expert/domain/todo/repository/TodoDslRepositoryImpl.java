package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoDetailResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TodoDslRepositoryImpl implements TodoDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        QTodo todo = new QTodo("todo");
        QUser user = new QUser("user"); // 별칭

        // fetchFirst() : 첫 번째 결과를 가져오거나 결과가 없으면 null을 가져옵니다.
        // fetchOne() : 결과가 여러건이면 fail이 난다.
        return Optional.ofNullable(
                queryFactory.selectFrom(todo)
                        .leftJoin(todo.user, user).fetchJoin()
                        .where(todo.id.eq(todoId))
                        .fetchOne()
        );
    }

    @Override
    public List<TodoDetailResponse> findAllOrderByCreatedAtDesc(TodoSearchRequest requestDto) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;
        List<Todo> todos = queryFactory.selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(makeWhere(requestDto))
                .orderBy()
                .limit(makeLimit(requestDto))
                .offset(requestDto.getSize())
                .orderBy(todo.createdAt.desc())
                .fetch();
        return todos.stream().map(TodoDetailResponse::from).toList();
    }

    private long makeLimit(TodoSearchRequest requestDto) {
        return (long) requestDto.getSize() * requestDto.getPage();
    }

    private BooleanBuilder makeWhere(TodoSearchRequest requestDto) {
        BooleanBuilder builder = new BooleanBuilder();
        QUser user = QUser.user;
        QTodo todo = QTodo.todo;

        if (!requestDto.getTitle().isEmpty())
            builder.and(todo.title.contains(requestDto.getTitle()));

        if (!requestDto.getNickname().isEmpty())
            builder.and(user.nickname.contains(requestDto.getNickname()));

        if (!requestDto.getWeather().isEmpty())
            builder.and(todo.weather.contains(requestDto.getWeather()));

        if (requestDto.getStartCreateDate() != null && requestDto.getEndCreateDate() != null) {
            builder.and(todo.createdAt.loe(requestDto.getStartCreateDate()))
                    .and(todo.createdAt.goe(requestDto.getEndCreateDate()));
        }

        return builder;
    }
}
