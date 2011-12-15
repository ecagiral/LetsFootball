package com.erman.football.server;

import java.util.HashSet;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.erman.football.shared.ClientMatch;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Match {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    public void setKey(Key key) {
        this.key = key;
    }
    
    @Persistent
	String date;
    
    @Persistent
	String time;
    
    @Persistent
	String location;
    
    @Persistent
	boolean mailSent;
    
    @Persistent
	boolean paid;
	
    @Persistent
	HashSet<Long> teamA;
	
    @Persistent
	HashSet<Long> teamB;

    public long getKey() {
		return key.getId();
	}

	public Match(String date){
    	this.date = date;
    }
    
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
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
	 
    public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isMailSent() {
		return mailSent;
	}

	public void setMailSent(boolean mailSent) {
		this.mailSent = mailSent;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public static Match convert(ClientMatch match){
    	Match matchDO = new Match(match.getDate());
    	matchDO.setLocation(match.getLocation());
    	matchDO.setTime(match.getTime());
    	matchDO.setPaid(match.isPaid());
    	matchDO.setMailSent(match.isMailSent());
    	matchDO.setTeamA(match.getTeamA());
    	matchDO.setTeamB(match.getTeamA());
    	return matchDO;
    }
	
	public void update(ClientMatch match){
		this.setLocation(match.getLocation());
		this.setMailSent(match.isMailSent());
		this.setPaid(match.isPaid());
		this.setTime(match.getTime());
		this.setTeamA(match.getTeamA());
		this.setTeamB(match.getTeamB());
	}
    
    public ClientMatch convert(){
    	ClientMatch match = new ClientMatch();
    	match.setKey(this.getKey());
    	match.setDate(this.getDate());
    	match.setTime(this.getTime());
    	match.setLocation(this.getLocation());
    	match.setMailSent(this.isMailSent());
    	match.setPaid(this.isPaid());
    	match.setTeamA(new HashSet<Long>(this.getTeamA()));
    	match.setTeamB(new HashSet<Long>(this.getTeamB()));
    	return match;
    }
}
