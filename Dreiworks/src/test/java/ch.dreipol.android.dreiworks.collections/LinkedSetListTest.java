package ch.dreipol.android.dreiworks.collections;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//import ch.dreipol.android.blinq.services.Profile;
import rx.functions.Func1;

/**
 * Created by melbic on 16/07/14.
 */
public class LinkedSetListTest {
    private LinkedSetList<Long, String> mLinkedSetList;

    @Before
    public void setUp() {
        mLinkedSetList = new LinkedSetList<Long, String>(new Func1<String, Long>() {
            @Override
            public Long call(String profile) {
                return (long) profile.hashCode();
            }
        });

    }

    @Test
    public void testingAdd() {
        String hans = getHans();
        mLinkedSetList.add(hans);
        Assert.assertTrue(mLinkedSetList.size() == 1);
    }

    @Test
    public void testingAddingHansTwice() {
        String hans1 = getHans();
        String hans2 = getHans();
        mLinkedSetList.add(hans1);
        mLinkedSetList.add(hans2);
        Assert.assertTrue(mLinkedSetList.size() == 1);
    }

    @Test
    public void testAddMap() {
        addHansAndVreni();
        Assert.assertTrue(mLinkedSetList.size() == 2);
        Assert.assertTrue(mLinkedSetList.contains("Hans"));
        Assert.assertTrue(mLinkedSetList.contains("Vreni"));
    }

    @Test
    public void testIterator() {
        addHansAndVreni();
        FutureAwareIterator<String> iterator = mLinkedSetList.iterator();
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
        mLinkedSetList.addAll(m);
    }

    @Test
    public void testIteratorNewInsertions() {
        addHansAndVreni();
        FutureAwareIterator<String> iterator = mLinkedSetList.iterator();
        Assert.assertEquals(2, iterator.aheadCount());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next(),"Hans");
        Assert.assertEquals(1, iterator.aheadCount());
        mLinkedSetList.add("Peter");
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
