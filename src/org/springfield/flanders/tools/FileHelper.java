/*
 * Created on Feb 11, 2008
 */
package org.springfield.flanders.tools;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileHelper {

	public static boolean saveStringToFile(String fileToSave, String data) {
		Writer fw = null;
		try {
			fw = new FileWriter(fileToSave);
			fw.write(data);
			fw.append('\n');
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (fw != null)
				try {
					fw.close();
				} catch (IOException e) {
				}
		}
		return true;
	}
	
	public static String getFileExtension(String fn){
		if(fn == null || fn.indexOf(".") == -1){
			return null;
		}
		return fn.substring(fn.lastIndexOf(".") + 1);
	}

}