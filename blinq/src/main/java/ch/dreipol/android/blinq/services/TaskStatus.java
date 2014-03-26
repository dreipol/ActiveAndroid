
package ch.dreipol.android.blinq.services;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class TaskStatus {

    @Expose
    private String status;
    @Expose
    private List<Object> message = new ArrayList<Object>();
    @Expose
    private String task_id;
    @Expose
    private String error;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Object> getMessage() {
        return message;
    }

    public void setMessage(List<Object> message) {
        this.message = message;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", task_id, status);
    }
}