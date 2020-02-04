package uk.co.ricky.pdf;

import java.util.*;
/**
 * Insert the type's description here.
 * Creation date: (15/03/2002 14:19:15)
 * @author: McLaren Richard
 */
class PdfPages extends PdfObject {
	private Vector pages;
/**
 * PdfPages constructor comment.
 */
public PdfPages(PdfDocument document) {
	super(document);
	type = PAGES;
	pages = new Vector();
	addPage();
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 13:17:23)
 * @return uk.co.ricky.pdf.PdfPage
 */
public PdfPage addPage() {
	PdfPage page = new PdfPage(document);
	page.setParent( this );
	pages.addElement(page);
	return page;
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 15:01:00)
 * @return uk.co.ricky.pdf.PdfPage
 * @param page int
 */
public PdfPage getPage(int page) {
	return (PdfPage)(pages.elementAt(page - 1));
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 15:52:51)
 * @return byte[]
 */
public void toOutputStream( java.io.OutputStream os) {
	java.io.PrintWriter pw = new java.io.PrintWriter(os, true);

	pw.print( objectId + " 0 obj" + NL );
	pw.print("<<" + NL);
	pw.print(TYPE + type + NL);
	pw.print(MEDIABOX + "[ 0 0 595 842 ]" + NL);
	pw.print(COUNT + pages.size() + NL);
	pw.print(KIDS + "[ ");
	for (int i = 0; i < pages.size(); i++){
		PdfPage page = (PdfPage)pages.elementAt(i);
		pw.print(objectRef(page) + " ");
	}
	pw.print("]" + NL);
	pw.print(">>" + NL);
	pw.print(ENDOBJ + NL);
	pw.close();

}
}
