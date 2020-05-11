package com.jvmops.gumtree.scraper;

import com.jvmops.gumtree.subscriptions.CityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;

@Profile("scraper")
@Component
@Slf4j
@AllArgsConstructor
public class ScrapJob {
    private CityService cityService;
    private Scrapper scrapper;
    private AdEvaluator adEvaluator;

    @PostConstruct
    void scrapAds() {
        cityService.cities().stream()
                .map(scrapper::scrapAds)
                .flatMap(Collection::stream)
                .forEach(adEvaluator::processAd);
    }
}