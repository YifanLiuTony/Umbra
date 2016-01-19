package com.website.umbra.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async thread for ClientService
 * @author Gustav
 *
 */
public interface ClientServiceAsync {
  public void addClientInfo(ClientInfo user, AsyncCallback<Void> async);
  public void updateUser(ClientInfo user, AsyncCallback<Void> async);
  public void getClient(AsyncCallback<ClientInfo> async);
  public void loadUserMapNames(AsyncCallback<String[]> callback);
  public void saveCrimeMap(String name, int month, String[] crimeTypes, double lat, double lon, int radius, AsyncCallback<Void> callback);
  public void removeClient(String username, AsyncCallback<Void> async);
  public void loadCrimeMap(String name, AsyncCallback<CrimeMapClient> callback);
  public void isUserAdmin(AsyncCallback<Boolean> async);
}
