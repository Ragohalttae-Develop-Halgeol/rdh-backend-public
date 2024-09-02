package com.sfermions.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sfermions.dto.api.ApiResponse;
import com.sfermions.dto.contract.AddContractRequest;
import com.sfermions.dto.contract.UpdateContractRequest;
import com.sfermions.dto.user.UpdateUserRequest;
import com.sfermions.entity.Contract;
import com.sfermions.service.ContractService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    // api 작동 테스트
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<?>> getTest() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Api /api/contract/test OK", null));
    }

    // contract id 값을 가지고 contract 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getContractById(@PathVariable("id") Long id) {
        Contract contract = contractService.getContractById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Api /api/contract/test OK", contract));
    }

    // user id를 가지고 user가 요청보낸 contract 조회
    @GetMapping("/request/{userId}")
    public ResponseEntity<ApiResponse<?>> getContractsByRequestUserId(@PathVariable("userId") Long userId) {
        List<Contract> contracts = contractService.getContractsByRequesterId(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Api /request/{userId} Success", contracts));
    }

    // user id를 가지고 user가 요청받은 contract 조회
    @GetMapping("/receive/{userId}")
    public ResponseEntity<ApiResponse<?>> getContractsByReceiveUserId(@PathVariable("userId") Long userId) {
        List<Contract> contracts = contractService.getContractsByReceiverId(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Api /receive/{userId} Success", contracts));
    }

    // contract 생성 (요청을 보낼 때 사용)
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createContract(@RequestBody AddContractRequest request) {
        Contract contract = contractService.createContract(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Create Contract Successfully", contract));
    }

    // contract 상태(대기, 수락, 거절) 변경 
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateContractById(@PathVariable("id") Long id, @RequestBody UpdateContractRequest request) {
        Contract updatedContract = contractService.updatedContractById(id, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Update Success", updatedContract));
    }
}