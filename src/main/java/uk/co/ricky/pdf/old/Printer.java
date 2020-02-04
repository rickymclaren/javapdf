package uk.co.ricky.pdf.old;

import java.util.*; 
import java.awt.*;

/**
 * Insert the type's description here.
 * Creation date: (14/02/02 23:37:19)
 * @author: Administrator
 */
public class Printer {

	private static final int PDF_PAGE_HEIGHT = 842;		// points
	private static final int PDF_PAGE_WIDTH = 595;		// points

	private StringBuffer sPDF = new StringBuffer();
	private StringBuffer sPDFLines = new StringBuffer();
	private double dPDFCurrentX;
	private double dPDFCurrentY;
	private boolean bPDFBold;
	private boolean bPDFItalic;
	private int iKeepTogether;

	private int iTopMargin;
	private int iBottomMargin;
	private int iLeftMargin;
	private int iRightMargin;

	private int iCurrStyle;
	private String msFontName;
	private double mfFontSize;
	private int iLineSpBefore;
	private int iLineSpAfter;

	private String bulletText = "\u00b7   ";
		
	private Vector pages = new Vector();	// Vector of Strings containing page text
	private Vector lines = new Vector();	// Vector of Strings containing page lines
	private Vector tabs = new Vector(); 
	
/**
 * Printer constructor comment.
 */
public Printer() {
	this(1440, 1440, 1440, 1440);
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/02 02:59:59)
 * @param top int
 * @param bottom int
 * @param left int
 * @param right int
 */
public Printer(int top, int bottom, int left, int right) {

	iTopMargin = top;
	iBottomMargin = bottom;
	iLeftMargin = left;
	iRightMargin = right;

	init();
		
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 12:18:55)
 * @param inches double
 */
public void addTab(double inches) {

	tabs.addElement(new Integer((int)(inches * 1440)));
	
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/02 00:27:32)
 */
public void bullet() {
	print(bulletText);
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 12:22:23)
 */
public void bulletKeep() {

	setCurrentX(getCurrentX() + textWidth(bulletText));
	
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 12:28:04)
 */
public void clearTabs() {
	tabs.removeAllElements();	
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/02 03:14:57)
 */
public void drawBox(int x, int y, int width, int height) {
	
	sPDFLines.append( ((iLeftMargin + x) / 20) + " " + (PDF_PAGE_HEIGHT - (iTopMargin + y) / 20) + " " + (width / 20) + " " + (-height / 20) + " re" + Pdf.NL);
	
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/02 23:38:01)
 */
public String endDoc() throws java.io.IOException {

	// Add the last page
	pages.addElement(sPDF);
	lines.addElement(sPDFLines);

	int[] pageIndex = new int[pages.size()];
	int[] pageStream = new int[pages.size()];

	Pdf pdf = new Pdf();

//	java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage("C:/ricky/javalogo52x88.gif");
	java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage("C:/ricky/abbey_national.gif");
	java.awt.MediaTracker tracker = new java.awt.MediaTracker (new java.awt.Canvas());
	tracker.addImage (img, 0);
	try {
	      tracker.waitForID (0); 
	} catch (InterruptedException e) {
	}
	int h = img.getHeight(null);
	int w = img.getWidth(null);
	System.out.println( "Height=" + h );
	System.out.println( "Width=" + w );

	java.awt.image.PixelGrabber pg = new java.awt.image.PixelGrabber(img, 0, 0, w, h, true);
	try {
		pg.grabPixels();
	} catch (InterruptedException ie) {
		System.out.println( "grabPixels interrupted" );
	}
	int[] pixels = (int[])pg.getPixels();
	
	int iLogo = pdf.createObject();
	pdf.addProperty("/Type /XObject");
	pdf.addProperty("/Subtype /Image");
	pdf.addProperty("/Name /Im1");
	pdf.addProperty("/Width " + w);
	pdf.addProperty("/Height " + h);
	pdf.addProperty("/BitsPerComponent 8");
	pdf.addProperty("/ColorSpace /DeviceRGB");
	byte[] imageStream = new byte[pixels.length * 3];
	for (int i = 0; i < pixels.length; i++){
		imageStream[i *3] = (byte)(pixels[i] >>> 16);
		imageStream[i *3 + 1] = (byte)(pixels[i] >>> 8);
		imageStream[i *3 + 2] = (byte)(pixels[i]);
	}

	boolean deflate = true;
	if (deflate) {
		String hexString;
		boolean Ascii85 = true;
		if (Ascii85) {
			pdf.addProperty("/Filter [/ASCII85Decode /FlateDecode]");
			pdf.addProperty("/Predictor 1");
			Ascii85OutputStreamToString hex85 = new Ascii85OutputStreamToString();
			java.util.zip.DeflaterOutputStream dos = new java.util.zip.DeflaterOutputStream(hex85);
			dos.write(imageStream, 0, imageStream.length);
			dos.flush();
			dos.close();
			hex85.close();
			hexString = hex85.toString();
		} else {
			pdf.addProperty("/Filter [/ASCIIHexDecode /FlateDecode]");
			pdf.addProperty("/Predictor 1");
			AsciiHexOutputStreamToString hex = new AsciiHexOutputStreamToString();
			java.util.zip.DeflaterOutputStream dos = new java.util.zip.DeflaterOutputStream(hex);
			dos.write(imageStream, 0, imageStream.length);
			dos.flush();
			dos.close();
			hex.close();
			hexString = hex.toString();
		}
		System.out.println( "Deflated " + imageStream.length + " to " + hexString.length());
		pdf.setStream(hexString);
		
	} else {
		pdf.setStream( new String(imageStream) );
	}
	

	int outlines = pdf.createObject();
	pdf.addProperty("/Type /Outlines");
	pdf.addProperty("/Count 0");
		
	int font = pdf.createObject();
	pdf.addProperty("/Type /Font");
	pdf.addProperty("/Subtype /Type1");
	pdf.addProperty("/Name /F1");
	pdf.addProperty("/BaseFont /Helvetica");
	
	int fontBold = pdf.createObject();
	pdf.addProperty("/Type /Font");
	pdf.addProperty("/Subtype /Type1");
	pdf.addProperty("/Name /F2");
	pdf.addProperty("/BaseFont /Helvetica-Bold");
		
	int fontItalic = pdf.createObject();
	pdf.addProperty("/Type /Font");
	pdf.addProperty("/Subtype /Type1");
	pdf.addProperty("/Name /F3");
	pdf.addProperty("/BaseFont /Helvetica-Oblique");
	
	int fontBoldItalic = pdf.createObject();
	pdf.addProperty("/Type /Font");
	pdf.addProperty("/Subtype /Type1");
	pdf.addProperty("/Name /F4");
	pdf.addProperty("/BaseFont /Helvetica-BoldOblique");
		
	int resources = pdf.createObject();
	pdf.addProperty("/Procset [ /PDF /Text /ImageB ]");
	//pdf.addProperty("/Procset [ /PDF /Text ]");
	pdf.addProperty("/Font << /F1 " + font + " 0 R " + "/F2 " + fontBold + " 0 R /F3 " + fontItalic + " 0 R " + "/F4 " + fontBoldItalic + " 0 R >>");
	pdf.addProperty("/XObject << /Im1 " + iLogo + " 0 R >>");
		
	int iPages = pdf.createObject();
	pdf.addProperty("/Type /Pages");
	pdf.addProperty("/MediaBox [ 0 0 " + PDF_PAGE_WIDTH + " " + PDF_PAGE_HEIGHT + " ]");

	for (int i = 0; i < pages.size(); i++){
		pageStream[i] = pdf.createObject();
		pdf.appendToStream("BT");
		pdf.appendToStream(((StringBuffer)pages.elementAt(i)).toString());
		pdf.appendToStream("ET");
		pdf.appendToStream("0.5 w");
		pdf.appendToStream(((StringBuffer)lines.elementAt(i)).toString());
		pdf.appendToStream("S");
		pdf.appendToStream("q");
//		pdf.appendToStream("147 0 0 60 70.8" + " " + (PDF_PAGE_HEIGHT - 400) + " cm");
		pdf.appendToStream(w + " 0 0 " + h  + " 70.8" + " " + (PDF_PAGE_HEIGHT - 800) + " cm");
		pdf.appendToStream("/Im1 Do");
		pdf.appendToStream("Q");
		
		pageIndex[i] = pdf.createObject();
		pdf.addProperty("/Type /Page");
		pdf.addProperty("/Parent " + iPages + " 0 R");
		pdf.addProperty("/Resources " + resources + " 0 R");
		pdf.addProperty("/Contents " + pageStream[i] + " 0 R");
	}

	pdf.setCurrentObject(iPages)	;
	pdf.addProperty("/Count " + pages.size());
	StringBuffer sb = new StringBuffer("/Kids [ ");
	for (int i = 0; i < pages.size(); i++){
		sb.append(pageIndex[i]);
		sb.append(" 0 R ");
	}
	sb.append("]");
	pdf.addProperty(sb.toString());
	 
	int catalog = pdf.createObject();
	pdf.addProperty("/Type /Catalog");
	pdf.addProperty("/Pages " + iPages + " 0 R");
	pdf.addProperty("/Outlines " + outlines + " 0 R");
		
	pdf.setRoot(catalog);

	return pdf.endPdf();
	
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 12:31:17)
 * @return int
 */
public int getCentre() {
	return  PDF_PAGE_WIDTH * 20 / 2 - iLeftMargin;
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/02 01:07:18)
 * @return double
 */
public double getCurrentX() {
	return dPDFCurrentX;
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/02 03:20:37)
 * @return double
 */
public double getCurrentY() {
	return dPDFCurrentY;
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/02 01:14:15)
 * @return java.lang.String
 */
public String getFontName() {
	return msFontName;
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/02 01:14:41)
 */
public double getFontSize() {
	return mfFontSize;	
}
/**
 * Insert the method's description here.
 * Creation date: (21/02/2002 11:25:02)
 * @return int
 */
public double getLeftMargin() {
	return iLeftMargin;
}
/**
 * Insert the method's description here.
 * Creation date: (21/02/2002 11:25:02)
 * @return int
 */
public double getRightMargin() {
	return iRightMargin;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 14:46:22)
 * @return double
 */
public double getScaleHeight() {
	return PDF_PAGE_HEIGHT * 20; // A4 points -> twips
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 14:47:51)
 * @return double
 */
public double getScaleLeft() {
	return iLeftMargin;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 14:49:03)
 */
public double getScaleWidth() {
	return PDF_PAGE_WIDTH * 20; // A4 points -> twips
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 13:09:59)
 * @param tab int
 */
public void gotoTab(int tab) {
	Integer i = (Integer)tabs.elementAt(tab-1);
	setCurrentX(i.doubleValue());
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/02 02:58:44)
 */
private void init() {

	sPDF.append("/F1 10 Tf" + Pdf.NL);
	sPDF.append("1 0 0 1 " + (iLeftMargin / 20) + " " + ((PDF_PAGE_HEIGHT - iTopMargin) / 20) + " Tm" + Pdf.NL);
	sPDF.append("10 TL" + Pdf.NL);

	mfFontSize = 10;

}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 14:50:55)
 * @param bLeft0 int
 * @param bTop0 int
 * @param bLeft1 int
 * @param bTop1 int
 */
