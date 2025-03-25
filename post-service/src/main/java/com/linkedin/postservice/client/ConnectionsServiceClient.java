package com.linkedin.postservice.client;

import com.linkedin.postservice.dto.PersonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name = "connections-service", path = "/connections/core")
public interface ConnectionsServiceClient {

    @GetMapping("/{userId}/first-degree")
    List<PersonDTO> getFirstDegreeConnections(@PathVariable Long userId);

}
