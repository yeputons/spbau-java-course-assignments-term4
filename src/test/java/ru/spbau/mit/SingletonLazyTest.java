package net.yeputons.spbau.java;

import net.yeputons.spbau.java.utils.TestSupplier;
import org.junit.Test;

import java.util.function.Supplier;

import static org.junit.Assert.*;

public class SingletonLazyTest extends AbstractMultithreadedLazyTest {
    private void test(Supplier<TestSupplier> supplierFactory, Integer expected) throws InterruptedException {
        final TestSupplier[] suppliers = new TestSupplier[LAZIES_COUNT];
        for (int i = 0; i < suppliers.length; i++) {
            suppliers[i] = supplierFactory.get();
        }
        Integer[][] results = performTest(LazyFactory::createSingletonLazy, suppliers);

        for (Integer[] threadResults : results) {
            for (Integer result : threadResults) {
                assertEquals(expected, result);
            }
        }
        for (TestSupplier s : suppliers)
            assertEquals(1, s.getCallsCount());
    }

    @Test
    public void testComputation() throws InterruptedException {
        test(TestSupplier::new, 1);
    }

    @Test
    public void testNullFirst() throws InterruptedException {
        test(() -> new TestSupplier(1), null);
    }
}
