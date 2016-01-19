package com.website.umbra.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.website.umbra.client.Crime;
import com.website.umbra.client.NotLoggedInException;
import com.website.umbra.shared.LatLon;

/**
 * Parsing Script that gets new crimes from the masterfile and stores them in the database
 * @author Joon
 *
 */
public class ParserScript {
	
	private int crimesAdded;
	private LinkedList<Crime> crimes;
	
	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	public ParserScript() {}
	
	public int addNewCrimes() {
		PersistenceManager pm = getPersistenceManager();
		Query query = pm.newQuery(Crime.class);
		query.setResult("max(month)");
		int latestMonth;
		try {
			latestMonth = (int) query.execute();
		} catch (NullPointerException e) {
			latestMonth = 0;
			// Database doesn't exist, no latestMonth
		}
		String masterFile = "https://dl.dropboxusercontent.com/s/nursjn2caqopfee/masterfile.txt?dl=0";
		
		try {
			checkLoggedIn();
		} catch (NotLoggedInException e2) {
			e2.printStackTrace();
		}
		URL masterFileURL;
		try {
			masterFileURL = new URL(masterFile);
			parseMasterFile(masterFileURL, latestMonth);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		try {
			pm.makePersistentAll(crimes);
		} finally {
			pm.close();
		}
		return crimesAdded;
	}
	
	private void parseMasterFile(URL masterFile, int latestMonth) {
		ArrayList<String> list = new ArrayList<String>();
		URL fileToParse;
		fileToParse = masterFile;
		BufferedReader fileReader = null;
		try {
			String line = "";
			fileReader = new BufferedReader(new InputStreamReader(fileToParse.openStream()));
			while ((line = fileReader.readLine()) != null) {
				list.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		crimes = new LinkedList<Crime>();
		crimesAdded = 0;
		for (int i = 1; i <= list.size() - 1; i++) { // Starts at i=1 because first line is "TYPE,YEAR,MONTH,HUNDRED_BLOCK,LAT,LON"
			String crimeString = list.get(i);
			String split[] = crimeString.split(",");
			if (split.length == 6){ // check proper format
				try {
					split[4] = split[4].replace("(", "");
					split[4] = split[4].replace("\"", "");
					split[5] = split[5].replace(")", "");
					split[5] = split[5].replace("\"", "");
					Double lat = Double.parseDouble(split[4]);
					Double lon = Double.parseDouble(split[5]);
					
					String crimeType = split[0];
					int year = Integer.parseInt(split[1]);
					int month = Integer.parseInt(split[2]);
					String address = split[3];
					LatLon location = new LatLon(lat,lon);
					
					Crime crime = new Crime(crimeType, year, month, address, location);
					if (crime.getMonth() > latestMonth){
						crimes.add(crime);
						crimesAdded++;
					}
				} catch (NumberFormatException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public void parseMasterFileTest(URL masterFile, int latestMonth) {
		parseMasterFile(masterFile, latestMonth);
	}
	
	public List<Crime> getCrimesTest() {
		return crimes;
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

	private static PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

}
