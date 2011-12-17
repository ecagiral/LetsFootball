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
	private int capacity;
	
	@Persistent
	private String location;

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public long getKey() {
		return key.getId();
	}
	
	public static PitchDO generate(Pitch pitch){
		PitchDO pitchDO = new PitchDO();
		pitchDO.setCapacity(pitch.getCapacity());
		pitchDO.setLocation(pitch.getLocation());
		pitchDO.setName(pitch.getName());
		return pitchDO;
	}
	
	public Pitch convert(){
		Pitch pitch = new Pitch();
		pitch.setKey(getKey());
		pitch.setCapacity(getCapacity());
		pitch.setLocation(getLocation());
		pitch.setName(getName());
		return pitch;
	}
	
	public void update(Pitch pitch){
		this.setCapacity(pitch.getCapacity());
		this.setLocation(pitch.getLocation());
		this.setName(pitch.getName());
	}
}
