package com.erman.football.server.data;

import java.util.Date;
import java.util.HashSet;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.erman.football.shared.ClientMatch;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class MatchDO {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
	Date date;
    
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

	public static MatchDO convert(ClientMatch match){
    	MatchDO matchDO = new MatchDO();
    	matchDO.setDate(match.getDate());
    	matchDO.setLocation(match.getLocation());
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
		this.setTeamA(match.getTeamA());
		this.setTeamB(match.getTeamB());
	}
    
    public ClientMatch convert(){
    	ClientMatch match = new ClientMatch();
    	match.setKey(this.getKey());
    	match.setDate(this.getDate());
    	match.setLocation(this.getLocation());
    	match.setMailSent(this.isMailSent());
    	match.setPaid(this.isPaid());
    	match.setTeamA(new HashSet<Long>(this.getTeamA()));
    	match.setTeamB(new HashSet<Long>(this.getTeamB()));
    	return match;
    }
}
