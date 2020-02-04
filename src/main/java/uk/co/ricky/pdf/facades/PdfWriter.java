package uk.co.ricky.pdf.facades;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import uk.co.ricky.pdf.PdfDocument;
import uk.co.ricky.pdf.PdfException;
import uk.co.ricky.pdf.PdfFont;
import uk.co.ricky.pdf.PdfPage;
import uk.co.ricky.pdf.PdfResources;
/**
 * Insert the type's description here.
 * Creation date: (20/03/2002 15:11:01)
 * @author: McLaren Richard
 */
public class PdfWriter {
	private PdfDocument document;
	private OutputStream os;
/**
 * PdfWriter constructor comment.
 */
public PdfWriter(OutputStream os) throws PdfException {
	super();
	this.os = os;
	document =  new PdfDocument();
	document.getResources().addFont("F1", PdfFont.HELVETICA);
	document.getCurrentPage().setFont(document.getResources().getFont("F1"));
	document.getCurrentPage().setFontSize(10);

}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 15:32:12)
 */
public void close() throws IOException {

	byte[] pdfData = document.toPdf();
//	System.out.println( new String(pdfData) );
	os.write(pdfData);
	os.close();
	
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 15:21:27)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {

	FileOutputStream fos = null;
	int line = 0;
	try {
		fos = new FileOutputStream(args[0]);
		PdfWriter pw = new PdfWriter(fos);

		while (line++ < 200){
			pw.println("Line " + line);
		}

		pw.close();
		
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println( "On line " + line);
	}
	
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 15:13:07)
 * @param text java.lang.String
 */
public void print(String text) {
	PdfPage page = document.getCurrentPage();
	page.print(text)	;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 15:13:07)
 * @param text java.lang.String
 */
public void println(String text) {
	PdfPage page = document.getCurrentPage();
	page.println(text)	;
	if (page.getCurrentY() > (PdfPage.PAGE_HEIGHT - page.getTopMargin() - page.getBottomMargin())) {
		document.addPage();
	}
}
/**
 * Insert the method's description here.
 * Creation date: (03/04/2002 10:33:18)
 * @param paragraph java.lang.String
 */
public void printParagraph(String paragraph) {

	PdfPage page = document.getCurrentPage();
	page.printParagraph(paragraph);
	
}
}
