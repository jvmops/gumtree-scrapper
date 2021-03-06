package com.jvmops.gumtree.subscriptions;

import com.jvmops.gumtree.subscriptions.model.City;
import com.jvmops.gumtree.subscriptions.model.CityNotFound;
import com.jvmops.gumtree.subscriptions.model.Subscription;
import com.jvmops.gumtree.subscriptions.ports.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
@Slf4j
public class CityService {

    private final CityRepository cityRepository;

    public City start(Subscription subscription) {
        City city = cityRepository.findByNameIgnoreCase(subscription.getCity())
                .orElseThrow(() -> new CityNotFound(subscription.getCity()));
        boolean subscribed = city.subscribe(subscription.getEmail());
        if (subscribed) {
            log.info("{} subscribed for {} report", subscription.getEmail(), city.getName());
            cityRepository.save(city);
        } else {
            log.warn("{} is already subscribed for {} report", subscription.getEmail(), city.getName());
        }

        return city;
    }

    public City cancel(Subscription subscription) {
        City city = cityRepository.findByNameIgnoreCase(subscription.getCity())
                .orElseThrow(() -> new CityNotFound(subscription.getCity()));

        boolean unsubscribed = city.unsubscribe(subscription.getEmail());
        if (unsubscribed) {
            log.info("{} has been unsubscribed from {} report", subscription.getEmail(), city.getName());
            cityRepository.save(city);
        } else {
            log.warn("There is no such email as {} among {} subscribers", subscription.getEmail(), city.getName());
        }

        return city;
    }

    public Set<City> cities() {
        return findAll().collect(Collectors.toSet());
    }

    public City getByName(String city) {
        return cityRepository.findByNameIgnoreCase(city)
                .orElseThrow(() -> new CityNotFound(city));
    }

    /**
     * @param cityUrlCode - gumtree thingy, just check url for apartment listing and you will know
     */
    public City add(String cityName, String cityUrlCode) {
        return cityRepository.save(
                City.builder()
                        .id(ObjectId.get())
                        .name(cityName)
                        .code(cityUrlCode)
                        .build()
        );
    }

    private Stream<City> findAll() {
        var iterable = cityRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public void remove(City city) {
        cityRepository.delete(city);
    }
}


