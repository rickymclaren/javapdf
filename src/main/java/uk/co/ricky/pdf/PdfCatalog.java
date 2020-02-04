package uk.co.ricky.pdf;

/**
 * Insert the type's description here.
 * Creation date: (15/03/2002 11:27:38)
 * @author: McLaren Richard
 */
class PdfCatalog extends PdfObject {
	private PdfOutlines outlines;
	private PdfPages pages;
/**
 * PdfCatalog constructor comment.
 */
public PdfCatalog(PdfDocument document) {
	super(document);
	type = CATALOG;
	outlines = new PdfOutlines(document);
	pages = new PdfPages(document);
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 14:59:34)
 * @return uk.co.ricky.pdf.PdfOutlines
 */
public PdfOutlines getOutlines() {
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 14:59:02)
 * @return uk.co.ricky.pdf.PdfPages
 */
public PdfPages getPages() {
	return pages;
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 15:06:46)
 * @return byte[]
 */
public void  toOutputStream( java.io.OutputStream os ) {
	java.io.PrintWriter pw = new java.io.PrintWriter(os, true);

	pw.print( objectId + " 0 obj" + NL );
	pw.print("<<" + NL);
	pw.print(TYPE + type + NL);
	pw.print(OUTLINES + objectRef(outlines) + NL);
	pw.print(PAGES + objectRef(pages) + NL);
	pw.print(">>" + NL);
	pw.print(ENDOBJ + NL);
	pw.close();

}
}
