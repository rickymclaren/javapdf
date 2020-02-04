package uk.co.ricky.pdf;

/**
 * Insert the type's description here.
 * Creation date: (15/03/2002 14:26:16)
 * @author: McLaren Richard
 */
class PdfOutlines extends PdfObject {
/**
 * PdfOutline constructor comment.
 */
public PdfOutlines(PdfDocument document) {
	super(document);
	type = OUTLINES;
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 15:52:51)
 * @return byte[]
 */
public void toOutputStream( java.io.OutputStream os ) {
	java.io.PrintWriter pw = new java.io.PrintWriter(os, true);

	pw.print( objectId + " 0 obj" + NL );
	pw.print("<<" + NL);
	pw.print(TYPE + type + NL);
	pw.print(COUNT + "0" + NL);	// TODO : Add outlines
	pw.print(">>" + NL);
	pw.print(ENDOBJ + NL);
	pw.close();

}
}
