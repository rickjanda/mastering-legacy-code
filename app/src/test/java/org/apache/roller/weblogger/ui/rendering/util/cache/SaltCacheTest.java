package org.apache.roller.weblogger.ui.rendering.util.cache;

import org.apache.roller.weblogger.util.cache.ExpiringCacheEntry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class SaltCacheTest {

    @Test
    public void get_unknownSalt() {
        final SaltCache saltCache = SaltCache.getInstance();

        final Object retVal = saltCache.get("unknownSalt");

        assertNull(retVal);
    }

    @Test
    public void get_knownSalt() {
        final SaltCache saltCache = spy(new SaltCache());
        doReturn(new ExpiringCacheEntry(true, 10000)).when(saltCache).getCacheEntry("knownSalt");

        final Object cachedValue = saltCache.get("knownSalt");

        assertEquals(true, cachedValue);
    }

    @Test
    public void get_knownSaltExpired() {
        final SaltCache saltCache = spy(new SaltCache());
        doReturn(new ExpiringCacheEntry(true, 1)).when(saltCache).getCacheEntry("knownSaltExpired");

        final Object cachedValue = saltCache.get("knownSaltExpired");

        assertNull(cachedValue);
    }
}
