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
