package ch.dreipol.android.dreiworks.collections;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//import ch.dreipol.android.blinq.services.Profile;
import rx.functions.Func1;

/**
 * Created by melbic on 16/07/14.
 */
public class FunctionMapTest {
    private FunctionMap<Long, String> mFunctionMap;

    @Before
    public void setUp() {
        mFunctionMap = new FunctionMap<Long, String>(new Func1<String, Long>() {
            @Override
            public Long call(String profile) {
                return (long) profile.hashCode();
            }
        });

    }

    @Test
    public void testingAdd() {
        String hans = getHans();
        mFunctionMap.add(hans);
        Assert.assertTrue(mFunctionMap.size() == 1);
    }

    @Test
    public void testingAddingHansTwice() {
        String hans1 = getHans();
        String hans2 = getHans();
        mFunctionMap.add(hans1);
        mFunctionMap.add(hans2);
        Assert.assertTrue(mFunctionMap.size() == 1);
    }

    @Test
    public void testAddMap() {
        addHansAndVreni();
        Assert.assertTrue(mFunctionMap.size() == 2);
        Assert.assertTrue(mFunctionMap.contains("Hans"));
        Assert.assertTrue(mFunctionMap.contains("Vreni"));
    }

    @Test
    public void testStandardIterator() {
        addHansAndVreni();
        Iterator<String> iterator = mFunctionMap.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next().equals("Hans"));
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next().equals("Vreni"));
        Assert.assertFalse(iterator.hasNext());
    }

    private void addHansAndVreni() {
        Map<Long, String> m = new HashMap<Long, String>();
        String hans = "Hans";
        String vreni = "Vreni";
        m.put((long) hans.hashCode(), hans);
        m.put((long) vreni.hashCode(), vreni);
        mFunctionMap.addAll(m);
    }

    @Test
    public void testIteratorNewInsertions() {
        addHansAndVreni();
        mFunctionMap.setIterator(new CompoundIterator<String>(2, mFunctionMap.values()));
        FutureAwareIterator<String> iterator = (FutureAwareIterator<String>) mFunctionMap.iterator();
        Assert.assertEquals(2, iterator.aheadCount());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next(), "Hans");
        Assert.assertEquals(1, iterator.aheadCount());
        mFunctionMap.add("Peter");
        Assert.assertEquals(2, iterator.aheadCount());

    }

    private String getHans() {
//        Profile hans = new Profile();
//        hans.setAge(20);
//        hans.setFb_id(1234l);
//        hans.setFirst_name("Hans");
        return "hans";
    }
}
