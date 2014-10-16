/* 
* ThreadedObject.java
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
package org.springfield.flanders.thread;

 public class ThreadedObject implements Runnable {
    
	 static ThreadLocal state = new ThreadLocal();
	    
	    public String getState() {
	        // Retrieve value.
	        String currState = state.get().toString();
	    return currState;
	    }    
	    
	    
	    public void setState(String newState){
	    // Set value.
	    	state.set(newState);
	    }
	 
	 
	 // This method is called when the thread runs
	 public void run() {
    
    }
 
}

    


