package snedeker.goetz.ir.assignmentOne.utils;

import java.io.File;

public class Metrics {

	public static int getFolderSizeOnDisk(File directory) {
		int size = 0;
		for (File file : directory.listFiles()) {
			if (file.isFile()) {
				size += file.length();
			} else {
				size += getFolderSizeOnDisk(file);
			}
		}
		return size;
	}

}
