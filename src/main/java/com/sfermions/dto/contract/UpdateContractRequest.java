package com.sfermions.dto.contract;

import com.sfermions.entity.enums.ContractStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateContractRequest {
    private ContractStatus contractStatus;
}
