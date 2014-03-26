
package ch.dreipol.android.blinq.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile {

    private String distance;
    private String first_name;
    private String color_bottom;
    private String last_active;
    private String color_top;
    private Integer age;
    private Integer fb_id;
    private String sex;
    private List<Photo> photos = new ArrayList<Photo>();
    private Boolean liked_me;
    private Mutual_data mutual_data;
    private Boolean approved;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getColor_bottom() {
        return color_bottom;
    }

    public void setColor_bottom(String color_bottom) {
        this.color_bottom = color_bottom;
    }

    public String getLast_active() {
        return last_active;
    }

    public void setLast_active(String last_active) {
        this.last_active = last_active;
    }

    public String getColor_top() {
        return color_top;
    }

    public void setColor_top(String color_top) {
        this.color_top = color_top;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getFb_id() {
        return fb_id;
    }

    public void setFb_id(Integer fb_id) {
        this.fb_id = fb_id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public Boolean getLiked_me() {
        return liked_me;
    }

    public void setLiked_me(Boolean liked_me) {
        this.liked_me = liked_me;
    }

    public Mutual_data getMutual_data() {
        return mutual_data;
    }

    public void setMutual_data(Mutual_data mutual_data) {
        this.mutual_data = mutual_data;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
