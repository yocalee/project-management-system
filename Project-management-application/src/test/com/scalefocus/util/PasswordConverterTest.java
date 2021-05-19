package com.scalefocus.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordConverterTest {

    @Test
    public void encodePasswordShouldReturnEncodedString() {
        assertEquals("[49, 50, 51]", PasswordConverter.encodePassword("123"));
    }

    @Test
    public void matchPasswordsShouldReturnBoolean() {
        assertFalse(PasswordConverter.matches("456","[49, 50, 51]" ));
        assertTrue(PasswordConverter.matches("456","[52, 53, 54]" ));
    }

    @Test(expected = NullPointerException.class)
    public void encodePasswordShouldThrowExceptionWhenNull() {
        PasswordConverter.encodePassword(null);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void matchPasswordShouldThrowExceptionWhenNull() {
        PasswordConverter.matches("", null);
    }
}