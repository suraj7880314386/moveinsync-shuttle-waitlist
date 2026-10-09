package com.moveinsync.shuttle.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfig {
    /** Single time source; tests replace it with a mutable clock. */
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
