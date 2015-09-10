/* 
* FlandersRestlet.java
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
package org.springfield.flanders.restlet;

import org.restlet.Context;
import org.restlet.routing.Router;

public class FlandersRestlet extends Router {

	public FlandersRestlet(Context cx) {
		super(cx);
		this.setRoutingMode(Router.MODE_BEST_MATCH);
		this.attach("/extract",FlandersResource.class);
		
		// logging purposes
		this.attach("/logging",LoggingResource.class);
	}

}