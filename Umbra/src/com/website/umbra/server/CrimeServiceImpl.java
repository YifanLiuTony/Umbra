package com.website.umbra.server;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.website.umbra.client.Crime;
import com.website.umbra.client.CrimeService;
import com.website.umbra.client.NotLoggedInException;
import com.website.umbra.shared.LatLon;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Gateway between the server and client for crime data.
 * @author Joon
 *
 */
public class CrimeServiceImpl extends RemoteServiceServlet implements CrimeService {
	
	  private static final Logger LOG = Logger.getLogger(CrimeServiceImpl.class.getName());
	  private static final PersistenceManagerFactory PMF =
	      JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	@Override
	public Crime[] getCrimes(int monthParam, String[] crimeTypes, LatLon location, int radius) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		LinkedList<Crime> crimeList = new LinkedList<Crime>();
		try {
			Query q = pm.newQuery(Crime.class);
			if (crimeTypes.length > 0) {
				StringBuffer sb = new StringBuffer();
				sb.append("(");
				for (int i = 0; i <= crimeTypes.length - 1; i++){
					if (i == crimeTypes.length - 1){
						sb.append("crimeTypeLowerCase == " + "'" + crimeTypes[i] + "'" + ")");
					} else {
						sb.append("crimeTypeLowerCase == " + "'" + crimeTypes[i] + "'" + " || ");
					}
				}
				q.setFilter("month == monthParam" + " && " + sb.toString());
			} else {
				q.setFilter("month == monthParam");
			}
			q.declareParameters("int monthParam, String[] crimeTypes");
			List<Crime> crimes = (List<Crime>) q.execute(monthParam, crimeTypes);
			for (Crime crime : crimes) {
				crimeList.add(crime);
			}
		} finally {
			pm.close();
		}		
		Crime[] crimeArray = getCrimesByLocation(location, radius, crimeList);
		return crimeArray;
	}
	
	@Override
	public int updateDatabase(){
		System.out.println("running PS");
		ParserScript ps = new ParserScript();
		int crimesAdded = ps.addNewCrimes();
		return crimesAdded;
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

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

	private Crime[] getCrimesByLocation(LatLon location, int radius, List<Crime> tempCrimes) {
		
		LinkedList<Crime> crimes = new LinkedList<Crime>();
		for (Crime crime : tempCrimes) {
			if (getDistance(location, crime.getLocation()) <= radius) {
				crimes.add(crime);
			}
		}
		Crime[] returnList = new Crime[crimes.size()];
		returnList = crimes.toArray(returnList);
		return returnList;
	}
	
	double radius(double x) {
		return x * Math.PI / 180;
	};

	double getDistance(LatLon center, LatLon crimeLoc) {
		double earthRad = 6378137; // Earth�s mean radius in meter
		double dLat = radius(crimeLoc.getLat() - center.getLat());
		double dLon = radius(crimeLoc.getLon() - center.getLon());
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
				Math.cos(radius(center.getLat())) * Math.cos(radius(crimeLoc.getLat())) *
				Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = earthRad * c;
		return d * 0.000621371; // returns the distance in miles
	};

}
