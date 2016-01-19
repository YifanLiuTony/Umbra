package com.website.umbra.client;


import java.util.LinkedList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Account panel, part of main UI
 * @author Gustav
 * @author Tony
 *
 */
public class AccountBar extends Composite {
	private static Umbra app;
	public HorizontalPanel accountBar;
	private Button saveButton;
	private Button loadButton;
	private ListBox crimeMapLoader;
	private TextBox saveBox;
	private Crime[] crimes;
	private static final String BOX_WIDTH = "300px";
	private static final String HEIGHT = "30px";
	private static final String WIDTH_CRIME = "200px";
	private static final String WIDTH_CRIME_LOADER = "150px";
	private static final String WIDTH_BUTTON = "100px";
	private static final int SPACING = 10;
	private String crimeMapSelected;
	private LinkedList<String> savedCrimes = new LinkedList<String>();
	private int hashCounter = 0;
	
	/**
	 * Constructs an AccountBar.
	 * @param app The app containing the mainUI
	 * @param crimes List of crimes
	 */
	public AccountBar(Umbra app) {
		AccountBar.app = app;
		accountBar = new HorizontalPanel();
		initWidget(accountBar);
		accountBar.setHeight(HEIGHT);
		draw();
	}
	private void draw() {
		// Save Button
		saveButton = new Button("Save");
		saveButton.addStyleName("Save");
		saveButton.setWidth(WIDTH_BUTTON);
	    saveButton.addClickHandler(new ClickHandler() {
	      @Override
		public void onClick(ClickEvent event) {
	    	  saveButton.setEnabled(false);
	    	  save(app);
	      }
		});
	    
		saveBox = new TextBox();
		saveBox.setWidth(BOX_WIDTH);
		saveBox.getElement().setPropertyString("placeholder", "Crime Map Name");
		saveBox.addStyleName("savebox");
		saveBox.setTitle("Name your crime map?");
	    
	    // Load Button
		loadButton = new Button("Load");
		loadButton.addStyleName("Load");
		loadButton.setWidth(WIDTH_BUTTON);
		loadButton.addClickHandler(new ClickHandler() {
	      @Override
		public void onClick(ClickEvent event) {
	    	  loadButton.setEnabled(false);
	    	  load(app);
	      }
		});
		
		//Crime Map Selector
		crimeMapLoader = new ListBox();
		crimeMapLoader.addStyleName("Load Crime Map:");
		crimeMapLoader.setTitle("Crime Maps");
		crimeMapLoader.setHeight(HEIGHT);
		crimeMapLoader.setWidth(WIDTH_CRIME_LOADER);
		
		accountBar.addStyleName("account_bar");
		accountBar.add(saveBox);
		accountBar.add(saveButton);
		accountBar.add(crimeMapLoader);
		accountBar.add(loadButton);
		
	}
	
	/**
	 * Sets crimeMapSelected to the current crime map
	 */
	public void getSelectedCrimeMap() {
		
		for (int i = 0; i < crimeMapLoader.getItemCount(); i++) {
	        if (crimeMapLoader.isItemSelected(i)) {
	            crimeMapSelected = crimeMapLoader.getValue(i);
	        }
	    }
	}
	
	private void save(Umbra app){
		String name = saveBox.getText();
		if(name.equals("") || Umbra.getMap().sizeOfMarkers() == 0){
			Window.alert("Please type a name or create a CrimeMap");
		}else{
			app.save(name);
		}
	}
	
	public void addSave(String name) {
		savedCrimes.add(name);
		crimeMapLoader.addItem(name);
		enableSave();
		crimeMapLoader.setSelectedIndex(crimeMapLoader.getItemCount()-1);
	}
	
	private void load(Umbra app){
		getSelectedCrimeMap();
		app.loadCrimeMap(crimeMapSelected);
	}
	public void addWidget(Widget widget) {
		accountBar.add(widget);
	}
	public void enableSave() {
		saveButton.setEnabled(true);
	}
	public void enableLoad() {
		loadButton.setEnabled(true);
	}
}
