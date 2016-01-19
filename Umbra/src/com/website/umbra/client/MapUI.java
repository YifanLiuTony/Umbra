package com.website.umbra.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.events.MouseEvent;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.overlays.InfoWindow;
import com.google.gwt.maps.client.overlays.InfoWindowOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.website.umbra.shared.LatLon;

/**
 * The MAP UI class, uses Google Maps API, part of the main UI
 * @author Gustav
 * @author Tony
 *
 */
public class MapUI extends Composite{
	
	public static MapWidget map;
	public static VerticalPanel mapPanel;
	private static List<Marker> allMarkers = new ArrayList<Marker>();


	/**
	 * Constructs a MapUI object.
	 */
	public MapUI() {
		mapPanel = new VerticalPanel();
		initWidget(mapPanel);
		drawMap();
	}
	
	private void drawMap() {
		mapPanel.clear();
	    LatLng UBC = LatLng.newInstance(49.2611, -123.2531);;
	    MapOptions opts = MapOptions.newInstance();
	    opts.setZoom(11);
	    opts.setCenter(UBC);
	    opts.setMapTypeId(MapTypeId.HYBRID);
	    
	    map = new MapWidget(opts);
	    mapPanel.add(map);
	    map.setSize("750px", "500px");
	    
	    map.addClickHandler(new ClickMapHandler() {
	      @Override
		public void onEvent(ClickMapEvent event) {
	      }
	    });

	}
	/**
	 * Generates a marker on the LatLon
	 * @param latlon
	 */
	public void drawPoint(LatLon latlon) {
	    LatLng point = LatLng.newInstance(latlon.getLat(), latlon.getLon());
		MarkerOptions options = MarkerOptions.newInstance();
		options.setPosition(point);
		options.setTitle("YOUR POINT");
		    
		final Marker marker = Marker.newInstance(options);
		marker.setMap(map);
		    
		marker.addClickHandler(new ClickMapHandler() {
		  @Override
		public void onEvent(ClickMapEvent event) {
		    drawInfoWindow(marker, "", event.getMouseEvent());
		  }

		});
		allMarkers.add(marker);
	}
	
	/**
	 * Generates a heatmap based on the locations of the given crimes.
	 * @param crimes
	 */
	public void drawHeatMap(Crime[] crimes) {
		if (crimes.length == 0) {
			Window.alert("No crimes to display");
		} else {
			for (Crime c : crimes) {
				double lat = c.getLat();
				double lon = c.getLon();
				
			    LatLng point = LatLng.newInstance(lat, lon);
				MarkerOptions options = MarkerOptions.newInstance();
				options.setPosition(point);
				options.setTitle(c.getCrimeType());
				    
				final Marker marker = Marker.newInstance(options);
				//An alternative to our current markers
				
				String crimeType = "";
				try {
					if (c.getCrimeType() != null) {
						crimeType = c.getCrimeType().toLowerCase();
					}
				
				final String crimeAddress = c.getAddress();
				if (crimeType.equals("commercial break and enter")) {
					marker.setIcon("https://maps.gstatic.com/intl/en_ALL/mapfiles/markers2/measle.png");
				} else if (crimeType.equals("commercial break and enter")) {
					marker.setIcon("https://maps.gstatic.com/intl/en_ALL/mapfiles/markers2/measle.png");
				} else if (crimeType.equals("mischief over $5000")) {
					marker.setIcon("https://maps.gstatic.com/intl/en_ALL/mapfiles/markers2/measle.png");
				} else if (crimeType.equals("mischief over $5000")) {
					marker.setIcon("https://maps.gstatic.com/intl/en_ALL/mapfiles/markers2/measle.png");
				} else if (crimeType.equals("theft from auto over  $5000")) {
					marker.setIcon("https://maps.gstatic.com/intl/en_us/mapfiles/markers2/measle_blue.png");
				} else if (crimeType.equals("theft from auto under $5000")) {
					marker.setIcon("https://maps.gstatic.com/intl/en_us/mapfiles/markers2/measle_blue.png");
				} else if (crimeType.equals("theft of auto over $5000")) {
					marker.setIcon("https://maps.gstatic.com/intl/en_us/mapfiles/markers2/measle_blue.png");
				} else if (crimeType.equals("theft of auto under $5000")) {
					marker.setIcon("https://maps.gstatic.com/intl/en_us/mapfiles/markers2/measle_blue.png");
				} else {
					marker.setIcon("https://maps.gstatic.com/intl/en_ALL/mapfiles/markers2/measle.png");			}
				marker.setMap(map);
				  
				marker.addClickHandler(new ClickMapHandler() {
				  @Override
				public void onEvent(ClickMapEvent event) {
				    drawInfoWindow(marker, crimeAddress, event.getMouseEvent());
				  }
				});
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				allMarkers.add(marker);
			}
		}
	}
	
	protected void drawInfoWindow(Marker marker, String address, MouseEvent mouseEvent) {
		   if (marker == null || mouseEvent == null) {
		     return;
		   }
		   HTML html = new HTML("You clicked on: " + marker.getTitle() + " at " + address);
		   InfoWindowOptions options = InfoWindowOptions.newInstance();
		   options.setContent(html);
		   InfoWindow iw = InfoWindow.newInstance(options);
		   iw.open(map, marker);
	}
	
	public void clearOverlays() {
		for (int i=0; i<allMarkers.size();i++){
			allMarkers.get(i).clear();;
		}
		allMarkers = new ArrayList<Marker>();
	}

    public void addPoint(LatLng pt){
    	return;
    }
    public void addPoints(final List<LatLng> pts){
    	return;
    }
    public int sizeOfMarkers() {
    	return allMarkers.size();
    }
}