public void line(int bLeft0, int bTop0, int bLeft1, int bTop1) {
	
  sPDFLines.append(((iLeftMargin + bLeft0) / 20) + " " + (PDF_PAGE_HEIGHT - (iTopMargin + bTop0) / 20) + " m" + Pdf.NL);
  sPDFLines.append(((iLeftMargin + bLeft1) / 20) + " " + (PDF_PAGE_HEIGHT - (iTopMargin + bTop1) / 20) + " l" + Pdf.NL);
  
}
/**
 * Insert the method's description here.
 * Creation date: (21/02/2002 11:36:02)
 */
public void newPage() {

	pages.addElement(sPDF);
	lines.addElement(sPDFLines);

	sPDF.setLength(0);
	sPDFLines.setLength(0);

	setCurrentX(0);
	setCurrentX(1);
	
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 13:12:40)
 * @param text java.lang.String
 */
public void outputRight(String text) {
	
	double startPoint = getCurrentX() - textWidth(text);
	setCurrentX(startPoint);
   	print(text);
   	
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 13:17:24)
 * @param text java.lang.String
 */
public void outputText(String sOutputText) {
	
	double iCurrentX;
	boolean iDone;
	double dOutputLength;
	int iNumChars;
	int iSpaceLeft;
	int iSpacePos;
	String sText;

	int iLowGuess, iHighGuess,  iGuess;
	double dLowWidth, dHighWidth, dGuessWidth;

	int font = 0;
	if (bPDFBold) {
		font++;
	}
	if (bPDFItalic) {
		font += 2;
	}

	// rmc 02.09.98 - Add new OutputTextExt
	if (sOutputText.indexOf('^') != -1) {
		outputTextExt(sOutputText);
		return;
	}

	sText = sOutputText;
	iCurrentX = getCurrentX();

	iSpaceLeft = (int)(getScaleWidth() - getLeftMargin() - getRightMargin() - iCurrentX);
	
   //' rmc 3.10.95 - allow for negative space left
	if (iSpaceLeft < 0) {
	    iSpaceLeft = 0;
	}

	// Convert Spaceleft from twips to 1000ths of the font size.
	iSpaceLeft *= 50 / mfFontSize;


	int[] charWidths = Pdf.getCharWidths(font);
	Hashtable kerningTable = Pdf.getKerningTable(font);

	while (sText.length() > 0) {
		
		int previous = 0;
		int c = 0; 
		double length = 0;
		int numChars = 0;
		StringBuffer pair = null;
		Integer adjust = null;
		while (length < iSpaceLeft && numChars < sText.length()) {
			previous = c;
			c = sText.charAt(numChars);
			int width = charWidths[c];
			pair = new StringBuffer();
			pair.append( (char)c );
			pair.append( (char)previous );
			adjust = (Integer) kerningTable.get(pair.toString());
			if (adjust != null) {
				System.out.println( "Adjusting " + (char)previous + (char)c + " by " + adjust);
				width += adjust.shortValue();
			}
			length += width;
			numChars++;
		}
		if (length > iSpaceLeft) {
			numChars--;
		}
		print(sText.substring(0, numChars), false); 
		sText = sText.substring(numChars).trim();
		if (sText.length() > 0) {
			print("", true);
		}
		
	}
	
	
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 13:49:03)
 * @param sOutputText java.lang.String
 */
