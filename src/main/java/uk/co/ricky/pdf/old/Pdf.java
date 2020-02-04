package uk.co.ricky.pdf.old;

import java.io.*;  
import java.util.*;
import java.text.*;

/**
 * Insert the type's description here.
 * Creation date: (14/02/02 21:59:19)
 * @author: Administrator
 */
public class Pdf {
	public static final String NL = "\r\n";
	
	private File pdfFile;
	private Vector pdfObjects = new Vector();
	private int currentObject;
	private int root;
	static int[][] charWidths = null;
	static Hashtable[] kerningTable = null;
/**
 * Pdf constructor comment.
 */
public Pdf() {
	super();

	if (charWidths == null) {
		loadFontMetrics();
	}
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/02 22:43:17)
 * @param property java.lang.String
 */
public void addProperty(String property) {

	((PdfObject)pdfObjects.elementAt(currentObject)).addProperty(property);
	
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/02 22:38:00)
 * @param data java.lang.String
 */
public void appendToStream(String data) {

	PdfObject o = (PdfObject)pdfObjects.elementAt(currentObject);
	o.appendToStream(data);
	o.appendToStream(NL);
		
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/02 22:34:21)
 * @return int
 */
public int createObject() {
	pdfObjects.addElement(new PdfObject());
	currentObject = pdfObjects.size() - 1;
	return pdfObjects.size();
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/02 22:48:04)
 */
public String endPdf() {

	int startXref;
	int[] xref = new int[pdfObjects.size()];

	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw, true);
		
	pw.print("%PDF-1.2");
	pw.print(NL);
	pw.print("%\u00e2\u00e3\u00cf\u00d3");
	pw.print(NL);

	for (int i = 0; i < pdfObjects.size(); i++){
		PdfObject o = (PdfObject) pdfObjects.elementAt(i);
		String stream = o.getStream();
		String dictionary = o.getDictionary();

		xref[i] = sw.getBuffer().length();
		pw.print( (i+1) + " 0 obj" + NL );
		if (stream.length() > 0) {
			pw.print("<<" + NL);
			if (dictionary.length() > 0) {
				pw.print(dictionary + NL);
			}
			pw.print("/Length " + stream.length() + NL);
			pw.print(">>" + NL);
			pw.print("stream" + NL);
			pw.print(stream);
			pw.print("endstream" + NL);
		} else {
			pw.print("<<" + NL);
			pw.print(dictionary + NL);
			pw.print(">>" + NL);
		}
		pw.print("endobj" + NL);
		
	}
	
	int startxref = sw.getBuffer().length();
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
	pw.print("/Root " + root + " 0 R " + NL);
	pw.print(">> " + NL);
	pw.print("startxref" + NL);
	pw.print(startxref + NL);
	pw.print("%%EOF" + NL);

	return sw.toString();
	
}
/**
 * Insert the method's description here.
 * Creation date: (28/02/02 21:56:11)
 * @return int[]
 * @param font int
 */
public static int[] getCharWidths(int font) {
	if (charWidths == null) {
		loadFontMetrics();
	}
	return charWidths[font];
}
/**
 * Insert the method's description here.
 * Creation date: (04/03/02 05:12:16)
 * @return java.util.Hashtable
 * @param font int
 */
public static Hashtable getKerningTable(int font) {
	if (charWidths == null) {
		loadFontMetrics();
	}
	return kerningTable[font];
}
/**
 * Load the font metrics from disk or try to build a font metrics file from the Acrobat files.
 * Creation date: (14/02/02 22:15:25)
 */
private static void loadFontMetrics() {

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
		String directory = "J:/acrobat3/reader/fonts/pfm/";
		String[] files = {	"hv______.pfm",
									"hvb_____.pfm",
									"hvo_____.pfm",
									"hvbo____.pfm" };

		charWidths = new int[files.length][255];
		kerningTable = new Hashtable[files.length];
		for (int iFile = 0; iFile < files.length; iFile++) {
			
			RandomAccessFile raf = new RandomAccessFile(directory + files[iFile], "r");

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
				charWidths[iFile][i] = charWidth;
			}

			// Read the kerning table
			kerningTable[iFile] = new Hashtable();
			raf.seek(131);
			int kernPairs = readLittleEndian(raf, 4);
			raf.seek( kernPairs );
			int numPairs = readLittleEndian(raf, 2);
			
			for (int i = 0; i < numPairs; i++){
				StringBuffer pair = new StringBuffer();
				pair.append( (char)raf.readUnsignedByte() );
				pair.append( (char)raf.readUnsignedByte() );
				short adjust = (short) readLittleEndian(raf, 2);
				kerningTable[iFile].put(pair.toString(), new Integer(adjust));
			}

			// Close the file	
			raf.close();
		}

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
 * Creation date: (14/02/02 22:32:20)
 * @param currentObject int
 */
public void setCurrentObject(int currentObject) {

	this.currentObject = currentObject - 1;
		
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/02 22:05:54)
 * @param root int
 */
public void setRoot(int root) {

	this.root = root;
		
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/02 22:21:43)
 * @param stream java.lang.String
 */
public void setStream(String stream) {

	((PdfObject) pdfObjects.elementAt(currentObject)).setStream(stream);
	
}
/**
 * Calculates the width of a piece of text in twips.
 * Creation date: (14/02/02 22:08:15)
 * @return double
 * @param text java.lang.String
 * @param font int
 * @param fontSize double
 */
public static double textWidth(String text, int font, double fontSize) {

	double result = 0.0;

	if (charWidths == null) {
		loadFontMetrics();
	}

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
			int width = charWidths[font][c];
			pair = new Short((short)((previous << 8) + c));
			adjust = (Integer) kerningTable[font].get(pair);
			if (adjust != null) {
				System.out.println( "Adjusting " + (char)previous + (char)c + " by " + adjust);
				width += adjust.shortValue();
			}
			result += width;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println( "Text=" + text);
			System.out.println( "i=" + i + ", font=" + font + " char=" + text.charAt(i) + "(" + (int)(text.charAt(i)) + ")");
		}
	}
	return result * fontSize * 20 / 1000.0;
	
}
}
