package com.example.springbootdemoconsumer.service.impl;

import com.example.springbootdemoconsumer.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @Override
    @Async
    public void testLog() {
        log.info("testLog");
    }
}
