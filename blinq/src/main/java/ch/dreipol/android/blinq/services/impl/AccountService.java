package ch.dreipol.android.blinq.services.impl;

import java.io.IOException;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.IAccountService;
import ch.dreipol.android.blinq.services.SaveResult;
import ch.dreipol.android.blinq.services.model.Photo;
import ch.dreipol.android.blinq.services.model.SearchSettings;
import ch.dreipol.android.blinq.services.model.SettingsProfile;
import ch.dreipol.android.blinq.util.Bog;
import ch.dreipol.android.dreiworks.ICacheService;
import ch.dreipol.android.dreiworks.JsonStoreName;
import ch.dreipol.android.dreiworks.jsonstore.CachedModel;
import rx.Observable;
import ch.dreipol.android.blinq.services.network.UploadProfile;

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
            saveMeToCache(myProfile);
        } catch (IOException e) {
            Bog.e(Bog.Category.SYSTEM, e.toString());
            saveResult = SaveResult.ERROR;
        }
        return Observable.just(saveResult);
    }


    @Override
    public void update() {
        try {
            CachedModel<SettingsProfile> cachedModel = getMeFromCache();
            if (cachedModel.doesExist()) {
                UploadProfile uploadProfile = new UploadProfile(cachedModel.getObject());
                String language = getService().getContext().getString(R.string.lang);
                LocationService.LocationInformation information = getService().getLocationService().getCurrentLocationInformation();
                uploadProfile.setLanguage(language);
                uploadProfile.setLocation(information.getLocation());
                getService().getNetworkService().update(uploadProfile);
            }
        } catch (IOException e) {
            Bog.e(Bog.Category.NETWORKING, "The profile couldn't be received from Json store. " + e.toString());
        }
    }

    @Override
    public void removePhoto(Photo photo) {
        try {
            CachedModel<SettingsProfile> cachedModel = getMeFromCache();
            SettingsProfile profile = cachedModel.getObject();
            profile.removePhoto(photo);
            saveMeToCache(profile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getService().getNetworkService().removePhoto(photo);
    }

    protected CachedModel<SettingsProfile> getMeFromCache() throws IOException {
        return getService().getJsonCacheService().get(JsonStoreName.SETTINGS_PROFILE.toString(), SettingsProfile.class);
    }

    protected void saveMeToCache(SettingsProfile myProfile) throws IOException {
        this.getService().getJsonCacheService().put(JsonStoreName.SETTINGS_PROFILE.toString(), myProfile);
    }

}
