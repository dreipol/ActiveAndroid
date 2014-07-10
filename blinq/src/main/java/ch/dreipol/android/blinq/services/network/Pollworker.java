package ch.dreipol.android.blinq.services.network;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import ch.dreipol.android.blinq.services.ServerStatus;
import ch.dreipol.android.blinq.services.TaskStatus;
import ch.dreipol.android.blinq.services.network.retrofit.PollService;
import ch.dreipol.android.blinq.util.Bog;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;

/**
 * Created by phil on 26.03.14.
 */
public class Pollworker {

    private HashMap<String, BehaviorSubject<TaskStatus<JsonElement>>> mTaskMap;
    private PollService mService;
    private Scheduler.Worker mWorker;

    public Pollworker(PollService service) {

        mService = service;
        mTaskMap = new HashMap<String, BehaviorSubject<TaskStatus<JsonElement>>>();
        mWorker = Schedulers.io().createWorker();
        mWorker.schedulePeriodically(new Action0() {
            @Override
            public void call() {
                if (mTaskMap.size() > 0) {
                    poll();
                } else {
                    mWorker.unsubscribe();
                }
            }
        }, 200, 2000, TimeUnit.MILLISECONDS);
//TODO: remove subscription when finished aso
    }

    private void poll() {
        Set<String> keySet = mTaskMap.keySet();
        if (!keySet.isEmpty()) {
            HashMap body = new HashMap();
            body.put("tasks", keySet);
            Observable<GroupedObservable<ServerStatus, TaskStatus<JsonElement>>> taskStatus = mService.poll(body)
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Bog.e(Bog.Category.NETWORKING, "Networking error", throwable);
                        }
                    })
                    .flatMap(new Func1<List<TaskStatus<JsonElement>>, Observable<TaskStatus<JsonElement>>>() {
                        @Override
                        public Observable<TaskStatus<JsonElement>> call(List<TaskStatus<JsonElement>> taskStatuses) {
                            return Observable.from(taskStatuses);
                        }
                    })
                    .groupBy(new Func1<TaskStatus<JsonElement>, ServerStatus>() {
                        @Override
                        public ServerStatus call(TaskStatus<JsonElement> taskStatus) {
                            ServerStatus status = taskStatus.getStatus();
                            Bog.v(Bog.Category.NETWORKING, status.toString());
                            return status;
                        }
                    });

            filterObservable(taskStatus, ServerStatus.CANCEL).subscribeOn(Schedulers.io())
                    .subscribe(new Action1<TaskStatus<JsonElement>>() {
                        @Override
                        public void call(TaskStatus<JsonElement> taskStatus) {
                            Subject<TaskStatus<JsonElement>, TaskStatus<JsonElement>> subject = mTaskMap.remove(taskStatus.getTask_id());
                            subject.onError(new ServerError());
                        }
                    });
            filterObservable(taskStatus, ServerStatus.PENDING).subscribeOn(Schedulers.io()).subscribe(new Action1<TaskStatus<JsonElement>>() {
                @Override
                public void call(TaskStatus<JsonElement> jsonElementTaskStatus) {
                    Bog.e(Bog.Category.NETWORKING, String.format("%s: Pending", jsonElementTaskStatus.getTask_id()));
                }
            });
            filterObservable(taskStatus, ServerStatus.SUCCESS).subscribeOn(Schedulers.io()).subscribe(new Action1<TaskStatus<JsonElement>>() {
                @Override
                public void call(TaskStatus<JsonElement> taskStatus) {
                    Subject<TaskStatus<JsonElement>, TaskStatus<JsonElement>> subject = mTaskMap.remove(taskStatus.getTask_id());
                    subject.onNext(taskStatus);
                }
            });
        }
    }

    private Observable<TaskStatus<JsonElement>> filterObservable(Observable<GroupedObservable<ServerStatus, TaskStatus<JsonElement>>> taskStatus, final ServerStatus status) {
        Observable<TaskStatus<JsonElement>> statusObservable = taskStatus.filter(new Func1<GroupedObservable<ServerStatus, TaskStatus<JsonElement>>, Boolean>() {
            @Override
            public Boolean call(GroupedObservable<ServerStatus, TaskStatus<JsonElement>> stringTaskStatusGroupedObservable) {
                ServerStatus key = stringTaskStatusGroupedObservable.getKey();
                return key == status;
            }
        }).flatMap(new Func1<GroupedObservable<ServerStatus, TaskStatus<JsonElement>>, Observable<TaskStatus<JsonElement>>>() {

            @Override
            public Observable<TaskStatus<JsonElement>> call(GroupedObservable<ServerStatus, TaskStatus<JsonElement>> serverStatusTaskStatusGroupedObservable) {
                return serverStatusTaskStatusGroupedObservable;
            }
        });
        return statusObservable.subscribeOn(Schedulers.io());
    }

    public Observable<TaskStatus<JsonElement>> addTaskStatus(TaskStatus<JsonElement> status) {
        BehaviorSubject<TaskStatus<JsonElement>> subject = BehaviorSubject.create(status);
        mTaskMap.put(status.getTask_id(), subject);
//        mTaskStatuses = Observable.merge(mTaskStatuses);
        return subject;
    }
}
