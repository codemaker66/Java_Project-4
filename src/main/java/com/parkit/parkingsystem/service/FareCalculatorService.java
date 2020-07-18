package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		long inHour = ticket.getInTime().getTimeInMillis();
		long outHour = ticket.getOutTime().getTimeInMillis();

		long duration = outHour - inHour;

		if (duration >= 3600000) {

			int h = (int) ((duration / 1000) / 3600);

			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {
				ticket.setPrice((h * Fare.CAR_RATE_PER_HOUR)
						- ((ticket.getDiscount()) ? 0.05 * h * Fare.CAR_RATE_PER_HOUR : 0));
				break;
			}
			case BIKE: {
				ticket.setPrice((h * Fare.BIKE_RATE_PER_HOUR)
						- ((ticket.getDiscount()) ? 0.05 * h * Fare.BIKE_RATE_PER_HOUR : 0));
				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
			}

		} else if (duration < 3600000 && duration > 1800000) {

			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {
				ticket.setPrice((0.75 * Fare.CAR_RATE_PER_HOUR)
						- ((ticket.getDiscount()) ? 0.05 * 0.75 * Fare.CAR_RATE_PER_HOUR : 0));
				break;
			}
			case BIKE: {
				ticket.setPrice((0.75 * Fare.BIKE_RATE_PER_HOUR)
						- ((ticket.getDiscount()) ? 0.05 * 0.75 * Fare.BIKE_RATE_PER_HOUR : 0));
				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
			}

		} else if (duration <= 1800000) {

			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {
				ticket.setPrice(0 * Fare.CAR_RATE_PER_HOUR);
				break;
			}
			case BIKE: {
				ticket.setPrice(0 * Fare.BIKE_RATE_PER_HOUR);
				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
			}

		}

	}
}