package uk.co.ricky.pdf.old;

/**
 * Insert the type's description here.
 * Creation date: (14/03/2002 10:19:20)
 * @author: McLaren Richard
 */
public class AsciiHexOutputStreamToString extends java.io.OutputStream {
	private StringBuffer sb;
/**
 * AsciiHexOutputStream constructor comment.
 */
public AsciiHexOutputStreamToString() {
	super();
	this.sb = new StringBuffer();
}
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 10:51:51)
 * @return java.lang.String
 */
public String toString() {
	return sb.toString();
}
/**
 * write method comment.
 */
public void write(int b) throws java.io.IOException {

	String s = Integer.toHexString(b & 0x0FF);
	if (s.length() == 1) {
		sb.append('0');
	}
	sb.append(s);

}
}
