package com.byteInnovations.service;

import java.util.List;
import java.util.Map;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.output.PrinterOutputStream;

public class EscPosBillPrinter {
    public static void printReceipt(List<Map<String, String>> products, String total) throws Exception {
        try (EscPos escpos = new EscPos(new PrinterOutputStream())) {
            Style style = new Style().setFontName(Style.FontName.Font_B)
                                     .setFontSize(Style.FontSize._1, Style.FontSize._1);
            List<String> lines = ReceiptBuilder.buildReceiptLines(products, total);
            for (String line : lines) {
                escpos.writeLF(style, line);
            }
            escpos.feed(2);
            escpos.cut(EscPos.CutMode.FULL);
        }
    }
}