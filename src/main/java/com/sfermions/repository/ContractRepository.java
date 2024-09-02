package com.sfermions.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sfermions.entity.Contract;
import com.sfermions.entity.User;
import java.util.List;
import java.util.Optional;


public interface ContractRepository extends JpaRepository<Contract, Long> {
    // 특정 requestUserId가 요청한 모든 계약을 가져오는 메서드
    List<Contract> findByRequestUserId(Long requestUserId);

    // 특정 receiveUserId가 받은 모든 계약을 가져오는 메서드
    List<Contract> findByReceiveUserId(Long receiveUserId);

    // 특정 postId로 모든 Contract를 가져오는 메서드
    List<Contract> findByPostId(Long postId);
}