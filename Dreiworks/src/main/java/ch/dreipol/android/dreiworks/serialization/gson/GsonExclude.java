package ch.dreipol.android.dreiworks.serialization.gson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Samuel Bichsel, dreipol GmbH on 13.01.14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface GsonExclude
{
}
