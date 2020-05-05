package com.jvmops.gumtree.report;

import com.jvmops.gumtree.city.City;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Slf4j
@Builder
class ApartmentReport {
    private City city;
    private List<Ad> newApartments;
    private List<Ad> gasApartments;
    private List<Ad> dishwasherApartments;
    private List<Ad> dishwasherAndGasApartments;
    private List<Ad> cheapestApartments;
}
