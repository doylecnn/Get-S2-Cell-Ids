package com.doylecnn;

import java.util.ArrayList;
import java.util.List;

import com.google.common.geometry.*;

/**
 * Created with IntelliJ IDEA.
 * User: doylecnn
 * Date: 13-5-22
 * Time: 下午10:58
 * To change this template use File | Settings | File Templates.
 */
public class S2Geometry {
    public List<String> cellsForRegion (LocationCoordinate2D neCoord, LocationCoordinate2D swCoord,  int minZoomLevel, int maxZoomLevel)
    {
        S2LatLngRect rect = new S2LatLngRect(S2LatLng.fromDegrees(Math.min(neCoord.getLatitude(), swCoord.getLatitude()), Math.min(neCoord.getLongitude(), swCoord.getLongitude())), S2LatLng.fromDegrees(Math.max(neCoord.getLatitude(), swCoord.getLatitude()), Math.max(neCoord.getLongitude(), swCoord.getLongitude())));

        S2RegionCoverer coverer = new S2RegionCoverer();
        coverer.setMinLevel(minZoomLevel);
        coverer.setMaxLevel(maxZoomLevel);

        ArrayList<S2CellId> covering = new ArrayList<S2CellId>();
        coverer.getCovering(rect, covering);

        List<String> cellsArray = new ArrayList<String>(covering.size());

        for(S2CellId cellId : covering) {
            cellsArray.add(String.format("%016x", cellId.id()));
        }

        return cellsArray;
    }

    public LocationCoordinate2D coordinateForCellId(long numCellId)
    {
        S2CellId cellId = new S2CellId(numCellId);
        S2LatLng latLng = cellId.toLatLng();
        return new LocationCoordinate2D(latLng.lat().degrees(), latLng.lng().degrees());
    }
}
