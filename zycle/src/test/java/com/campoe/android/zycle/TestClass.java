package com.campoe.android.zycle;

import org.junit.Assert;
import org.junit.Test;

public class TestClass {

    @Test
    public void testInt() {
        int i = 23;
        int o = ~i;
        Assert.assertEquals(i, o);
    }

}
