package ch.dreipol.android.blinq.util.activeandroid;

import ch.dreipol.android.blinq.services.model.MutualData;

/**
 * Created by melbic on 21/08/14.
 */
public  class MutualDataSerializer extends SerializableTypeSerializer<MutualData> {

    @Override
    public Class<?> getDeserializedType() {
        return MutualData.class;
    }
}
