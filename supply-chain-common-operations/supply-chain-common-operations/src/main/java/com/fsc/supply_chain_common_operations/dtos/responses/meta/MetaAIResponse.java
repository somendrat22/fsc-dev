package com.fsc.supply_chain_common_operations.dtos.responses.meta;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaAIResponse {

    private Integer top_logprobs;
    private Double temperature;
    private Double top_p;
    private String service_tier;
    private String model;
    private Reasoning reasoning;
    private Boolean background;
    private List<Object> tools;
    private String tool_choice;
    private String truncation;

    private String id;
    private String object;
    private String status;
    private Double created_at;
    private Double completed_at;
    private Object error;
    private Object incomplete_details;
    private List<Output> output;
    private Object instructions;
    private Usage usage;
    private Boolean parallel_tool_calls;
    private Boolean store;

    @Data
    public static class Reasoning {
        private String effort;
        private List<Object> summary;
    }

    @Data
    public static class Output {
        private String type;
        private String id;
        private String status;
        private String role;
        private List<Content> content;
    }

    @Data
    public static class Content {
        private String type;
        private String text;
        private List<Object> annotations;
        private List<Object> logprobs;
    }

    @Data
    public static class Usage {
        private Integer input_tokens;
        private InputTokenDetails input_tokens_details;
        private Integer output_tokens;
        private OutputTokenDetails output_tokens_details;
        private Integer total_tokens;
    }

    @Data
    public static class InputTokenDetails {
        private Integer cached_tokens;
    }

    @Data
    public static class OutputTokenDetails {
        private Integer reasoning_tokens;
    }
}

