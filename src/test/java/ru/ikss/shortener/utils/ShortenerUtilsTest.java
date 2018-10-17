package ru.ikss.shortener.utils;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tests for shorten algorithm")
public class ShortenerUtilsTest {

    @Test
    @DisplayName("Test inverse")
    public void inverseTest() {
        long id = RandomUtils.nextLong(1, Long.MAX_VALUE);
        String shorten = ShortenerUtils.encode(id);
        assertEquals(id, ShortenerUtils.decode(shorten), "Test that encoding and decoding results same id");
    }

    @Test
    @DisplayName("Test zero index")
    public void zeroIndexTest() {
        long id = 0;
        String shorten = ShortenerUtils.encode(id);
        assertTrue(shorten.length() > 0, "Test that zero id has short representation");
    }

    @Test
    @DisplayName("Test zero index equality")
    public void zeroIndexEqualityTest() {
        long id = 0;
        String shorten = ShortenerUtils.encode(id);
        assertEquals(id, ShortenerUtils.decode(shorten), "Test that encoding and decoding results same id");
    }
}
