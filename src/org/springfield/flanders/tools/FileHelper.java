/* 
* FileHelper.java
* 
* Copyright (c) 2014 Noterik B.V.
* 
* This file is part of flanders, related to the Noterik Springfield project.
*
* flanders is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* flanders is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with flanders.  If not, see <http://www.gnu.org/licenses/>.
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