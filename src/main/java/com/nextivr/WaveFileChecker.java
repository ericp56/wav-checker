package com.nextivr;

import java.io.EOFException;
import java.io.FileInputStream;

import com.xsun.media.sound.RIFFReader;

public class WaveFileChecker {

	public static void check(String filePathAndName) throws Exception {
		FileInputStream input = new FileInputStream(filePathAndName);
		try (RIFFReader riffiterator = new RIFFReader(input)) {

			if (!riffiterator.getType().toUpperCase().equals("WAVE")) {
				System.out.println("Not a WAVE file");
				return;
			}
			if (riffiterator.hasNextChunk()) {
				RIFFReader chunk = riffiterator.nextChunk();
				int pcmByte = chunk.read();
				if (pcmByte < 0)
					throw new EOFException();
				int junk = chunk.read();
				if (junk < 0)
					throw new EOFException();
				int channelByte = chunk.read();
				if (channelByte < 0)
					throw new EOFException();
				if (pcmByte != 1) {
					System.out.println("Not a PCM file");
					return;
				}
				if (channelByte != 2) {
					System.out.println("Not a Stereo file");
					return;
				}

			}
			while (riffiterator.hasNextChunk()) {
				RIFFReader chunk = riffiterator.nextChunk();
				if (chunk.getFormat().equals("data")) {
					long size = chunk.getSize() / 4;
					for (int i = 0; i < size; i++) {
						int ch1 = chunk.read();
						int ch2 = chunk.read();
						int ch3 = chunk.read();
						int ch4 = chunk.read();
						if (ch1 < 0)
							throw new EOFException();
						if (ch2 < 0)
							throw new EOFException();
						if (ch3 < 0)
							throw new EOFException();
						if (ch4 < 0)
							throw new EOFException();
						// System.out.println(String.format("%s %s %s %s %s", i,
						// ch1, ch2, ch3, ch4));
						if (ch3 > 0 || ch4 > 0) {
							return;
						}
					}
				}
			}
			throw new Exception("No bytes in right channel.");
		} catch (Exception ex) {
			throw ex;
		}

	}

	public static void main(String[] args) throws Exception {
		try {
			if (args.length == 0) {
				throw new Exception("Filename required");
			}
			check(args[0]);
		} catch (Exception ex) {
			System.err.println(ex.getLocalizedMessage());
			ex.printStackTrace();
		}

	}

}
