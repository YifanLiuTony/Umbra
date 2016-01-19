package com.website.umbra.client;

import java.util.ArrayList;
import java.util.LinkedList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.website.umbra.client.LoginInfo;
import com.website.umbra.client.LoginService;
import com.website.umbra.client.LoginServiceAsync;
import com.website.umbra.client.NotLoggedInException;
import com.website.umbra.shared.LatLon;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Umbra implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	
	private FlexTable umbraTable;
	private static MapUI map;
	private SearchBar searchBar;
	private SideBar sideBar;
	private AccountBar accountBar;
	private static Crime[] crimes;
	private Boolean adminBool;
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Please sign in to your Google Account to access the Umbra application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	private final CrimeServiceAsync crimeService = GWT.create(CrimeService.class);
	private final ClientServiceAsync clientService = GWT.create(ClientService.class);
	
	// variables for saving current crime map
	private int monthCM;
	private String[] crimeTypesCM;
	private double latCM;
	private double lonCM;
	private int radiusCM;

	
	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {
		// Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			@Override
			public void onFailure(Throwable error) {
				handleError(error);
			}
			@Override
			public void onSuccess(LoginInfo result) {
				loginInfo = result;
			    if(loginInfo.isLoggedIn()) {
			    	loadUmbra();
			    } else {
			    	loadLogin();
			    }
			}
		});
	}
	
	private void loadLogin() {
			// Assemble login panel.
			signInLink.setHref(loginInfo.getLoginUrl());
			loginPanel.add(loginLabel);
			loginPanel.add(signInLink);
			RootPanel.get("back_panel").add(loginPanel);
	}
	
	private void loadUmbra(){
		signOutLink.setHref(loginInfo.getLogoutUrl());

		umbraTable = new FlexTable();
		umbraTable.addStyleName("background");
		
		loadMapApi();
		loadSearchBar();
		isUserAdmin();
		
		// Load the maps into place
		signOutLink.addStyleName("signout_link");
		umbraTable.setWidget(2,0, signOutLink);
		umbraTable.getFlexCellFormatter().setVerticalAlignment(3, 1, HasVerticalAlignment.ALIGN_BOTTOM);
		RootPanel.get("back_panel").add(umbraTable);

	}
	
	private void loadSearchBar() {
		sideBar = new SideBar();
    	searchBar = new SearchBar(this, sideBar);
	}
	
	private void loadAccountBar() {
		accountBar = new AccountBar(this);
		clientService.loadUserMapNames(new AsyncCallback<String[]>(){

			@Override
			public void onFailure(Throwable caught) {
				System.err.println("On Failure error");
			}

			@Override
			public void onSuccess(String[] result) {
				for (String name : result) {
					accountBar.addSave(name);
				}
			}

		});
	}
	
	private void loadCrimes(LinkedList<String> crimeTypes, int radius, int month, LatLon location){
		String[] temp = new String[crimeTypes.size()];
		for (int i=0; i < crimeTypes.size(); i++){
			temp[i] = crimeTypes.get(i).toLowerCase();
		}
		
		monthCM = month;
		crimeTypesCM = temp;
		latCM = location.getLat();
		lonCM = location.getLon();
		radiusCM = radius;
		crimeService.getCrimes(month, temp, location, radius, new AsyncCallback<Crime[]>(){
			@Override
			public void onFailure(Throwable caught) {
				System.err.println("On Failure error");
				sideBar.unloadingMsg();
				searchBar.enableButton();
			}
			@Override
			public void onSuccess(Crime[] result) {
				crimes = result;
				map.drawHeatMap(result);
				sideBar.displayCrimes(crimes);
				searchBar.enableButton();
			}
		});
	}
	
	public void search(final LinkedList<String> crimeTypes, final int radius, final int month, String address) {
		sideBar.loadingCrimeMsg();
		map.clearOverlays();
		Geocoder geoCoder = Geocoder.create();
        GeocoderRequest request = GeocoderRequest.create();
        request.setAddress(address);
        geoCoder.geocode(request, new Geocoder.Callback() {
			@Override
			public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
		           if (b == GeocoderStatus.OK) {
	                    GeocoderResult result = a.shift();
	                    double lat = result.getGeometry().getLocation().lat();
	                    double lon = result.getGeometry().getLocation().lng();
	                    map.drawPoint(new LatLon(lat, lon));
	                    loadCrimes(crimeTypes, radius, month, new LatLon(lat, lon));
	                } else {
	                    Window.alert("Location not found");
	                }
			}      
       });
	}
	
	public void updateDatabase() {
		crimeService.updateDatabase(new AsyncCallback<Integer>(){
			@Override
			public void onFailure(Throwable caught) {
				handle(caught);	
			}
			private void handle(Throwable caught) {
				System.err.println("On Failure error");
				searchBar.postUpdateDb(-1);
			}
			@Override
			public void onSuccess(Integer result) {
				int crimesAdded = result;
				searchBar.postUpdateDb(crimesAdded);
			}
		});
	}
	
	public void isUserAdmin(){
		adminBool = false;
		clientService.isUserAdmin(new AsyncCallback<Boolean>(){
			@Override
			public void onFailure(Throwable caught) {
				handle(caught);				
			}
			private void handle(Throwable caught) {
				System.err.println("On Failure error");
			}
			@Override
			public void onSuccess(Boolean result) {
				adminBool = result;
				if (adminBool){
					searchBar.drawDBButton();
				}
			}
		});
	}
	

	private void loadMapApi() {
	    boolean sensor = true;
	    // load all the libs for use in the maps
	    ArrayList<LoadLibrary> loadLibraries = new ArrayList<LoadApi.LoadLibrary>();
	    loadLibraries.add(LoadLibrary.ADSENSE);
	    loadLibraries.add(LoadLibrary.DRAWING);
	    loadLibraries.add(LoadLibrary.GEOMETRY);
	    loadLibraries.add(LoadLibrary.PANORAMIO);
	    loadLibraries.add(LoadLibrary.PLACES);
	    loadLibraries.add(LoadLibrary.WEATHER);
	    loadLibraries.add(LoadLibrary.VISUALIZATION);

	    Runnable onLoad = new Runnable() {
	        @Override
	        public void run() {
	        	drawMap();
	        }
	    };
	        LoadApi.go(onLoad, loadLibraries, sensor);
			loadAccountBar();
	}
	private void drawMap() {
	    MapUI newMap = new MapUI();
	    Umbra.setMap(newMap);
		umbraTable.setWidget(1,0,sideBar);
		umbraTable.getFlexCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
		umbraTable.setWidget(0,1,searchBar);
		umbraTable.setWidget(1,1,getMap());
		umbraTable.setWidget(2,1, accountBar);
	}

	public static MapUI getMap() {
		return map;
	}

	public static void setMap(MapUI map) {
		Umbra.map = map;
	}
	
	public void save(final String name) {
		clientService.saveCrimeMap(name, monthCM, crimeTypesCM, latCM, lonCM, radiusCM, new AsyncCallback<Void>(){
			@Override
			public void onFailure(Throwable caught) {
				handle(caught);
			}

			private void handle(Throwable caught) {
				System.err.println("On Failure error");
				accountBar.enableSave();
			}

			@Override
			public void onSuccess(Void result) {
				accountBar.addSave(name);
				accountBar.enableSave();
			}
		});
	}

	public void loadCrimeMap(String crimeId) {
		clientService.loadCrimeMap(crimeId, new AsyncCallback<CrimeMapClient>(){
			@Override
			public void onFailure(Throwable caught) {
				handle(caught);
			}
			private void handle(Throwable caught) {
				System.err.println("On Failure error");
				sideBar.unloadingMsg();
				searchBar.enableButton();
				accountBar.enableLoad();
			}
			@Override
			public void onSuccess(CrimeMapClient result) {
				if (result != null) {
					LinkedList<String> crimeTypes = new LinkedList<String>();
					for (String crimeType : result.getCrimeTypes()){
						crimeTypes.add(crimeType);
					}
					loadCrimes(crimeTypes, result.getRadius(), result.getMonth(), new LatLon(result.getLat(), result.getLon()));
					Umbra.map.clearOverlays();
				}
				accountBar.enableLoad();
			}

		});
	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}
}

