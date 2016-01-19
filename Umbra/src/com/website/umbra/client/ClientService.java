package com.website.umbra.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Callback for ClientService functions
 * @author Gustav
 *
 */
@RemoteServiceRelativePath("client")
public interface ClientService extends RemoteService  {
	/**
	 * Adds a clientInfo to a logged in user.
	 * @param user A ClientInfo object
	 * @throws NotLoggedInException
	 */
	@Deprecated
	  public void addClientInfo(ClientInfo user) throws NotLoggedInException;
	
	/**
	 * Updates the user settings. 
	 * @param user A clientInfo object
	 * @throws NotLoggedInException
	 */
	  public void updateUser(ClientInfo user) throws NotLoggedInException;
	  
	  /**
	   * Get the ClientInfo object from a logged in user.
	   * @return
	   * @throws NotLoggedInException
	   */
	  public ClientInfo getClient() throws NotLoggedInException;
	  
	  
	  /**
	   * Get List of CrimeMaps for a user
	   * @return LinkedList<CrimeMap>
	   * @throws NotLoggedInException
	   */
	  public String[] loadUserMapNames() throws NotLoggedInException;
	  
	  /**
	   * Get a single CrimeMap by name
	   * @return LinkedList<CrimeMap>
	   * @throws NotLoggedInException
	   */
	  public CrimeMapClient loadCrimeMap(String name) throws NotLoggedInException;
	  
	  
	  /**
	   * Saves a crimeMap to a users account.
	   * @return
	   * @throws NotLoggedInException
	   */
	  public void saveCrimeMap(String name, int month, String[] crimeTypes, double lat, double lon, int radius) throws NotLoggedInException;

	  /**
	   * Removes a user from the database
	   * @param username
	   * @throws NotLoggedInException
	   */
	  public void removeClient(String username) throws NotLoggedInException;
	  
	  public Boolean isUserAdmin() throws NotLoggedInException;


}
