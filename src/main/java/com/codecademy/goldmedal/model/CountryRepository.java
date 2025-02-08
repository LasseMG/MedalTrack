package com.codecademy.goldmedal.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends CrudRepository<Country, Integer> {
    List<Country> getAllOrderByNameAsc();
    List<Country> getAllOrderByNameDesc();
    List<Country> getAllOrderByGdpAsc();
    List<Country> getAllOrderByGdpDesc();
    List<Country> getAllOrderByPopulationAsc();
    List<Country> getAllOrderByPopulationDesc();
    Optional<Country> findByName(String name);

}
