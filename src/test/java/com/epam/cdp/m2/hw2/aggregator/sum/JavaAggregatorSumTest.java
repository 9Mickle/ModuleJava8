package com.epam.cdp.m2.hw2.aggregator.sum;

import com.epam.cdp.m2.hw2.aggregator.Aggregator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public abstract class JavaAggregatorSumTest {

    static long begin;

    @BeforeClass
    public static void start() {
        begin = System.currentTimeMillis();
    }

    @Parameterized.Parameter(0)
    public List<Integer> numbers;

    @Parameterized.Parameter(1)
    public long expected;

    private Aggregator aggregator;

    public JavaAggregatorSumTest(Aggregator aggregator) {
        this.aggregator = aggregator;
    }

    @Parameterized.Parameters
    public static List<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i <= 10_000_000L; i++) {
            numbers.add(i);
        }
        data.add(new Object[]{numbers, 50000005000000L});
        data.add(new Object[]{asList(1, 2, 3, 4, 5, 6, 7, 8), 36});
        data.add(new Object[]{asList(10, -10, 3), 3});
        data.add(new Object[]{emptyList(), 0});
        return data;
    }

    @Test
    public void test() {
        long actual = aggregator.sum(numbers);
        assertEquals(expected, actual);
    }

    @AfterClass
    public static void end() {
        System.out.println("Итоговое время: " + (System.currentTimeMillis() - begin));
    }
}