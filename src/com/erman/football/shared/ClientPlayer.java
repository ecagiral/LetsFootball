package com.erman.football.shared;

import java.io.Serializable;

public class ClientPlayer extends DataObject implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6486401923712917968L;
	
	private long key;
	private long facebookId;
	private String email;
	private String name;
	private boolean admin;
	private boolean notify;
	
	public ClientPlayer(){
		email = "player@mail";
		name = "name";
		admin = false;
		notify = false;
	}
	
	public ClientPlayer(String email){
		this.email = email;
	}
	
	
	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public void setEmail(String email){
		this.email=email;
	}
	
	public String getEmail(){
		return email;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		String[] tokens = name.trim().toLowerCase().split("\\s+");
		String result = "";
		for(String token:tokens){
			String word = token.trim(); 
			result += " "+Character.toUpperCase(word.charAt(0))+word.substring(1);
		}
		this.name = result.trim();
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isNotify() {
		return notify;
	}

	public void setNotify(boolean notify) {
		this.notify = notify;
	}

	@Override
	public long getOwner() {
		return key;
	}

	public long getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(long facebookId) {
		this.facebookId = facebookId;
	}
	
	
}
