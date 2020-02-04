package uk.co.ricky.pdf.facades;

/**
 * Insert the type's description here.
 * Creation date: (02/04/2002 15:37:47)
 * @author: McLaren Richard
 */
public class PdfServlet extends javax.servlet.http.HttpServlet {
/**
 * PdfServlet constructor comment.
 */
public PdfServlet() {
	super();
}
/**
 * service method comment.
 */
protected void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws javax.servlet.ServletException, java.io.IOException {

	String text = "Our new Board structure, which I announced earlier this month, is in line with our vision of the Group's earnings being split broadly between three major value centres: Retail Banking, Wholesale Banking, and Wealth Management and Long-Term Savings.<p>Looking beyond this year, we have the customer franchises and platforms across each of these three divisions to deliver good organic growth and stable, high quality earnings.";
	resp.setContentType("text/html");

	java.io.PrintWriter pw = resp.getWriter();

	pw.println( "<HTML>" );
	pw.println( "<BODY>" );
	pw.println( "<FORM method=POST target=blank>" );
	pw.println( "<TEXTAREA name=text cols=80 rows=10>" + text + "</TEXTAREA>" );
	pw.println( "<INPUT type=SUBMIT value=Go>" );
	pw.println( "</FORM>" );
	pw.println( "</HTML>" );
		
}
/**
 * service method comment.
 */
protected void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws javax.servlet.ServletException, java.io.IOException {

	resp.setContentType("application/pdf");
	long start = System.currentTimeMillis();
	long end = 0;
	
	try {
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		PdfWriter pw = new PdfWriter(baos);

		String text = req.getParameter("text");
		int i = -1;
		if (text != null) {
			while ( (i = text.indexOf("<p>")) != -1) {
				pw.printParagraph( text.substring(0, i) );
				pw.println("");
				text = text.substring(i + 3);
			}
			pw.printParagraph( text );
			pw.println("");
		}

		pw.close();
		end = System.currentTimeMillis();
		System.out.println( "Elapsed time=" + (end-start) );
		
		resp.setContentLength(baos.size());
		resp.getOutputStream().write(baos.toByteArray());
		
	} catch (uk.co.ricky.pdf.PdfException e) {
		e.printStackTrace(new java.io.PrintWriter(resp.getOutputStream()));
	}
		
}
}
