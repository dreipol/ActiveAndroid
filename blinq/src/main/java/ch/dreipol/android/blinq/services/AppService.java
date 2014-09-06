package ch.dreipol.android.blinq.services;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import ch.dreipol.android.dreiworks.ICacheService;

/**
 * Created by phil on 22.03.14.
 */
public class AppService {

    public enum ServiceType {
        RUNTIME, LOCATION, FACEBOOK, NETWORK, VALUE_STORE, IMAGE_CACHE, ACCOUNT, DATABASE, JSON_CACHE, MATCHES
    }

    private static volatile AppService instance = null;

    private Context mContext;

    private Map<ServiceType, IService> mServices;

    public static AppService getInstance() {
        if (instance == null) {

            throw new IllegalStateException("BLINQ App Services not initialized. Call initialize before using the service.");
        }
        return instance;
    }

    public static void initialize(IServiceConfiguration configuration) {
        if (instance == null) {
            synchronized (AppService.class) {
                instance = new AppService(configuration);
            }
        } else {
            instance.clear();
            instance.setup(configuration);
        }
    }

// convenience methods...

    public ILocationService getLocationService() {
        return (ILocationService) getService(ServiceType.LOCATION);
    }

    public IFacebookService getFacebookService() {
        return (IFacebookService) getService(ServiceType.FACEBOOK);
    }

    public IValueStoreService getValueStore() {
        return (IValueStoreService) getService(ServiceType.VALUE_STORE);
    }

    public INetworkMethods getNetworkService() {
        return (INetworkMethods) getService(ServiceType.NETWORK);
    }

    public IRuntimeService getRuntimeService() {
        return (IRuntimeService) getService(ServiceType.RUNTIME);
    }

    public IImageCacheService getImageCacheService() {
        return (IImageCacheService) getService(ServiceType.IMAGE_CACHE);
    }

    public IMatchesService getMatchesService() {
        return (IMatchesService) getService(ServiceType.MATCHES);
    }

    public IAccountService getAccountService() {
        return (IAccountService) getService(ServiceType.ACCOUNT);
    }
    public IDatabaseService getDatabaseService() {
        return (IDatabaseService) getService(ServiceType.DATABASE);
    }
    public ICacheService getJsonCacheService() {
        return (ICacheService) getService(ServiceType.JSON_CACHE);
    }
    public Context getContext() {
        return mContext;
    }

    public void registerService(ServiceType serviceType, IService service) {
        mServices.put(serviceType, service);
    }

    public IService getService(ServiceType serviceType) {
        return mServices.get(serviceType);
    }

    private void setup(IServiceConfiguration configuration) {

        mServices = new HashMap<ServiceType, IService>();

        mContext = configuration.getContext();

        if (mContext == null) {
            throw new RuntimeException("Cannot work with a null context");
        }

        try {
            registerService(ServiceType.RUNTIME, configuration.runtimeServiceBuilder().build(this));
            registerService(ServiceType.DATABASE, configuration.databaseServiceBuilder().build(this));
            registerService(ServiceType.LOCATION, configuration.locationServiceBuilder().build(this));
            registerService(ServiceType.FACEBOOK, configuration.facebookServiceBuilder().build(this));
            registerService(ServiceType.NETWORK, configuration.networkServiceBuilder().build(this));
            registerService(ServiceType.VALUE_STORE, configuration.valueStoreServiceBuilder().build(this));
            registerService(ServiceType.JSON_CACHE, configuration.jsonCacheServiceBuilder().build(this));
            registerService(ServiceType.IMAGE_CACHE, configuration.imageCacheServiceBuilder().build(this));
            registerService(ServiceType.MATCHES, configuration.matchesServiceBuilder().build(this));
            registerService(ServiceType.ACCOUNT, configuration.accountServiceBuilder().build(this));

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }


    private void clear() {
        for (IService service : mServices.values()) {
            service.dispose();
        }
        mServices = new HashMap<ServiceType, IService>();
    }


    private AppService(IServiceConfiguration configuration) {
        setup(configuration);
    }
}
