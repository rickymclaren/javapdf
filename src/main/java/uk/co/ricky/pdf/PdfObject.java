package uk.co.ricky.pdf;

import java.io.*;
import java.util.*;

/** 
 * Insert the type's description here.
 * Creation date: (14/02/02 22:24:51)
 * @author: Administrator
 */
abstract class PdfObject {

	// Standardise newlines
	protected static final String NL = "\r\n";
	
	// Object types
	protected static final String TYPE = "/Type ";
	protected static final String CATALOG = "/Catalog ";
	protected static final String OUTLINES = "/Outlines ";
	protected static final String PAGES = "/Pages ";
	protected static final String PAGE = "/Page ";
	protected static final String PAGECONTENT = "/PageContent ";
	protected static final String FONT = "/Font ";
	protected static final String XOBJECT = "/XObject ";

	protected static final String ENDOBJ = "endobj";

	// General attributes
	protected static final String MEDIABOX = "/MediaBox ";
	protected static final String COUNT = "/Count ";
	protected static final String KIDS = "/Kids ";
	protected static final String PROCSET = "/Procset ";
	protected static final String PARENT = "/Parent ";
	protected static final String RESOURCES = "/Resources ";
	protected static final String CONTENTS = "/Contents ";
	
	protected PdfDocument document;	
	protected String type;
	protected int objectId;
	
/**
 * PdfObject constructor comment.
 */
public PdfObject(PdfDocument document) {
	super();
	this.document = document;
	document.addObject(this);
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 11:58:09)
 * @return int
 */
public int getObjectId() {
	return objectId;
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 11:26:31)
 * @return java.lang.String
 */
public String getType() {
	return type;
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 15:19:21)
 * @return java.lang.String
 * @param o uk.co.ricky.pdf.PdfObject
 */
public static String objectRef(PdfObject o) {
	return o.getObjectId() + " 0 R";
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 09:50:56)
 * @param id int
 */
public void setObjectId(int id) {

	objectId = id;
		
}
/**
 * Insert the method's description here.
 * Creation date: (26/03/2002 15:29:15)
 * @param os java.io.OutputStream
 */
public abstract void toOutputStream(OutputStream os);
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 09:25:44)
 * @return byte[]
 */
public byte[] toPdf() {

	java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
	toOutputStream( baos );
	return baos.toByteArray();
	
}
}
