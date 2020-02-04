/*
 * Created on 25-May-05
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package uk.co.ricky.xml;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Wrapper class to provide simplified access to an XML document.
 * Attempts to hide the tree based structure of DOM and provide a more intuitive interface.
 * It works as a wrapper to the DOM Element type and provides simple methods like
 * <UL><LI>textValue<LI>attributeValue<LI>getElement(s)</UL>
 * that take care of navigating the DOM tree for you.
 * It allows you to drill down an XML document e.g.
 * getRoot().getElement("xxx").getElement("yyy").textValue()
 * 
 * Creation date: (03/07/2002 15:49:59)
 * @author: McLaren Richard
 */
public class XMLElement {
	protected Element element;
	/**
	 * Load and parse an XML document using a reader and return the root element..
	 */
	public XMLElement(Reader reader) throws XMLElementException {
		super();

		try {
			DOMParser parser = new DOMParser();
			parser.parse(new InputSource(reader));
			Document document = parser.getDocument();
			element = document.getDocumentElement();

		} catch (Exception e) {
			throw new XMLElementException(e.getMessage());
		}

	}
	/**
	 * Parse an XML String and return the root element.
	 * Creation date: (03/07/2002 16:04:39)
	 * @param xml java.lang.String
	 */
	public XMLElement(String xml) throws XMLElementException {

		this(new StringReader(xml));

	}
	/**
	 * Create an XMLElement from a DOM Element.
	 * Used in drilling down the tree.
	 * Creation date: (03/07/2002 17:06:13)
	 */
	public XMLElement(Element element) {
		this.element = element;
	}
	/**
	 * Create a new document from scratch
	 * @param rootElement Name of the root element.
	 */
	public static XMLElement createDocument(String rootElement) throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		doc.appendChild(doc.createElement(rootElement));
		return new XMLElement(doc.getDocumentElement());
	}
	/**
	 * Add a new child element. 
	 * @return the new child element
	 */
	public XMLElement addElement(String name) {
		Element newElement = element.getOwnerDocument().createElement(name);
		element.appendChild(newElement);
		return new XMLElement(newElement);
	}
	/**
	 * Add a new child element with a text value
	 * @return this element
	 */
	public XMLElement addElement(String name, String value) {
		Element newElement = element.getOwnerDocument().createElement(name);
		element.appendChild(newElement);
		new XMLElement(newElement).setTextValue(value);
		return this;
	}

	
	
	public void removeElement(XMLElement oldElement) {
		 
		 
		 //NodeList nodes = element.getChildNodes();
		 
		// int x = nodes.getLength();

		 // for (int y = 0; )			 
		 
		 element.getParentNode().removeChild(oldElement.getDOMElement());
		 
	}




	/**
	 * Set an attribute
	 */
	public XMLElement setAttribute(String name, String value) {
		element.setAttribute(name, value);
		return this;
	}
	/**
	 * Set the text value of an element
	 */
	public XMLElement setTextValue(String value) {
		element.appendChild(element.getOwnerDocument().createTextNode(value));
		return this;
	}
	/**
	 * Import an element from another document as a child of this one
	 */
