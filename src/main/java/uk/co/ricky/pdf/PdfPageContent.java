package uk.co.ricky.pdf;

/**
 * Insert the type's description here.
 * Creation date: (15/03/2002 14:23:27)
 * @author: McLaren Richard
 */
class PdfPageContent extends PdfObject {
	StringBuffer text = new StringBuffer();
	StringBuffer lines = new StringBuffer();
	StringBuffer graphics = new StringBuffer();
/**
 * PdfPage constructor comment.
 */
public PdfPageContent(PdfDocument document) {
	super(document);
	type = PAGECONTENT;
	text.append("/F1 10 Tf" + NL + "1 0 0 1 72 -29 Tm" + NL + "10 TL" + NL );
	graphics.append("0.5 w" + NL);
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 15:52:51)
 * @return byte[]
 */
public void toOutputStream( java.io.OutputStream os ) {
	java.io.PrintWriter pw = new java.io.PrintWriter(os, true);

	String stream = "BT" + NL + text + NL + "ET" + NL + lines + "S" +  NL + graphics;
	pw.print( objectId + " 0 obj" + NL );
	pw.print("<<" + NL);
	pw.print("/Length " + stream.length() + NL);
	pw.print(">>" + NL);
	pw.print("stream" + NL);
	pw.print( stream );
	pw.print("endstream" + NL);
	pw.print(ENDOBJ + NL);
	pw.close();

}
}
