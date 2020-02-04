package uk.co.ricky.pdf;

import java.io.*;
import java.util.*;
/**
 * Insert the type's description here.
 * Creation date: (15/03/2002 14:26:16)
 * @author: McLaren Richard
 */
class PdfImage extends PdfObject {
	private String name;
	private String subType;
	private int width;
	private int height;
	private int bitsPerComponent;
	private String ColorSpace;
	private String filter;
	private String predictor;

	private byte[] imageData;
	
/**
 * PdfOutline constructor comment.
 */
public PdfImage(PdfDocument document) {
	super(document);
	type = XOBJECT;
	subType = "/Image";
	bitsPerComponent = 8;
	ColorSpace = "/DeviceRGB";
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 13:47:23)
 * @param imageStream byte
 */
public void compress(byte[] imageData)  throws IOException { 
	
	filter = "[/FlateDecode]";
	predictor = "1";
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	java.util.zip.DeflaterOutputStream dos = new java.util.zip.DeflaterOutputStream(baos);
	dos.write(imageData, 0, imageData.length);
	dos.flush();
	dos.close();
	this.imageData = baos.toByteArray();
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 17:09:59)
 * @return double
 */
public double getHeight() {
	return height;
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 14:06:35)
 * @return java.lang.String
 */
public String getName() {
	return name;
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 17:09:32)
 * @return double
 */
public double getWidth() {
	return width;
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 13:38:11)
 * @param fileName java.lang.String
 */
public void loadImage(String fileName) {

	try {
		System.out.println( "Loading image " + fileName );

		// Load the image and wait for it to load fully
		java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage(fileName);
		java.awt.MediaTracker tracker = new java.awt.MediaTracker (new java.awt.Canvas());
		tracker.addImage (img, 0);
		try {
		      tracker.waitForID (0); 
		} catch (InterruptedException e) {
		}
		height = img.getHeight(null);
		width = img.getWidth(null);
		System.out.println( "Height=" + height );
		System.out.println( "Width=" + width );

		// Grab the pixels from the image
		java.awt.image.PixelGrabber pg = new java.awt.image.PixelGrabber(img, 0, 0, width, height, true);
		try {
			pg.grabPixels();
		} catch (InterruptedException ie) {
			System.out.println( "grabPixels interrupted" );
		}
		int[] pixels = (int[])pg.getPixels();

		// 	Translate to RGB
		byte[] imageStream = new byte[pixels.length * 3];
		for (int i = 0; i < pixels.length; i++){
			imageStream[i *3] = (byte)(pixels[i] >>> 16);
			imageStream[i *3 + 1] = (byte)(pixels[i] >>> 8);
			imageStream[i *3 + 2] = (byte)(pixels[i]);
		}

		// Compress the RGB data
		compress( imageStream );

	} catch (Exception e) {
		e.printStackTrace();
	}
	
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 14:10:26)
 * @param name java.lang.String
 */
public void setName(String name) {
	this.name = name;
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 15:52:51)
 * @return byte[]
 */
public void toOutputStream(OutputStream os) {
	java.io.PrintWriter pw = new java.io.PrintWriter(os, true);

	pw.print( objectId + " 0 obj" + NL );
	pw.print("<<" + NL);
	pw.print("/Type " + type + NL);
	pw.print("/Subtype " + subType + NL);
	pw.print("/Name " + name + NL);
	pw.print("/Width " + width + NL);
	pw.print("/Height " + height + NL);
	pw.print("/BitsPerComponent " + bitsPerComponent + NL);
	pw.print("/ColorSpace " + ColorSpace + NL);
	pw.print("/Filter " + filter + NL);
	pw.print("/Predictor " + predictor + NL);
	pw.print("/Length " + imageData.length + NL);
	pw.print(">>" + NL);
	pw.print("stream" + NL);
	pw.flush();

	try {
		os.write(imageData);
	} catch (Exception e) {
		e.printStackTrace();
	}

	pw.print("endstream" + NL);
	pw.print(ENDOBJ + NL);
	pw.close();

}
}
