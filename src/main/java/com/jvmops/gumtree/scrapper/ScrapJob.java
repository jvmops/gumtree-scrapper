package com.jvmops.gumtree.scrapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Profile("scrapper")
@Component
@Slf4j
@AllArgsConstructor
public class ScrapJob {
    private static final String GUMTREE_WROCLAW_APARTMENTS_URL = "/s-mieszkania-i-domy-do-wynajecia/wroclaw/zmywarka/v1c9008l3200114q0p1";

    private AdScrapper adScrapper;
    private AdEvaluator adEvaluator;

    @PostConstruct
    void execute() {
        scrapGumtreeAds();
    }

    private void scrapGumtreeAds() {
        log.info("Scrapping ads...");
        List<Ad> scrappedAds = adScrapper.scrapAds(GUMTREE_WROCLAW_APARTMENTS_URL);

        log.info("{} ads scrapped. Evaluation process started...", scrappedAds.size());
        adEvaluator.processAds(scrappedAds);
    }
}