package uk.co.ricky.pdf.old;

/** 
 * Insert the type's description here.
 * Creation date: (15/02/02 01:15:42)
 * @author: Administrator
 */
public class Test { 

	
/**
 * Test constructor comment.
 */
public Test() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (15/02/02 01:16:43)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {

	long start = System.currentTimeMillis();
	
	String paragraph = "Our new Board structure, which I announced earlier this month, is in line with our vision of the Group's earnings being split broadly between three major value centres: Retail Banking, Wholesale Banking, and Wealth Management and Long-Term Savings. Looking beyond this year, we have the customer franchises and platforms across each of these three divisions to deliver good organic growth and stable, high quality earnings.";
	
	try {
		Printer p = new Printer();

		p.setFont(12, false, false);
		
		int y = (int)p.getCurrentY();
		
		p.outputTextOld(paragraph);  
		p.print("", true);

		p.drawBox(0, y, (int)(p.getScaleWidth() - p.getLeftMargin() - p.getRightMargin()), (int)p.getCurrentY() - y);

		
		p.print("", true);
		p.print("", true);
		
		String s = paragraph.substring(0, 10);
		p.print( s, true );
		p.outputText( s );
		p.print( "", true );
		for (int i = 0; i < s.length(); i++){
			p.print( new Character(s.charAt(i )).toString());
		}
		p.print("", true);

		String pdf = p.endDoc();

		java.io.FileWriter fw = new java.io.FileWriter("C:/ricky/test.pdf");
		fw.write(pdf);
		fw.close();
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		long end = System.currentTimeMillis();
		System.out.println( "Elapsed time = " + (end - start) + " ms");
		System.exit(0);
	}
}
}
