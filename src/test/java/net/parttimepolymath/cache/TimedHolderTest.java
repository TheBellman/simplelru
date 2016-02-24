package net.parttimepolymath.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class TimedHolderTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testSuccess() {
        TimedHolder<String> instance = new TimedHolder<>(10000, "the content");
        String content = instance.getContent();
        assertNotNull(content);
        assertEquals("the content", content);

        instance.setContent("a llama");
        content = instance.getContent();
        assertNotNull(content);
        assertEquals("a llama", content);
    }

    @Test
    public void testFail() throws InterruptedException {
        TimedHolder<String> instance = new TimedHolder<>(10, "the content");
        Thread.sleep(50);
        String content = instance.getContent();
        assertNull(content);
    }
}
