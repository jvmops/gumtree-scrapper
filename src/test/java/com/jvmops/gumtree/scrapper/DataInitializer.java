package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.MongoTest;
import com.jvmops.gumtree.config.Time;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@Slf4j
abstract class DataInitializer extends MongoTest {

    @Autowired
    protected AdScrapperRepository adScrapperRepository;
    @Autowired
    protected Time time;

    @BeforeEach
    private void insertData() {
        deleteAll();
        createTestAds().forEach(adScrapperRepository::save);
    }

    private List<Ad> createTestAds() {
        List<LocalDate> updates = List.of(time.now().minusWeeks(5).toLocalDate(), time.now().minusWeeks(4).toLocalDate());
        Ad thisWillBeModifiedInTest = Ad.builder()
                .city("katowice")
                .title("Modify this ad")
                .description("This ad can be modified during tests")
                .price(1800)
                .creationTime(time.now().minusWeeks(5))
                .modificationTime(time.now().minusWeeks(4))
                .gumtreeCreationDate(time.now().minusWeeks(4).toLocalDate())
                .updates(updates)
                .build();

        return List.of(thisWillBeModifiedInTest);
    }
}
