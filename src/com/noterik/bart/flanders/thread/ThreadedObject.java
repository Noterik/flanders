package com.noterik.bart.flanders.thread;

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

    


