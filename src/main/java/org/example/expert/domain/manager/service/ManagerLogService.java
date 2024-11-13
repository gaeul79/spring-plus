package org.example.expert.domain.manager.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.manager.entity.ManagerLog;
import org.example.expert.domain.manager.repository.ManagerLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagerLogService {
    private final ManagerLogRepository managerLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(Long userId, Long todoId, String result) {
        ManagerLog log = ManagerLog.builder()
                .userId(userId)
                .todoId(todoId)
                .result(result)
                .build();
        managerLogRepository.save(log);
    }
}
