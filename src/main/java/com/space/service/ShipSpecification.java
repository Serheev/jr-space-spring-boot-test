package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class ShipSpecification {
    public static Specification<Ship> create() {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("id"));
    }

    public static Specification<Ship> likeName(String likeName) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + likeName + "%");
    }

    public static Specification<Ship> likePlanet(String likePlanet) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("planet"), "%" + likePlanet + "%");
    }

    public static Specification<Ship> equalShipType(ShipType shipType) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("shipType"), shipType);
    }

    public static Specification<Ship> greaterThanOrEqualToProdDate(Date after) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), after);
    }

    public static Specification<Ship> lessThanOrEqualToProdDate(Date before) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), before);
    }

    public static Specification<Ship> greaterThanOrEqualToSpeed(Double minSpeed) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), minSpeed);
    }


    public static Specification<Ship> lessThanOrEqualToSpeed(Double maxSpeed) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("speed"), maxSpeed);
    }

    public static Specification<Ship> greaterThanOrEqualToCrewSize(Integer minCrewSize) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize);
    }

    public static Specification<Ship> lessThanOrEqualToCrewSize(Integer maxCrewSize) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize);
    }

    public static Specification<Ship> greaterThanOrEqualToRating(Double minRating) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating);
    }


    public static Specification<Ship> lessThanOrEqualToRating(Double maxRating) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating);
    }

    public static Specification<Ship> isUsed(Boolean isUsed) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isUsed"), isUsed);
    }


    public static Specification<Ship> getShipSpecification(
            String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed,
            Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating
    ) {

        Specification<Ship> specification = ShipSpecification.create();

        specification = name != null ? specification.and(ShipSpecification.likeName(name)) : specification;
        specification = planet != null ? specification.and(ShipSpecification.likePlanet(planet)) : specification;
        specification = shipType != null ? specification.and(ShipSpecification.equalShipType(shipType)) : specification;

        specification = after != null ? specification.and(ShipSpecification.greaterThanOrEqualToProdDate(new Date(after))) : specification;
        specification = before != null ? specification.and(ShipSpecification.lessThanOrEqualToProdDate(new Date(before))) : specification;

        specification = minSpeed != null ? specification.and(ShipSpecification.greaterThanOrEqualToSpeed(minSpeed)) : specification;
        specification = maxSpeed != null ? specification.and(ShipSpecification.lessThanOrEqualToSpeed(maxSpeed)) : specification;

        specification = minCrewSize != null ? specification.and(ShipSpecification.greaterThanOrEqualToCrewSize(minCrewSize)) : specification;
        specification = maxCrewSize != null ? specification.and(ShipSpecification.lessThanOrEqualToCrewSize(maxCrewSize)) : specification;

        specification = minRating != null ? specification.and(ShipSpecification.greaterThanOrEqualToRating(minRating)) : specification;
        specification = maxRating != null ? specification.and(ShipSpecification.lessThanOrEqualToRating(maxRating)) : specification;

        specification = isUsed != null ? specification.and(ShipSpecification.isUsed(isUsed)) : specification;

        return specification;
    }
}
