package org.example.expert.domain.manager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.service.ManagerService;
import org.example.expert.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;

    @PostMapping("/todos/{todoId}/managers")
    public ResponseEntity<?> saveManager(
            @AuthenticationPrincipal UserDetailsImpl userDetail,
            @PathVariable long todoId,
            @Valid @RequestBody ManagerSaveRequest managerSaveRequest
    ) {
         return ResponseEntity.ok(managerService.saveManager(userDetail.getId(), todoId, managerSaveRequest));
    }

    @GetMapping("/todos/{todoId}/managers")
    public ResponseEntity<List<ManagerResponse>> getMembers(@PathVariable long todoId) {
        return ResponseEntity.ok(managerService.getManagers(todoId));
    }

    @DeleteMapping("/todos/{todoId}/managers/{managerId}")
    public void deleteManager(
            @AuthenticationPrincipal UserDetailsImpl userDetail,
            @PathVariable long todoId,
            @PathVariable long managerId
    ) {
        managerService.deleteManager(userDetail.getId(), todoId, managerId);
    }
}
