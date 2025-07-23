package com.byteInnovations.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReceiptBuilder {

	 public static List<String> buildReceiptLines(List<Map<String, String>> products, String total) {
	    List<String> lines = new ArrayList<>();
	    
	    lines.add(center(" Waffle N'Fries", 38));
	    lines.add(center("3, Thambiah Rd, Gokulam Colony,", 38));
	    lines.add(center("Ramakrishnapuram, West Mambalam,", 38));
	    lines.add(center("Chennai - 600033", 38));

	    lines.add("--------------------------------------");
	    String header = String.format("%-25s %-3s %6s", center("ITEM", 17), "QTY", "AMOUNT");
	    lines.add(header);
	    lines.add(" ");
	    int nameMaxLength = 25;
	    for (Map<String, String> item : products) {
	        String name = item.get("product");
	        String qty = item.get("quantity");
	        String price = item.get("price");
	        List<String> nameParts = splitText(name, nameMaxLength);

	        String formattedPrice = (price.length() < 6 ? " ₹" : "₹") + price;
	        String firstLine = String.format("%-26s %-2s %6s", nameParts.get(0), qty, formattedPrice);

	        lines.add(firstLine);

	        for (int i = 1; i < nameParts.size(); i++) {
	            lines.add(nameParts.get(i));
	        }
	    }
	    lines.add(" ");
	    lines.add("--------------------------------------");
	    
	   
	    	//lines.add(padRight("TOTAL", 26) + "Rs."+total);
	    	//lines.add("TOTAL                     "+ "Rs."+total);
	    	String priceString = "Rs." + total;
	    	int totalLineLength = 36; // or any fixed width you want the line to be
	    	int leftTextLength = totalLineLength - priceString.length();

	    	lines.add(padRight("TOTAL", leftTextLength) + priceString);
	    
	    lines.add(" ");	
	    lines.add(" ");	
	    lines.add(" ");	
	    lines.add(center("Thank you for shopping!", 40));

	    return lines;
	}
	
	/*
	 * private static String formatPrice(String price) { // Add one space before ₹
	 * if the price is less than 100 to keep alignment consistent return
	 * (price.length() < 6 ? " " : ") + price; }
	 */


	// Helper method to center-align text
	 public static String center(String text, int width) {
		    int padSize = width - text.length();
		    int padStart = padSize / 2;
		    int padEnd = padSize - padStart;
		    return " ".repeat(Math.max(0, padStart)) + text + " ".repeat(Math.max(0, padEnd));
		}

	// Helper method to pad text to the right
	private static String padRight(String text, int length) {
	    return String.format("%-" + length + "s", text);
	}

	// Helper method to split long product names into chunks
	private static List<String> splitText(String text, int maxLength) {
	    List<String> parts = new ArrayList<>();
	    String[] words = text.split(" ");
	    StringBuilder line = new StringBuilder();

	    for (String word : words) {
	        if (line.length() + word.length() + 1 > maxLength) {
	            parts.add(line.toString());
	            line = new StringBuilder();
	        }
	        if (line.length() > 0) line.append(" ");
	        line.append(word);
	    }

	    if (line.length() > 0) {
	        parts.add(line.toString());
	    }

	    return parts;
	}

	
	
    
}
/*public static List<String> buildReceiptLines(List<Map<String, String>> products, String total) {
List<String> lines = new ArrayList<String>();

lines.add(center("BYTE INNOVATIONS"));
lines.add("--------------------------------------");

for (Map<String, String> item : products) {
    String name = item.get("product");
    String qty = item.get("quantity");
    String price = item.get("price");

    // Align: name left, qty + price right
    String line = String.format("%-25s %-2s ₹%-6s", name, qty, price);
    lines.add(line);
}

lines.add("--------------------------------------");
lines.add(padRight("TOTAL:", 25) + "₹" + total);
lines.add(" ");
lines.add(" ");

lines.add(center("Thank you for shopping!"));

return lines;
}

private static String center(String text) {
int width = 32; // characters per line
int padSize = (width - text.length()) / 2;
return " ".repeat(Math.max(0, padSize)) + text;
}

private static String padRight(String text, int length) {
return String.format("%-" + length + "s", text);
}*/