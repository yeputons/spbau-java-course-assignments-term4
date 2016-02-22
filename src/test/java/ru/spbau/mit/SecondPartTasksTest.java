package ru.spbau.mit;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        fail();
    }
}