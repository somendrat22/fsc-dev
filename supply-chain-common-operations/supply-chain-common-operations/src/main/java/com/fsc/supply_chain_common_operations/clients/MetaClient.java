package com.fsc.supply_chain_common_operations.clients;

import com.fsc.supply_chain_common_operations.constants.SystemConstant;
import com.fsc.supply_chain_common_operations.dtos.requests.meta.MetaModelRequest;
import com.fsc.supply_chain_common_operations.dtos.responses.meta.MetaAIResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class MetaClient {


    public MetaAIResponse getAIResponse(MetaModelRequest metaModelRequest){
        // We want to make request to meta api
        String endpoint = SystemConstant.META_AI_ENDPOINT;
        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        // Add authorization if required
        headers.setBearerAuth(SystemConstant.META_API_KEY);
        // Build
        RequestEntity request = RequestEntity.post(endpoint).headers(headers).body(metaModelRequest);
        // We need to hit the request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MetaAIResponse> response = restTemplate.exchange(
                request,
                MetaAIResponse.class
        );
        return response.getBody();
    }
}
