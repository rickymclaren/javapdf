package uk.co.ricky.pdf;

/** 
 * Insert the type's description here.
 * Creation date: (15/02/02 01:15:42)
 * @author: Administrator
 */
class Test { 

	
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
		
		PdfDocument doc = new PdfDocument();

		doc.getResources().addFont("F1", PdfFont.HELVETICA);
		doc.getResources().addFont("F2", PdfFont.HELVETICA_BOLD_OBLIQUE);
		//doc.getResources().addImage("java", "D:/ricky/javalogo52x88.gif");
		doc.getResources().addImage("abbey", "D:/ricky/abbey.gif");

		PdfPage page = doc.getCurrentPage();

		page.setFont(doc.getResources().getFont("F1"));
		page.setFontSize(12);
		
		page.setColour(1, 0, 0);
		page.println( "Red" );
		page.setColour(0, 1, 0);
		page.println( "Green" );
		page.setColour(0, 0, 1);
		page.println( "Blue" );

		page.drawBox(0, 0, 72, page.getCurrentY());
		page.drawLine(0, 0, 72, page.getCurrentY());

		page.setFont(doc.getResources().getFont("F2"));
		page.setFontSize(20);
		page.println( "F2 20 point" );

		page.print("text1");
		page.println("text2");

		page.printParagraph(paragraph);
		
		//page.drawImage("java", 72, 72*4);
		//page.drawImage("java", 72*3, 72*4, 2);
		page.drawImage("abbey", 72, 72*7);
		page.drawImage("abbey", 72*5, 72*7, 0.5);

		java.io.FileOutputStream fos = new java.io.FileOutputStream("D:/ricky/test.pdf");
		fos.write(doc.toPdf());
		fos.close();
		
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		long end = System.currentTimeMillis();
		System.out.println( "Elapsed time = " + (end - start) + " ms");
		System.exit(0);
	}
}
}
