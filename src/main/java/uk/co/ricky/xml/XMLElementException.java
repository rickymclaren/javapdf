package uk.co.ricky.xml;

/**
 * Insert the type's description here.
 * Creation date: (03/07/2002 16:33:43)
 * @author: McLaren Richard
 */
public class XMLElementException extends Exception {
/**
 * XMLUtilsException constructor comment.
 */
public XMLElementException() {
	super();
}
/**
 * XMLUtilsException constructor comment.
 * @param s java.lang.String
 */
public XMLElementException(String s) {
	super("XMLElementException:" + s);
}
}
