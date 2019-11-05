package com.jvmops.gumtree.config;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;

@Configuration
@Profile("scrapper")
@Slf4j
public class Selenium {
    @Autowired
    private GumtreeScrapperProperties gumtreeScrapperProperties;

    @Bean
    WebDriverFactory webDriverFactory() {
        return new WebDriverFactory(gumtreeScrapperProperties.getSeleniumUrl());
    }

    public class WebDriverFactory {
        private URL seleniumServerUrl;

        WebDriverFactory(String seleniumServerUrl) {
            try {
                this.seleniumServerUrl = new URL(seleniumServerUrl);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
        }

        public RemoteWebDriver initialize() {
            DesiredCapabilities chrome = DesiredCapabilities.chrome();
            return new RemoteWebDriver(seleniumServerUrl, chrome);
        }
    }

    @PostConstruct
    public void log() {
        log.warn("COMPONENT: Selenium created!");
    }
}