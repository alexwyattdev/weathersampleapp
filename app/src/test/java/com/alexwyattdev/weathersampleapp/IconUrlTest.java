package com.alexwyattdev.weathersampleapp;

import static com.alexwyattdev.weathersampleapp.util.ExtensionsKt.toIconUrl;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

// Unit test to check whether the url retrieval for the images are correct
public class IconUrlTest {

    @Test
    public void testSunny() {
        assertEquals("https://openweathermap.org/img/wn/01d@2x.png", toIconUrl("01d"));
    }

    @Test
    public void testPartlyCloudy() {
        assertEquals("https://openweathermap.org/img/wn/02d@2x.png", toIconUrl("02d"));
    }

    @Test
    public void testCloudy() {
        assertEquals("https://openweathermap.org/img/wn/03d@2x.png", toIconUrl("03d"));
    }

    @Test
    public void testRainy() {
        assertEquals("https://openweathermap.org/img/wn/10d@2x.png", toIconUrl("10d"));
    }

    @Test
    public void testSnowy() {
        assertEquals("https://openweathermap.org/img/wn/13d@2x.png", toIconUrl("13d"));
    }

}
