package ch.dreipol.android.dreiworks.collections;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import ch.dreipol.android.dreiworks.jsonstore.AESEncryption;
import ch.dreipol.android.dreiworks.jsonstore.JsonStore;
import ch.dreipol.android.dreiworks.jsonstore.streamprovider.CacheStreamProvider;
import ch.dreipol.android.dreiworks.serialization.gson.AndroidFieldNamingStrategy;
import ch.dreipol.android.dreiworks.serialization.gson.GsonExclusionStrategy;

/**
 * Created by melbic on 26/08/14.
 */
public class JsonStoreTest {

    private JsonStore mJsonStore;

    @Before
    public void setUp() {
        mJsonStore = new JsonStore(getGSONBuilder().create(), new CacheStreamProvider());
    }

    @Test
    public void testWriting() throws IOException {
        TestPojo one = new TestPojo(1, "one");
        mJsonStore.put("test", one);
    }

    @Test
    public void testReading() throws IOException {
        TestPojo pojo = new TestPojo(1, "one");
        mJsonStore.put("test", pojo);
        TestPojo readPojo = mJsonStore.get("test", TestPojo.class);
        Assert.assertEquals(pojo, readPojo);
    }

    @Test
    public void testList() throws IOException {
        ArrayList<TestPojo> list = new ArrayList<TestPojo>();
        for (int i = 0; i < 5; i++) {
            list.add(new TestPojo(i, String.valueOf(i)));
        }
        String key = "list";
        mJsonStore.put(key, list);
        ArrayList<TestPojo> readList = mJsonStore.get(key, new TypeToken<ArrayList<TestPojo>>() {
        });
        Assert.assertEquals(list, readList);
    }


    @Test
    public void testAESEncrypted() throws IOException {
        TestPojo p = new TestPojo(1, "eins");
        mJsonStore = new JsonStore(getGSONBuilder().create(), new CacheStreamProvider(), new AESEncryption());

        String key = "test123456789erwhijldfshjkdfsjkhldfwhjkkhjdfshjkdfs";
        mJsonStore.put(key, p);
        Assert.assertEquals(p, mJsonStore.get(key, TestPojo.class));
    }

    class TestPojo {
        private int mInt;
        private String mString;

        public TestPojo(int i, String s) {
            mInt = i;
            mString = s;
        }

        @Override
        public String toString() {
            return "TestPojo{" +
                    "mInt=" + mInt +
                    ", mString='" + mString + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestPojo testPojo = (TestPojo) o;

            if (mInt != testPojo.mInt) return false;
            if (mString != null ? !mString.equals(testPojo.mString) : testPojo.mString != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = mInt;
            result = 31 * result + (mString != null ? mString.hashCode() : 0);
            return result;
        }
    }

    public static GsonBuilder getGSONBuilder() {
        return new GsonBuilder()
                .setFieldNamingStrategy(new AndroidFieldNamingStrategy(FieldNamingPolicy.UPPER_CAMEL_CASE))
                .setExclusionStrategies(new GsonExclusionStrategy());
    }
}
