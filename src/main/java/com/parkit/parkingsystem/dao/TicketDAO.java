package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;

public class TicketDAO {

	private static final Logger logger = LogManager.getLogger("TicketDAO");

	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	/**
	 * This method save the vehicle ticket in the database.
	 * 
	 * @param ticket contain an object of type Ticket.
	 * @return true if the ticket was correctly saved in the database.
	 */
	public boolean saveTicket(Ticket ticket) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.SAVE_TICKET);
			ps.setInt(1, ticket.getParkingSpot().getId());
			ps.setString(2, ticket.getVehicleRegNumber());
			ps.setDouble(3, ticket.getPrice());
			ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTimeInMillis()));
			ps.setTimestamp(5,
					(ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTimeInMillis())));
			ps.setBoolean(6, ticket.getDiscount());
			result = ps.execute();
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
			dataBaseConfig.closePreparedStatement(ps);
		}
		return result;
	}

	/**
	 * This method retrieve the ticket of the given vehicle registration number.
	 * 
	 * @param vehicleRegNumber contain a string that represent the vehicle registration number.
	 * @return the ticket of the given vehicle.
	 */
	public Ticket getTicket(String vehicleRegNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Ticket ticket = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_TICKET);
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				ticket = new Ticket();
				ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
				ticket.setParkingSpot(parkingSpot);
				ticket.setId(rs.getInt(2));
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(rs.getDouble(3));
				Timestamp timestamp = rs.getTimestamp(4);
				Calendar inTime = Calendar.getInstance();
				inTime.setTimeInMillis(timestamp.getTime());
				ticket.setInTime(inTime);
				ticket.setDiscount(rs.getBoolean(5));
			}
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeResultSet(rs);
		}
		return ticket;
	}

	/**
	 * This method update the ticket data for a given vehicle.
	 * 
	 * @param ticket contain an object of type Ticket.
	 * @return true if the ticket was updated correctly in the database.
	 */
	public boolean updateTicket(Ticket ticket) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
			ps.setDouble(1, ticket.getPrice());
			ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTimeInMillis()));
			ps.setInt(3, ticket.getId());
			ps.execute();
			result = true;
		} catch (Exception ex) {
			logger.error("Error saving ticket info", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
			dataBaseConfig.closePreparedStatement(ps);
		}
		return result;
	}

	/**
	 * This method check for vehicle registration number existence in the database.
	 * 
	 * @param vehicleRegNumber contain a string that represent the vehicle registration number.
	 * @return true if vehicle registration number exists in the database.
	 */
	public boolean checkVehicleRegNumber(String vehicleRegNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_VEHICLE_REG_NUMBER);
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = true;
			}
		} catch (Exception ex) {
			logger.error("Error fetching vehicle registration number", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeResultSet(rs);
		}
		return result;
	}

	/**
	 * This method retrieve the fare and out time of the given vehicle registration number.
	 * 
	 * @param vehicleRegNumber contain a string that represent the vehicle registration number.
	 * @return the ticket of the given vehicle.
	 */
	public Ticket getFareAndOutTime(String vehicleRegNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Ticket ticket = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_FARE_AND_OUT_TIME);
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				ticket = new Ticket();
				ticket.setPrice(rs.getDouble(1));
				Timestamp timestamp = rs.getTimestamp(2);
				Calendar ouTime = Calendar.getInstance();
				ouTime.setTimeInMillis(timestamp.getTime());
				ticket.setOutTime(ouTime);
			}
		} catch (Exception ex) {
			logger.error("Error fetching vehicle fare and out time", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeResultSet(rs);
		}
		return ticket;
	}

	/**
	 * This method retrieve the vehicle parking spot number from the database.
	 * 
	 * @param vehicleRegNumber contain a string that represent the vehicle registration number.
	 * @return an integer that represent the parking spot number.
	 */
	public int getVehicleParkingNumber(String vehicleRegNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int number = 0;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_VEHICLE_REG_NUMBER);
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				number = rs.getInt(1);
			}
		} catch (Exception ex) {
			logger.error("Error fetching vehicle parking number", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeResultSet(rs);
		}
		return number;
	}

}
