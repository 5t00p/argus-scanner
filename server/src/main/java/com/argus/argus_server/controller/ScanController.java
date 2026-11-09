package com.argus.argus_server.controller;

import com.argus.argus_server.model.Scan;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scans")
public class ScanController {

    @GetMapping
    public List<Scan> getScans() {

        return List.of(
                new Scan(
                        1L,
                        "PHONE001",
                        "2026-11-09"
                ),
                new Scan(
                        2L,
                        "PHONE002",
                        "2026-11-09"
                )
        );
    }
}
