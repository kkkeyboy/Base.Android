package com.jone.base.impl.http;

import org.junit.Test;

import java.lang.reflect.ParameterizedType;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

      new Hehe<ABA>(){}.getTypeD();

    }

  static abstract class Hehe<T> {
        public Class getTypeD() {
            return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
    }

    static class ABA{}
}