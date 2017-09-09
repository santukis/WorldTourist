package com.david.worldtourist.itemsmap.data.remote;


import android.util.Log;

import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.itemsmap.domain.usecase.model.Route;
import com.david.worldtourist.itemsmap.domain.usecase.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GoogleDirectionsJSONParser {

    private final String STATUS = "status";
    private final String OK = "OK";
    private final String ROUTES = "routes";
    private final String POLYLINE = "overview_polyline";
    private final String POINTS = "points";
    private final String LEGS = "legs";
    private final String STEPS  ="steps";
    private final String DISTANCE = "distance";
    private final String DURATION = "duration";
    private final String INSTRUCTIONS = "html_instructions";
    private final String TEXT = "text";

    Route getRouteFromJson(String json) {

        Route requestedRoute = new Route();

        try {
            JSONObject googleObject = new JSONObject(json);

            if (googleObject.getString(STATUS).equals(OK)) {
                JSONArray routes = googleObject.getJSONArray(ROUTES);

                if (routes.length() > 0) {
                    JSONObject route = routes.getJSONObject(0);
                    String codedRoute = route.optJSONObject(POLYLINE).optString(POINTS);

                    requestedRoute.setRoutePoints(getRoutePoints(codedRoute));

                    JSONArray legs = route.optJSONArray(LEGS);

                    if (legs.length() > 0) {
                        JSONObject leg = legs.getJSONObject(0);
                        String distance = leg.getJSONObject(DISTANCE).getString(TEXT);
                        requestedRoute.setDistance(distance);
                        String duration = leg.getJSONObject(DURATION).getString(TEXT);
                        requestedRoute.setDuration(duration);

                        JSONArray steps = leg.getJSONArray(STEPS);

                        requestedRoute.setRouteSteps(getRouteSteps(steps));
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("JSON", e.getLocalizedMessage());
            return Route.EMPTY_ROUTE;
        }

        return requestedRoute;
    }

    private List<GeoCoordinate> getRoutePoints(String encodedPath) {
        //Adapted from com.google.maps.android.Polyline
        // public static List<LatLng> decode(String encodedPath) {}

        List<GeoCoordinate> routePoints = new ArrayList<>();

        int len = encodedPath.length();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while(index < len) {
            int result = 1;
            int shift = 0;

            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while(b >= 31);

            lat += (result & 1) != 0?~(result >> 1):result >> 1;
            result = 1;
            shift = 0;

            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while(b >= 31);

            lng += (result & 1) != 0?~(result >> 1):result >> 1;
            routePoints.add(new GeoCoordinate((double)lat * 1.0E-5D, (double)lng * 1.0E-5D));
        }

        return routePoints;
    }

    private List<Step> getRouteSteps(JSONArray steps) throws JSONException {
        List<Step> routeSteps = new ArrayList<>();

        for(int stepPosition = 0; stepPosition < steps.length(); stepPosition++) {
            JSONObject step = steps.getJSONObject(stepPosition);
            routeSteps.add(new Step(
                    step.optJSONObject(DISTANCE).optString(TEXT),
                    step.optJSONObject(DURATION).optString(TEXT),
                    step.optString(INSTRUCTIONS)));
        }
        return routeSteps;
    }
}
