package com.erman.football.server.data;

import java.util.Arrays;
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
	Long[] teamA;

	@Persistent
	Long[] teamB;
	
	@Persistent
	Long[] team;

	public long getKey() {
		return key.getId();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long[] getTeamA() {
		return teamA;
	}

	public void setTeamA(Long[] teamA) {
		this.teamA = teamA;
	}

	public Long[] getTeamB() {
		return teamB;
	}

	public void setTeamB(Long[] teamB) {
		this.teamB = teamB;
	}
	
	public void setTeam(Long[] team) {
		this.team = team;
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
		convertTeams(match,matchDO);
		return matchDO;
	}

	public void update(ClientMatch match){
		this.setLocation(match.getLocation());
		this.setMailSent(match.isMailSent());
		this.setPaid(match.isPaid());
		convertTeams(match,this);
	}

	public ClientMatch convert(){
		ClientMatch match = new ClientMatch();
		match.setKey(this.getKey());
		match.setDate(this.getDate());
		match.setLocation(this.getLocation());
		match.setMailSent(this.isMailSent());
		match.setPaid(this.isPaid());
		HashSet<Long> teamA = new HashSet<Long>();
		if(this.getTeamA()!=null){
			
			for(Long aPlayer:this.getTeamA()){
				teamA.add(aPlayer);
			}
		}
		match.setTeamA(teamA);
		HashSet<Long> teamB = new HashSet<Long>();
		if(this.getTeamB()!=null){
			
			for(Long bPlayer:this.getTeamB()){
				teamB.add(bPlayer);
			}
		}
		match.setTeamB(teamB);
		return match;
	}
	
	private static void convertTeams(ClientMatch match, MatchDO matchDO){
		Long[] teamA = null;
		Long[] teamB = null;
		if(!match.getTeamA().isEmpty()){
			teamA = new Long[match.getTeamA().size()];
			int index = 0;
			for(Long aPlayer:match.getTeamA()){
				teamA[index] = aPlayer;
				index++;
			}
			
		}
		matchDO.setTeamA(teamA);
		if(!match.getTeamB().isEmpty()){
			teamB = new Long[match.getTeamB().size()];
			int index = 0;
			for(Long bPlayer:match.getTeamB()){
				teamB[index] = bPlayer;
				index++;
			}
			
		}
		matchDO.setTeamB(teamB);
		if(teamA==null){
			if(teamB == null){
				matchDO.setTeam(null);
			}else{
				matchDO.setTeam(Arrays.copyOf(teamB,teamB.length));
			}
		}else{
			if(teamB == null){
				matchDO.setTeam(Arrays.copyOf(teamA,teamA.length));
			}else{
				//Merge two team
				Long[] team = Arrays.copyOf(teamA, teamA.length + teamB.length);
				System.arraycopy(teamB, 0, team, teamA.length, teamB.length);
				matchDO.setTeam(team);
			}
		}
	}
}
