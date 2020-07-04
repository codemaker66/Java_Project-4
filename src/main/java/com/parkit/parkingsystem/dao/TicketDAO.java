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

    public boolean saveTicket(Ticket ticket){
        try (Connection con = dataBaseConfig.getConnection()) {
            PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            //ps.setInt(1,ticket.getId());
            ps.setInt(1,ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTimeInMillis()));
            ps.setTimestamp(5, (ticket.getOutTime() == null)?null: (new Timestamp(ticket.getOutTime().getTime().getTime())) );
            return ps.execute();
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
            return false;
        }
    }

    public Ticket getTicket(String vehicleRegNumber) {
        Ticket ticket = null;
        try (Connection con = dataBaseConfig.getConnection()) {
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1,vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)),false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                Timestamp timestamp = rs.getTimestamp(4);
                Calendar inTime = Calendar.getInstance();
                inTime.setTimeInMillis(timestamp.getTime());
                ticket.setInTime(inTime);
                timestamp = rs.getTimestamp(4);
                Calendar outTime = Calendar.getInstance();
                outTime.setTimeInMillis(timestamp.getTime());
                ticket.setOutTime(outTime);
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }
        
        return ticket;
    }

    public boolean updateTicket(Ticket ticket) {
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTimeInMillis()));
            ps.setInt(3,ticket.getId());
            ps.execute();
            return true;
        }catch (Exception ex){
            logger.error("Error saving ticket info",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }

	public boolean checkVehicleRegNumberExistence(String vehicleRegNumber) {
		
	        try (Connection con = dataBaseConfig.getConnection()) {
	            PreparedStatement ps = con.prepareStatement(DBConstants.GET_REG_NUMBER);
	            ps.setString(1,vehicleRegNumber);
	            ResultSet rs = ps.executeQuery();
	            if(rs.next()){
	               return true;
	            }
	            dataBaseConfig.closeResultSet(rs);
	            dataBaseConfig.closePreparedStatement(ps);
	        }catch (Exception ex){
	            logger.error("Error fetching vehicle registration number",ex);
	        }
		
		return false;
	}
	
	public int getVehicleParkingNumber(String vehicleRegNumber) {
		int num = 0;
        try (Connection con = dataBaseConfig.getConnection()) {
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_REG_NUMBER);
            ps.setString(1,vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
               return rs.getInt(1);
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            logger.error("Error fetching vehicle registration number",ex);
        }
	
	return num;
}

	public boolean checkFareAndOutTime(String string) {
		 try (Connection con = dataBaseConfig.getConnection()) {
	            PreparedStatement ps = con.prepareStatement(DBConstants.CHECK_FARE_AND_OUT_TIME);
	            ps.setString(1,string);
	            ResultSet rs = ps.executeQuery();
	            if(rs.next()){
	               return true;
	            }
	            dataBaseConfig.closeResultSet(rs);
	            dataBaseConfig.closePreparedStatement(ps);
	        }catch (Exception ex){
	            logger.error("Error in tests",ex);
	        }
		return false;
	}
	
}
