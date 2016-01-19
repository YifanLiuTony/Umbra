package com.website.umbra.client;

import java.util.LinkedList;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SideBar extends Composite{
	private TabPanel tab;
	private FlowPanel frame;
	private VerticalPanel panel;
	private FlexTable sideBar;
	private CrimeBar crimeBar;
	private TextBox loadingMsg;
	private TextBox postUpdateDbMsg1;
	private TextBox postUpdateDbMsg2;
	private VerticalPanel consolePanel;
	
	private String HEIGHT = "30px";
	private String WIDTH = "350px";
	private String WIDTH_RADIUS = "150px";
	private String WIDTH_MONTH = "150px";

	private final String crime_label1 = "Please Select Crime Type";
	private final String crime_label2 = "(Hold CRTL to select multiple types)";
	private final String radius_label = "Please Select Radius";
	private final String month_label = "Crimes after month: ";
	
	private static ListBox crimeType;
	private static ListBox radius;
	private static ListBox month;
	
	public SideBar() {
		frame = new FlowPanel();
		consolePanel = new VerticalPanel();
		tab = new TabPanel();
		tab.addStyleName("tab_panel");

		panel = new VerticalPanel();
		panel.setWidth(WIDTH);
		panel.addStyleName("preferences");
		tab.add(panel,"Preferences");
		
		crimeBar = new CrimeBar();
		crimeBar.setWidth(WIDTH);
		crimeBar.addStyleName("crime_bar");
		
		tab.add(crimeBar,"Statistics");
		
		tab.selectTab(0);
		
		frame.add(tab);
		frame.add(consolePanel);
		initWidget(frame);
		draw();
	}

	// draw the GUI
	private void draw() {
		sideBar = new FlexTable();
		
		drawCrimeTypes();
		drawRadius();
		drawDate();
		
		// set table for sideBar
		sideBar.setText(0,0,crime_label1);
		sideBar.setText(1,0,crime_label2);
		sideBar.setWidget(2,0,crimeType);
		sideBar.getCellFormatter().addStyleName(0, 0, "header");
		sideBar.getCellFormatter().addStyleName(1, 0, "header");
		
		sideBar.setText(3,0,radius_label);
		sideBar.setWidget(4,0,radius);
		sideBar.getCellFormatter().addStyleName(3, 0, "header");
		
		sideBar.setText(5,0,month_label);
		sideBar.setWidget(6,0,month);
		sideBar.getCellFormatter().addStyleName(5, 0, "header");
		
		
		//sideBar.setCellSpacing(SPACING);
		
		panel.add(sideBar);
	}
	
	// draw crime type field
	private void drawCrimeTypes(){
		crimeType = new ListBox();
		crimeType.setMultipleSelect(true);
		crimeType.setVisibleItemCount(crimeType.getItemCount());
		crimeType.setTitle("Crime Type");
		
		//crimeType.setHeight(HEIGHT);
		crimeType.addItem("All Recorded Crimes");
		crimeType.addItem("Commercial Break and Enter");
		crimeType.addItem("Mischief Over $5000");
		crimeType.addItem("Mischief Under $5000");
		crimeType.addItem("Theft From Auto Over  $5000");
		crimeType.addItem("Theft From Auto Under $5000");
		crimeType.addItem("Theft Of Auto Over $5000");
		crimeType.addItem("Theft Of Auto Under $5000");

		crimeType.setItemSelected(0, true);
		
	}
	
	// Get all the selected crime types
	public static LinkedList<String> getSelectedTypes() {
		LinkedList<String> temp = new LinkedList<String>();
		
		if(crimeType.isItemSelected(0)){
			for (int i = 1; i < crimeType.getItemCount(); i++) {
				temp.add(crimeType.getValue(i));
		    }
			return temp;
		}
		
		for (int i = 0; i < crimeType.getItemCount(); i++) {
	        if (crimeType.isItemSelected(i)) {
	            temp.add(crimeType.getValue(i));
	        }
	    }
		return temp;
	}
	
	// draw the radius field
	private void drawRadius() {

		radius = new ListBox();
		radius.addStyleName("radius");
		radius.setTitle("Radius of the crime");
		radius.setHeight(HEIGHT);
		radius.setWidth(WIDTH_RADIUS);
		
		// Choices of radius
		radius.addItem("0.5 miles");
		radius.addItem("1 mile");
		radius.addItem("2 miles");
		radius.addItem("5 miles");
		
		radius.setSelectedIndex(1);
		
	}
	
	// Get the selected radius
	public static int getSelectedRadius() {
		String[] temp;
		
		for (int i = 0; i < radius.getItemCount(); i++) {
	        if (radius.isItemSelected(i)) {
	            temp = radius.getValue(i).split(" ");
	            return Integer.parseInt(temp[0]);
	        }
	    }
		return -1;
	}
	
	// draw the month field
		private void drawDate() {
			month = new ListBox();
			month.addStyleName("month");
			month.setTitle("Earliest crime time that you want to include");
			month.setHeight(HEIGHT);
			month.setWidth(WIDTH_MONTH);
			
			month.addItem("Jan");
			month.addItem("Feb");
			month.addItem("Mar");
			month.addItem("Apr");
			month.addItem("May");
			month.addItem("Jun");
			month.addItem("Jul");
			month.addItem("Aug");
			month.addItem("Sep");
			month.addItem("Oct");
			month.addItem("Nov");
			
		}
		

		// Get the selected minimum month 
		public static int getMonth(){
			return month.getSelectedIndex()+1;
		}

		public void displayCrimes(Crime[] crimes) {
			if (crimes.length > 0) {
				tab.selectTab(1);
				crimeBar.displayCrimes(crimes);
			}
			if(loadingMsg!=null){
				unloadingMsg();
				
			}
		}

		public void unloadingMsg() {
			loadingMsg.setVisible(false);
		}

		public void loadingCrimeMsg() {
			consolePanel.clear();
			
			loadingMsg = new TextBox();
			loadingMsg.setReadOnly(true);
			loadingMsg.setText("Loading Crimes...");
			loadingMsg.setSize("350px", "80px");
			loadingMsg.setAlignment(TextAlignment.CENTER);
			loadingMsg.addStyleName("header");

			consolePanel.add(loadingMsg);
		}

		public void postUpdateDb(int crimesAdded) {
			consolePanel.clear();
			postUpdateDbMsg1 = new TextBox();
			postUpdateDbMsg2 = new TextBox();
			postUpdateDbMsg1.setReadOnly(true);
			postUpdateDbMsg2.setReadOnly(true);
			postUpdateDbMsg1.addStyleName("header");
			postUpdateDbMsg2.addStyleName("header");
			postUpdateDbMsg1.setWidth("200px");
			postUpdateDbMsg2.setWidth("200px");
			if(crimesAdded != -1){
				postUpdateDbMsg1.setText("Database Successfully Updated");
				consolePanel.add(postUpdateDbMsg1);
				postUpdateDbMsg2.setText("Crimes added: "+crimesAdded);
				consolePanel.add(postUpdateDbMsg2);
			}else{
				postUpdateDbMsg1.setText("Database Failed to Update");
				consolePanel.add(postUpdateDbMsg1);
			}
		}

		public void updatingDbMsg() {
			consolePanel.clear();

			loadingMsg = new TextBox();
			loadingMsg.setReadOnly(true);
			loadingMsg.addStyleName("header");
			loadingMsg.setText("Updating Database...");
			loadingMsg.setSize("350px", "80px");
			loadingMsg.setAlignment(TextAlignment.CENTER);
			loadingMsg.addStyleName("header");

			consolePanel.add(loadingMsg);
		}
		
}
