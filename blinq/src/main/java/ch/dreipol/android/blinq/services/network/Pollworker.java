package ch.dreipol.android.blinq.services.network;

import java.util.HashMap;
import java.util.List;

import ch.dreipol.android.blinq.services.TaskStatus;
import ch.dreipol.android.blinq.services.network.retrofit.PollService;
import ch.dreipol.android.blinq.util.Bog;
import rx.functions.Action1;

/**
 * Created by phil on 26.03.14.
 */
public class Pollworker {

    private HashMap<String, TaskStatus> mTaskMap;
    private PollService mService;

    public Pollworker(PollService service) {

        mService = service;
        mTaskMap = new HashMap<String, TaskStatus>();
    }

    public void poll() {
        HashMap body = new HashMap();
        body.put("tasks", mTaskMap.keySet());
        mService.poll(body).doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Bog.e(Bog.Category.NETWORKING, "Networking error", throwable);
            }
        }).subscribe(new Action1<List<TaskStatus>>() {
                    @Override
                    public void call(List<TaskStatus> taskStatuses) {
                        Bog.v(Bog.Category.NETWORKING, taskStatuses.toString());
                    }

                });
    }

    public void addTaskStatus(TaskStatus status) {
        mTaskMap.put(status.getTask_id(), status);
    }

}
