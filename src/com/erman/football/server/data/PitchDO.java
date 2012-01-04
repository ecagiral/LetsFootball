package com.erman.football.server.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.erman.football.shared.Pitch;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class PitchDO {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String name;
	
	@Persistent
	private String phone;
	
	@Persistent
	private int capacity;
	
	@Persistent
	private double longitude;
	
	@Persistent
	private double latitude;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public long getKey() {
		return key.getId();
	}
	
	public static PitchDO generate(Pitch pitch){
		PitchDO pitchDO = new PitchDO();
		pitchDO.setCapacity(pitch.getCapacity());
		pitchDO.setLatitude(pitch.getLatitude());
		pitchDO.setLongitude(pitch.getLongitude());
		pitchDO.setName(pitch.getName());
		pitchDO.setPhone(pitch.getPhone());
		return pitchDO;
	}
	
	public Pitch convert(){
		Pitch pitch = new Pitch();
		pitch.setKey(getKey());
		pitch.setCapacity(getCapacity());
		pitch.setLatitude(getLatitude());
		pitch.setLongitude(getLongitude());
		pitch.setPhone(getPhone());
		pitch.setName(getName());
		return pitch;
	}
	
	public void update(Pitch pitch){
		this.setCapacity(pitch.getCapacity());
		this.setLatitude(pitch.getLatitude());
		this.setLongitude(pitch.getLongitude());
		this.setPhone(pitch.getPhone());
		this.setName(pitch.getName());
	}
}
