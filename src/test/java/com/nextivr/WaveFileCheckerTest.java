package com.nextivr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

public class WaveFileCheckerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testSingleLeftUlaw() throws Exception{
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("whitenoise-mono32float.wav").getFile());
            String absolutePath = file.getAbsolutePath();

            WaveFileChecker.check(absolutePath);

            assertEquals("Not a PCM file", outContent.toString().trim());
    }
    @Test
    public void testWhiteNoiseMono32Float() {
        try {

            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("hi-left-pan-pcm.wav").getFile());
            String absolutePath = file.getAbsolutePath();

            WaveFileChecker.check(absolutePath);

        } catch (Exception ex) {
            assertEquals("No bytes in right channel.", ex.getLocalizedMessage());
            return;
        }

        fail("PCM with audio in both channels.");

    }

    @Test
    public void testPCMWithFullAudio() {
        try {

            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("pluck-pcm-left-right.wav").getFile());
            String absolutePath = file.getAbsolutePath();

            WaveFileChecker.check(absolutePath);

        } catch (Exception ex) {
            assertEquals("No bytes in right channel.", ex.getMessage());
            return;
        }

        assertEquals("", outContent.toString().trim());

    }
}