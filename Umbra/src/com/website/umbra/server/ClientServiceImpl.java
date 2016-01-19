
package com.website.umbra.server;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.website.umbra.client.ClientInfo;
import com.website.umbra.client.Crime;
import com.website.umbra.client.CrimeMapClient;
import com.website.umbra.client.NotLoggedInException;
import com.website.umbra.client.ClientService;


/**
 * Gateway between the server and client for user specific data.
 * @author Gustav
 *
 */
public class ClientServiceImpl extends RemoteServiceServlet implements
		ClientService {

	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	@Override
	@Deprecated
	public void addClientInfo(ClientInfo user) throws NotLoggedInException {

	    checkLoggedIn();
	    PersistenceManager pm = getPersistenceManager();
	    try {
	      pm.makePersistent(new Client(user));
	    } finally {
	      pm.close();
	    }

	}

	@Override
	/**
	 * Used to update a user info. Overwrites if already exists.
	 */
	public void updateUser(ClientInfo user) throws NotLoggedInException {
		// first we delete
		checkLoggedIn();
	    PersistenceManager pm = getPersistenceManager();
	    try {
	      long deleteCount = 0;
	      Query q = pm.newQuery(Client.class, "user == u");
	      q.declareParameters("com.google.appengine.api.users.User u");
	      List<Client> linfo
	      = (List<Client>) q.execute(getUser());
	      for (Client inf : linfo) {
	          deleteCount++;
	          pm.deletePersistent(inf);
	      }
	    } finally {
	    	pm.makePersistent((new Client(user)));
	    }
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

	@Override
	/**
	 * Returns the clientInfo for a specific user
	 */
	public ClientInfo getClient() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		Client info = null;
		try {
			Query q = pm.newQuery(Client.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			q.setOrdering("createDate");
			List<Client> linfo = (List<Client>) q.execute(getUser());
			if (linfo.size() != 0){
				info = linfo.get(0);
			}
		} finally {
			pm.close();
		}
		return info.getClientInfo();
	}
	
	
	@Override
	public String[] loadUserMapNames() throws NotLoggedInException {
	    checkLoggedIn();
	    PersistenceManager pm = getPersistenceManager();
	    try {
	    	Query q = pm.newQuery(CrimeMap.class, "user == u");
	    	q.declareParameters("com.google.appengine.api.users.User u");
	    	List<CrimeMap> maps = (List<CrimeMap>) q.execute(getUser());
			String[] loadedNames = new String[maps.size()];
	    	for (int i=0;i<maps.size();i++) {
	    		loadedNames[i] = maps.get(i).getName();
	    	}
	    	
	    	return loadedNames;
	    } finally {
	    	pm.close();
	    }
	}
	private void checkLoggedIn() throws NotLoggedInException {
		if (getUser() == null) {
			throw new NotLoggedInException("Not logged in.");
		}
	}

	private User getUser() {
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}
	
	@Override
	public Boolean isUserAdmin(){
		UserService userService = UserServiceFactory.getUserService();
		return userService.isUserAdmin();
	};

	/**
	 * Saved a crimeMap with a specified name
	 * 
	 * @params name, crime[]
	 */
	@Override
	public void saveCrimeMap(String name, int month, String[] crimeTypes, double lat, double lon, int radius) throws NotLoggedInException {
	    checkLoggedIn();
	    PersistenceManager pm = getPersistenceManager();
	    try {
		    pm.makePersistent(new CrimeMap(getUser(), crimeTypes, radius, month, name, lat, lon));
	    } finally {
	    	pm.close();
	    }
	}
	
	/**
	 * Loads the specified crimeMap
	 * 
	 * @param name exact string of the crime map name
	 */
	@Override
	public CrimeMapClient loadCrimeMap(String name) throws NotLoggedInException {
	    checkLoggedIn();
	    PersistenceManager pm = getPersistenceManager();
	    try {
	    	Query q = pm.newQuery(CrimeMap.class, "user == u");
	    	q.declareParameters("com.google.appengine.api.users.User u");
	    	List<CrimeMap> maps = (List<CrimeMap>) q.execute(getUser());
			for (CrimeMap map : maps) {
				if (map.getName().equals(name)) {
					CrimeMap rm = map;
					CrimeMapClient crimeMapClient = new CrimeMapClient(rm.getCrimeTypes(), rm.getRadius(), rm.getMonth(), rm.getName(), rm.getLat(), rm.getLon());
					return crimeMapClient;
				}
			}
	        return null;
	    } finally {
	    	pm.close();
	    }
	}
	
	/**
	 * Deletes a user off the database.
	 * 
	 * @param userName
	 *            a string username, must be exact match
	 */
	@Override
	public void removeClient(String userName) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();

		try {

			Query q = pm.newQuery(Client.class);
			q.setFilter("username == userNameParam");
			q.declareParameters("String userNameParam");
			long deleteCount = 0;
			List<Client> linfo = (List<Client>) q.execute(userName);
			for (Client inf : linfo) {
				deleteCount++;
				pm.deletePersistent(inf);
			}
		} finally {
		}
	}
}
