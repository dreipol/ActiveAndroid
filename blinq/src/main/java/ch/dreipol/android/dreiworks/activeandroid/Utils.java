package ch.dreipol.android.dreiworks.activeandroid;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.lang.reflect.Field;

/**
 * Created by melbic on 22/08/14.
 */
public abstract class Utils {

    public static <T extends Model, O extends Model> From getManyAsFrom(Field f, O object) throws IllegalAccessException {
        Class<T> declaringClass = (Class<T>) f.getDeclaringClass();
        String columnName = Cache.getTableInfo(declaringClass).getColumnName(f);
        T o = (T) f.get(object);
        return new Select().from(declaringClass).where(columnName + "=?", o.getId());
    }

    public static <T extends Model, O extends Model> From getManyAsFrom(Class<T> declaringClass, O object) throws IllegalAccessException {
//       TODO: first search by name
        Field theField = null;
        for (Field f : declaringClass.getFields()) {
            if (f.getType().isAssignableFrom(object.getClass())) {
                theField = f;
                break;
            }
        }
        if (theField == null) {
            throw new RuntimeException(new NoSuchFieldException());
        }
        String columnName = Cache.getTableInfo(declaringClass).getColumnName(theField);
        T o = (T) theField.get(object);
        return new Select().from(declaringClass).where(columnName + "=?", o.getId());
    }
}
