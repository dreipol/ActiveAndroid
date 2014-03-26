
package ch.dreipol.android.blinq.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mutual_data {

    private List<Object> schools = new ArrayList<Object>();
    private List<Object> likes = new ArrayList<Object>();
    private List<Object> places = new ArrayList<Object>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<Object> getSchools() {
        return schools;
    }

    public void setSchools(List<Object> schools) {
        this.schools = schools;
    }

    public List<Object> getLikes() {
        return likes;
    }

    public void setLikes(List<Object> likes) {
        this.likes = likes;
    }

    public List<Object> getPlaces() {
        return places;
    }

    public void setPlaces(List<Object> places) {
        this.places = places;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