public void outputTextExt(String sOutputText) {

	// This function works like OutputText but handles different font styles

	// rmc 18.09.96 - change printwidth variables to singles to handle larger paragraphs

	double iCurrentX;
	boolean iDone;
	double fOutputLength;
	int iNumChars;
	double fSpaceLeft;
	int iSpacePos;
	String sText;

	int iLowGuess, iHighGuess, iGuess;
	double fLowWidth, fHighWidth, fGuessWidth;
	char sFormat;

	iCurrentX = getCurrentX();
	while (sOutputText.length() > 0) {

	    int carat = sOutputText.indexOf('^');
		if (carat != -1) {
			sText = sOutputText.substring(0, carat);
			sFormat = sOutputText.charAt(carat+1);
			sOutputText = sOutputText.substring(carat+2);
		} else {
			sText = sOutputText;
			sFormat = ' ';
			sOutputText = "";
		}
		
		fSpaceLeft = getScaleWidth() - getLeftMargin() - getRightMargin() - getCurrentX();
		// rmc 3.10.95 - allow for negative space left
		if (fSpaceLeft < 0) {
	        fSpaceLeft = 0;
		}
		fOutputLength = textWidth(sText);
			
		while (fOutputLength > fSpaceLeft) {
			// Ok, find how many characters we can fit on.
			// rmc 19.08.96 - Use binary search instead of scan from end
			iLowGuess = 1;
			fLowWidth = textWidth(sText.substring(0, 1));
			iHighGuess = sText.length();
			fHighWidth = textWidth(sText);
			while ((iHighGuess - iLowGuess) > 1) {
				iGuess = (iHighGuess + iLowGuess) / 2;
				fGuessWidth = textWidth(sText.substring(0, iGuess));
				if (fGuessWidth == fSpaceLeft) {
					iLowGuess = iGuess;
					iHighGuess = iGuess + 1;
				} else if (fGuessWidth > fSpaceLeft) {
					iHighGuess = iGuess;
				} else {
					iLowGuess = iGuess;
				}
			}
			iNumChars = iLowGuess;
			// rmc 19.08.96 - end
			// Now find the last space so that we can make a clean break
			iSpacePos = iNumChars;
			// rmc 3.10.95 - allow for iNumChars = 0
			iDone = (iNumChars == 0);
			while ( !  iDone ) {
				if (sText.charAt(iSpacePos-1) == ' ') {
					iDone = true;
				} else {
					iSpacePos = iSpacePos - 1;
					iDone = (iSpacePos == 0);
				}
			}
			iNumChars = iSpacePos;
	
			// Now print what we can
			print(sText.substring(0, iNumChars));
			setCurrentX(iCurrentX);
			sText = sText.substring(iNumChars);
			fOutputLength = textWidth(sText);
			fSpaceLeft = getScaleWidth() - getLeftMargin() - getRightMargin() - getCurrentX();
		}

		// Print the remainder without a carraige return
		print(sText);
		
		switch (sFormat) {
			case 'B': setFontBold(true); break;
			case 'b': setFontBold(false); break;
			case 'I': setFontItalic(true); break;
			case 'i': setFontItalic(false); break;
		}
		
	}
		
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 13:17:24)
 * @param text java.lang.String
 */
