package com.website.umbra.client;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;

public class CrimeBar extends Composite {
	
	// Frames
	private FlexTable crimeBar;
	private ScrollPanel scroll;
	
	//private LinkedList<Crime> crimes;
	
	// Label
	private final String barLabel = "Selected Crimes";
	
	private String WIDTH = "350px";
	private String HEIGHT = "500px";
	
	public CrimeBar (){
		
		crimeBar = new FlexTable();
		crimeBar.addStyleName("crimeBar");
		scroll = new ScrollPanel(crimeBar);
		initWidget(scroll);
		
		scroll.setWidth(WIDTH);

		loadHeader();
		crimeBar.setTitle(barLabel);
		crimeBar.setBorderWidth(2);
	}
	
	private void loadHeader() {
		crimeBar.setText(0, 0, "Crime Type");
		crimeBar.setText(0, 1, "Frequency");;
		crimeBar.setText(0, 2, "Percentage");
		crimeBar.getCellFormatter().addStyleName(0, 0, "header");
		crimeBar.getCellFormatter().addStyleName(0, 1, "header");
		crimeBar.getCellFormatter().addStyleName(0, 2, "header");
		crimeBar.getColumnFormatter().setWidth(0, "350px");
		
		
		crimeBar.setText(1, 0, "Commercial Break and Enter");
		crimeBar.setText(2, 0, "Mischief Over $5000");
		crimeBar.setText(3, 0, "Mischief Under $5000");
		crimeBar.setText(4, 0, "Theft From Auto Over  $5000");
		crimeBar.setText(5, 0, "Theft From Auto Under  $5000");
		crimeBar.setText(6, 0, "Theft Of Auto Over $5000");
		crimeBar.setText(7, 0, "Theft Of Auto Under $5000");
		
		for(int i=1;i<=7;i++){
			crimeBar.setText(i,1,"0");
			crimeBar.setText(i,2,"0%");
		}
	}
	
	public void scrollUp(){
		scroll.scrollToTop();
	}

	public void displayCrimes(Crime[] crimes) {
		crimeBar.clear();
		loadHeader();
		
		int length = crimes.length;
		
		// counts of different crime types
		ArrayList<Integer> crime_num = new ArrayList<Integer>();
		int crime1 = 0;
		int crime2 = 0;
		int crime3 = 0;
		int crime4 = 0;
		int crime5 = 0;
		int crime6 = 0;
		int crime7 = 0;
		
		for(int i=0;i<length;i++){
			switch (crimes[i].getCrimeType().toLowerCase()){
				case "commercial break and enter":
					crime1++;
					break;
				case "mischief over $5000":
					crime2++;
					break;
				case "mischief under $5000":
					crime3++;
					break;
				case "theft from auto over  $5000":
					crime4++;
					break;
				case "theft from auto under $5000":
					crime5++;
					break;
				case "theft of auto over $5000":
					crime6++;
					break;
				case "theft of auto under $5000":
					crime7++;
					break;
				
			}
		}
		crime_num.add(crime1);
		crime_num.add(crime2);
		crime_num.add(crime3);
		crime_num.add(crime4);
		crime_num.add(crime5);
		crime_num.add(crime6);
		crime_num.add(crime7);
		
		int sum = crime1+crime2+crime3+crime4+crime5+crime6+crime7;
		
		if(sum==0){
			Window.alert("No crimes found");
		}
		
		ArrayList<Double> percents = new ArrayList<Double>();
		percents.add(percent(crime1,sum));
		percents.add(percent(crime2,sum));
		percents.add(percent(crime3,sum));
		percents.add(percent(crime4,sum));
		percents.add(percent(crime5,sum));
		percents.add(percent(crime6,sum));
		percents.add(percent(crime7,sum));

		for(int i=1;i<=7;i++){
			
			crimeBar.setText(i,1,Integer.toString(crime_num.get(i-1)));
			crimeBar.setText(i,2,String.valueOf(percents.get(i-1))+"%");
		}
	}
	
	private double percent(int n, int sum){
		if(sum == 0) {
			return 0.0;
		}
		return (Math.round((n*1000)/sum))/10.0;
	}
}
