package com.codecademy.goldmedal.model;

import java.util.List;

public interface CountryRepository {
    List<Country> getAllOrderByNameAsc();
    List<Country> getAllOrderByNameDesc();
    List<Country> getAllOrderByGdpAsc();
    List<Country> getAllOrderByGdpDesc();
    List<Country> getAllOrderByPopulationAsc();
    List<Country> getAllOrderByPopulationDesc();

}
