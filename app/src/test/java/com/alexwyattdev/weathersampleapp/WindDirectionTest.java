package com.alexwyattdev.weathersampleapp;

import static com.alexwyattdev.weathersampleapp.util.ExtensionsKt.toWindDirection;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

// Unit test to check whether the wind direction conversion results in the desired output
public class WindDirectionTest {

    @Test
    public void testNorth() {
        assertEquals("N", toWindDirection(0));
    }

    @Test
    public void testNorthEast() {
        assertEquals("NE", toWindDirection(45));
    }

    @Test
    public void testEast() {
        assertEquals("E", toWindDirection(90));
    }

    @Test
    public void testSouthEast() {
        assertEquals("SE", toWindDirection(135));
    }

    @Test
    public void testSouth() {
        assertEquals("S", toWindDirection(180));
    }

    @Test
    public void testSouthWest() {
        assertEquals("SW", toWindDirection(225));
    }

    @Test
    public void testWest() {
        assertEquals("W", toWindDirection(270));
    }

    @Test
    public void testNorthWest() {
        assertEquals("NW", toWindDirection(315));
    }

}
