package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;

import java.util.List;

public interface ShipService {
    /**
     * Creates and returns a new ship
     *
     * @param ship - ship to create
     * @return
     */
    Ship create(Ship ship);

    /**
     * Returns a list of all available ships
     *
     * @return list of ships
     */
    List<Ship> readAll(String name, String planet, ShipType shipType, Long after, Long before,
                       Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                       Double minRating, Double maxRating, String order, Integer pageNumber, Integer pageSize);

    /**
     * Returns the number of all available ships
     *
     * @return number of ships
     */
    Long countAll(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                  Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating,
                  Double maxRating
    );

    /**
     * Returns the ship by its ID
     *
     * @param id - Ship ID
     * @return - ship object with a given ID
     */
    Ship read(Long id);

    /**
     * Updates the data of the ship with the given ID,
     * according to the transmitted data
     *
     * @param ship - ship, according to which the data needs to be updated
     * @param id   - id of the ship that needs to be updated
     * @return - true if the data has been updated, otherwise false
     */
    Ship update(Ship ship, Long id);

    /**
     * Deletes the ship with the given ID
     *
     * @param id - ship id to be deleted
     * @return - true if the ship was removed, otherwise false
     */
    boolean delete(Long id);

    /**
     * Checks the data for correctness, as well as the presence of all required data
     *
     * @param ship
     * @return true if the data is correct
     */
    boolean isValidRequiredData(Ship ship);

    /**
     * Checks existing data for correctness only
     *
     * @param ship
     * @return true only if all existing data is correct
     */
    boolean isValidExistingData(Ship ship);
}
