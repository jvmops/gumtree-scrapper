package com.jvmops.gumtree.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public interface Time {
    default LocalDateTime now() {
        return LocalDateTime.now();
    }
}

@Component
@ConditionalOnMissingBean(name = "customTime")
class Now implements Time {}