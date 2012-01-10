package com.erman.football.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;


public class ClientMatch extends DataObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5868415561756106979L;
	
	private long key;
	private Date date;
	private String location;
	private long owner;
	private boolean paid;
	private boolean mailSent;
	private boolean played;
	private String teamAName;
	private String teamBName;
	private HashSet<Long> teamA;
	private HashSet<Long> teamB;
	
	public ClientMatch() {
		this.date = new Date();
		this.location = "0";
		this.owner = 0;
		this.paid = false;
		this.mailSent = false;
		this.played = false;
		this.teamAName = "Team A";
		this.teamBName = "Team B";
		this.teamA = new HashSet<Long>();
		this.teamB = new HashSet<Long>();
	}
	
	public void setKey(long key) {
		this.key = key;
	}

	public long getKey() {
		return key;
	}
	
	public boolean isPlayed() {
		return played;
	}
	
	public void setPlayed(boolean played) {
		this.played = played;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public HashSet<Long> getTeamA() {
		return teamA;
	}
	
	public void setTeamA(HashSet<Long> teamA) {
		this.teamA = teamA;
	}
	
	public HashSet<Long> getTeamB() {
		return teamB;
	}
	
	public void setTeamB(HashSet<Long> teamB) {
		this.teamB = teamB;
	}

	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public boolean isPaid() {
		return paid;
	}
	
	public void setPaid(boolean paid) {
		this.paid = paid;
	}
	
	public boolean isMailSent() {
		return mailSent;
	}
	
	public void setMailSent(boolean mailSent) {
		this.mailSent = mailSent;
	}

	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public String getTeamAName() {
		return teamAName;
	}

	public void setTeamAName(String teamAName) {
		this.teamAName = teamAName;
	}

	public String getTeamBName() {
		return teamBName;
	}

	public void setTeamBName(String teamBName) {
		this.teamBName = teamBName;
	}
	
}
