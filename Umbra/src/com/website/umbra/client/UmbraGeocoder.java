package com.website.umbra.client;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.Window;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;
import com.website.umbra.shared.LatLon;

/**
 * Geocoder for the project, used throughout the project to convert the address to a LatLon
 * @author Gustav
 *
 */
public class UmbraGeocoder {
	public static LatLon latlon;
	
	public static void Geocode(String address) {
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
	                    latlon = new LatLon(lat, lon);
	                } else {
	                    Window.alert("Location not found");
	                }
			}      
	    });
	}
	
	/**
	 * Geocode an address, passes it off to the API thread which will then draw the point on the map
	 * @param address a string that is the address
	 * @param map the mapUI
	 */
	public static void Geocode(String address, final MapUI map) {
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
	                    latlon = new LatLon(lat, lon);
	                    map.drawPoint(latlon);
	                } else {
	                    Window.alert("Location not found");
	                }
			}      
	    });
	}

}
