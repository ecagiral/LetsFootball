package com.erman.football.shared;

import java.io.Serializable;

public class Schedule extends DataObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -134665000559676476L;
	
	private long pitch;
	private long date;
	
	public Schedule(){
		this.pitch = 0;
		this.date = 0;
	}

	public long getPitch() {
		return pitch;
	}

	public void setPitch(long pitch) {
		this.pitch = pitch;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
	
	

}