public void outputTextOld(String sOutputText) {
	
	double iCurrentX;
	boolean iDone;
	double dOutputLength;
	int iNumChars;
	int iSpaceLeft;
	int iSpacePos;
	String sText;

	int iLowGuess, iHighGuess,  iGuess;
	double dLowWidth, dHighWidth, dGuessWidth;


	sText = sOutputText;
	iCurrentX = getCurrentX();

	iSpaceLeft = (int)(getScaleWidth() - getLeftMargin() - getRightMargin() - iCurrentX);
	
   //' rmc 3.10.95 - allow for negative space left
	if (iSpaceLeft < 0) {
	    iSpaceLeft = 0;
	}
	dOutputLength = textWidth(sText);

	while (dOutputLength > iSpaceLeft) {
		// Ok, find how many characters we can fit on.
		// rmc 30.09.96 - Use binary search instead of scan from end
		iLowGuess = 1;
		dLowWidth = textWidth(sText.substring(0, 1));
		iHighGuess = sText.length();
		dHighWidth = textWidth(sText);
		while ((iHighGuess - iLowGuess) > 1) {
			iGuess = (iHighGuess + iLowGuess) / 2;
			dGuessWidth = textWidth(sText.substring(0,  iGuess));
			if (dGuessWidth == iSpaceLeft) {
				iLowGuess = iGuess;
				iHighGuess = iGuess + 1;
			} else if (dGuessWidth > iSpaceLeft) {
				iHighGuess = iGuess;
			} else {
				iLowGuess = iGuess;
			}
		}
		iNumChars = iLowGuess;
		// rmc 19.08.96 - end
		// Now find the last space so that we can make a clean break
		iSpacePos = iNumChars;
		// rmc 3.10.95 - allow for iNumChars = 0
		iDone = (iNumChars == 0);
		while ( ! iDone ) {
			if (sText.charAt(iSpacePos-1)  == ' ' ) {
				iDone = true;
			} else {
				iSpacePos = iSpacePos - 1;
				if (iSpacePos == 0) {
					iDone = true;
				}
			}
		}
		if (iSpacePos != 0) {
			iNumChars = iSpacePos;
		}
		// Force at least one character for anybody who insists on printing outside the margins.
		if (iNumChars < 1) {
			iNumChars = 1;
		}

		// Now print what we can
		print(sText.substring(0, iNumChars), true);
		setCurrentX(iCurrentX);
		sText = sText.substring( iNumChars );
		dOutputLength = textWidth(sText);
	}
	
	// Print the remainder without a carraige return
	print(sText);
	
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/02 01:00:30)
 * @param text java.lang.String
 */
