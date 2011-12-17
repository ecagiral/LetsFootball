package com.erman.football.shared;

import java.io.Serializable;

public class Pitch implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 816063373769471937L;
	
	private long key;
	private String name;
	private String location;
	private int capacity;
	
	public Pitch(){
		name = "isim";
		location = "00.0000000, 00.0000000";
		capacity = 0;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}	
}
