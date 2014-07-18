package ch.dreipol.android.dreiworks.collections;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Created by melbic on 17/07/14.
 */
public class CompoundIteratorTest {

    private ArrayList<Integer> mList;


    @Before
    public void setUp() {
        mList = new ArrayList<Integer>();

        for (Integer i = 0; i < 10; i++) {
            mList.add(i);
        }
    }

    @Test
    public void test2Iterators() {
        CompoundIterator<Integer> compoundIterator = new CompoundIterator<Integer>(2, mList);
        ILazyIterator<Integer> firstIterator = compoundIterator.getIterator(0);
        ILazyIterator<Integer> secondIterator = compoundIterator.getIterator(1);
        firstIterator.move();
        Assert.assertEquals(Integer.valueOf(0), firstIterator.head());
        Assert.assertTrue(firstIterator.hasNext());
        firstIterator.move();
        Assert.assertEquals(Integer.valueOf(1), firstIterator.head());
        secondIterator.move();
        Assert.assertEquals(Integer.valueOf(2), secondIterator.head());
        Random randomGenerator = new Random();
        ILazyIterator<Integer> it;
        for (int i = 3; i < 10; i++) {
            int iteratorNumber = randomGenerator.nextInt(2);
            System.out.println("Iterator: " + iteratorNumber);
            it = compoundIterator.getIterator(iteratorNumber);
            it.move();
            Assert.assertEquals(i, it.head().intValue());
        }
    }

    @Test(expected = IllegalAccessError.class)
    public void testIteratorNotMoved() {
        CompoundIterator<Integer> compoundIterator = new CompoundIterator<Integer>(2, mList);
        ILazyIterator<Integer> firstIterator = compoundIterator.getIterator(0);
        Integer i = firstIterator.head();
    }

    @Test(expected = NoSuchElementException.class)
    public void testLimit() {
        CompoundIterator<Integer> compoundIterator = new CompoundIterator<Integer>(2, mList);
        ILazyIterator<Integer> firstIterator = compoundIterator.getIterator(0);
        ILazyIterator<Integer> secondIterator = compoundIterator.getIterator(1);

        ILazyIterator<Integer> it;
        for (Integer i : mList) {
            it = (i % 2) == 0 ? secondIterator : firstIterator;
            it.move();
            Assert.assertEquals(i, it.head());
        }
        Assert.assertFalse(firstIterator.hasNext());
        Assert.assertFalse(secondIterator.hasNext());
        firstIterator.move();
    }

    @Test
    public void testAsIterator() {
        CompoundIterator<Integer> compoundIterator = new CompoundIterator<Integer>(2, mList);
        int number = 0;
        Assert.assertEquals(10, compoundIterator.aheadCount());
        while (compoundIterator.hasNext()) {
            Integer i = compoundIterator.next();
            Assert.assertEquals(number++, i.intValue());
        }
        Assert.assertEquals(0, compoundIterator.aheadCount());
    }

    @Test
    public void testMixedUsage() {
        CompoundIterator<Integer> compoundIterator = new CompoundIterator<Integer>(2, mList);
        ILazyIterator<Integer> firstIterator = compoundIterator.getIterator(0);
        ILazyIterator<Integer> secondIterator = compoundIterator.getIterator(1);

        Integer number = null;
        for (Integer i : mList) {
            switch (i % 3) {
                case 0:
                    firstIterator.move();
                    number = firstIterator.head();
                    break;
                case 1:
                    secondIterator.move();
                    number = secondIterator.head();
                    break;
                case 2:
                    number = compoundIterator.next();
                    break;

            }
            Assert.assertEquals(number, i);
        }
    }
}