//	public XMLElement importElement(XMLElement fromElement) {
//		element.appendChild(element.getOwnerDocument().importNode(fromElement.getDOMElement(), true));
//		return this;
//	}
	/**
	 * Serialise a document to a stream
	 */
	public void toWriter(Writer writer) throws IOException {
		Document doc = element.getOwnerDocument();
		OutputFormat of = new OutputFormat(doc);
		XMLSerializer serial = new XMLSerializer(writer, of);
		serial.serialize(doc);
	}
	/**
	 * Return a named attribute of this element.
	 * Creation date: (03/07/2002 17:12:56)
	 * @return java.lang.String
	 * @param attribute java.lang.String
	 */
	public String attributeValue(String name) {

		String result = null;

		NamedNodeMap map = element.getAttributes();
		if (map.getLength() > 0) {
			for (int i = 0; i < map.getLength(); i++) {
				Node n = map.item(i);
				if (n.getNodeName().equals(name)) {
					result = n.getNodeValue();
					break;
				}
			}
		}

		return result;
	}
	/**
	 * Get the underlying DOM Element.
	 * Creation date: (03/07/2002 16:10:11)
	 * @return org.w3c.dom.Element
	 * @param name java.lang.String
	 */
	public Element getDOMElement() {

		return element;

	}
	/**
	 * Fetch a single named element below this one.
	 * Creation date: (03/07/2002 16:10:11)
	 * @return org.w3c.dom.Element
	 * @param name java.lang.String
	 */
	public XMLElement getElement(String name) throws XMLElementException {

		Element result = null;
		Vector elements = getDOMElements(element, name);
		if (elements.size() == 0) {
			throw new XMLElementException("Unable to find element <" + name + ">");
		} else if (elements.size() > 1) {
			throw new XMLElementException("Found more than one  element <" + name + ">");
		} else {
			result = (Element) elements.elementAt(0);
		}

		return new XMLElement(result);

	}
	/**
	* Fetch a single named element with a specific Attribute and Value.
	* Creation date: (03/07/2002 16:10:11)
	* @return org.w3c.dom.Element
	* @param name java.lang.String
	*/
	public XMLElement getElementWithAttribute(Element inElement, String attr, String attrVal) throws XMLElementException {

		System.out.println("Recursing in " + inElement.getNodeName());
		// vector now contains all the child nodes for that current element 
		Vector elements = getDOMElements(inElement);

		if (elements.size() == 0) {
			return null;
		}
		// this is v.v.v.v.v.v.v.v scary recursion dont look at this on monday after a heavy weekend
		for (int x = 0; x < elements.size(); x++) {
			Element e = (Element) elements.elementAt(x);
			// does the elememt have the attribute we want
			if (e.getAttribute(attr)!= null) {
				// does the attribute have the value we want
				if (e.getAttribute(attr).compareTo(attrVal) == 0) {
					return new XMLElement(e);
				}
			}
			// if no to both above then does it have any kids and if it does then recurse
			System.out.println("checking " + e.getNodeName());
			if (e.hasChildNodes()) {
				XMLElement XMLe = getElementWithAttribute(e, attr, attrVal);
				if (XMLe != null) {
					return XMLe;
				}
			}
		}

		return null;
	}


	/**
	* Fetch a single named element with a specific name without knowing where on the tree it is.
	* Creation date: (03/07/2002 16:10:11)
	* @return org.w3c.dom.Element
	* @param name java.lang.String
	*/

	public XMLElement getElementWithName(Element inElement, String name) throws XMLElementException {

		System.out.println("Recursing in " + inElement.getNodeName());
		// vector now contains all the child nodes for that current element 
		Vector elements = getDOMElements(inElement);

		if (elements.size() == 0) {
			return null;
		}
		// this is v.v.v.v.v.v.v.v scary recursion dont look at this on monday after a heavy weekend
		for (int x = 0; x < elements.size(); x++) {
			Element e = (Element) elements.elementAt(x);
			// does the elememt have the name we want
			if (e.getNodeName().equals(name)) {
				// does the attribute have the value we want
				return new XMLElement(e);
			}
			// if no to both above then does it have any kids and if it does then recurse
			System.out.println("checking " + e.getNodeName());
			if (e.hasChildNodes()) {
				XMLElement XMLe = getElementWithName(e, name);
				if (XMLe != null) {
					return XMLe;
				}
			}
		}

		return null;
	}
	/**
		 * Finds zero or more named elements below this one.
		 * Creation date: (03/07/2002 16:10:11)
		 * @return org.w3c.dom.Element
		 * @param name java.lang.String
		 */
	public Enumeration getElements(String name) throws XMLElementException {

		Vector elements = getDOMElements(element, name);
		Vector result = new Vector();

		for (int i = 0; i < elements.size(); i++) {
			result.addElement(new XMLElement((Element) elements.elementAt(i)));
		}

		return result.elements();

	}
	/**
	 * Finds zero or more named elements below this one.
	 * Creation date: (03/07/2002 16:10:11)
	 * @return org.w3c.dom.Element
	 * @param name java.lang.String
	 */
	public Enumeration getElements() throws XMLElementException {

		Vector elements = getDOMElements(element);
		Vector result = new Vector();

		for (int i = 0; i < elements.size(); i++) {
			result.addElement(new XMLElement((Element) elements.elementAt(i)));
		}

		return result.elements();

	}
	/**
	 * Returns the name of this element.
	 * Useful if you want to find the name of the root element.
	 * Creation date: (03/07/2002 17:27:58)
	 * @return java.lang.String
	 */
	public String getName() {
		return element.getNodeName();
	}
	/**
	 * Returns the root element.
	 * Creation date: (03/07/2002 16:14:58)
	 * @return org.w3c.dom.Element
	 */
	public XMLElement getRoot() {
		return new XMLElement(element.getOwnerDocument().getDocumentElement());
	}
	/**
	 * Returns the contents of this element. i.e. anything between the start and end tag that is not within a sub element.
	 * Creation date: (03/07/2002 17:12:21)
	 * @return java.lang.String
	 */
	public String textValue() {

		StringBuffer text = new StringBuffer();

		NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() == Node.TEXT_NODE) {
				text.append(nodes.item(i).getNodeValue());
			}
		}

		return text.toString();

	}
	/**
	 * Internal function to get all the named DOM elements immediately below this element.
	 * Returns a Vector which is slightly more useful than a DOM NodeList
	 * Creation date: (03/07/2002 16:42:13)
	 * @return java.util.Vector
	 * @param element org.w3c.dom.Element
	 * @param Name java.lang.String
	 */
	private Vector getDOMElements(Element element, String name) {
		Vector elements = new Vector();
		NodeList children = element.getChildNodes();

		// iterate over children
		for (int i = 0; i < children.getLength(); i++) {
			// add this child to the vector if it is an element of the desired name
			Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(name)) {
				elements.addElement((Element) node);
			}
		}

		// return the child elements
		return elements;
	}
	/**
	 * Internal function to get all the DOM elements immediately below this element.
	 * Returns a Vector which is slightly more useful than a DOM NodeList
	 * Creation date: (03/07/2002 16:42:13)
	 * @return java.util.Vector
	 * @param element org.w3c.dom.Element
	 * @param Name java.lang.String
	 */
	private Vector getDOMElements(Element element) {
		Vector elements = new Vector();
		NodeList children = element.getChildNodes();

		// iterate over children
		for (int i = 0; i < children.getLength(); i++) {
			// add this child to the vector if it is an element of the desired name
			Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				elements.addElement((Element) node);
			}
		}

		// return the child elements
		return elements;
	}
	/**
	 * Test Harness.
	 * Creation date: (03/07/2002 17:20:05)
	 * @param args java.lang.String[]
	 */
	public static void main(String[] args) {

		String xml = "<book title=\"XML Element Testing\">" + "Unusual " + "<author><name>Richard McLaren</name><dob>02/11/1962</dob></author>" + "but " + "<chapter number=\"1\">" + "<paragraph>This is chapter 1 paragraph 1</paragraph>" + "<paragraph>This is chapter 1 paragraph 2</paragraph>" + "</chapter>" + "apparently " + "<chapter number=\"2\">" + "<paragraph>This is chapter 2 paragraph 1</paragraph>" + "<paragraph>This is chapter 2 paragraph 2 &amp; it contains an ampersand</paragraph>" + "</chapter>" + "legal" + "</book>";

		try {
			XMLElement root = new XMLElement(xml);

			System.out.println(xml + "\n\n");

			System.out.println(root.getName());
			System.out.println("Title = " + root.attributeValue("title"));
			System.out.println("text value of root = " + root.textValue());

			System.out.println("author=" + root.getElement("author").getElement("name").textValue());
			System.out.println("dob=" + root.getElement("author").getElement("dob").textValue());

			Enumeration chapters = root.getElements("chapter");
			while (chapters.hasMoreElements()) {
				XMLElement chapter = (XMLElement) chapters.nextElement();
				System.out.println("Chapter " + chapter.attributeValue("number"));

				Enumeration paragraphs = chapter.getElements("paragraph");
				while (paragraphs.hasMoreElements()) {
					XMLElement paragraph = (XMLElement) paragraphs.nextElement();
					System.out.println(paragraph.textValue());
				}
			}

			StringWriter sw;
			sw = new StringWriter();
			root.toWriter(sw);
			System.out.println(sw.toString());

			System.out.println("\n#############################\n");

			XMLElement newDoc = createDocument("wrapvaluation");
			XMLElement clientDetails = newDoc.addElement("ifa").setAttribute("ifaid", "JAMESBROWN").setAttribute("organizationid", "11547").addElement("wrapclient").setAttribute("wrapreferenceno", "288").addElement("clientdetails").setAttribute("forename", "Jemima").setAttribute("surname", "Laidlaw").setAttribute("title", "Mrs").setAttribute("dateofbirth", "30/06/1952").setAttribute("email", "laidlawj@home.co.uk");

			clientDetails.addElement("address").addElement("line", "652 Mutley Plain").addElement("line", "Plymouth").addElement("line", "Devon");

			clientDetails.addElement("contacts").addElement("contact").addElement("description", "Day").addElement("number", "01752 698 741");

			clientDetails.getElement("contacts").addElement("contact").addElement("description", "Evening").addElement("number", "01752 698 742");

		//	clientDetails.importElement(root);

			sw = new StringWriter();
			newDoc.toWriter(sw);
			System.out.println(sw.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
