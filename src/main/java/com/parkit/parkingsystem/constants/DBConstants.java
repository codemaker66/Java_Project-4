package com.parkit.parkingsystem.constants;

public class DBConstants {

	public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
	public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";
	public static final String CHECK_PARKING_SPOT_AVAILABILITY = "select * from parking where PARKING_NUMBER=?";

	public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME, DISCOUNT) values(?,?,?,?,?,?)";
	public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
	public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.DISCOUNT, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? and t.OUT_TIME is NULL order by t.IN_TIME limit 1";
	public static final String GET_VEHICLE_REG_NUMBER = "select * from ticket where VEHICLE_REG_NUMBER=?";
	public static final String CHECK_FARE_AND_OUT_TIME = "select * from ticket where PRICE is not NULL and OUT_TIME is not NULL and VEHICLE_REG_NUMBER=?";
}
