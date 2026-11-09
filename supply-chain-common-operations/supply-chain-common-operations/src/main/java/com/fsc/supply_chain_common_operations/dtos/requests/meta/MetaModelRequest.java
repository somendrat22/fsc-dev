package com.fsc.supply_chain_common_operations.dtos.requests.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaModelRequest {
    private String model;
    private List<MetaInputMessageRequest> input;
    @JsonProperty("stream")
    private boolean stream;
}
