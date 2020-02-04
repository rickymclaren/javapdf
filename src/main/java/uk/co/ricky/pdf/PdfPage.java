package uk.co.ricky.pdf;

/**
 * Insert the type's description here.
 * Creation date: (15/03/2002 14:23:27)
 * @author: McLaren Richard
 */
public class PdfPage extends PdfObject {
	protected PdfPages parent;
	protected PdfPageContent pageContent;
	protected PdfFont font;

	// Note that all measurements are in points
	protected double leftMargin = 72;
	protected double rightMargin = 72;
	protected double topMargin = 72;
	protected double bottomMargin = 72;
	protected double x = 0;
	protected double y = 0;
	protected double fontSize = 10;

	public static final int PAGE_HEIGHT = 842;	
	public static final int PAGE_WIDTH = 595;		
	
/**
 * PdfPage constructor comment.
 */
public PdfPage(PdfDocument document) {
	super(document);
	type = PAGE;
	pageContent = new PdfPageContent(document);
}
/**
 * Insert the method's description here.
 * Creation date: (19/03/2002 17:17:09)
 * @param x double
 * @param y double
 * @param w double
 * @param h double
 */
public void drawBox(double x, double y, double w, double h) {
	pageContent.lines.append( (leftMargin + x) + " " + (PAGE_HEIGHT - topMargin - y) + " " + w + " " + (-h) + " re" + NL);
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 17:03:17)
 * @param name java.lang.String
 * @param x double
 * @param y double
 * @param width double
 * @param height double
 */
public void drawImage(String name, double x, double y) {

	PdfImage image = document.getResources().getImage(name);
	double w = image.getWidth();
	double h = image.getHeight();
	
	pageContent.graphics.append("q" + NL);
	pageContent.graphics.append(w + " 0 0 " + h  + " " + x + " " + (PAGE_HEIGHT - y) + " cm" + NL);
	pageContent.graphics.append(image.getName() + " Do" + NL);
	pageContent.graphics.append("Q" + NL);
	
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 17:03:17)
 * @param name java.lang.String
 * @param x double
 * @param y double
 * @param width double
 * @param height double
 */
public void drawImage(String name, double x, double y, double scaling) {

	PdfImage image = document.getResources().getImage(name);
	double w = image.getWidth() * scaling;
	double h = image.getHeight() * scaling;
	
	pageContent.graphics.append("q" + NL);
	pageContent.graphics.append(w + " 0 0 " + h  + " " + x + " " + (PAGE_HEIGHT - y) + " cm" + NL);
	pageContent.graphics.append(image.getName() + " Do" + NL);
	pageContent.graphics.append("Q" + NL);
	
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 14:11:08)
 * @param x1 double
 * @param y1 double
 * @param x2 double
 * @param y2 double
 */
public void drawLine(double x1, double y1, double x2, double y2) {
	pageContent.lines.append((leftMargin + x1) + " " + (PAGE_HEIGHT - topMargin - y1) + " m" + NL);
	pageContent.lines.append((leftMargin + x2) + " " + (PAGE_HEIGHT - topMargin - y2) + " l" + NL);
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 15:20:55)
 * @return double
 */
public double getBottomMargin() {
	return bottomMargin;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 13:42:19)
 * @return double
 */
public double getCurrentX() {
	return x;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 13:42:19)
 * @return double
 */
public double getCurrentY() {
	return y;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 15:46:15)
 * @return uk.co.ricky.pdf.PdfFont
 */
public PdfFont getFont() {
	return font;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 15:46:41)
 * @return double
 */
public double getFontSize() {
	return fontSize;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 15:20:55)
 * @return double
 */
public double getLeftMargin() {
	return leftMargin;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 15:20:55)
 * @return double
 */
public double getRightMargin() {
	return rightMargin;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 15:20:55)
 * @return double
 */
public double getTopMargin() {
	return topMargin;
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 14:49:38)
 * @param text java.lang.String
 */
protected void outputText(String text) {

	int currPos;
	StringBuffer sb = new StringBuffer();

	// escape characters if necessary
	for (int i = 0; i < text.length(); i++){
		char c = text.charAt(i);
		switch (c) {
			case '(':
			case ')':
			case '\\':
				sb.append('\\');
				// fall through
				
			default:
				sb.append(c);
		}
	}

	pageContent.text.append("1 0 0 1 " + (leftMargin + x) + " " + (PAGE_HEIGHT - topMargin - y - fontSize) + " Tm" + NL);
	pageContent.text.append("(" + sb + ") Tj" + NL);

}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 14:49:38)
 * @param text java.lang.String
 */
public void print(String text) {

	outputText( text );
	x += font.textWidth(text, fontSize);
	
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 15:07:53)
 * @param text java.lang.String
 */
public void println(String text) {

	outputText( text );
	x = 0;
	y += fontSize;
		
}
/**
 * Insert the method's description here.
 * Creation date: (03/04/2002 10:36:10)
 * @param paragraph java.lang.String
 */
public void printParagraph(String paragraph) {

	String[] lines = font.splitParagraph( paragraph, fontSize, PAGE_WIDTH - leftMargin - rightMargin - x );

	PdfPage page = this;
	if (y + lines.length * fontSize > PAGE_HEIGHT - topMargin - bottomMargin) {
		page = document.addPage();
	}
	for (int i = 0; i < lines.length; i++){
		page.println( lines[i] );
	}
	
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 15:20:55)
 * @param newBottomMargin double
 */
public void setBottomMargin(double newBottomMargin) {
	bottomMargin = newBottomMargin;
}
/**
 * Insert the method's description here.
 * Creation date: (19/03/2002 09:58:13)
 * @param red double
 * @param green double
 * @param blue double
 */
public void setColour(double red, double green, double blue) {
	pageContent.text.append(red + " " + green + " " + blue + " rg" + NL)	;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 14:43:25)
 * @param font uk.co.ricky.pdf.PdfFont
 */
public void setFont(PdfFont font) {
	this.font = font;
	pageContent.text.append(font.name + " " + fontSize + " Tf" +NL);
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 14:41:27)
 * @param size double
 */
public void setFontSize(double size) {
	fontSize = size;
	pageContent.text.append(font.name + " " + fontSize + " Tf" +NL);
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 15:20:55)
 * @param newLeftMargin double
 */
public void setLeftMargin(double newLeftMargin) {
	leftMargin = newLeftMargin;
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 13:19:41)
 * @param parent uk.co.ricky.pdf.PdfPages
 */
public void setParent(PdfPages parent) {
	this.parent = parent;	
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 15:20:55)
 * @param newRightMargin double
 */
public void setRightMargin(double newRightMargin) {
	rightMargin = newRightMargin;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 15:20:55)
 * @param newTopMargin double
 */
public void setTopMargin(double newTopMargin) {
	topMargin = newTopMargin;
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
	pw.print(PARENT + objectRef(parent) + NL);
	pw.print(RESOURCES + objectRef(document.getResources()) + NL);
	pw.print(CONTENTS + objectRef(pageContent) + NL);
	pw.print(">>" + NL);
	pw.print(ENDOBJ + NL);
	pw.close();

}
}
