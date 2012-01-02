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
	private String location;
	private int capacity;
	
	public Pitch(){
		name = "isim";
		location = "41.010,28.970";
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
		String latlng[] = location.split(",");
		return new LatLng(Double.parseDouble(latlng[0]),Double.parseDouble(latlng[1]));
	}

	public void setLocation(HasLatLng location) {
		this.location = Double.toString(location.getLatitude())+","+Double.toString(location.getLongitude());
	}
	
	public String getStrLocation(){
		return location;
	}
	
	public void setLocation(String location) {
		this.location =  location;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}	
}
