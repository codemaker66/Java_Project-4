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
    public void calculateFareCar(){
    	Calendar inTime = Calendar.getInstance();
        long ms = System.currentTimeMillis() - (  60 * 60 * 1000);
        inTime.setTimeInMillis(ms);
        Calendar outTime = Calendar.getInstance();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }

    @Test
    public void calculateFareBike(){
    	Calendar inTime = Calendar.getInstance();
        long ms = System.currentTimeMillis() - (  60 * 60 * 1000);
        inTime.setTimeInMillis(ms);
        Calendar outTime = Calendar.getInstance();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
    }

    @Test
    public void calculateFareUnkownType(){
    	Calendar inTime = Calendar.getInstance();
        long ms = System.currentTimeMillis() - (  60 * 60 * 1000);
        inTime.setTimeInMillis(ms);
        Calendar outTime = Calendar.getInstance();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
    	Calendar inTime = Calendar.getInstance();
        long ms = System.currentTimeMillis() + (  60 * 60 * 1000);
        inTime.setTimeInMillis(ms);
        Calendar outTime = Calendar.getInstance();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
    	Calendar inTime = Calendar.getInstance();
        long ms = System.currentTimeMillis() - (  45 * 60 * 1000);
        inTime.setTimeInMillis(ms);
        Calendar outTime = Calendar.getInstance();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
    	Calendar inTime = Calendar.getInstance();
        long ms = System.currentTimeMillis() - (  45 * 60 * 1000);
        inTime.setTimeInMillis(ms);
        Calendar outTime = Calendar.getInstance();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
    	Calendar inTime = Calendar.getInstance();
        long ms = System.currentTimeMillis() - (  24 * 60 * 60 * 1000);
        inTime.setTimeInMillis(ms);
        Calendar outTime = Calendar.getInstance();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }
    
    @Test
    public void calculateFareCarUnderThirtyMinutes(){
    	Calendar inTime = Calendar.getInstance();
        long ms = System.currentTimeMillis() - ( 30 * 60 * 1000);
        inTime.setTimeInMillis(ms);
        Calendar outTime = Calendar.getInstance();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }
    
    @Test
    public void calculateFareBikeUnderThirtyMinutes(){
    	Calendar inTime = Calendar.getInstance();
        long ms = System.currentTimeMillis() - ( 30 * 60 * 1000);
        inTime.setTimeInMillis(ms);
        Calendar outTime = Calendar.getInstance();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0 * Fare.BIKE_RATE_PER_HOUR) , ticket.getPrice());
    }
    
    @Test
    public void calculateFareCarWithFivePercentDiscount(){
    	Calendar inTime = Calendar.getInstance();
        long ms = System.currentTimeMillis() - (  60 * 60 * 1000);
        inTime.setTimeInMillis(ms);
        Calendar outTime = Calendar.getInstance();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        FareCalculatorService.discount = true;
        fareCalculatorService.calculateFare(ticket);
        FareCalculatorService.discount = false;
        assertEquals( (Fare.CAR_RATE_PER_HOUR)  - (0.05 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithFivePercentDiscount(){
    	Calendar inTime = Calendar.getInstance();
        long ms = System.currentTimeMillis() - (  60 * 60 * 1000);
        inTime.setTimeInMillis(ms);
        Calendar outTime = Calendar.getInstance();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        FareCalculatorService.discount = true;
        fareCalculatorService.calculateFare(ticket);
        FareCalculatorService.discount = false;
        assertEquals((Fare.BIKE_RATE_PER_HOUR) - (0.05 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }
    
    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTimeAndWithFivePercentDiscount(){
    	Calendar inTime = Calendar.getInstance();
        long ms = System.currentTimeMillis() - (  45 * 60 * 1000);
        inTime.setTimeInMillis(ms);
        Calendar outTime = Calendar.getInstance();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        FareCalculatorService.discount = true;
        fareCalculatorService.calculateFare(ticket);
        FareCalculatorService.discount = false;
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR) - (0.05 * 0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTimeAndWithFivePercentDiscount(){
    	Calendar inTime = Calendar.getInstance();
        long ms = System.currentTimeMillis() - (  45 * 60 * 1000);
        inTime.setTimeInMillis(ms);
        Calendar outTime = Calendar.getInstance();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        FareCalculatorService.discount = true;
        fareCalculatorService.calculateFare(ticket);
        FareCalculatorService.discount = false;
        assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR) - (0.05 * 0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

}
