
package ch.dreipol.android.blinq.services.model;

import java.util.HashMap;
import java.util.Map;

public class Photo {

    private String full_id;
    private String profile_id;
    private Long object_id;
    private String thumb_id;
    private Integer eviction;
    private Long pk;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getFull_id() {
        return full_id;
    }

    public void setFull_id(String full_id) {
        this.full_id = full_id;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public Long getObject_id() {
        return object_id;
    }

    public void setObject_id(Long object_id) {
        this.object_id = object_id;
    }

    public String getThumb_id() {
        return thumb_id;
    }

    public void setThumb_id(String thumb_id) {
        this.thumb_id = thumb_id;
    }

    public Integer getEviction() {
        return eviction;
    }

    public void setEviction(Integer eviction) {
        this.eviction = eviction;
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
