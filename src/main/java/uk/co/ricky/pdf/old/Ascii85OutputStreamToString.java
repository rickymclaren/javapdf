package uk.co.ricky.pdf.old;

/**
 * Insert the type's description here.
 * Creation date: (14/03/2002 10:19:20)
 * @author: McLaren Richard
 */
public class Ascii85OutputStreamToString extends java.io.OutputStream {
	private int[] input = new int[4];
	private int[] output = new int[5];
	private int numBytes = 0;
	private StringBuffer sb = new StringBuffer();
	boolean closed = false;
/**
 * AsciiHexOutputStream constructor comment.
 */
public Ascii85OutputStreamToString() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 14:36:36)
 */
public void close() {

	if ( ! closed) {
		if (numBytes > 0) {
			for (int i = numBytes; i < 4 ; i++){
				input[i] = 0;
			}
			long in = (input[0] << 24) | (input[1] << 16) | (input[2] << 8) | input[3];
			if (in < 0) {
				in += 0x100000000L;
			}
			for (int i = 4; i >=0 ; i--){
				output[i] = (int)(in % 85);
				in /= 85;
			}
			for (int i = 0; i < numBytes + 1; i++){
				int b85 = output[i] + '!';
				sb.append((char)b85);
			}
			numBytes = 0;
		}
		sb.append("~>");
	}
	closed = true;
	
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

	input[numBytes++] = b & 0x0FF;
	if (numBytes == input.length) {
		long in = (input[0] << 24) | (input[1] << 16) | (input[2] << 8) | input[3];
		if (in < 0) {
			in += 0x100000000L;
		}
		for (int i = 4; i >=0 ; i--){
			output[i] = (int)(in % 85);
			in /= 85;
		}
		if (output[0] == 0 && output[1] == 0 && output[2] == 0 && output[3] == 0 && output[4] == 0) {
			sb.append('z');
		} else {
			for (int i = 0; i < numBytes + 1; i++){
				int b85 = output[i] + '!';
				sb.append((char)b85);
			}
		}
		numBytes = 0;
	}

}
}
