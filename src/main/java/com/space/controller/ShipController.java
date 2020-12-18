package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("rest/ships")
public class ShipController {

    private final ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping
    public ResponseEntity<List<Ship>> read(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String planet,
            @RequestParam(required = false) ShipType shipType,

            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,

            @RequestParam(required = false) Boolean isUsed,

            @RequestParam(required = false) Double minSpeed,
            @RequestParam(required = false) Double maxSpeed,

            @RequestParam(required = false) Integer minCrewSize,
            @RequestParam(required = false) Integer maxCrewSize,

            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating,

            @RequestParam(defaultValue = "ID") String order,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "3") Integer pageSize
    ) {
        final List<Ship> ships = shipService.readAll(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize,
                maxCrewSize, minRating, maxRating, order, pageNumber, pageSize);

        return ships != null && !ships.isEmpty()
                ? new ResponseEntity<>(ships, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("count")
    public ResponseEntity<Long> countAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String planet,
            @RequestParam(required = false) ShipType shipType,

            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,

            @RequestParam(required = false) Boolean isUsed,

            @RequestParam(required = false) Double minSpeed,
            @RequestParam(required = false) Double maxSpeed,

            @RequestParam(required = false) Integer minCrewSize,
            @RequestParam(required = false) Integer maxCrewSize,

            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating
    ) {
        final Long count = shipService.countAll(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize,
                maxCrewSize, minRating, maxRating);

        return count != null
                ? new ResponseEntity<>(count, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ship> findById(@PathVariable Long id) {
        if (id <= 0 || !(id instanceof Long)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        final Ship ship = shipService.read(id);

        return ship != null && id >= 0
                ? new ResponseEntity<>(ship, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Ship> save(@RequestBody Ship newShip) {
        final Ship ship = shipService.create(newShip);

        return ship != null
                ? new ResponseEntity<>(ship, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        if (id <= 0 || !(id instanceof Long)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        final boolean isDelete = shipService.delete(id);

        return isDelete
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Ship> update(
            @RequestBody(required = false) Ship ship,
            @PathVariable Long id
    ) {
        if (id <= 0 || !(id instanceof Long) || !shipService.isValidExistingData(ship))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        final Ship updatedShip = shipService.update(ship, id);

        return updatedShip != null
                ? new ResponseEntity<>(updatedShip, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
