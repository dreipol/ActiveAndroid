package ch.dreipol.android.blinq.services.network;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import ch.dreipol.android.blinq.services.ServerStatus;
import ch.dreipol.android.blinq.services.network.retrofit.PollService;
import ch.dreipol.android.blinq.util.Bog;
import ch.dreipol.android.blinq.util.NullSubscription;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
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

    }

    public Observable<TaskStatus<JsonElement>> addTaskStatus(TaskStatus<JsonElement> status) {
        BehaviorSubject<TaskStatus<JsonElement>> subject = BehaviorSubject.create(status);
        mTaskMap.put(status.getTask_id(), subject);
        schedulePolling();
        return subject;
    }

    private void schedulePolling() {
        if (mWorker == null || mWorker.isUnsubscribed()) {
            mWorker = Schedulers.io().createWorker();
            mWorker.schedulePeriodically(new Action0() {
                @Override
                public void call() {
                    Subscription subscription = poll();
                    if (mTaskMap.size() == 0) {
                        subscription.unsubscribe();
                        mWorker.unsubscribe();
                    }
                }
            }, 200, 2000, TimeUnit.MILLISECONDS);
        }
    }

    private Subscription poll() {
        Bog.v(Bog.Category.NETWORKING, "polling");
        Set<String> keySet = mTaskMap.keySet();
        Subscription subscription = new NullSubscription();
        if (!keySet.isEmpty()) {
            HashMap body = new HashMap();
            body.put("tasks", keySet);
            Observable<List<TaskStatus<JsonElement>>> pollingObservable = mService.poll(body);
            ConnectableObservable<GroupedObservable<ServerStatus, TaskStatus<JsonElement>>> taskStatus = pollingObservable
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
                    }).publish();

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
                    subject.onCompleted();
                }
            });
            subscription = taskStatus.connect();
        }
        return subscription;
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
}