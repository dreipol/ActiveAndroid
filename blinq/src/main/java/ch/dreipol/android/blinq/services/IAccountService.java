package ch.dreipol.android.blinq.services;

import ch.dreipol.android.blinq.services.model.SearchSettings;
import ch.dreipol.android.blinq.services.model.SettingsProfile;
import rx.Observable;

/**
 * Created by phil on 21/08/14.
 */
public interface IAccountService extends IService {

    public rx.Observable<SearchSettings> getSearchSettings();

    public rx.Observable<SaveResult> saveSearchSettings(SearchSettings newSettings);


    public Observable<SettingsProfile> getMe();

    Observable<SaveResult> saveMe(SettingsProfile myProfile);
}
