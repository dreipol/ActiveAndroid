package ch.dreipol.android.blinq.services.impl;

import java.io.IOException;

import ch.dreipol.android.blinq.services.IAccountService;
import ch.dreipol.android.blinq.services.SaveResult;
import ch.dreipol.android.blinq.services.model.SearchSettings;
import ch.dreipol.android.blinq.services.model.SettingsProfile;
import ch.dreipol.android.dreiworks.JsonStoreName;
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

    @Override
    public Observable<SettingsProfile> getMe() {
        this.getService().getNetworkService().getMe();
        return this.getService().getJsonCacheService().getObservable(JsonStoreName.SETTINGS_PROFILE.toString(), SettingsProfile.class);
    }
}
