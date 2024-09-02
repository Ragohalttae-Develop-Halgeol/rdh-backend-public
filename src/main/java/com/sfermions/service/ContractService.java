package com.sfermions.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sfermions.dto.contract.AddContractRequest;
import com.sfermions.dto.contract.UpdateContractRequest;
import com.sfermions.entity.Contract;
import com.sfermions.entity.Post;
import com.sfermions.entity.User;
import com.sfermions.entity.enums.ContractStatus;
import com.sfermions.repository.ContractRepository;
import com.sfermions.repository.PostRepository;
import com.sfermions.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final UserRepository userRepository;
    private final ContractRepository contractRepository;
    private final PostRepository postRepository;

    // 특정 거래 가져오기 
    public Contract getContractById(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request Request User not found"));
    }

    // 특정 사용자 ID가 요청한 모든 계약을 가져오는 메서드
    public List<Contract> getContractsByRequesterId(Long requesterId) {
        return contractRepository.findByRequestUserId(requesterId);
    }

    // 특정 사용자 ID가 받은 모든 계약을 가져오는 메서드
    public List<Contract> getContractsByReceiverId(Long receiverId) {
        return contractRepository.findByReceiveUserId(receiverId);
    }

    // 계약 생성
    public Contract createContract(AddContractRequest addContractRequest) {
        // 요청한 유저와 받은 유저를 각각 찾아옵니다.
        User requestUser = userRepository.findById(addContractRequest.getRequestUserId())
                .orElseThrow(() -> new RuntimeException("Request Request User not found"));

        User receiveUser = userRepository.findById(addContractRequest.getReceiveUserId())
                .orElseThrow(() -> new RuntimeException("Receive Receive User not found"));

        Post post = postRepository.findById(addContractRequest.getPostId())
                .orElseThrow(() -> new RuntimeException("Receive Post not found"));

        // Contract 저장
        return contractRepository.save(Contract.builder()
                .requestUser(requestUser)
                .receiveUser(receiveUser)
                .post(post)
                .status(ContractStatus.PENDING) // 초기 상태를 PENDING으로 설정
                .build());
    }

    @Transactional
    public Contract updatedContractById(Long id, UpdateContractRequest updateContractRequest) {
        Contract existingContract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract with id " + id + " not found"));

        existingContract.update(updateContractRequest.getContractStatus());
        return existingContract;
    }

    public List<User> getUsersByPostId(Long postId) {
        // 해당 postId로 모든 Contract 가져오기
        List<Contract> contracts = contractRepository.findByPostId(postId);

        // 각 Contract의 requestUserId를 사용하여 User 엔티티를 가져오기
        List<User> users = contracts.stream()
                .map(contract -> userRepository.findById(contract.getRequestUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found")))
                .collect(Collectors.toList());

        return users;
    }
}
