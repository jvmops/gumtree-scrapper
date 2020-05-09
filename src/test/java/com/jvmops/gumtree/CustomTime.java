package com.jvmops.gumtree;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("customTime")
public class CustomTime implements Time {

    @Override
    public LocalDateTime now() {
        // date is adjusted to meet dumped ads from resources/json/ads.json
        return LocalDateTime.parse("2020-05-07T23:01:00");
    }
}
