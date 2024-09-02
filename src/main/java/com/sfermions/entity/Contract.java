package com.sfermions.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sfermions.entity.enums.ContractStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contract")
@Getter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Contract extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private Long id;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    // PENDING = 0, REJECTED = -1, ACCEPTED = 1 
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ContractStatus status = ContractStatus.PENDING;  // 초기 상태를 PENDING으로 설정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_user_id", nullable = false) // 거래를 요청한 사람
    private User requestUser; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_user_id", nullable = false) // 거래 요청을 받은 사람
    private User receiveUser; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    public Contract(User requestUser, User receiveUser, Post post, ContractStatus status) {
        this.requestUser = requestUser;
        this.receiveUser = receiveUser;
        this.post = post;
        this.status = status;
    }

    public void update(ContractStatus status) {
        this.status = status;
    }

}