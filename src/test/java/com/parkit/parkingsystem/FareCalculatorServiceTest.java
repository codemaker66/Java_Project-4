package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Test
	public void calculateFareCar() {
		// Arrange
		Calendar inTime = Calendar.getInstance();
		inTime.setTimeInMillis(System.currentTimeMillis() - (60 * 60 * 1000));
		Calendar outTime = Calendar.getInstance();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// Act
		fareCalculatorService.calculateFare(ticket);

		// Assert
		assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareBike() {
		// Arrange
		Calendar inTime = Calendar.getInstance();
		inTime.setTimeInMillis(System.currentTimeMillis() - (60 * 60 * 1000));
		Calendar outTime = Calendar.getInstance();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// Act
		fareCalculatorService.calculateFare(ticket);

		// Assert
		assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareUnkownType() {
		// Arrange
		Calendar inTime = Calendar.getInstance();
		inTime.setTimeInMillis(System.currentTimeMillis() - (60 * 60 * 1000));
		Calendar outTime = Calendar.getInstance();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// Assert
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void calculateFareBikeWithFutureInTime() {
		// Arrange
		Calendar inTime = Calendar.getInstance();
		inTime.setTimeInMillis(System.currentTimeMillis() + (60 * 60 * 1000));
		Calendar outTime = Calendar.getInstance();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// Assert
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void calculateFareBikeWithLessThanOneHourParkingTime() {
		// Arrange
		Calendar inTime = Calendar.getInstance();
		inTime.setTimeInMillis(System.currentTimeMillis() - (45 * 60 * 1000));
		Calendar outTime = Calendar.getInstance();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// Act
		fareCalculatorService.calculateFare(ticket);

		// Assert
		assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithLessThanOneHourParkingTime() {
		// Arrange
		Calendar inTime = Calendar.getInstance();
		inTime.setTimeInMillis(System.currentTimeMillis() - (45 * 60 * 1000));
		Calendar outTime = Calendar.getInstance();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// Act
		fareCalculatorService.calculateFare(ticket);

		// Assert
		assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithMoreThanADayParkingTime() {
		// Arrange
		Calendar inTime = Calendar.getInstance();
		inTime.setTimeInMillis(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
		Calendar outTime = Calendar.getInstance();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// Act
		fareCalculatorService.calculateFare(ticket);

		// Assert
		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarUnderThirtyMinutes() {
		// Arrange
		Calendar inTime = Calendar.getInstance();
		inTime.setTimeInMillis(System.currentTimeMillis() - (30 * 60 * 1000));
		Calendar outTime = Calendar.getInstance();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// Act
		fareCalculatorService.calculateFare(ticket);

		// Assert
		assertEquals((0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareBikeUnderThirtyMinutes() {
		// Arrange
		Calendar inTime = Calendar.getInstance();
		inTime.setTimeInMillis(System.currentTimeMillis() - (30 * 60 * 1000));
		Calendar outTime = Calendar.getInstance();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// Act
		fareCalculatorService.calculateFare(ticket);

		// Assert
		assertEquals((0 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithFivePercentDiscount() {
		// Arrange
		Calendar inTime = Calendar.getInstance();
		inTime.setTimeInMillis(System.currentTimeMillis() - (60 * 60 * 1000));
		Calendar outTime = Calendar.getInstance();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setDiscount(true);

		// Act
		fareCalculatorService.calculateFare(ticket);

		// Assert
		assertEquals(Fare.CAR_RATE_PER_HOUR - (0.05 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareBikeWithFivePercentDiscount() {
		// Arrange
		Calendar inTime = Calendar.getInstance();
		inTime.setTimeInMillis(System.currentTimeMillis() - (60 * 60 * 1000));
		Calendar outTime = Calendar.getInstance();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setDiscount(true);

		// Act
		fareCalculatorService.calculateFare(ticket);

		// Assert
		assertEquals(Fare.BIKE_RATE_PER_HOUR - (0.05 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithLessThanOneHourParkingTimeAndWithFivePercentDiscount() {
		// Arrange
		Calendar inTime = Calendar.getInstance();
		inTime.setTimeInMillis(System.currentTimeMillis() - (45 * 60 * 1000));
		Calendar outTime = Calendar.getInstance();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setDiscount(true);

		// Act
		fareCalculatorService.calculateFare(ticket);

		// Assert
		assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR) - (0.05 * 0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareBikeWithLessThanOneHourParkingTimeAndWithFivePercentDiscount() {
		// Arrange
		Calendar inTime = Calendar.getInstance();
		inTime.setTimeInMillis(System.currentTimeMillis() - (45 * 60 * 1000));
		Calendar outTime = Calendar.getInstance();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setDiscount(true);

		// Act
		fareCalculatorService.calculateFare(ticket);

		// Assert
		assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR) - (0.05 * 0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

}
