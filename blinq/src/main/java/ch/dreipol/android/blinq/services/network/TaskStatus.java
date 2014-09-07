
package ch.dreipol.android.blinq.services.network;

import com.google.gson.annotations.Expose;

import ch.dreipol.android.blinq.services.ServerStatus;

enum Status {}
public class TaskStatus<T> {

    @Expose
    private ServerStatus status;
    @Expose
    private T message;
    @Expose
    private String task_id;
    @Expose
    private String error;

    public ServerStatus getStatus() {
        return status;
    }

    public void setStatus(ServerStatus status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = ServerStatus.fromString(status);
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
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

    public static <T> TaskStatus<T> initialStatus() {
        TaskStatus<T> task = new TaskStatus<T>();
        task.setStatus("initial");
        return task;
    }
}