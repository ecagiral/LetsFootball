package com.erman.football.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;


public class Match extends DataObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5868415561756106979L;
	
	private long key;
	private Date date;
	private Pitch location;
	private long owner;
	private boolean paid;
	private boolean mailSent;
	private boolean played;
	private String teamAName;
	private String teamBName;
	private HashMap<Long,ClientPlayer> teamA;
	private HashMap<Long,ClientPlayer> teamB;
	
	public Match() {
		this.date = new Date();
		this.location = new Pitch();
		this.owner = 0;
		this.paid = false;
		this.mailSent = false;
		this.played = false;
		this.teamAName = "Team A";
		this.teamBName = "Team B";
		this.teamA = new HashMap<Long,ClientPlayer>();
		this.teamB = new HashMap<Long,ClientPlayer>();
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
	
	public HashMap<Long,ClientPlayer> getTeamA() {
		return teamA;
	}
	
	public void setTeamA(HashMap<Long,ClientPlayer> teamA) {
		this.teamA = teamA;
	}
	
	public HashMap<Long,ClientPlayer> getTeamB() {
		return teamB;
	}
	
	public void setTeamB(HashMap<Long,ClientPlayer> teamB) {
		this.teamB = teamB;
	}

	public Pitch getLocation() {
		return location;
	}
	
	public void setLocation(Pitch location) {
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
