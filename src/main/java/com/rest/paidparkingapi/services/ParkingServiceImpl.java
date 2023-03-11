package com.rest.paidparkingapi.services;

import com.rest.paidparkingapi.dtos.ParkingDto;
import com.rest.paidparkingapi.dtos.VehicleDto;
import com.rest.paidparkingapi.dtos.mappers.ParkingMapper;
import com.rest.paidparkingapi.dtos.mappers.VehicleMapper;
import com.rest.paidparkingapi.entities.Parking;
import com.rest.paidparkingapi.entities.Sale;
import com.rest.paidparkingapi.entities.Vehicle;
import com.rest.paidparkingapi.enums.VehicleType;
import com.rest.paidparkingapi.exceptions.ParkingException;
import com.rest.paidparkingapi.exceptions.ParkingNotAvailableException;
import com.rest.paidparkingapi.exceptions.VehicleNotFoundException;
import com.rest.paidparkingapi.exceptions.VehicleTypeNotFoundException;
import com.rest.paidparkingapi.repositories.ParkingRepository;
import com.rest.paidparkingapi.repositories.SaleRepository;
import com.rest.paidparkingapi.repositories.VehicleRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingServiceImpl implements ParkingService {
    @Value("${parking.cars.capacity}")
    private Integer parkingCarsCapacity;
    @Value("${parking.buses.capacity}")
    private Integer parkingBusesCapacity;
    @Value("${parking.cars.hourly.price}")
    private Integer parkingCarsHourlyPrice;
    @Value("${parking.buses.hourly.price}")
    private Integer parkingBusesHourlyPrice;
    @Value("${parking.cars.daily.price}")
    private Integer parkingCarsDailyPrice;
    @Value("${parking.buses.daily.price}")
    private Integer parkingBusesDailyPrice;
    @Value("${parking.cars.max.hours}")
    private Integer parkingCarsMaxHours;
    @Value("${parking.buses.max.hours}")
    private Integer parkingBusesMaxHours;
    @Autowired
    private ParkingRepository parkingRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private VehicleMapper vehicleMapper;
    @Autowired
    private ParkingMapper parkingMapper;

    public ParkingServiceImpl() {
    }

    @Transactional
    @Override
    public Long enter(@NonNull VehicleDto vehicleDto, Long parkingId) throws ParkingNotAvailableException, VehicleTypeNotFoundException, ParkingException, VehicleNotFoundException {
        Parking parking = checkParking(parkingId);
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDto);
        if (checkIfVehicleEntered(vehicle)) {
            throw new ParkingException("Vehicle already entered!");
        }
        return addVehicleToParking(vehicle, parking);
    }

    @Override
    public void exit(Long parkingId, Long vehicleId) throws VehicleNotFoundException {
        Vehicle vehicle = checkVehicle(vehicleId);
        Parking parking = checkIfVehicleIsInTheParking(parkingId, vehicle);
        parkingCheckout(vehicle, parking);
        calculateVehicleStayCost(vehicle, parking);
    }

    @Override
    public Parking createParking(ParkingDto parkingDto) {
        Parking parking = parkingMapper.toEntity(parkingDto);
        return parkingRepository.save(parking);
    }

    @Override
    public Optional<Parking> getAllVehiclesInParking(Long parkingId) {
        return parkingRepository.findById(parkingId);
    }

    private void calculateVehicleStayCost(Vehicle vehicle, Parking parking) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(vehicle.getEnterDateTime(), now);
        //plus 1 hour, because it starts from 0
        long hoursDuration = duration.toHours()+1;

        BigDecimal stayCost = BigDecimal.ZERO;
        if (VehicleType.CAR.equals(vehicle.getType())) {
            if (hoursDuration > parkingCarsMaxHours) {
                //plus 1, because it starts from 0
                long dailyDuration = duration.toDays()+1;
                stayCost = BigDecimal.valueOf(dailyDuration).multiply(BigDecimal.valueOf(parkingCarsDailyPrice));
            } else {
                stayCost = BigDecimal.valueOf(hoursDuration).multiply(BigDecimal.valueOf(parkingCarsHourlyPrice));
            }
        } else if (VehicleType.BUS.equals(vehicle.getType())) {
            if (hoursDuration > parkingBusesMaxHours) {
                //plus 1, because it starts from 0
                long dailyDuration = duration.toDays()+1;
                stayCost = BigDecimal.valueOf(dailyDuration).multiply(BigDecimal.valueOf(parkingBusesDailyPrice));
            } else {
                stayCost = BigDecimal.valueOf(hoursDuration).multiply(BigDecimal.valueOf(parkingBusesHourlyPrice));
            }
        }

        registerSale(now, stayCost, vehicle.getType(), parking.getParkingId());

        updateParking(parking);

        vehicleRepository.delete(vehicle);
    }

    private void registerSale(LocalDateTime now, BigDecimal stayCost, VehicleType type, Long parkingId) {
        Sale newSale = new Sale();
        newSale.setSaleDate(now);
        newSale.setAmount(stayCost);
        newSale.setVehicleType(type);
        newSale.setParkingId(parkingId);
        saleRepository.save(newSale);
    }

    private Vehicle printTicket(Vehicle vehicle) {
        vehicle.setEnterDateTime(LocalDateTime.now());
        return updateVehicle(vehicle);
    }

    private boolean checkIfVehicleEntered(Vehicle vehicle) throws VehicleNotFoundException {
        if (vehicle.getVehicleId() != null) {
            Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicle.getVehicleId());
            if (optionalVehicle.isEmpty()) {
                throw new VehicleNotFoundException("Vehicle id is provided by sequence!");
            }
            return true;
        }
        return false;
    }

    private void parkingCheckout(Vehicle vehicle, Parking parking) {
        if (VehicleType.CAR.equals(vehicle.getType())) {
            parking.setCarsCurrentCount(parking.getCarsCurrentCount() - 1);
        } else if (VehicleType.BUS.equals(vehicle.getType())) {
            parking.setBusesCurrentCount(parking.getBusesCurrentCount() - 1);
        }
        removeVehicleFromParking(vehicle, parking);
        updateParking(parking);
    }

    private Long addVehicleToParking(Vehicle vehicle, Parking parking) throws ParkingNotAvailableException, VehicleTypeNotFoundException {
        checkParkingAvailability(vehicle, parking);
        vehicle = printTicket(vehicle);
        setVehicleToParking(vehicle, parking);
        updateParking(parking);
        return vehicle.getVehicleId();
    }

    private void checkParkingAvailability(Vehicle vehicle, Parking parking) throws ParkingNotAvailableException, VehicleTypeNotFoundException {
        if (VehicleType.CAR.equals(vehicle.getType())) {
            if (parkingCarsCapacity <= parking.getCarsCurrentCount()) {
                throw new ParkingNotAvailableException("Parking not available!");
            }
            //increment the cars in parking with 1
            parking.setCarsCurrentCount(parking.getCarsCurrentCount() + 1);
        } else if (VehicleType.BUS.equals(vehicle.getType())) {
            if (parkingBusesCapacity <= parking.getBusesCurrentCount()) {
                throw new ParkingNotAvailableException("Parking not available!");
            }
            //increment the buses in parking with 1
            parking.setBusesCurrentCount(parking.getBusesCurrentCount() + 1);
        } else {
            throw new VehicleTypeNotFoundException("Wrong vehicle type!");
        }
    }

    private void setVehicleToParking(Vehicle vehicle, Parking parking) {
        List<Vehicle> vehicles = new ArrayList<>();
        if (parking.getVehicles() != null) {
            vehicles = parking.getVehicles();
        }
        vehicles.add(vehicle);
        parking.setVehicles(vehicles);
    }

    private void removeVehicleFromParking(Vehicle vehicle, Parking parking) {
        List<Vehicle> vehicles = new ArrayList<>();
        if (parking.getVehicles() != null) {
            vehicles = parking.getVehicles();
        }
        vehicles.remove(vehicle);
        parking.setVehicles(vehicles);
    }

    private Parking checkIfVehicleIsInTheParking(Long parkingId, Vehicle vehicle) throws VehicleNotFoundException {
        Optional<Parking> optionalParking = parkingRepository.findParkingByParkingIdAndVehiclesIsContaining(parkingId, vehicle);
        if (optionalParking.isEmpty()) {
            throw new VehicleNotFoundException("Vehicle not found in parking with id " + parkingId);
        }
        return optionalParking.get();
    }

    private Parking checkParking(Long parkingId) throws ParkingException {
        Optional<Parking> optionalParking = parkingRepository.findById(parkingId);
        if (optionalParking.isEmpty()) {
            throw new ParkingException("Parking not found!");
        }
        return optionalParking.get();
    }

    private Vehicle checkVehicle(Long vehicleId) throws VehicleNotFoundException {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleId);
        if (optionalVehicle.isEmpty()) {
            throw new VehicleNotFoundException("Vehicle not found!");
        }
        return optionalVehicle.get();
    }

    private Vehicle updateVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    private void updateParking(Parking parking) {
        parkingRepository.save(parking);
    }
}
