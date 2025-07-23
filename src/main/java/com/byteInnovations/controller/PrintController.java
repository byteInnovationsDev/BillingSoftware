package com.byteInnovations.controller;


import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.byteInnovations.PdfBillSimulator;
import com.byteInnovations.service.EscPosBillPrinter;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/print")
public class PrintController {

    @PostMapping("/bill")
    public ResponseEntity<String> printBill(@RequestBody Map<String, Object> payload) {
        try {
            List<Map<String, String>> products = (List<Map<String, String>>) payload.get("products");
            String total = String.valueOf(payload.get("total"));

            EscPosBillPrinter.printReceipt(products, total);
            return ResponseEntity.ok("Printed Successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Print failed: " + e.getMessage());
        }
    }

    @PostMapping("/bill/preview")
    public ResponseEntity<byte[]> previewBill(@RequestBody Map<String, Object> payload) {
        try {
            List<Map<String, String>> products = (List<Map<String, String>>) payload.get("products");
            String total = String.valueOf(payload.get("total"));

            byte[] pdfBytes = PdfBillSimulator.generatePdf(products, total);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header("Content-Disposition", "inline; filename=receipt.pdf")
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
