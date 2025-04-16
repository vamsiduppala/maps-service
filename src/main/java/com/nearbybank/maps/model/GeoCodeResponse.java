package com.nearbybank.maps.model;

import java.util.List;

public class GeoCodeResponse {
    public List<Result> results;
    public String status;

    public static class Result {
        public Geometry geometry;
		public String vicinity;
		public String name;
    }

    public static class Geometry {
        public Location location;
    }

    public static class Location {
        public double lat;
        public double lng;
    }
}
