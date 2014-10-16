/* 
* FlandersApplication.java
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

import javax.servlet.ServletContext;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Restlet;
import org.springfield.flanders.GlobalConfig;
import org.springfield.flanders.homer.LazyHomer;

import com.noelios.restlet.ext.servlet.ServletContextAdapter;

public class FlandersApplication extends Application {
	private static LazyHomer lh = null; 
	public FlandersApplication() {
		super();
	}

	public FlandersApplication(Context parentContext) {
		super(parentContext);
	}

	public void start() {
		try {
			super.start();
		} catch (Exception e) {
			System.out.println("Error starting application");
			e.printStackTrace();
		}

	}

	@Override
	public Restlet createRoot() {
		ServletContextAdapter adapter = (ServletContextAdapter) getContext();
		ServletContext servletContext = adapter.getServletContext();
		
		lh = new LazyHomer();
		lh.init(servletContext.getRealPath("/"));
		GlobalConfig.instance().initialize(servletContext.getRealPath("/"));
		// disable logging
		Component component = (Component)servletContext.getAttribute("com.noelios.restlet.ext.servlet.ServerServlet.component");
		component.getLogService().setEnabled(false);
				
		return new FlandersRestlet(super.getContext());
	}
}
