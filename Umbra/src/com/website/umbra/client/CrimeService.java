package com.website.umbra.client;

import com.google.gwt.user.client.rpc.RemoteService;
//Different from Stockwatcher StockService
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.website.umbra.shared.LatLon;

/**
 * Callback for CrimeService
 * @author Joon
 *
 */
@RemoteServiceRelativePath("crime")
public interface CrimeService extends RemoteService {
	public Crime[] getCrimes(int month, String[] crimeTypes, LatLon location, int radius) throws NotLoggedInException;
	public int updateDatabase() throws NotLoggedInException;
}