package com.fsc.supply_chain_common_operations.dtos.requests.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaInputMessageRequest {
    private String role;
    private List<MetaInputContentRequest> content;
}
