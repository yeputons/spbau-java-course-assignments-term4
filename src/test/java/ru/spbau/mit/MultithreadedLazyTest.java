package net.yeputons.spbau.java;

import net.yeputons.spbau.java.utils.AbstractLazyFactory;
import net.yeputons.spbau.java.utils.TestSupplier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class MultithreadedLazyTest extends AbstractMultithreadedLazyTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {(AbstractLazyFactory) LazyFactory::createSingletonLazy, false},
                {(AbstractLazyFactory) LazyFactory::createLockFreeLazy, true}
        });
    }

    private final AbstractLazyFactory factory;
    private final boolean shouldHaveConflicts;

    public MultithreadedLazyTest(AbstractLazyFactory factory, boolean shouldHaveConflicts) {
        this.factory = factory;
        this.shouldHaveConflicts = shouldHaveConflicts;
    }

    private void test(Supplier<TestSupplier> supplierFactory, boolean shouldHaveNull) throws InterruptedException {
        final TestSupplier[] suppliers = new TestSupplier[LAZIES_COUNT];
        for (int i = 0; i < suppliers.length; i++) {
            suppliers[i] = supplierFactory.get();
        }
        Integer[][] results = performTest(factory, suppliers);

        boolean haveNull = false;
        boolean haveNonNull = false;
        for (int lazy = 0; lazy < LAZIES_COUNT; lazy++) {
            for (int th = 0; th < THREADS_COUNT; th++) {
                assertEquals(results[0][lazy], results[th][lazy]);
            }
            if (results[0][lazy] == null) {
                haveNull = true;
            } else {
                haveNonNull = true;
            }
        }
        assertEquals(shouldHaveNull, haveNull);

        boolean haveConflicts = false;
        for (TestSupplier s : suppliers) {
            assertTrue(s.getCallsCount() >= 1);
            haveConflicts = haveConflicts || s.getCallsCount() > 1;
            assertTrue(s.getCallsCount() < THREADS_COUNT); // check for caching
        }
        assertEquals(shouldHaveConflicts, haveConflicts);
    }

    @Test
    public void testComputation() throws InterruptedException {
        test(TestSupplier::new, false);
    }

    @Test
    public void testNullFirstTen() throws InterruptedException {
        test(() -> new TestSupplier(10), true);
    }

    @Test
    public void testNullFirst() throws InterruptedException {
        test(() -> new TestSupplier(1), true);
    }
}
