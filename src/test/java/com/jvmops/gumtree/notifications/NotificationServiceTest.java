package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.subscriptions.City;
import com.jvmops.gumtree.subscriptions.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

//TODO: improve this test
@SpringBootTest(classes = Main.class)
class NotificationServiceTest extends DataInitializer {
    public static final City KATOWICE = new City("Katowice");

    @Autowired
    private ApartmentReportFactory apartmentReportFactory;
    @Mock
    private CityService cityService;
    @Mock
    private NotificationSender notificationSender;

    private NotificationService notificationService;

    @BeforeEach
    void setup() {
        notificationService = new NotificationService(apartmentReportFactory, notificationSender, cityService);
    }

    @Test
    void subscribers_can_be_notified() {
        Mockito.when(cityService.cities())
                .thenReturn(Set.of(KATOWICE));

        notificationService.notifySubscribers();

        Mockito.verify(notificationSender).notifySubscribers(Mockito.any());
    }

    @Test
    void initial_email_can_be_send() {
        Mockito.when(cityService.cities())
                .thenReturn(Set.of(KATOWICE));

        notificationService.initialEmail(KATOWICE, "test@gmail.com");

        Mockito.verify(notificationSender).initialEmail(
                Mockito.any(),
                Mockito.eq("test@gmail.com")
        );
    }
}