package com.jg.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter longCounter(final MeterRegistry meterRegistry) {
        return Counter.builder("info.submissions")
                .tag("length", "long")
                .description("Counter for long info.")
                .register(meterRegistry);
    }

    @Bean
    public Counter shortCounter(final MeterRegistry meterRegistry) {
        return Counter.builder("info.submissions")
                .tag("length", "short")
                .description("Counter for short info.")
                .register(meterRegistry);
    }

}
