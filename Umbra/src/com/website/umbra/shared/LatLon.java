package com.website.umbra.shared;

import java.io.Serializable;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * LatLon class, used to store a location
 *
 */

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true", embeddedOnly = "true")
public class LatLon implements Serializable{
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String keyEncoded;
	@Persistent
	private double latitude;
	@Persistent
	private double longitude;
	
	public LatLon(){}
	
	/**
	 * Constructs a LatLon with a given latitude and longitude
	 * @param latitude 
	 * @param longitude
	 */
	public LatLon(double latitude, double longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * Returns the latitude
	 * @return latitude
	 */
	public double getLat(){
		return latitude;
	}
	
	/**
	 * Returns the Longitude
	 * @return longitude
	 */
	public double getLon(){
		return longitude;
	}
	
	/**
	 * Sets the latitude
	 * @param latitude
	 */
	public void setLat(double latitude){
		this.latitude = latitude;
	}
	
	/**
	 * Sets the longitude
	 * @param longitude
	 */
	public void setLon(double longitude){
		this.longitude = longitude;
	}
}
