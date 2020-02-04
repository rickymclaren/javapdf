package uk.co.ricky.xml;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.apache.xerces.parsers.*;
/**
 * Insert the type's description here.
 * Creation date: (11/04/2002 09:06:41)
 * @author: McLaren Richard
 */
public class TreeDump {
	static int level;
/**
 * TreeDump constructor comment.
 */
public TreeDump() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (11/04/2002 09:31:42)
 * @param text java.lang.String
 */
public static void dump(String text) {
	for (int i=0;i<level;i++) {
		System.out.print("  ");
	}
	System.out.println( text );
}
/**
 * Insert the method's description here.
 * Creation date: (11/04/2002 09:16:10)
 * @param node org.w3c.dom.Element
 */
public static void dumpNode(Node node) {

	switch (node.getNodeType()) {
		case Node.DOCUMENT_NODE : {
			dump( "Document Node: " + node.getNodeName() );
			break;
		}
		case Node.ELEMENT_NODE : {
			dump( "Element Node: " + node.getNodeName() );
			
			StringBuffer sb = new StringBuffer();
			NamedNodeMap map = node.getAttributes();
			if (map.getLength() > 0) {
				for (int i=0;i<map.getLength(); i++) {
					Node n = map.item(i);
					sb.append("@" + n.getNodeName()  + "=" + n.getNodeValue() + " ");
				}
				dump(sb.toString());
			}


			break;
		}
		case Node.TEXT_NODE : {
			dump( "Text Node: " + node.getNodeValue() );
			break;
		}
		case Node.COMMENT_NODE : {
			dump( "Comment Node: " + node.getNodeValue() );
			break;
		}
		default : {
			dump( "Unknown Node: (" + node.getNodeType() + ") " + node.getNodeName() );
			break;
		}
	}

	if (node.hasChildNodes()) {
		level++;
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			dumpNode(nl.item(i));
		}
		level--;
	}
		
}
/**
 * Insert the method's description here.
 * Creation date: (11/04/2002 09:06:55)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	try {
		DOMParser dom = new DOMParser();
		//dom.setIncludeIgnorableWhitespace(true);
		InputSource is = new InputSource(new java.io.FileInputStream(args[0]));
		dom.parse(is);
		dumpNode( dom.getDocument().getDocumentElement() );
	} 	catch (Exception e) {
		e.printStackTrace();
	}
}
}
