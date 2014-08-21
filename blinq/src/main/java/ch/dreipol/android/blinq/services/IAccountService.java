package ch.dreipol.android.blinq.services;

import ch.dreipol.android.blinq.services.model.SearchSettings;

/**
 * Created by phil on 21/08/14.
 */
public interface IAccountService extends IService {

    public rx.Observable<SearchSettings> getSearchSettings();

    public rx.Observable<SaveResult> saveSearchSettings(SearchSettings newSettings);


}
