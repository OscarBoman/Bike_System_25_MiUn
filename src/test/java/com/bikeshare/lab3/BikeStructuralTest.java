package com.bikeshare.lab3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bikeshare.model.Bike;
import com.bikeshare.model.Bike.BikeStatus;
import com.bikeshare.model.Bike.BikeType;

public class BikeStructuralTest {
    @Test
    @DisplayName("test charging battery")
    void validChargingBattery() {

        Bike bike = new Bike(50, BikeType.ELECTRIC);
        double chargeAmount = 25.2;

        bike.chargeBattery(chargeAmount);

        assertEquals(75.2, bike.getBatteryLevel());
    }

    @Test
    @DisplayName("Test exceptions charging battery")
    void invalidChargingBike() {
        Bike bike = new Bike(100, BikeType.ELECTRIC);
        Bike bike2 = new Bike(50, BikeType.STANDARD);

        double chargeAmount = 25;
        double chargeAmount2 = 110;

        assertThrows(IllegalStateException.class, () -> {
            bike2.chargeBattery(chargeAmount);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            bike.chargeBattery(chargeAmount2);
        });

        assertEquals(100, bike.getBatteryLevel());
        assertEquals(50, bike2.getBatteryLevel());
    }

    @Test
    @DisplayName("Test mark as broken method")
    void markAsBrokenTest() {
        Bike bike = new Bike(BikeStatus.AVAILABLE);
        bike.markAsBroken();
        assertEquals(BikeStatus.BROKEN, bike.getStatus());
        assertEquals(true, bike.needsMaintenance());

    }

    @Test
    @DisplayName("test valid completeMaintenance")
    void validMaintenenceTest() {
        Bike bike = new Bike(BikeStatus.MAINTENANCE, BikeType.ELECTRIC);
        bike.completeMaintenance();
        assertEquals(BikeStatus.AVAILABLE, bike.getStatus());
        assertEquals(false, bike.needsMaintenance());
        assertEquals(100, bike.getBatteryLevel());

    }

    @Test
    @DisplayName("test invalid status completeMaintenance")
    void invalidMaintenanceTest() {
        Bike bike = new Bike(BikeStatus.AVAILABLE, BikeType.ELECTRIC, 50);
        assertThrows(IllegalStateException.class, () -> {
            bike.completeMaintenance();
        });
        assertEquals(50, bike.getBatteryLevel());
    }

    @Test
    @DisplayName("Valid send to maintanence teset")
    void validSendToMaintenence() {
        Bike bike = new Bike(BikeStatus.AVAILABLE);
        bike.sendToMaintenance();
        assertEquals(BikeStatus.MAINTENANCE, bike.getStatus());
    }

    @Test
    @DisplayName("Invalid bike in use test ")
    void invalidSendToMaintenence() {
        Bike bike = new Bike(BikeStatus.IN_USE);
        assertThrows(IllegalStateException.class, () -> {
            bike.sendToMaintenance();
        });
        assertEquals(BikeStatus.IN_USE, bike.getStatus());
    }

    @Test
    @DisplayName("end ride electric bike")
    void endRideElectricTest() {
        Bike electricBike = new Bike(BikeStatus.IN_USE, BikeType.ELECTRIC);
        double distanceTraveled = 50;
        electricBike.endRide(distanceTraveled);

        assertEquals(BikeStatus.AVAILABLE, electricBike.getStatus());
        assertEquals(0, electricBike.getBatteryLevel());
        assertEquals(1, electricBike.getTotalRides());
        assertEquals(50, electricBike.getTotalDistance());
    }

    @Test
    @DisplayName("invalid status bike endRide")
    void invalidEndRide() {
        Bike bike = new Bike(BikeStatus.AVAILABLE);
        Bike bike1 = new Bike(BikeStatus.IN_USE);
        assertThrows(IllegalStateException.class, () -> {
            bike.endRide(25);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            bike1.endRide(-24);
        });
    }

    @Test
    @DisplayName("valid start ride test")
    void startRideTest() {
        Bike bike = new Bike(BikeStatus.AVAILABLE);
        bike.startRide();
        assertEquals(BikeStatus.IN_USE, bike.getStatus());
    }

    @Test
    @DisplayName("illegal state start ride test")
    void illegalStateStartRideTest() {
        Bike bike = new Bike(BikeStatus.BROKEN);
        assertThrows(IllegalStateException.class, () -> {
            bike.startRide();
        });
        assertEquals(BikeStatus.BROKEN, bike.getStatus());
    }

    @Test
    @DisplayName("low battery test start ride")
    void lowBatteryStartRideTest() {
        Bike bike = new Bike(BikeStatus.AVAILABLE, BikeType.ELECTRIC, 5);
        assertThrows(IllegalStateException.class, () -> {
            bike.startRide();
        });
        assertEquals(BikeStatus.AVAILABLE, bike.getStatus());
    }

    @Test
    @DisplayName("valid and invalid state reserve bike")
    void reserveBikeTest() {
        Bike bike1 = new Bike(BikeStatus.AVAILABLE);
        Bike bike2 = new Bike(BikeStatus.IN_USE);

        bike1.reserve();
        assertEquals(BikeStatus.RESERVED, bike1.getStatus());

        assertThrows(IllegalStateException.class, () -> {
            bike2.reserve();
        });

        assertEquals(BikeStatus.IN_USE, bike2.getStatus());
    }
}
