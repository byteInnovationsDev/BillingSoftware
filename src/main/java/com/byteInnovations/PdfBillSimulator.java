package com.byteInnovations;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import com.byteInnovations.service.ReceiptBuilder;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.PdfWriter;

public class PdfBillSimulator {
	
	public static byte[] generatePdf(List<Map<String, String>> products, String total) throws Exception {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();

	    List<String> lines = ReceiptBuilder.buildReceiptLines(products, total);

	    int lineHeight = 20;
	    int estimatedHeight = lines.size() * lineHeight; // including margins
	    int maxHeight = 400; // max page height (adjust as needed)
	    
	    // If estimated height is large, reduce the growth to 90% scale
	    if (estimatedHeight > maxHeight * 0.9) {
	    	maxHeight = (int)(estimatedHeight * 0.8);
	    }

	    Rectangle customSize = new Rectangle(226, Math.min(estimatedHeight, maxHeight)); // width 80mm (226 units)
	    Document doc = new Document(customSize, 10, 10, 10, 10);

	    PdfWriter.getInstance(doc, baos);
	    doc.open();

	    Font font = FontFactory.getFont(FontFactory.COURIER, 9);

	    for (String line : lines) {
	        doc.add(new Paragraph(line, font));
	    }

	    doc.close();
	    return baos.toByteArray();
	}

    
}
/*
public static byte[] generatePdf(List<Map<String, String>> products, String total) throws Exception {
ByteArrayOutputStream baos = new ByteArrayOutputStream();

List<String> lines = ReceiptBuilder.buildReceiptLines(products, total);

int lineHeight = 22; // approx height per line
int dynamicHeight = (lines.size() * lineHeight); // margins + lines

Rectangle customSize = new Rectangle(226, dynamicHeight); // 80mm width
Document doc = new Document(customSize, 10, 10, 10, 10);

PdfWriter.getInstance(doc, baos);
doc.open();

Font font = FontFactory.getFont(FontFactory.COURIER, 9);	

for (String line : lines) {
    doc.add(new Paragraph(line, font));
}

doc.close();
return baos.toByteArray();
}
*/