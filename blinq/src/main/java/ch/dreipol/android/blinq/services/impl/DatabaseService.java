package ch.dreipol.android.blinq.services.impl;


import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IDatabaseService;
import ch.dreipol.android.blinq.services.model.ChatMessage;
import ch.dreipol.android.blinq.services.model.Match;
import ch.dreipol.android.blinq.services.model.MutualData;
import ch.dreipol.android.blinq.services.model.Profile;
import ch.dreipol.android.blinq.util.activeandroid.ListSerializer;
import ch.dreipol.android.blinq.util.activeandroid.MutualDataSerializer;
import ch.dreipol.android.blinq.util.activeandroid.SerializableTypeSerializer;

/**
 * Created by melbic on 21/08/14.
 */
public class DatabaseService extends BaseService implements IDatabaseService {
    @Override
    public void dispose() {
        super.dispose();
        ActiveAndroid.dispose();
    }

    @Override
    public void setup(AppService appService) {
        super.setup(appService);
        initializeDatabase();
    }

    protected void initializeDatabase() {
        Configuration.Builder builder = new Configuration.Builder(getService().getContext());
        builder.addModelClasses(Profile.class, Match.class, ChatMessage.class);
        builder.addTypeSerializer(MutualDataSerializer.class);
        ActiveAndroid.initialize(builder.create());
    }

    protected void removeDatabase() {
        String path = ActiveAndroid.getDatabase().getPath();
        ActiveAndroid.dispose();
        getService().getContext().deleteDatabase(path);
    }

    public void resetDatabase() {
        removeDatabase();
        initializeDatabase();
    }
}
