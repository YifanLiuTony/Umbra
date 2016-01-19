package com.website.umbra.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.website.umbra.shared.LatLon;

/**
 * Async thread for CrimeService
 * @author Joon
 *
 */
public interface CrimeServiceAsync {
	public void getCrimes(int month, String[] crimeTypes, LatLon location, int radius, AsyncCallback<Crime[]> async);
	public void updateDatabase(AsyncCallback<Integer> async);
}