/* 
* FlandersResource.java
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

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.restlet.alternative.Context;
import org.restlet.alternative.data.MediaType;
import org.restlet.alternative.Request;
import org.restlet.alternative.Response;
import org.restlet.alternative.data.Status;
import org.restlet.alternative.representation.Representation;
import org.restlet.alternative.resource.Get;
import org.restlet.alternative.resource.Post;
import org.restlet.alternative.resource.ServerResource;
import org.restlet.alternative.representation.StringRepresentation;
import org.restlet.alternative.representation.Variant;
import org.springfield.flanders.CineExtractor;
import org.springfield.flanders.FfprobeMetaDataExtractor;
import org.springfield.flanders.IdtRawExtractor;
import org.springfield.flanders.MjpegIndexer;
import org.springfield.flanders.RtmpdumpMetadataExtractor;
import org.springfield.flanders.homer.LazyHomer;
import org.springfield.flanders.homer.MountProperties;
import org.springfield.flanders.tools.FileHelper;
import org.springfield.mojo.ftp.FtpHelper;


public class FlandersResource extends ServerResource {

	private static Logger log = Logger.getLogger(FlandersResource.class);

	/** Number of times to retry FTP */
	public static final int NUM_RETRIES = 10;
	
	/** Time (milliseconds) to wait between tries */
	public static final long TIME_TO_WAIT = 5000;
	
	// the decimal format is used to parse the interval value of the request xml
	private static DecimalFormat df = new DecimalFormat("#.####");

	public FlandersResource() {
		//constructor
	}
	
	public void doInit(Context context, Request request, Response response) {
		super.init(context, request, response);

		// add representational variants allowed
		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	// allowed actions: POST, PUT, GET, DELETE
	public boolean allowPut() {
		return true;
	}

	public boolean allowPost() {
		return true;
	}

	public boolean allowGet() {
		return true;
	}

	/**
	 * GET get resource of type modelClass
	 */
	@Get
	public void handleGet() {
		Representation result = null;
		result = new StringRepresentation("GET this...");

		getResponse().setEntity(result);
	}

	@Post
	public void handlePost(Representation representation){
		String xml = "";
		log.info("entering the POST!!!!!");
		try {
			if (representation == null) {
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				getResponse().setEntity("<status>Error: the request data could not be read</status>",
						MediaType.TEXT_XML);
			} else {
				xml = representation.getText();
			}
		} catch (IOException e2) {
			e2.printStackTrace();
			return;
		}
		System.out.println("REPRESENTATION: " + xml);
		if (representation != null && xml != null) {
			Document document = null;
			try {
				document = DocumentHelper.parseText(xml);
			} catch (DocumentException e1) {
				e1.printStackTrace();
			}
			Node node = document.selectSingleNode("//root");
			String mount = node.valueOf("mount");
			String source = node.valueOf("source");
			String stream = node.valueOf("stream");
			String filename = node.valueOf("file");
			String mjpegIndex = node.valueOf("index");
			
			if (mjpegIndex!=null && mjpegIndex.equals("true") && mount!=null && source!=null) {
				MountProperties mp = LazyHomer.getMountProperties(mount);
				if (mp != null) {		
					String response = MjpegIndexer.extractMetaData(source, mp.getPath());
					getResponse().setEntity(new StringRepresentation(response));
				} else {
					getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
					getResponse().setEntity("<status>Error: internal mount properties not found</status>", MediaType.TEXT_XML);
				}
			} else if(source!=null && mount!=null) {
				if (!mount.equals("")) {
					MountProperties mp = LazyHomer.getMountProperties(mount);
					if (mp.getProtocol().equals("file")) {
						source = mp.getPath() + source;	
					} else if (mp.getProtocol().equals("ftp")) {
						/** get the file from streaming machines to the the flanders server */	
						String fileName = source.substring(source.lastIndexOf("/")+1);
						String rawUri = source.substring(0,source.lastIndexOf("/"));
						String itemUri;
						if (rawUri.indexOf("/rawaudio/") > -1) {
						    itemUri = rawUri.substring(0, rawUri.lastIndexOf("/rawaudio/")+1);
						} else {
						    itemUri = rawUri.substring(0, rawUri.lastIndexOf("/rawvideo/")+1);
						}
					
						if(getFileByFtp(mp.getHostname(), mp.getAccount(), mp.getPassword(), fileName, mp.getPath(), rawUri, itemUri)){
							source = mp.getPath()+itemUri+File.separator+fileName;
						}
					}
				}
				String ext = FileHelper.getFileExtension(source);
				if(ext == null){ //assume folder with raw idt video
				    	log.info("FOUND RAW EXTENSION");
				    	String response = IdtRawExtractor.extractMetaData(source);
					getResponse().setEntity(new StringRepresentation(response));
				} else if(ext.toLowerCase().equals("cine")) { 
				    	log.info("FOUND CINE EXTENSION");	
				    	String response = CineExtractor.extractMetaData(source);
					getResponse().setEntity(new StringRepresentation(response));
				}else {
				    	String response = FfprobeMetaDataExtractor.extractMetaData(source);
					getResponse().setEntity(new StringRepresentation(response));
				}
			} else if(stream!=null && filename!=null) {
				String response = RtmpdumpMetadataExtractor.extractMetaData(stream,filename);
				getResponse().setEntity(new StringRepresentation(response));
			} else {
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				getResponse().setEntity("<status>Error: incorrect parameters</status>", MediaType.TEXT_XML);
			}
		}
	}
	
	/**
	 * This function copies the file from the streaming machine into the flanders server where the metadata
	 * is extracted.
	 * 
	 * @param server
	 * @param fileName
	 * @param mount
	 * @param rawUri
	 * @param itemUri
	 * @return
	 */
	 private boolean getFileByFtp(String server, String username, String password, String fileName, String mount, String rawUri, String itemUri){
		 String rFolder = rawUri;
		 String lFolder = mount + itemUri;
		 
		 // retry on failure
		 boolean success = false;
		 for(int i=0; i<NUM_RETRIES; i++) {
			 System.out.println("server "+server+" user "+username+" password "+password+" remote folder "+rFolder+" filename "+fileName);
			 success = FtpHelper.commonsGetFile(server, username, password, rFolder, lFolder, fileName);
			 if(success) {
				 break;
			 }
			 log.error("FTP failed ("+(i+1)+"), retrying ... ");
			 try {
				Thread.sleep(TIME_TO_WAIT);
			} catch (InterruptedException e) {}
		 }		 
		 // FTP
		 return success;
	 }
}