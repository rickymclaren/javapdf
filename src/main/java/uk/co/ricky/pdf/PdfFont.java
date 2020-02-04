package uk.co.ricky.pdf;

import java.util.*;
import java.io.*;

/**
 * Insert the type's description here.
 * Creation date: (15/03/2002 14:23:27)
 * @author: McLaren Richard
 */
public class PdfFont extends PdfObject {
	protected String subType;
	protected String name;
	protected String baseFont;

	protected int[] charWidths = null;
	protected Hashtable kerningTable = null;

	public static final  int COURIER = 1;
	public static final  int COURIER_BOLD = 2;
	public static final  int COURIER_BOLD_OBLIQUE = 3;
	public static final  int COURIER_OBLIQUE = 4;
	public static final  int HELVETICA = 5;
	public static final  int HELVETICA_BOLD = 6;
	public static final  int HELVETICA_BOLD_OBLIQUE = 7;
	public static final  int HELVETICA_OBLIQUE = 8;
	public static final  int TIMES_ROMAN = 9;
	public static final  int TIMES_BOLD = 10; 
	public static final  int TIMES_ITALIC = 11;
	public static final  int TIMES_BOLD_ITALIC = 12;
	public static final  int SYMBOL = 13;
	public static final  int ZAPFDINGBATS = 14;
	
/**
 * PdfPage constructor comment.
 */
public PdfFont(PdfDocument document) {
	super(document);
	type = FONT;
	subType = "/Type1";
}
/**
 * Load the font metrics from disk or try to build a font metrics file from the Acrobat files.
 * Creation date: (14/02/02 22:15:25)
 */
private void loadFontMetrics(String fileName) {

/* The pfm file has the following format (VB Code - note that it uses LittleEndian format)

Type PFM_Type									Offset
	version As Integer							0
	Length As Long								2
	copyright As String * 60					6
	type As Integer								66
	points As Integer								68
	verres As Integer							70
	horres As Integer							72
	ascent As Integer							74
	intleading As Integer						76
	extleading As Integer						78
	italic As String * 1							80
	uline As String * 1							81
	overs As String * 1							82
	weight As Integer							83
	charset As String * 1						85
	pixwidth As Integer							86
	pixheight As Integer						88
	kind As String * 1								90
	avgwidth As Integer						91
	maxwidth As Integer						93
	firstchar As String * 1						95
	lastchar As String * 1						96
	defchar As String * 1						97
	breakchar As String * 1					98
	widthby As Integer							99
	device As Long								101
	face As Long									105
	bits As Long									109
	bitoff As Long									113
	extlen As Integer								117
	psext As Long									119
	chartab As Long								123
	res1 As Long									127
	kernpairs As Long							131
	kerntrack As Long							135
	fontname As Long							139

End Type

*/

	try {
		String directory = "J:/Acrobat4_Reader/Resource/Font/PFM/";

		charWidths = new int[255];
		kerningTable = new Hashtable();
			
		RandomAccessFile raf = new RandomAccessFile(directory + fileName, "r");

		// Read the first and last characters
		raf.seek(95);
		int firstChar = raf.readUnsignedByte();
		int lastChar = raf.readUnsignedByte();

		// Read the Character Widths	
		raf.seek(123);
		int chartab = readLittleEndian(raf, 4);
		raf.seek(chartab);
		for (int i = firstChar; i < lastChar; i++){
			int charWidth = readLittleEndian(raf, 2);
			charWidths[i] = charWidth;
		}

		// Read the kerning table
		kerningTable = new Hashtable();
		raf.seek(131);
		int kernPairs = readLittleEndian(raf, 4);
		raf.seek( kernPairs );
		int numPairs = readLittleEndian(raf, 2);
		
		for (int i = 0; i < numPairs; i++){
			StringBuffer pair = new StringBuffer();
			pair.append( (char)raf.readUnsignedByte() );
			pair.append( (char)raf.readUnsignedByte() );
			short adjust = (short) readLittleEndian(raf, 2);
			kerningTable.put(pair.toString(), new Integer(adjust));
		}

		// Close the file	
		raf.close();

	} catch (Exception e) {
		e.printStackTrace();
	}
	
}
/**
 * Insert the method's description here.
 * Creation date: (21/02/2002 14:45:17)
 * @return int
 * @param raf java.io.RandomAccessFile
 */
static private int readLittleEndian(RandomAccessFile raf, int numBytes) throws IOException {
	int result = 0;
	for (int i = 0; i < 2; i++){
		result += (raf.readUnsignedByte() << (8*i));
	}
	return result;
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 12:09:01)
 * @param baseFont int
 */
public void setBaseFont(int newBaseFont) {

	String filePfm = null;
										
	switch (newBaseFont) {
		case COURIER: 		
			baseFont = "/Courier";
			filePfm = "Com_____.pfm";
			break;
		case COURIER_BOLD: 	
			baseFont = "/Courier-Bold";
			filePfm = "Cob_____.pfm";
			break;
		case COURIER_BOLD_OBLIQUE:
			baseFont = "/Courier-BoldOblique";
			filePfm = "Cobo____.pfm";
			break;
		case COURIER_OBLIQUE:
			baseFont = "/Courier-Oblique";
			filePfm = "Coo_____.pfm";
			break;
		case HELVETICA:
			baseFont = "/Helvetica";
			filePfm = "_a______.pfm";
			break;
		case HELVETICA_BOLD:
			baseFont = "/Helvetica-Bold";
			filePfm = "_ab_____.pfm";
			break;
		case HELVETICA_BOLD_OBLIQUE:
			baseFont = "/Helvetica-BoldOblique";
			filePfm = "_abi____.pfm";
			break;
		case HELVETICA_OBLIQUE:
			baseFont = "/Helvetica-Oblique";
			filePfm = "_ai_____.pfm";
			break;
		case TIMES_ROMAN:
			baseFont = "/Times-Roman";
			filePfm = "_er_____.pfm";
			break;
		case TIMES_BOLD:
			baseFont = "/Times-Bold";
			filePfm = "_eb_____.pfm";
			break;
		case TIMES_ITALIC:
			baseFont = "/Times-Italic";
			filePfm = "_ei_____.pfm";
			break;
		case TIMES_BOLD_ITALIC:
			baseFont = "/Times-BoldItalic";
			filePfm = "_ebi____.pfm";
			break;
		case SYMBOL:
			baseFont = "/Symbol";
			filePfm = "Sy______.pfm";
			break;
		case ZAPFDINGBATS:
			baseFont = "/ZapfDingbats";
			filePfm = "Zd______.pfm";
			break;
		default: 
			baseFont = "/Courier";
			filePfm = "Com_____.pfm";
			break;
	}

	if (filePfm != null) {
		loadFontMetrics(filePfm);
	}
	
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2002 12:07:50)
 * @param name java.lang.String
 */
public void setName(String name) {
	this.name = name;	
}
/**
 * Insert the method's description here.
 * Creation date: (03/04/2002 10:40:56)
 * @return java.lang.String[]
 * @param paragraph java.lang.String
 */
public String[] splitParagraph(String paragraph, double fontSize, double width) {
	Vector v = new Vector();

	// while we still have something left to split
	while (paragraph.length() > 0) {
		
		// find the number of characters that just pushes us over the width
		int i = 0;
		for (i = 0; i < paragraph.length(); i++){
			if (textWidth(paragraph.substring(0, i), fontSize) > width) {
				break;
			}
		}

		if (i < paragraph.length()) {
			// we could not fit it all in so split at the last space
			while (i > 0 && paragraph.charAt(i) != ' ') {
				i --;
			}
			// making sure that we print at least one character
			if (i == 0) {
				i = 1;
			}
			v.addElement(paragraph.substring(0, i));
			paragraph = paragraph.substring(i + 1);
		} else {
			// it all fits
			v.addElement(paragraph);
			paragraph = "";
		}
	}
	
	String[] result = new String[v.size()];
	v.copyInto(result);
	return result;
}
/**
 * Calculates the width of a piece of text in twips.
 * Creation date: (14/02/02 22:08:15)
 * @return double
 * @param text java.lang.String
 * @param font int
 * @param fontSize double
 */
public double textWidth(String text, double fontSize) {

	double result = 0.0;

	int previous = 0;
	int c = 0;
	Short pair = null;
	Integer adjust = null;
	for (int i = 0; i < text.length(); i++){
		try {
			previous = c;
			c = text.charAt(i);
			if (c < 32 || c > 255) {
				System.out.println( "replacing char " + c + " with M");
				c = 'M';
			}
			int width = charWidths[c];
			pair = new Short((short)((previous << 8) + c));
			adjust = (Integer) kerningTable.get(pair);
			if (adjust != null) {
				System.out.println( "Adjusting " + (char)previous + (char)c + " by " + adjust);
				width += adjust.shortValue();
			}
			result += width;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println( "Text=" + text);
			System.out.println( "i=" + i + " char=" + text.charAt(i) + "(" + (int)(text.charAt(i)) + ")");
		}
	}
	return result * fontSize / 1000.0;
	
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 15:52:51)
 * @return byte[]
 */
public void toOutputStream(OutputStream os) {
	java.io.PrintWriter pw = new java.io.PrintWriter(os, true);

	pw.print( objectId + " 0 obj" + NL );
	pw.print("<<" + NL);
	pw.print("/Type " + type + NL);
	pw.print("/Subtype " + subType + NL);
	pw.print("/Name " + name + NL);
	pw.print("/BaseFont " + baseFont + NL);
	pw.print(">>" + NL);
	pw.print(ENDOBJ + NL);
	pw.close();

}
}
