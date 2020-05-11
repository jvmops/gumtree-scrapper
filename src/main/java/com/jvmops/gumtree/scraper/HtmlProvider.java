package com.jvmops.gumtree.scraper;

import com.jvmops.gumtree.subscriptions.City;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
class HtmlProvider {
    public static final String GUMTREE_URL = "https://www.gumtree.pl";
    private static final String URL_TEMPLATE = "%s/s-mieszkania-i-domy-do-wynajecia/%sp%s";

    String adListing(City city, int pageNumber) {
        String url = String.format(URL_TEMPLATE, GUMTREE_URL, city.getUrlCode(), pageNumber);
        log.info("Scrapping {} ads from page {}... {}", city.getName(), pageNumber, url);
        return get(url);
    }

    String adListing(String cityUrlCode) {
        String url = String.format(URL_TEMPLATE, GUMTREE_URL, cityUrlCode, 1);
        log.info("Scrapping ads using url code {} from {}", cityUrlCode, url);
        return get(url);
    }

    String adDetails(ListedAd listedAd) {
        return get(listedAd.getUrl());
    }

    private String get(String url) {
        try {
            return Jsoup.connect(url).get().html();
        } catch (IOException e) {
            log.error("Unable to fetch html from: {}", url, e);
            return "";
        }
    }
}