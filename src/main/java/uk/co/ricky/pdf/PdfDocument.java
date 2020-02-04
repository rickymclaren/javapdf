package uk.co.ricky.pdf;

import java.util.*;
import java.io.*;
import java.text.*;
/**
 * Insert the type's description here.
 * Creation date: (15/03/2002 14:24:12)
 * @author: McLaren Richard
 */
public class PdfDocument {
	private static final String NL = "\r\n";
	private PdfCatalog catalog;
	private PdfPage currentPage;
	private PdfResources resources;
	private Vector pdfObjects = new Vector();
/**
 * PdfDocument constructor comment.
 */
public PdfDocument() {
	super();
	catalog = new PdfCatalog( this );
	currentPage = catalog.getPages().getPage(1);
	resources = new PdfResources( this );
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 09:49:27)
 * @param newObject uk.co.ricky.pdf.PdfObject
 */
public void addObject(PdfObject newObject) {

	pdfObjects.addElement(newObject);
	newObject.setObjectId( pdfObjects.size() );
	
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 15:17:30)
 * @return uk.co.ricky.pdf.PdfPage
 */
public PdfPage addPage() {
	PdfFont font = currentPage.getFont();
	double size = currentPage.getFontSize();
	
	PdfPage page =  catalog.getPages().addPage();
	currentPage = page;

	page.setFont(font);
	page.setFontSize(size);
	
	return page;
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 17:00:46)
 * @return uk.co.ricky.pdf.PdfPage
 */
public PdfPage getCurrentPage() {
	return currentPage;
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 13:27:04)
 * @return uk.co.ricky.pdf.PdfResources
 */
public PdfResources getResources() {
	return resources;
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 14:35:31)
 * @return byte[]
 */
public byte[] toPdf() throws IOException {
	java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
	PrintWriter pw = new PrintWriter(baos, true);

	pw.print("%PDF-1.2");
	pw.print(NL);
	pw.print("%\u00e2\u00e3\u00cf\u00d3");
	pw.print(NL);

	pw.flush();

	int[] xref = new int[pdfObjects.size()];
	
	for (int i = 0; i < pdfObjects.size(); i++){
		xref[i] = baos.size();
		PdfObject o = (PdfObject)pdfObjects.elementAt(i);
		o.toOutputStream(baos);
	}

	
	int startxref = baos.size();
	
	pw.print("xref" + NL);
	pw.print("0 " + (pdfObjects.size() + 1) + " " + NL);
	pw.print("0000000000 65535 f" + NL);
	NumberFormat nf = NumberFormat.getInstance();
	nf.setGroupingUsed(false);
	nf.setMaximumIntegerDigits(10);
	nf.setMinimumIntegerDigits(10);
	for (int i = 0; i < xref.length; i++){
		pw.print( nf.format(xref[i]) + " 00000 n" + NL);
		
	}
	pw.print("trailer" + NL);
	pw.print("<<" + NL);
	pw.print("/Size " + xref.length + " " + NL);
	pw.print("/Root " + catalog.getObjectId() + " 0 R " + NL);
	pw.print(">> " + NL);
	pw.print("startxref" + NL);
	pw.print(startxref + NL);
	pw.print("%%EOF" + NL);
	
	pw.close();
	
	return baos.toByteArray();
}
}
