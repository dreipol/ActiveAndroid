package ch.dreipol.android.dreiworks.collections;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
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

    @Test
    public void test2IteratorsWithoutMoving() {
        CompoundIterator<Integer> compoundIterator = new CompoundIterator<Integer>(2, mList);

    }
}
