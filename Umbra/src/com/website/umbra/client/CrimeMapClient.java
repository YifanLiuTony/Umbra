package com.website.umbra.client;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Entity;

import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class CrimeMapClient implements Serializable{
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String id;
	 @Persistent
	 private String name;
	 @Persistent
	 private String[] crimeTypes;
	 @Persistent
	 private int radius;
	 @Persistent
	 private int month;
	 @Persistent
	 private double lat;
	 @Persistent
	 private double lon;
	 
	 public CrimeMapClient() {}
	 
	 public CrimeMapClient(String[] CrimeTypes, int Radius, int Month, String name, double Lat, double Lon) {
		 this();
		 this.name = name;
		 this.crimeTypes = CrimeTypes;
		 this.radius = Radius;
		 this.month = Month;
		 this.lat = Lat;
		 this.lon = Lon;
	 }
	 
	 public String getId() {
		 return this.id;
	 }
	 
	 public String getName() {
		 return this.name;
	 }
	 
	 public String[] getCrimeTypes() {
		 return crimeTypes;
	 }
	 public int getRadius() {
		 return radius;
	 }
	 public int getMonth() {
		 return month;
	 }
	 
	 public double getLat(){
		 return lat;
	 }
	 public double getLon() {
		 return lon;
	 }

}
