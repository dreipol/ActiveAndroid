package ch.dreipol.android.dreiworks.serialization.gson;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;

import java.lang.reflect.Field;

/**
* Created by Samuel Bichsel, dreipol GmbH on 29.01.14.
*/
public class AndroidFieldNamingStrategy implements FieldNamingStrategy {
    final private FieldNamingPolicy mNamingPolicy;

    public AndroidFieldNamingStrategy(FieldNamingPolicy policy) {
        super();
        mNamingPolicy = policy;
    }


    @Override
    public String translateName(Field field) {
        String originalName = field.getName();
        String convertedName = mNamingPolicy.translateName(field);
        if (originalName.charAt(0) == 'm' && originalName.length() > 1 && Character.isUpperCase(originalName.codePointAt(1))) {
            convertedName = convertedName.substring(1);
            if(convertedName.charAt(0) == '_')
                convertedName = convertedName.substring(1);
        }
        return convertedName;
    }

}
