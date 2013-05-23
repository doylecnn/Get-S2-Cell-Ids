package com.doylecnn;

/**
 * Created with IntelliJ IDEA.
 * User: doylecnn
 * Date: 13-5-22
 * Time: 下午11:10
 * To change this template use File | Settings | File Templates.
 */
public class LocationCoordinate2D {
    private double latitude;
    private double longitude;

    public LocationCoordinate2D(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
