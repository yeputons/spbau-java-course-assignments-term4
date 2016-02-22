package ru.spbau.mit;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SecondPartTasksTest {
    @Test
    public void testFindQuotes() {
        fail();
    }

    @Test
    public void testPiDividedBy4() {
        assertEquals(Math.PI / 4, SecondPartTasks.piDividedBy4(), 1e-2);
    }

    @Test
    public void testFindPrinter() {
        Map<String, List<String>> compositions = new HashMap<>();
        compositions.put("1", Arrays.asList());
        compositions.put("2", Arrays.asList("foobar"));
        compositions.put("3", Arrays.asList("fo", "ba"));
        assertEquals("2", SecondPartTasks.findPrinter(compositions));

        compositions.put("3", Arrays.asList("fo", "ba", "baz"));
        assertEquals("3", SecondPartTasks.findPrinter(compositions));
    }

    @Test
    public void testCalculateGlobalOrder() {
        Map<String, Integer> order1 = new HashMap<>();
        order1.put("Apples", 1);
        order1.put("Oranges", 2);

        Map<String, Integer> order2 = new HashMap<>();
        order2.put("Apples", 4);
        order2.put("Bananas", 8);

        Map<String, Integer> order3 = new HashMap<>();
        order3.put("Oranges", 16);
        order3.put("Pineapples", 32);
        order3.put("", 64);

        Map<String, Integer> order4 = new HashMap<>();
        // empty order

        Map<String, Integer> order5 = new HashMap<>();
        order5.put("", 128);

        Map<String, Integer> order6 = new HashMap<>();
        order6.put("Something", 256);

        Map<String, Integer> expected = new HashMap<>();
        expected.put("Apples", 1 + 4);
        expected.put("Oranges", 2 + 16);
        expected.put("Bananas", 8);
        expected.put("Pineapples", 32);
        expected.put("", 64 + 128);
        expected.put("Something", 256);
        assertEquals(expected, SecondPartTasks.calculateGlobalOrder(Arrays.asList(order1, order2, order3, order4, order5, order6)));

        expected.put("Something", 512);
        // repeat one order twice
        assertEquals(expected, SecondPartTasks.calculateGlobalOrder(Arrays.asList(order1, order2, order3, order4, order5, order6, order6)));

        // empty order only
        assertEquals(new HashMap<>(), SecondPartTasks.calculateGlobalOrder(Arrays.asList(order4)));

        // no orders
        assertEquals(new HashMap<>(), SecondPartTasks.calculateGlobalOrder(Collections.emptyList()));
    }
}