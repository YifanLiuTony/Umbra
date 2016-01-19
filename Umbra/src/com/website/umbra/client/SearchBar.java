package com.website.umbra.client;

import java.util.LinkedList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * SearchBar Class, Part of the main UI
 * @author Gustav
 * @author Tony
 *
 */
public class SearchBar extends Composite {
	
	// Frames
	public HorizontalPanel search;
	private SideBar sideBar;
	private Umbra app;
	
	// Specifications
	private String BOX_WIDTH = "500px";
	
	// GUI fields
	private Button searchButton;
	private Button dbButton;
	private TextBox searchBox;


	public SearchBar(Umbra app, SideBar sideBar) {
		this.app = app;
		this.sideBar = sideBar;
		search = new HorizontalPanel();
		search.setWidth("750px");
		initWidget(search);
		drawSearch();
		

	// Listen for mouse events on the Search button.
		searchButton.addClickHandler(new ClickHandler() {
		    @Override
			public void onClick(ClickEvent event) {
		    	search();
		    }
		});

		    
		// Listen for keyboard events in the input box.
	    searchBox.addKeyDownHandler(new KeyDownHandler() {
	      @Override
		public void onKeyDown(KeyDownEvent event) {
	        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER&&searchButton.isEnabled()) {
	        	search();
	        }
	      }
	    });
	    
		// Listen for mouse events on the Update DB button.
	}
	
	protected void updateDatabase() {
		app.updateDatabase();
	}

	// Search the result
	private void search() {
		LinkedList<String> crimeTypes = SideBar.getSelectedTypes();
		int month = SideBar.getMonth();
		int radius = SideBar.getSelectedRadius();
		String addr = searchBox.getValue();
		
		
		// Validate address
		if(validateAddr(addr) && crimeTypes!=null){
			app.search(crimeTypes, radius,month, addr);
	      	searchButton.setEnabled(false);
			//app.loadingCrimes();
		}else{
			Window.alert("Address is invalid, please try again");
		}
	}
	
	
	
	// draw the search Box and Button
	private void drawSearch() {
		// Search Box
		searchBox = new TextBox();
		searchBox.getElement().setPropertyString("placeholder", "Search");
		searchBox.addStyleName("search_bar");
		searchBox.setWidth(BOX_WIDTH);
		searchBox.addStyleName("searchBox");
		searchBox.setTitle("Search");
		
		// Search Button
		searchButton = new Button("Search");
		searchButton.addStyleName("searchButton");
		
		dbButton = new Button("Update Database");
		dbButton.addStyleName("dbButton");
		search.addStyleName("search");
		search.add(searchBox);
		search.add(searchButton);
	}
	
	public void drawDBButton(){
		dbButton = new Button("Update Database");
		dbButton.addStyleName("dbButton");
		
		search.add(dbButton);
		
	    dbButton.addClickHandler(new ClickHandler() {
		    @Override
			public void onClick(ClickEvent event) {
		    	dbButton.setEnabled(false);
		    	sideBar.updatingDbMsg();
		    	updateDatabase();
		    }
		});
	}
	/**
	 * 
	 * @param address
	 * @return true if the address looks OKAY
	 */
	private boolean validateAddr(String address){
		String[] addr = address.split("[,]? ");
		
		// return false if address = null
		if(address.equals(null)) return false;
		
		
		
		// loop through the address value
		for(int i = 0; i<addr.length;i++){
			// special charater
			if(addr[i].matches("^[0-9a-zA-Z]*")){
				continue;
			}else{
				return false;
			}
		}
		return true;
	}
	
	public void enableButton(){
		searchButton.setEnabled(true);
	}

	public void postUpdateDb(int crimesAdded) {
		// TODO Auto-generated method stub
		sideBar.postUpdateDb(crimesAdded);
		dbButton.setEnabled(true);
	}
	
	

}
