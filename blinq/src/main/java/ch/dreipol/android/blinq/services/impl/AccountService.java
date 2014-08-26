package ch.dreipol.android.blinq.services.impl;

import ch.dreipol.android.blinq.services.IAccountService;
import ch.dreipol.android.blinq.services.SaveResult;
import ch.dreipol.android.blinq.services.model.SearchSettings;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by phil on 21/08/14.
 */
public class AccountService extends BaseService implements IAccountService {
    @Override
    public Observable<SearchSettings> getSearchSettings() {
        return BehaviorSubject.create(SearchSettings.defaultSettings());
    }

    @Override
    public Observable<SaveResult> saveSearchSettings(SearchSettings newSettings) {
        return BehaviorSubject.create(SaveResult.COMPLETE);
    }
}
