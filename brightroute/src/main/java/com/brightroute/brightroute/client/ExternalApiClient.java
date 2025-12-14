package com.brightroute.brightroute.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

// This component uses Spring Cloud OpenFeign to talk to an internal service (Loopback)
// This avoids external network restrictions/errors while still using the Spring Cloud technology.
@FeignClient(name = "localClient", url = "http://localhost:7070")
public interface ExternalApiClient {

    @GetMapping("/api/cloud-demo/data-source")
    String getData();
}
