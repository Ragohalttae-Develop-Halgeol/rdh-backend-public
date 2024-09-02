package com.sfermions.dto.contract;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddContractRequest {
    private long requestUserId;
    private long receiveUserId;
    private long postId;
}
