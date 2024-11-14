package org.example.expert.domain.manager.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerLogService managerLogService;
    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public Object saveManager(Long userId, Long todoId, ManagerSaveRequest managerSaveRequest) {
        managerLogService.saveLog(userId, todoId, "Manager save process started");

        // 일정을 만든 유저
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(userId, todo.getUser().getId())) {
            String errorMsg =  "담당자를 등록하려고 하는 유저가 유효하지 않거나, 일정을 만든 유저가 아닙니다.";
            managerLogService.saveLog(userId, todoId, errorMsg);
            throw new InvalidRequestException(errorMsg);
        }

        User managerUser = userRepository.findById(managerSaveRequest.getManagerUserId())
                .orElseThrow(() -> {
                    String errorMsg =  "등록하려고 하는 담당자 유저가 존재하지 않습니다.";
                    managerLogService.saveLog(userId, todoId, errorMsg);
                    return new InvalidRequestException(errorMsg);
                });

        if (ObjectUtils.nullSafeEquals(userId, managerUser.getId())) {
            String errorMsg =  "일정 작성자는 본인을 담당자로 등록할 수 없습니다.";
            managerLogService.saveLog(userId, todoId, errorMsg);
            throw new InvalidRequestException(errorMsg);
        }

        Manager newManagerUser = new Manager(managerUser, todo);
        Manager savedManagerUser = managerRepository.save(newManagerUser);
        managerLogService.saveLog(userId, todoId, "completed");
        return new ManagerSaveResponse(
                savedManagerUser.getId(),
                new UserResponse(managerUser.getId(), managerUser.getEmail())
        );
    }

    public List<ManagerResponse> getManagers(long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        List<Manager> managerList = managerRepository.findByTodoIdWithUser(todo.getId());

        List<ManagerResponse> dtoList = new ArrayList<>();
        for (Manager manager : managerList) {
            User user = manager.getUser();
            dtoList.add(new ManagerResponse(
                    manager.getId(),
                    new UserResponse(user.getId(), user.getEmail())
            ));
        }
        return dtoList;
    }

    @Transactional
    public void deleteManager(Long userId, long todoId, long managerId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(userId, todo.getUser().getId())) {
            throw new InvalidRequestException("해당 일정을 만든 유저가 유효하지 않습니다.");
        }

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new InvalidRequestException("Manager not found"));

        if (!ObjectUtils.nullSafeEquals(todo.getId(), manager.getTodo().getId())) {
            throw new InvalidRequestException("해당 일정에 등록된 담당자가 아닙니다.");
        }

        managerRepository.delete(manager);
    }
}