public void print(String text) {

	print( text, false );
		
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/02 00:30:50)
 * @param text java.lang.String
 */
public void print(String text, boolean newLine) {
	
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

	if (iKeepTogether != 1 && sb.length() > 0) {
		sPDF.append("1 0 0 1 " + ((int)(iLeftMargin + dPDFCurrentX) / 20) + " " + (PDF_PAGE_HEIGHT - (int)(iTopMargin + dPDFCurrentY) / 20 - mfFontSize + 2) + " Tm" + Pdf.NL);
		sPDF.append("(" + sb + ") Tj" + Pdf.NL);
	}

	if (newLine) {
		dPDFCurrentX = 0;
		dPDFCurrentY += mfFontSize * 20 + iLineSpBefore + iLineSpAfter;
		setCurrentY (dPDFCurrentY);
	} else {
		setCurrentX (getCurrentX() + textWidth(text));
	}

/*
	If bFooter Then Exit Sub

	iCurrPos = PrintGetCurrentY()
	If printer_or_screen = "E" Then
		If iCurrPos > iTexScaleHeight Then
			Call PrintNewPage
			 /* 23/05/96 raju - TEX/95/080 starts.
			Call PrintLogo
			  TEX/95/080 ends.
			 TEX/96/011 - (TEX/95/081).
			If iKeepTogether = 1 Then
				iKeepTogether = 2
				iKeepTogetherStart = PrintGetCurrentY()
				Error ERR_KEEP_TOGETHER
			End If
		End If
		Exit Sub
	End If
	If iCurrPos > iPrintHeight Then
		 rmc 20.11.95 - fix keep together
		If iKeepTogether = 1 Then
			If printer_or_screen = "S" Then
				ObjToPrintOn.FillColor = RGB(255, 255, 255)  ' Grey for screen.
				ObjToPrintOn.FillStyle = 0     'solid fill style
				ObjToPrintOn.Line (0, iKeepTogetherStart)-(iPrintWidth, iCurrPos), RGB(255, 255, 255), BF
			Else
				printer.FillColor = RGB(255, 255, 255)  ' Grey for printer.
				printer.FillStyle = 0     'solid fill style
				printer.Line (0, iKeepTogetherStart)-(iPrintWidth, iCurrPos), RGB(255, 255, 255), BF
			End If
			Call PrintNewPage
			iKeepTogether = 2
			iKeepTogetherStart = PrintGetCurrentY()
			Error ERR_KEEP_TOGETHER
		Else
			Call PrintNewPage
		End If
	End If
	*/
	
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/02 01:06:31)
 * @param x double
 */
