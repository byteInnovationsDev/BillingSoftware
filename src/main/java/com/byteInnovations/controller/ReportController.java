package com.byteInnovations.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.byteInnovations.model.OrderClass;
import com.byteInnovations.model.Purchase;
import com.byteInnovations.model.Report;
import com.byteInnovations.service.PurchaseServiceImpl;
import com.byteInnovations.service.ReportService;

@Controller
public class ReportController {

	@Autowired
	private PurchaseServiceImpl purchaseSer;
	@Autowired
	private ReportService ser;

	@GetMapping("/reports")
	public String home() {

		return "report";
	}

	@GetMapping("/genReports")
	public ResponseEntity<byte[]> exportExcel(@RequestParam(required = false) String reporttype,
			@RequestParam(required = false) LocalDate fromdate, @RequestParam(required = false) LocalDate todate) {

		List<Report> reportHeader = ser.findByDesc(reporttype);
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.createSheet("Report");
			Row dateHeaderRow = sheet.createRow(0);
			int numColumns = reportHeader.size();
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, numColumns - 1));
			Cell headerCell = dateHeaderRow.createCell(0);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
			String dateRange = "From: " + (fromdate != null ? fromdate.format(formatter) : "N/A") + " To: "
					+ (todate != null ? todate.format(formatter) : "N/A");
			/*
			 * if (!("PRODUCT WISE REPORT".equalsIgnoreCase(reporttype))) {
			 * headerCell.setCellValue(dateRange); headerCell.setCellStyle(dateHeaderStyle);
			 * }
			 */
			// Apply cell style for centering text
			CellStyle dateHeaderStyle = workbook.createCellStyle();
			dateHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
			dateHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			dateHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			dateHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			
			dateHeaderRow.setHeightInPoints(50);

			Font headerFont = workbook.createFont();
			headerFont.setFontHeightInPoints((short) 14);
			headerFont.setBold(true);
			dateHeaderStyle.setFont(headerFont);

			// Create header style
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setBorderTop(BorderStyle.THIN);
			headerStyle.setBorderBottom(BorderStyle.THIN);
			headerStyle.setBorderLeft(BorderStyle.THIN);
			headerStyle.setBorderRight(BorderStyle.THIN);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			/*
			 * Font headerFont1 = workbook.createFont();
			 * headerFont.setFontHeightInPoints((short) 12); headerFont.setBold(true);
			 */
			headerStyle.setFont(headerFont);

			// Data Row
			CellStyle dataCellStyle = workbook.createCellStyle();
			dataCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			dataCellStyle.setBorderTop(BorderStyle.THIN);
			dataCellStyle.setBorderBottom(BorderStyle.THIN);
			dataCellStyle.setBorderLeft(BorderStyle.THIN);
			dataCellStyle.setBorderRight(BorderStyle.THIN);

			Font boldFont = workbook.createFont();
			boldFont.setFontHeightInPoints((short) 13);
			boldFont.setBold(true);
			dataCellStyle.setFont(boldFont);


			// Total price
			CellStyle headerStyle1 = workbook.createCellStyle();
			headerStyle1.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
			headerStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle1.setBorderTop(BorderStyle.THIN);
			headerStyle1.setBorderBottom(BorderStyle.THIN);
			headerStyle1.setBorderLeft(BorderStyle.THIN);
			headerStyle1.setBorderRight(BorderStyle.THIN);
			headerStyle1.setAlignment(HorizontalAlignment.RIGHT);
			headerStyle1.setVerticalAlignment(VerticalAlignment.CENTER);

			// Create header row
			Row headerRow = sheet.createRow(1);
			headerRow.setHeightInPoints(25);
			if (!("PRODUCT WISE REPORT".equalsIgnoreCase(reporttype))) {
				//headerCell.setCellStyle(dateHeaderStyle);
				headerCell.setCellValue(dateRange);
				headerCell.setCellStyle(dateHeaderStyle);
				for (int i = 0; i < reportHeader.size(); i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(reportHeader.get(i).getName());
					cell.setCellStyle(headerStyle);
					sheet.autoSizeColumn(i);
				}
			}
			// Data Rows
			int rowIdx = 2;
			BigDecimal total = BigDecimal.ZERO;
			if ("SALES REPORT".equalsIgnoreCase(reporttype)) {
				List<OrderClass> reportList = ser.findOrdersBetweenDates(fromdate, todate);
				for (OrderClass order : reportList) {
					Row row = sheet.createRow(rowIdx++);

					Cell c0 = row.createCell(0);
					c0.setCellValue(order.getOrderId());
					c0.setCellStyle(dataCellStyle);

					Cell c1 = row.createCell(1);
					c1.setCellValue(order.getProductName());
					c1.setCellStyle(dataCellStyle);
					
					sheet.setColumnWidth(2, 4500);
					Cell c2 = row.createCell(2);
					c2.setCellValue(order.getCategory());
					c2.setCellStyle(dataCellStyle);

					Cell c3 = row.createCell(3);
					c3.setCellValue(order.getSubCategory());
					c3.setCellStyle(dataCellStyle);

					Cell c4 = row.createCell(4);
					c4.setCellValue(order.getCustomerName());
					c4.setCellStyle(dataCellStyle);

					Cell c5 = row.createCell(5);
					c5.setCellValue(order.getCustomerPhoneNumber());
					c5.setCellStyle(dataCellStyle);

					Cell c6 = row.createCell(6);
					c6.setCellValue(order.getOrderType());
					c6.setCellStyle(dataCellStyle);

					Cell c7 = row.createCell(7);
					c7.setCellValue(order.getPaymentType());
					c7.setCellStyle(dataCellStyle);

					Cell c8 = row.createCell(8);
					c8.setCellValue(order.getOrderDate().format(formatter));
					c8.setCellStyle(dataCellStyle);
					sheet.setColumnWidth(9, 2500);
					Cell c9 = row.createCell(9);
					c9.setCellValue(order.getQuantity());
					c9.setCellStyle(dataCellStyle);
					sheet.setColumnWidth(10, 00);
					Cell c10 = row.createCell(10);
					c10.setCellValue(order.getPrice().doubleValue());
					c10.setCellStyle(dataCellStyle);

					total = total.add(order.getPrice());
					System.out.println("======================"+total);
					row.setHeightInPoints(25);

				}

				rowIdx += 1;
				Row row = sheet.createRow(rowIdx);
				Cell cell = row.createCell(9);
				cell.setCellValue("TOTAL ");
				cell.setCellStyle(headerStyle);
				Cell cell1 = row.createCell(10);
				sheet.setColumnWidth(10, 4500);
				cell1.setCellValue("RS. "+total.doubleValue());
				cell1.setCellStyle(headerStyle);
				row.setHeightInPoints(20);

			} else if ("PRODUCT WISE REPORT".equalsIgnoreCase(reporttype)) {

				YearMonth startMonth = YearMonth.from(fromdate);
				YearMonth endMonth = YearMonth.from(todate);
				YearMonth currentMonth = startMonth;

				while (!currentMonth.isAfter(endMonth)) {
					LocalDate monthStart = currentMonth.atDay(1);
					LocalDate monthEnd = currentMonth.atEndOfMonth();

					// Add month title row
					Row monthTitleRow = sheet.createRow(rowIdx++);
					Cell monthTitleCell = monthTitleRow.createCell(0);
					monthTitleCell
							.setCellValue("Month: " + currentMonth.getMonth().name() + " " + currentMonth.getYear());
					monthTitleCell.setCellStyle(headerStyle); // Optional
					sheet.addMergedRegion(new CellRangeAddress(rowIdx - 1, rowIdx - 1, 0, 4));
					monthTitleRow.setHeightInPoints(25);
					
					// Fetch data for the current month
					List<OrderClass> reportList = ser.findOrdersBetweenDatesGroupByProduct(monthStart, monthEnd);
					total = BigDecimal.ZERO;
					Row headerRow1 = sheet.createRow(rowIdx++);
					for (int i = 0; i < reportHeader.size(); i++) {
						Cell cell = headerRow1.createCell(i);
						cell.setCellValue(reportHeader.get(i).getName());
						cell.setCellStyle(headerStyle);
						sheet.autoSizeColumn(i);
						headerRow1.setHeightInPoints(25);
					}
					
					for (OrderClass order : reportList) {
						Row row = sheet.createRow(rowIdx++);
						
						sheet.setColumnWidth(0, 11000);
						Cell cell0 = row.createCell(0);
						cell0.setCellValue(order.getProductName());
						cell0.setCellStyle(dataCellStyle);
						
						sheet.setColumnWidth(1, 5000);
						Cell cell1 = row.createCell(1);
						cell1.setCellValue(order.getCategory());
						cell1.setCellStyle(dataCellStyle);
						
						sheet.setColumnWidth(2, 8000);
						Cell cell2 = row.createCell(2);
						cell2.setCellValue(order.getSubCategory());
						cell2.setCellStyle(dataCellStyle);

						Cell cell3 = row.createCell(3);
						cell3.setCellValue(order.getQuantity());
						cell3.setCellStyle(dataCellStyle);
						
						sheet.setColumnWidth(4, 5000);
						Cell cell4 = row.createCell(4);
						cell4.setCellValue(order.getPrice().doubleValue());
						cell4.setCellStyle(dataCellStyle);

						total = total.add(order.getPrice());
						row.setHeightInPoints(25);

					}
					rowIdx+=1;
					// Total row
					Row totalRow = sheet.createRow(rowIdx++);
					Cell totalLabelCell = totalRow.createCell(3);
					totalLabelCell.setCellValue("TOTAL");
					totalLabelCell.setCellStyle(headerStyle);
					
					Cell totalValueCell = totalRow.createCell(4);
					totalValueCell.setCellValue("RS. "+total.doubleValue());
					totalValueCell.setCellStyle(headerStyle);

					// Space before next table
					rowIdx+=3;

					// Move to next month
					currentMonth = currentMonth.plusMonths(1);
				}

			} else if ("EXPENSE REPORT".equalsIgnoreCase(reporttype)) {
				List<Purchase> purchaseList = ser.findPurcheasesBetweenDates(fromdate, todate);
				int cost = 0;
				for (Purchase purchase : purchaseList) {
					Row row = sheet.createRow(rowIdx++);

					Cell cell0 = row.createCell(0);
					cell0.setCellValue(purchase.getProduct());
					cell0.setCellStyle(dataCellStyle);

					Cell cell1 = row.createCell(1);
					cell1.setCellValue(purchase.getQuantity());
					cell1.setCellStyle(dataCellStyle);

					sheet.setColumnWidth(2, 5000);
					Cell cell2 = row.createCell(2);
					cell2.setCellValue(purchase.getPurchaseDate().format(formatter)); // e.g., "16-Jun-2025"
					cell2.setCellStyle(dataCellStyle);
					sheet.setColumnWidth(3, 3000);
					Cell cell3 = row.createCell(3);
					cell3.setCellValue(purchase.getCost());
					cell3.setCellStyle(dataCellStyle);
					System.out.println(purchase.getPurchaseDate().format(formatter));
					cost += purchase.getCost();

					row.setHeightInPoints(25);

				}

				rowIdx += 1;
				Row row = sheet.createRow(rowIdx);
				Cell cell = row.createCell(2);
				cell.setCellValue("TOTAL");
				cell.setCellStyle(headerStyle);
				Cell cell1 = row.createCell(3);
				cell1.setCellValue("RS. "+cost);
				cell1.setCellStyle(headerStyle);
				row.setHeightInPoints(25);
			}

			workbook.write(out);

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx")
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(out.toByteArray());

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}

	/*
	 * List<OrderClass> reportList =
	 * ser.findOrdersBetweenDatesGroupByProduct(fromdate, todate);
	 * 
	 * for (OrderClass order : reportList) { Row row = sheet.createRow(rowIdx++);
	 * row.createCell(0).setCellValue(order.getProductName());
	 * row.createCell(1).setCellValue(order.getCategory());
	 * row.createCell(2).setCellValue(order.getSubCategory());
	 * row.createCell(3).setCellValue(order.getQuantity());
	 * row.createCell(4).setCellValue(order.getPrice().doubleValue()); total =
	 * total.add(order.getPrice()); row.setHeightInPoints(20); } rowIdx += 1; Row
	 * row = sheet.createRow(rowIdx); Cell cell = row.createCell(3);
	 * cell.setCellValue("TOTAL"); cell.setCellStyle(headerStyle); Cell cell1 =
	 * row.createCell(4); cell1.setCellValue(total.doubleValue());
	 * cell1.setCellStyle(headerStyle1);
	 * 
	 */

}
