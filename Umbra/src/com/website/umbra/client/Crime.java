package com.website.umbra.client;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.website.umbra.shared.LatLon;


/**
 * Crime object, saves the data for each individual crime
 * @author Joon
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Crime implements Serializable {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;
	@Persistent
	private String crimeType;
	@Persistent
	private String crimeTypeLowerCase;
	@Persistent
	private int year;
	@Persistent
	private int month;
	@Persistent
	private String address;
	@Persistent(dependent = "true")
	private double lat;
	@Persistent(dependent = "true")
	private double lon;
	
	public Crime() {}
	
	/**
	 * Constructs a Crime object. 
	 * @param crimeType String of the crimeType. Must be exact.
	 * @param location LatLon Location of the crime.
	 * @param month  Integer (1,2,3-12) of the month
	 */
	public Crime(String crimeType, int year, int month, String address, LatLon location){
	    this();
		this.crimeType = crimeType;
		this.crimeTypeLowerCase = crimeType.toLowerCase();
		this.year = year;
		this.month = month;
		this.address = address;
		this.lat = location.getLat();
		this.lon = location.getLon();
	}
	
    public String getId() {
        return this.id;
    }
	
	/**
	 * Gets the crimeType of the Crime object
	 * @return  a crimeType string
	 */
	public String getCrimeType() {
		return crimeType;
	}
	
	/**
	 * Returns the year of the crime
	 * @return  an int
	 */
	public int getYear() {
		return year;
	}
	
	/**
	 * Returns the month of the crime
	 * @return  an int
	 */
	public int getMonth() {
		return month;
	}
	
	/**
	 * Gets the address of the Crime object
	 * @return  an address string
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Returns the location of the crime
	 * @return  a LatLon 
	 */
	public LatLon getLocation() {
		return new LatLon(lat,lon);
	}
	
	/**
	 * Returns Lat
	 * @return  a double
	 */
	public double getLat() {
		return lat;
	}
	
	/**
	 * Returns the Lon
	 * @return  a double
	 */
	public double getLon() {
		return lon;
	}
}
