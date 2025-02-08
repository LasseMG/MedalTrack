package com.codecademy.goldmedal.controller;

import com.codecademy.goldmedal.model.*;
import org.apache.commons.text.WordUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/countries")
public class GoldMedalController {
    private final MedalRepository medalRepository;
    private final CountryRepository countryRepository;
    public GoldMedalController(MedalRepository medalRepository, CountryRepository countryRepository) {
        this.medalRepository = medalRepository;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
    }

    @GetMapping("/{country}")
    public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
        String countryName = WordUtils.capitalizeFully(country);
        return getCountryDetailsResponse(countryName);
    }

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    /**
     * Returns a list of medals from the desired country sorted by an attribute of medal
     * @param countryName medals of the desired country
     * @param sortBy selection of what to sort by
     * @param ascendingOrder true/false asc/desc
     * @return sorted list of medals by country
     */
    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy, boolean ascendingOrder) {
        List<GoldMedal> medalsList;
        switch (sortBy) {
            case "year":
                medalsList = ascendingOrder
                        ? medalRepository.getByCountryOrderByYearAsc(countryName)
                        : medalRepository.getByCountryOrderByYearDesc(countryName);
                break;
            case "season":
                medalsList = ascendingOrder
                        ? medalRepository.getByCountryOrderBySeasonAsc(countryName)
                        : medalRepository.getByCountryOrderBySeasonDesc(countryName);
                break;
            case "city":
                medalsList = ascendingOrder
                        ? medalRepository.getByCountryOrderByCityAsc(countryName)
                        : medalRepository.getByCountryOrderByCityDesc(countryName);
                break;
            case "name":
                medalsList = ascendingOrder
                        ? medalRepository.getByCountryOrderByNameAsc(countryName)
                        : medalRepository.getByCountryOrderByNameDesc(countryName);
                break;
            case "event":
                medalsList = ascendingOrder
                        ? medalRepository.getByCountryOrderByEventAsc(countryName)
                        : medalRepository.getByCountryOrderByEventDesc(countryName);
                break;
            default:
                medalsList = new ArrayList<>();
                break;
        }
        return new CountryMedalsListResponse(medalsList);
    }

    private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        Optional<Country> countryOptional = countryRepository.findByName(countryName);// TODO: get the country; this repository method should return a java.util.Optional
        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }

        Country country = countryOptional.get();
        Integer goldMedalCount = medalRepository.getMedalCountByCountry(countryName);// TODO: get the medal count

        List<GoldMedal> summerWins = medalRepository.getByCountryAndSeasonOrderByYearAsc(countryName, "Summer");// TODO: get the collection of wins at the Summer Olympics, sorted by year in ascending order
        Integer numberSummerWins = !summerWins.isEmpty() ? summerWins.size() : null;
        int totalSummerEvents = medalRepository.getMedalCountBySeason("Summer");// TODO: get the total number of events at the Summer Olympics
        Double percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (double) summerWins.size() / totalSummerEvents : null;
        Integer yearFirstSummerWin = summerWins.size() > 0 ? summerWins.get(0).getYear() : null;

        List<GoldMedal> winterWins = medalRepository.getByCountryAndSeasonOrderByYearAsc(countryName, "Winter");// TODO: get the collection of wins at the Winter Olympics
        Double numberWinterWins = Double.valueOf(!winterWins.isEmpty() ? winterWins.size() : null);
        int totalWinterEvents = medalRepository.getMedalCountBySeason("Winter");// TODO: get the total number of events at the Winter Olympics, sorted by year in ascending order
        var percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
        var yearFirstWinterWin = winterWins.size() > 0 ? winterWins.get(0).getYear() : null;

        var numberEventsWonByFemaleAthletes = // TODO: get the number of wins by female athletes
        var numberEventsWonByMaleAthletes = // TODO: get the number of wins by male athletes

        return new CountryDetailsResponse(
                countryName,
                country.getGdp(),
                country.getPopulation(),
                goldMedalCount,
                numberSummerWins,
                percentageTotalSummerWins,
                yearFirstSummerWin,
                numberWinterWins,
                percentageTotalWinterWins,
                yearFirstWinterWin,
                numberEventsWonByFemaleAthletes,
                numberEventsWonByMaleAthletes);
    }

    private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
        List<Country> countries;
        switch (sortBy) {
            case "name":
                countries = // TODO: list of countries sorted by name in the given order
                break;
            case "gdp":
                countries = // TODO: list of countries sorted by gdp in the given order
                break;
            case "population":
                countries = // TODO: list of countries sorted by population in the given order
                break;
            case "medals":
            default:
                countries = // TODO: list of countries in any order you choose; for sorting by medal count, additional logic below will handle that
                break;
        }

        var countrySummaries = getCountrySummariesWithMedalCount(countries);

        if (sortBy.equalsIgnoreCase("medals")) {
            countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
        }

        return countrySummaries;
    }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ?
                        t1.getMedals() - t2.getMedals() :
                        t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
        for (var country : countries) {
            var goldMedalCount = // TODO: get count of medals for the given country
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }


    @Override
    public <S extends Country> S save(S s) {
        return null;
    }

    @Override
    public <S extends Country> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Country> findById(GoldMedal goldMedal) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(GoldMedal goldMedal) {
        return false;
    }

    @Override
    public Iterable<Country> findAll() {
        return this.medalRepository.findAll();
    }

    @Override
    public Iterable<Country> findAllById(Iterable<GoldMedal> iterable) {
        return this.medalRepository.findAll();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(GoldMedal goldMedal) {

    }

    @Override
    public void delete(Country country) {

    }

    @Override
    public void deleteAllById(Iterable<? extends GoldMedal> iterable) {

    }

    @Override
    public void deleteAll(Iterable<? extends Country> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<GoldMedal> getByCountryOrderByYearAsc(String country) {
        return null;
    }

    @Override
    public List<GoldMedal> getByCountryOrderByYearDesc(String country) {
        return null;
    }

    @Override
    public List<GoldMedal> getByCountryOrderByCityAsc(String country) {
        return null;
    }

    @Override
    public List<GoldMedal> getByCountryOrderByCityDesc(String country) {
        return null;
    }

    @Override
    public List<GoldMedal> getByCountryOrderBySeasonAsc(String country) {
        return null;
    }

    @Override
    public List<GoldMedal> getByCountryOrderBySeasonDesc(String country) {
        return null;
    }

    @Override
    public List<GoldMedal> getByCountryOrderByEventAsc(String country) {
        return null;
    }

    @Override
    public List<GoldMedal> getByCountryOrderByEventDesc(String country) {
        return null;
    }

    @Override
    public List<GoldMedal> getByCountryOrderByNameAsc(String country) {
        return null;
    }

    @Override
    public List<GoldMedal> getByCountryOrderByNameDesc(String country) {
        return null;
    }
}
