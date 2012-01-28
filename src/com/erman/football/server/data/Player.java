package com.erman.football.server.data;

import javax.jdo.annotations.IdGeneratorStrategy;

import com.erman.football.shared.ClientPlayer;
import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Player {
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    public void setKey(Key key) {
        this.key = key;
    }
    
    public long getKey(){
    	return key.getId();
    }
    
    @Persistent
    private long facebookId;
    
    @Persistent
    private String email;

    @Persistent
    private String name;
    
    @Persistent
    private boolean admin;
    
    @Persistent 
    private boolean notify;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public long getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(long facebookId) {
		this.facebookId = facebookId;
	}

	public static Player convert(ClientPlayer player){
		Player playerDO = new Player();
		playerDO.setEmail(player.getEmail());
		playerDO.setName(player.getName());
		playerDO.setAdmin(player.isAdmin());
		playerDO.setNotify(player.isNotify());
		playerDO.setFacebookId(player.getFacebookId());
		return playerDO;
	}
	
	public void update(ClientPlayer player){
		this.setAdmin(player.isAdmin());
		this.setNotify(player.isNotify());
		this.setName(player.getName());
		this.setEmail(player.getEmail());
	}
	
	public ClientPlayer convert(){
		ClientPlayer player = new ClientPlayer(this.email);
		player.setKey(this.getKey());
		player.setName(this.getName());
		player.setAdmin(this.isAdmin());
		player.setNotify(this.isNotify());
		player.setFacebookId(this.getFacebookId());
		return player;
	}
}


