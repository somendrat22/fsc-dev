package com.fsc.supply_chain_common_operations.dtos.requests.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaInputContentRequest {
    private String type;
    private String text;
}
