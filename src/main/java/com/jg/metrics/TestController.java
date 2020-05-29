package com.jg.metrics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class TestController {

    private String info;

    @GetMapping("/info")
    public String getInfo() {
        return Objects.nonNull(info) ? info : "No info yet...";
    }

    @PostMapping("/info")
    public void postInfo(final String info) {
        this.info = info;
    }

}
