package com.erman.football.server.data;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.erman.football.shared.ClientPlayer;
import com.erman.football.shared.Match;
import com.erman.football.shared.Pitch;
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
	long owner;

	@Persistent
	boolean mailSent;

	@Persistent
	boolean paid;

	@Persistent
	Long[] teamA;
	
	@Persistent
	String teamAName;

	@Persistent
	Long[] teamB;
	
	@Persistent
	String teamBName;
	
	@Persistent
	Long[] team;
	
	@Persistent
	boolean played;
	
	@Persistent
	int teamAScore;
	
	@Persistent
	int teamBScore;

	public long getKey() {
		return key.getId();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setTeam(Set<Long> teamAList, Set<Long> teamBList) {
		//Set teamA
		Long[] tmpTeam = null;
		if(!teamAList.isEmpty()){
			tmpTeam = new Long[teamAList.size()];
			int index = 0;
			for(Long aPlayer:teamAList){
				tmpTeam[index] = aPlayer;
				index++;
			}
			
		}
		teamA = tmpTeam;
		
		//Set teamB
		tmpTeam = null;
		if(!teamBList.isEmpty()){
			tmpTeam = new Long[teamBList.size()];
			int index = 0;
			for(Long aPlayer:teamBList){
				tmpTeam[index] = aPlayer;
				index++;
			}
		}
		teamB = tmpTeam;
		
		//Set all team
		if(teamA==null){
			if(teamB == null){
				team = null;
			}else{
				team = Arrays.copyOf(teamB,teamB.length);
			}
		}else{
			if(teamB == null){
				team = Arrays.copyOf(teamA,teamA.length);
			}else{
				//Merge two team
				Long[] tmpFullTeam = Arrays.copyOf(teamA, teamA.length + teamB.length);
				System.arraycopy(teamB, 0, team, teamA.length, teamB.length);
				team = tmpFullTeam;
			}
		}
	}

	public HashSet<Long> getTeamA() {
		HashSet<Long> result = new HashSet<Long>();
		if(teamA!=null){
			
			for(Long aPlayer:teamA){
				result.add(aPlayer);
			}
		}
		return result;
	}
	
	public HashSet<Long> getTeamB() {
		HashSet<Long> result = new HashSet<Long>();
		if(teamB!=null){
			
			for(Long aPlayer:teamB){
				result.add(aPlayer);
			}
		}
		return result;
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

	public boolean isPlayed() {
		return played;
	}

	public void setPlayed(boolean played) {
		this.played = played;
	}

	public int getTeamAScore() {
		return teamAScore;
	}

	public void setTeamAScore(int teamAScore) {
		this.teamAScore = teamAScore;
	}

	public int getTeamBScore() {
		return teamBScore;
	}

	public void setTeamBScore(int teamBScore) {
		this.teamBScore = teamBScore;
	}

	public static MatchDO convert(Match match){
		MatchDO matchDO = new MatchDO();
		matchDO.setDate(match.getDate());
		matchDO.setLocation(Long.toString(match.getLocation().getKey()));
		matchDO.setPaid(match.isPaid());
		matchDO.setMailSent(match.isMailSent());
		matchDO.setOwner(match.getOwner());
		matchDO.setTeamAName(match.getTeamAName());
		matchDO.setTeamBName(match.getTeamBName());
		matchDO.setTeamAScore(match.getTeamAScore());
		matchDO.setTeamBScore(match.getTeamBScore());
		matchDO.setPlayed(match.isPlayed());
		return matchDO;
	}

	public void update(Match match){
		this.setDate(match.getDate());
		this.setLocation(Long.toString(match.getLocation().getKey()));
		this.setMailSent(match.isMailSent());
		this.setPaid(match.isPaid());
		this.setTeamAName(match.getTeamAName());
		this.setTeamBName(match.getTeamBName());
		this.setTeamAScore(match.getTeamAScore());
		this.setTeamBScore(match.getTeamBScore());
		this.setPlayed(match.isPlayed());
	}

	public Match convert(){
		Match match = new Match();
		match.setKey(this.getKey());
		match.setDate(this.getDate());
		Pitch pitch = Pitch_JDO_DB.getPitchbyId(Long.parseLong(this.getLocation())).convert();
		match.setLocation(pitch);
		match.setMailSent(this.isMailSent());
		match.setPaid(this.isPaid());
		match.setOwner(this.getOwner());
		match.setTeamAName(this.getTeamAName());
		match.setTeamBName(this.getTeamBName());
		match.setPlayed(this.isPlayed());
		match.setTeamAScore(this.getTeamAScore());
		match.setTeamBScore(this.getTeamBScore());
		HashMap<Long,ClientPlayer> teamA = new HashMap<Long,ClientPlayer>();
		for(Long playerId:this.getTeamA()){
			teamA.put(playerId,Player_JDO_DB.getUserbyId(playerId).convert());
		}
		match.setTeamA(teamA);
		HashMap<Long,ClientPlayer> teamB = new HashMap<Long,ClientPlayer>();
		for(Long playerId:this.getTeamB()){
			teamB.put(playerId,Player_JDO_DB.getUserbyId(playerId).convert());
		}
		match.setTeamB(teamB);
		return match;
	}
}
