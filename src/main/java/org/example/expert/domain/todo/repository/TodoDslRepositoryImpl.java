package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoDetailResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

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
    public Page<TodoDetailResponse> findAllOrderByCreatedAtDesc(TodoSearchRequest requestDto, Pageable pageable) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        JPAQuery<Long> cntTotal = queryFactory.select(todo.count()).from(todo)
                .leftJoin(todo.user, user)
                .where(makeWhere(requestDto));
        List<TodoDetailResponse> todos = queryFactory.selectFrom(todo)
                .leftJoin(todo.user, user)
                .where(makeWhere(requestDto))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(todo.createdAt.desc())
                .fetch()
                .stream()
                .map(TodoDetailResponse::from)
                .toList();
        return PageableExecutionUtils.getPage(todos, pageable, cntTotal::fetchOne);
    }

    private BooleanBuilder makeWhere(TodoSearchRequest requestDto) {
        BooleanBuilder builder = new BooleanBuilder();
        QUser user = QUser.user;
        QTodo todo = QTodo.todo;

        if (StringUtils.hasText(requestDto.getTitle()))
            builder.and(todo.title.contains(requestDto.getTitle()));

        if (StringUtils.hasText(requestDto.getNickname()))
            builder.and(user.nickname.contains(requestDto.getNickname()));

        if (StringUtils.hasText(requestDto.getWeather()))
            builder.and(todo.weather.contains(requestDto.getWeather()));

        if (requestDto.getStartCreateDate() != null && requestDto.getEndCreateDate() != null) {
            builder.and(todo.createdAt.goe(requestDto.getStartCreateDate()))
                    .and(todo.createdAt.loe(requestDto.getEndCreateDate()));
        }

        return builder;
    }
}
