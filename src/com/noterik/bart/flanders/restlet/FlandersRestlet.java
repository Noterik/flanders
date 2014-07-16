package com.noterik.bart.flanders.restlet;

import org.restlet.Context;
import org.restlet.Router;

public class FlandersRestlet extends Router {

	public FlandersRestlet(Context cx) {
		super(cx);
		this.setRoutingMode(Router.BEST);
		this.attach("/extract",FlandersResource.class);
	}

}