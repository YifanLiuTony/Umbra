package com.website.umbra.client;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * This class will store all the user preferences and settings, which will be loaded and saved each time a user logs in.
 * @author Gustav
 * 
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true", embeddedOnly = "true")
public class ClientInfo implements Serializable {
	
	@PrimaryKey
	@Persistent
	private String lastLoggedDate;
	@Persistent
	private String lastIP;
	@Persistent
	private String username;
	@Persistent
    private boolean admin;
	
	public ClientInfo(){
		
	}

	/**
	 * Get the last date the user logged in.
	 * @return  a string that is the date
	 */
	public String getLastLoggedInDate(){
		return lastLoggedDate;
	}
	
	/**
	 * Get the last IP the user logged in with
	 * @return  string that is the IP
	 */
	public String getLastIP(){
		return lastIP;
	}
	
	
	/**
	 * Gets the user's username
	 * @return
	 */
	public String getUsername(){
		return this.username;
	}
	
	/**
	 * Sets the username
	 * @param username
	 */
	public void setUsername(String username){
		this.username = username;
	}
	
	/**
	 * Sets the last logged in date.
	 * @param date
	 */
	public void setLastDate(String date){
		this.lastLoggedDate = date;
	}
	
}
