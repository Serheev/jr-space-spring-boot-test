package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class ShipServiceImpl implements ShipService {

    private static final Calendar GREGORIAN_YEAR_2800 = new GregorianCalendar(2800, 1, 1);
    private static final Calendar GREGORIAN_YEAR_3019 = new GregorianCalendar(3019, 1, 1);
    private static final Calendar GREGORIAN_YEAR_3020 = new GregorianCalendar(3020, 1, 1);

    ShipRepository shipRepository;

    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public Ship create(Ship ship) {
        toValidIsUsed(ship);
        if (isValidRequiredData(ship)) {
            ship.setSpeed(mathRoundDouble(ship.getSpeed()));
            ship.setRating(shipRatingGeneration(ship));
            return shipRepository.save(ship);
        }
        return null;
    }

    @Override
    public List<Ship> readAll(String name, String planet, ShipType shipType, Long after, Long before,
                              Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                              Double minRating, Double maxRating, String order, Integer pageNumber, Integer pageSize) {
        return shipRepository.findAll(
                ShipSpecification.getShipSpecification(name, planet, shipType, after, before, isUsed, minSpeed,
                        maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating),
                PageRequest.of(pageNumber, pageSize, Sort.by(ShipOrder.valueOf(order).getFieldName()))
        ).getContent();
    }

    @Override
    public Long countAll(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                         Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating,
                         Double maxRating
    ) {
        return shipRepository.count(
                ShipSpecification.getShipSpecification(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed,
                        minCrewSize, maxCrewSize, minRating, maxRating));
    }

    @Override
    public Ship read(Long id) {
        return shipRepository.findById(id).orElse(null);
    }

    @Override
    public Ship update(Ship ship, Long id) {
        if (shipRepository.existsById(id)) {
            ship.setId(read(id).getId());

            if (ship.getName() != null) ship.setName(ship.getName());
            else ship.setName(read(id).getName());
            if (ship.getPlanet() != null) ship.setPlanet(ship.getPlanet());
            else ship.setPlanet(read(id).getPlanet());
            if (ship.getShipType() != null) ship.setShipType(ship.getShipType());
            else ship.setShipType(read(id).getShipType());
            if (ship.getProdDate() != null) ship.setProdDate(ship.getProdDate());
            else ship.setProdDate(read(id).getProdDate());
            if (ship.isUsed() != null) ship.setIsUsed(ship.isUsed());
            else ship.setIsUsed(read(id).isUsed());
            if (ship.getSpeed() != null) ship.setSpeed(mathRoundDouble(ship.getSpeed()));
            else ship.setSpeed(read(id).getSpeed());
            if (ship.getCrewSize() != null) ship.setCrewSize(ship.getCrewSize());
            else ship.setCrewSize(read(id).getCrewSize());

            if (ship.isUsed() != null && ship.getProdDate() != null && ship.getSpeed() != null) {
                ship.setRating(shipRatingGeneration(ship));
            }

            return shipRepository.save(ship);
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        if (shipRepository.existsById(id)) {
            shipRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean isValidRequiredData(Ship ship) {
        if (isValidName(ship.getName())
                && isValidName(ship.getPlanet())
                && isValidShipType(ship.getShipType())
                && isValidProdDate(ship.getProdDate().getTime())
                && isValidSpeed(ship.getSpeed())
                && isValidCrewSize(ship.getCrewSize())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isValidExistingData(Ship ship) {
        if (ship.getName() != null && !isValidName(ship.getName())) return false;
        if (ship.getPlanet() != null && !isValidName(ship.getPlanet())) return false;
        if (ship.getShipType() != null && !isValidShipType(ship.getShipType())) return false;
        if (ship.getProdDate() != null && !isValidProdDate(ship.getProdDate().getTime())) return false;
        if (ship.getSpeed() != null && !isValidSpeed(ship.getSpeed())) return false;
        if (ship.getCrewSize() != null && !isValidCrewSize(ship.getCrewSize())) return false;
        return true;
    }

    private boolean isValidName(String name) {
        return name != null && !name.equals("") && name.length() <= 50;
    }

    private boolean isValidShipType(ShipType shipType) {
        return shipType != null && Arrays.asList(ShipType.values()).contains(shipType);
    }

    private boolean isValidProdDate(Long prodDate) {
        return prodDate != null && prodDate >= GREGORIAN_YEAR_2800.getTimeInMillis() && prodDate <= GREGORIAN_YEAR_3020.getTimeInMillis();
    }

    private void toValidIsUsed(Ship ship) {
        if (ship.isUsed() == null) ship.setIsUsed(false);
    }

    private boolean isValidSpeed(Double speed) {
        return speed != null && speed >= 0.01d && speed <= 0.99d;
    }

    private double mathRoundDouble(double d) {
        return new BigDecimal(d).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private boolean isValidCrewSize(Integer crewSize) {
        return crewSize != null && crewSize >= 1 && crewSize <= 9999;
    }

    private double shipRatingGeneration(Ship ship) {
        double kUsed = ship.isUsed() ? 0.5 : 1;
        int currentYear = GREGORIAN_YEAR_3019.get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ship.getProdDate());
        int productionYear = calendar.get(Calendar.YEAR);

        return mathRoundDouble((80 * ship.getSpeed() * kUsed) / (currentYear - productionYear + 1));
    }
}
