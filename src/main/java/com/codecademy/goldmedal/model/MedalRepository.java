package com.codecademy.goldmedal.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MedalRepository extends CrudRepository<Country, GoldMedal> {
List<GoldMedal> getByCountryOrderByYearAsc(String country);
List<GoldMedal> getByCountryOrderByYearDesc(String country);
List<GoldMedal> getByCountryOrderByCityAsc(String country);
List<GoldMedal> getByCountryOrderByCityDesc(String country);
List<GoldMedal> getByCountryOrderBySeasonAsc(String country);
List<GoldMedal> getByCountryOrderBySeasonDesc(String country);
List<GoldMedal> getByCountryOrderByEventAsc(String country);
List<GoldMedal> getByCountryOrderByEventDesc(String country);
List<GoldMedal> getByCountryOrderByNameAsc(String country);
List<GoldMedal> getByCountryOrderByNameDesc(String country);
}
