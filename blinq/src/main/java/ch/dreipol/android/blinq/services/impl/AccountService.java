package ch.dreipol.android.blinq.services.impl;

import java.io.IOException;

import ch.dreipol.android.blinq.services.IAccountService;
import ch.dreipol.android.blinq.services.SaveResult;
import ch.dreipol.android.blinq.services.model.SearchSettings;
import ch.dreipol.android.blinq.services.model.SettingsProfile;
import ch.dreipol.android.blinq.util.Bog;
import ch.dreipol.android.dreiworks.ICacheService;
import ch.dreipol.android.dreiworks.JsonStoreName;
import ch.dreipol.android.dreiworks.jsonstore.CachedModel;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by phil on 21/08/14.
 */
public class AccountService extends BaseService implements IAccountService {
    @Override
    public Observable<SearchSettings> getSearchSettings() {

        ICacheService jsonCacheService = getService().getJsonCacheService();
        String key = JsonStoreName.SEARCH_SETTINGS.toString();
        try {
            CachedModel<SearchSettings> searchModel = jsonCacheService.get(key, SearchSettings.class);
            if (searchModel.getObject() == null) {
                jsonCacheService.put(key, SearchSettings.defaultSettings());
            }
        } catch (IOException e) {
            return Observable.error(e);
        }
        return jsonCacheService.getObservable(key, SearchSettings.class);
    }

    @Override
    public Observable<SaveResult> saveSearchSettings(SearchSettings newSettings) {
        try {
            getService().getJsonCacheService().put(JsonStoreName.SEARCH_SETTINGS.toString(), newSettings);
        } catch (IOException e) {
            e.printStackTrace();
            return Observable.just(SaveResult.ERROR);
        }
        return Observable.just(SaveResult.COMPLETE);
    }

    @Override
    public Observable<SettingsProfile> getMe() {
        this.getService().getNetworkService().getMe();
        return this.getService().getJsonCacheService().getObservable(JsonStoreName.SETTINGS_PROFILE.toString(), SettingsProfile.class);
    }

    @Override
    public Observable<SettingsProfile> signup() {
        return this.getService().getNetworkService().signup();
    }

    @Override
    public Observable<SaveResult> saveMe(SettingsProfile myProfile) {
        //TODO: save to server
        SaveResult saveResult = SaveResult.COMPLETE;
        try {
            this.getService().getJsonCacheService().put(JsonStoreName.SETTINGS_PROFILE.toString(), myProfile);
        } catch (IOException e) {
            Bog.e(Bog.Category.SYSTEM, e.toString());
            saveResult = SaveResult.ERROR;
        }
        return Observable.just(saveResult);
    }
}
