package uk.co.ricky.pdf;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
/**
 * Insert the type's description here.
 * Creation date: (15/03/2002 14:26:16)
 * @author: McLaren Richard
 */
public class PdfResources extends PdfObject {
	private Hashtable fonts = new Hashtable();
	private Hashtable images = new Hashtable();
/**
 * PdfOutline constructor comment.
 */
public PdfResources(PdfDocument document) {
	super(document);
	type = OUTLINES;
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 12:34:04)
 * @param name java.lang.String
 * @param baseFont int
 */
public void addFont(String name, int baseFont) {

	PdfFont font = new PdfFont(document);
	font.setName("/" + name);
	font.setBaseFont(baseFont);
	fonts.put(name, font);
	
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 14:09:00)
 * @param name java.lang.String
 * @param fileName java.lang.String
 */
public void addImage(String name, String fileName) {

	PdfImage image = new PdfImage(document);
	image.loadImage(fileName);
	image.setName( "/" + name );
	images.put(name, image);
	
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 17:11:02)
 * @return uk.co.ricky.pdf.PdfImage
 * @param name java.lang.String
 */
public PdfFont getFont(String name) throws PdfException {
	PdfFont font = (PdfFont)(fonts.get(name));
	if (font == null) {
		throw new PdfException( "Cannot find font '" + name + "'");
	}
	return font;
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 17:11:02)
 * @return uk.co.ricky.pdf.PdfImage
 * @param name java.lang.String
 */
public PdfImage getImage(String name) {
	return (PdfImage)(images.get(name));
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 15:52:51)
 * @return byte[]
 */
public void toOutputStream( java.io.OutputStream os ) {
	java.io.PrintWriter pw = new java.io.PrintWriter(os, true);

	StringBuffer procset = new StringBuffer("[ /PDF ");
	if (fonts.size() > 0) {
		procset.append("/Text ");
	}
	if (images.size() > 0) {
		procset.append("/ImageB ");
	}
	procset.append("]");
	
	pw.print( objectId + " 0 obj" + NL );
	pw.print("<<" + NL);
	pw.print(PROCSET + procset + NL);

	if (fonts.size() > 0) {
		pw.print( "/Font << " );
		Enumeration e = fonts.elements();
		while (e.hasMoreElements()) {
			PdfFont font = (PdfFont) (e.nextElement());
			pw.print( font.name + " " + objectRef(font) + " " );
		}
		pw.print( ">>" + NL);
	}
	
	if (images.size() > 0) {
		pw.print( "/XObject << " );
		Enumeration e = images.elements();
		while (e.hasMoreElements()) {
			PdfImage image = (PdfImage) (e.nextElement());
			pw.print( image.getName() + " " + objectRef(image) + " " );
		}
		pw.print( ">>" + NL);
	}
	
	pw.print(">>" + NL);
	pw.print(ENDOBJ + NL);
	pw.close();

}
}
