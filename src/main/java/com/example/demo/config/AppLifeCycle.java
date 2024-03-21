package com.example.demo.config;

import com.example.demo.service.JwtService;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class AppLifeCycle implements SmartLifecycle {

    private static final Logger LOGGER = Logger.getLogger(AppLifeCycle.class.getName());

    private boolean isRunning = false;

    private final JwtService jwtService;

    public AppLifeCycle(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void start() {
        this.isRunning = true;
        LOGGER.info("The application has been started");
    }

    @Override
    public void stop() {
        jwtService.cleanTokensWhiteList();
        this.isRunning = false;
        LOGGER.info("The application has been shutdown");
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }
}
