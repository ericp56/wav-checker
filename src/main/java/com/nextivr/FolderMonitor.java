package com.nextivr;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Date;

public class FolderMonitor {

	public static void main(String[] args) throws Exception {
		if (args.length == 0)
			throw new Exception("missing parameter - path to monitor");
		monitorFolder("file://" + args[0]);

	}

	public static void monitorFolder(String path) throws Exception {
		WatchService watcher = FileSystems.getDefault().newWatchService();

		for (;;) {

			WatchKey key;
			Path dir = Paths.get(new URI(path));
			try {
				key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
			} catch (IOException x) {
				System.err.println(x);
				break;
			}

			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();

				if (kind == OVERFLOW) {
					continue;
				}

				@SuppressWarnings("unchecked")
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path filename = ev.context();

				if (kind == ENTRY_MODIFY) {
					String filePathAndName = dir.toString() + "\\" + filename;

					try {
						WaveFileChecker.check(filePathAndName);
					} catch (Exception e) {
						System.out.print((new Date()).toString() + ":" + filename+":");
						System.out.println(e.getMessage());
					}
				}

			}

			boolean valid = key.reset();
			if (!valid) {
				break;
			}
		}
	}

}
