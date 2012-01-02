package com.erman.football.shared;

import java.io.Serializable;

public class ClientPlayer extends DataObject implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6486401923712917968L;
	
	private long key;
	private String email;
	private String name;
	private String surname;
	private boolean admin;
	private boolean notify;
	
	public ClientPlayer(){
		email = "player@mail";
		name = "name";
		surname = "surname";
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
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
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
	
	public String getFullName(){
		return name+" "+surname;
	}
}
