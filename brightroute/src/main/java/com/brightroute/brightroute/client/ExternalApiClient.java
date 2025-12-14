package com.brightroute.brightroute.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "localClient", url = "http://localhost:7070")
public interface ExternalApiClient {

    @GetMapping("/api/cloud-demo/data-source")
    String getData();
}