public void setCurrentX(double x) {

	dPDFCurrentX = x;
		
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/02 01:05:28)
 * @param y double
 */
public void setCurrentY(double y) {
	dPDFCurrentY = y;	
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/02 02:36:38)
 * @param size double
 * @param bold boolean
 * @param italic boolean
 */
public void setFont(double size, boolean bold, boolean italic) {

	int font = 0;

	mfFontSize = size;
	bPDFBold = bold;
	bPDFItalic = italic;

	if (iKeepTogether != 1) {
	    font = 0;
		if (bold) {
			font += 1;
		}
		if (italic) {
			font += 2;
		}
		sPDF.append("/F" + (font+1) + " " + size + " Tf" +Pdf.NL);
	}
	
}
/**
 * Insert the method's description here.
 * Creation date: (21/02/2002 11:32:57)
 * @param bold boolean
 */
public void setFontBold(boolean bold) {

	setFont(mfFontSize, bold, bPDFItalic);
	
}
/**
 * Insert the method's description here.
 * Creation date: (21/02/2002 11:32:57)
 * @param bold boolean
 */
public void setFontItalic(boolean italic) {

	setFont(mfFontSize, bPDFBold, italic);
	
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/02 02:29:52)
 * @param style int
 */
public void setStyle(int style) {

	switch (style) {
		case 1: setFont( 12, true, false); break;
		case 2: setFont( 11, true, false); break;
		case 3: setFont( 11, true, false); break;
		case 4: setFont( 10, false, false); break;
		case 5: setFont( 10, false, false); break;
		case 6: setFont( 8, false, false); break;
		case 7: setFont( 12, true, true); break;
		case 8: setFont( 14, true, false); break;
		case 9: setFont( 14, false, false); break;
		case 10: setFont( 10, false, false); break;
		case 11: setFont( 10, true, false); break;
		case 12: setFont( 8, false, false); break;
		case 13: setFont( 8, true, false); break;
		case 14: setFont( 12, true, false); break;
		case 15: setFont( 34, true, false); break;
		case 16: setFont( 8, false, false); break;
		default: setFont( 12, true, false); break;
	}
	
	iCurrStyle = style;
		
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/02 01:09:51)
 * @param text java.lang.String
 */
public double textWidth(String text) {

	int font = 0;
	if (bPDFBold) {
		font++;
	}
	if (bPDFItalic) {
		font += 2;
	}
	return Pdf.textWidth(text, font, getFontSize());
		
}
}
