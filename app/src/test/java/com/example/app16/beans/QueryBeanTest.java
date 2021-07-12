package com.example.app16.beans;

import android.content.Context;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class QueryBeanTest {
    QueryBean bean;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        bean = new QueryBean(context);
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Tests the nonempty date format
     */
    @Test
    public void setDate1() {
        String invalidDate = "01-12-2020";
        boolean shouldBeFalse = bean.setDate1(invalidDate);
        assertFalse(shouldBeFalse);

        String validDate = "2020-12-01";
        boolean shouldBeTrue = bean.setDate1(validDate);
        assertTrue(shouldBeTrue);
    }

    /**
     * Tests the nonempty date format
     */
    @Test
    public void setDate2() {
        String invalidDate = "01-12-2020";
        boolean shouldBeFalse = bean.setDate1(invalidDate);
        assertFalse(shouldBeFalse);

        String validDate = "2020-12-01";
        boolean shouldBeTrue = bean.setDate1(validDate);
        assertTrue(shouldBeTrue);
    }

    /**
     * Tests just the date range
     */
    @Test
    public void validateRange() {
        bean.setDate1("2018-01-01");
        bean.setDate2("2020-01-02");
        boolean shouldBeFalse = bean.validateRange();
        assertFalse(shouldBeFalse);

        bean.setDate1("2018-01-01");
        bean.setDate2("2020-01-01");
        boolean shouldBeTrue = bean.validateRange();
        assertTrue(shouldBeTrue);
    }

    /**
     * Tests that the order of dates
     */
    @Test
    public void validateOrder() {
        bean.setDate1("2020-01-01");
        bean.setDate2("2018-01-01");
        boolean shouldBeFalse = bean.validateOrder();
        assertFalse(shouldBeFalse);

        bean.setDate1("2018-01-01");
        bean.setDate2("2020-01-01");
        boolean shouldBeTrue = bean.validateOrder();
        assertTrue(shouldBeTrue);
    }

    /**
     * Tests the share symbol's format
     */
    @Test
    public void setShareSymbol1() {
        String invalidSymbol = "doge/usd";
        boolean shouldBeFalse = bean.setShareSymbol1(invalidSymbol);
        assertFalse(shouldBeFalse);

        String validSymbol = "doge-usd";
        boolean shouldBeTrue = bean.setShareSymbol1(validSymbol);
        assertTrue(shouldBeTrue);
    }

    /**
     * Tests the share symbol's format
     */
    @Test
    public void setShareSymbol2() {
        String invalidSymbol = "doge/usd";
        boolean shouldBeFalse = bean.setShareSymbol2(invalidSymbol);
        assertFalse(shouldBeFalse);

        String validSymbol = "doge-usd";
        boolean shouldBeTrue = bean.setShareSymbol2(validSymbol);
        assertTrue(shouldBeTrue);
    }
}