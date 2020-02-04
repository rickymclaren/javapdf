package uk.co.ricky.pdf.old;

/** 
 * Insert the type's description here.
 * Creation date: (14/02/02 22:24:51)
 * @author: Administrator
 */
public class PdfObject {

	public static final int CATALOG = 1;
	public static final int OUTLINES = 2;
	public static final int RESOURCES = 3;
	public static final int FONT = 4;
	public static final int PAGES = 5;
	public static final int PAGE = 6;
	public static final int CONTENTS = 7;

	
	int type;
	StringBuffer dictionary = new StringBuffer();
	StringBuffer stream = new StringBuffer();
/**
 * PdfObject constructor comment.
 */
public PdfObject() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/02 22:44:48)
 * @param property java.lang.String
 */
public void addProperty(String property) {

	if (dictionary.length() > 0) {
		dictionary.append(Pdf.NL);
	}
	dictionary.append(property);
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/02 22:40:00)
 * @param data java.lang.String
 */
public void appendToStream(String data) {

	stream.append(data);
	
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/02 22:29:43)
 * @return java.lang.String
 */
public java.lang.String getDictionary() {
	return dictionary.toString();
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/02 22:29:43)
 * @return java.lang.String
 */
public java.lang.String getStream() {
	return stream.toString();
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/02 22:29:43)
 * @return int
 */
public int getType() {
	return type;
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/02 22:29:43)
 * @param newStream java.lang.String
 */
public void setStream(java.lang.String newStream) {
	stream = new StringBuffer(newStream);
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/02 22:29:43)
 * @param newType int
 */
public void setType(int newType) {
	type = newType;
}
}
