package uk.co.ricky.pdf.old;

/**
 * Insert the type's description here.
 * Creation date: (14/03/2002 10:19:20)
 * @author: McLaren Richard
 */
public class AsciiHexOutputStream extends java.io.OutputStream {
	private java.io.OutputStream os;
/**
 * AsciiHexOutputStream constructor comment.
 */
public AsciiHexOutputStream(java.io.OutputStream os) {
	super();
	this.os = os;
}
/**
 * write method comment.
 */
public void write(int b) throws java.io.IOException {

	os.write(((b >> 4) & 0x0F) + '0');
	os.write((b & 0x0F) + '0');
		
}
}
