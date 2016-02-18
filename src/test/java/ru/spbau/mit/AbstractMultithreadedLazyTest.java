package net.yeputons.spbau.java;

import net.yeputons.spbau.java.utils.AbstractLazyFactory;
import net.yeputons.spbau.java.utils.TestSupplier;
import net.yeputons.spbau.java.utils.TestThread;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

public abstract class AbstractMultithreadedLazyTest {
    protected final int THREADS_COUNT = 2000;
    protected final int LAZIES_COUNT = 200;

    protected Integer[][] performTest(AbstractLazyFactory factory, TestSupplier[] suppliers) throws InterruptedException {
        final TestThread[] ths = new TestThread[THREADS_COUNT];

        final ArrayList<Lazy<Integer>> lazies = new ArrayList<>();
        final Integer[][] results = new Integer[ths.length][LAZIES_COUNT];

        for (TestSupplier supplier : suppliers) {
            lazies.add(factory.createLazy(supplier));
        }

        for (int i = 0; i < ths.length; i++) {
            final int id = i;
            ths[i] = new TestThread() {
                @Override
                public void run() throws Exception {
                    ArrayList<Integer> order = new ArrayList<Integer>();
                    for (int i = 0; i < LAZIES_COUNT; i++) {
                        order.add(i);
                    }
                    Collections.shuffle(order);
                    for (Integer lazy : order) {
                        results[id][lazy] = lazies.get(lazy).get();
                    }
                }
            };
        }

        for (TestSupplier s : suppliers)
            assertEquals(0, s.getCallsCount());
        for (TestThread th : ths)
            th.start();
        for (TestThread th : ths)
            th.join();
        return results;
    }
}
