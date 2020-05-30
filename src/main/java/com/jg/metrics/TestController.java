package com.jg.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final MeterRegistry meterRegistry;

    private String info;

    @GetMapping("/info")
    public String getInfo() {
        return Objects.nonNull(info) ? info : "No info yet...";
    }

    @PostMapping("/info")
    public void postInfo(@RequestBody final String info) {
        this.info = info;

        // Custom Counter metrics.
        final Counter longCounter = Counter.builder("info.submissions")
                .tag("length", "long")
                .description("Counter for long info.")
                .register(meterRegistry);

        final Counter shortCounter = Counter.builder("info.submissions")
                .tag("length", "short")
                .description("Counter for short info.")
                .register(meterRegistry);


        if(info.length() > 10) {
            longCounter.increment();
        } else {
            shortCounter.increment();
        }

        // Custom Gauge metric.
        Gauge.builder("info.length", () -> new AtomicInteger(this.info.length()).get())
                .strongReference(true)
                .register(meterRegistry);
    }

}
