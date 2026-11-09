package com.argus.argus_server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/scans")
public class ScanController {

    @GetMapping
    public List<String> getScans() {
        return List.of(
                "com.example.app",
                "com.spyapp.test",
                "com.socialmedia.app"
        );
    }
}
