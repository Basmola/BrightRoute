package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.client.ExternalApiClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cloud-demo")
public class CloudDemoController {

    private final ExternalApiClient externalApiClient;

    public CloudDemoController(ExternalApiClient externalApiClient) {
        this.externalApiClient = externalApiClient;
    }

    @GetMapping("/ping")
    public String ping() {
        return "Controller is working!";
    }

    // This is the endpoint the Feign Client will call
    @GetMapping("/data-source")
    public String dataSource() {
        return "{\"message\": \"Success! Data received via Spring Cloud Feign (Local Loopback)\"}";
    }

    @GetMapping
    public String getDemoData() {
        try {
            System.out.println("Calling local Feign client...");
            // This calls the interface, which calls
            // http://localhost:7070/api/cloud-demo/data-source
            return "Result: " + externalApiClient.getData();
        } catch (Throwable e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            return "CRITICAL ERROR: " + e.getClass().getName() + ": " + e.getMessage() + "\n\nSTACK TRACE:\n"
                    + sw.toString();
        }
    }
}
