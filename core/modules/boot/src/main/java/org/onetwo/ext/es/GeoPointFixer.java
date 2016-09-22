package org.onetwo.ext.es;

public class GeoPointFixer {
	private double lat;
    private double lon;
    
	public GeoPointFixer() {
		super();
	}
	public GeoPointFixer(double lat, double lon) {
		super();
		this.lat = lat;
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
    
}
