package com.erman.football.client.service;

import java.io.Serializable;

public class PlayerException extends Exception implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7498966929216786270L;
	
	private String message;
	
	public PlayerException(){
		
	}
	
	public PlayerException(String msg){
		message = msg;
	}

	public String getMessage() {
		return message;
	}	

}
