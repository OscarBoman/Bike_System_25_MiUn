package com.bikeshare.lab3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bikeshare.model.Bike;
import com.bikeshare.model.Station;
import com.bikeshare.model.Bike.BikeStatus;
import com.bikeshare.model.Bike.BikeType;
import com.bikeshare.model.Station.StationStatus;

public class StationBikeIntegrationTest {

    @Test
    @DisplayName("Test add bike to station")
    void addBikeToStationTest() {
        Station station = new Station("1", "Station 1", "World 1", 10, StationStatus.ACTIVE);
        Bike bike = new Bike(BikeStatus.AVAILABLE, BikeType.ELECTRIC, "1");
        station.addBike(bike);
        assertEquals("1", bike.getCurrentStationId());
        assertEquals(1, station.getTotalBikeCount());
    }

    @Test
    @DisplayName("Test add bike with wrong state to station")
    void wrongStateBike_throwsException() {
        Station station = new Station("1", "Station 1", "World 1", 10, StationStatus.ACTIVE);
        Bike bike = new Bike(BikeStatus.IN_USE, BikeType.ELECTRIC, "1");
        assertThrows(IllegalStateException.class, () -> {
            station.addBike(bike);
        });

        assertEquals(0, station.getTotalBikeCount());
        assertEquals(null, bike.getCurrentStationId());

    }

    @Test
    @DisplayName("test exception add duplicated bike to station")
    void duplicatedBikeAddStation_throwsException() {
        Station station = new Station("1", "Station 1", "World 1", 10, StationStatus.ACTIVE);
        Bike bike2 = new Bike(BikeStatus.AVAILABLE, BikeType.ELECTRIC, "2");
        station.addBike(bike2);

        assertThrows(IllegalStateException.class, () -> {
            station.addBike(bike2);
        });

        assertEquals(1, station.getTotalBikeCount());
        assertEquals("1", bike2.getCurrentStationId());
    }

    @Test
    @DisplayName("Adding bike to inactive station throws exception")
    void addBikeToInactiveStation_throwsException() {
        // Arrange
        Station inactiveStation = new Station("1", "Station 1", "World 1", 10, StationStatus.INACTIVE);
        Bike bike = new Bike(BikeStatus.AVAILABLE, BikeType.ELECTRIC, "1");

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            inactiveStation.addBike(bike);
        });

        assertEquals(0, inactiveStation.getTotalBikeCount());
        assertEquals(null, bike.getCurrentStationId());
    }

    @Test
    @DisplayName("Adding bike to full station throws exception")
    void addBikeToFullStation_throwsException() {

        Station activeStation = new Station("2", "Station 2", "World 1", 1, StationStatus.ACTIVE);
        Bike bike1 = new Bike(BikeStatus.AVAILABLE, BikeType.ELECTRIC, "1");
        Bike bike2 = new Bike(BikeStatus.AVAILABLE, BikeType.ELECTRIC, "2");

        activeStation.addBike(bike1);

        assertThrows(IllegalStateException.class, () -> {
            activeStation.addBike(bike2);
        });

        assertEquals(1, activeStation.getTotalBikeCount());
        assertEquals(null, bike2.getCurrentStationId());
    }

}
