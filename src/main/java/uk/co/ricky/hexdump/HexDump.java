package uk.co.ricky.hexdump;

import java.util.*;
import java.io.*;
import java.text.*;

/**
 * Insert the type's description here.
 * Creation date: (18/02/2002 16:29:20)
 * @author: McLaren Richard
 */
public class HexDump {
/**
 * HexDump constructor comment.
 */
public HexDump() {
	super();
}
/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void main(java.lang.String[] args) {

	try {
		sun.misc.HexDumpEncoder hde = new sun.misc.HexDumpEncoder();

		File f = new File(args[0]);
		FileInputStream fis = new FileInputStream(f);
		byte[] buffer = new byte[(int)f.length()];

		FileWriter fw = new FileWriter(args[1]);

		int length;
		while ((length = fis.read(buffer)) != -1) {
			fw.write( hde.encodeBuffer(buffer) );
		}

		fis.close();
		fw.close();

	} catch (Exception e) {
		e.printStackTrace();
	}
		
}
}
