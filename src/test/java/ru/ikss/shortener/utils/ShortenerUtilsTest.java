package ru.ikss.shortener.utils;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Tests for shorten algorithm")
public class ShortenerUtilsTest {

    @Test
    @DisplayName("Test inverse")
    public void inverseTest() {
        long id = RandomUtils.nextLong(1, Long.MAX_VALUE);
        String shorten = ShortenerUtils.encode(id);
        assertEquals(id, ShortenerUtils.decode(shorten), "Test that encoding and decoding results same id");
    }
}
