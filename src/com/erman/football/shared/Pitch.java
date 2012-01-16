package com.erman.football.shared;

import java.io.Serializable;

import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.maps.client.base.LatLng;

public class Pitch extends DataObject implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9204615732872513610L;
	
	private long key;
	private String name;
	private String phone;
	private String city;
	private String town;
	private double latitude;
	private double longitude;
	private int capacity;
	
	public Pitch(){
		name = "isim";
		longitude = 41.010d;
		latitude = 28.970d;
		capacity = 10;
	}

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HasLatLng getLocation() {
		return new LatLng(latitude,longitude);
	}

	public void setLocation(HasLatLng location) {
		this.latitude = location.getLatitude();
		this.longitude = location.getLongitude();
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	
	
}
