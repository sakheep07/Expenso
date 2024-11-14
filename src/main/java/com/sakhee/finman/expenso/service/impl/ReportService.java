package com.sakhee.finman.expenso.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sakhee.finman.expenso.entity.Expense;
import com.sakhee.finman.expenso.entity.Income;
import com.sakhee.finman.expenso.entity.User;

@Service
public class ReportService {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ExpenseService expenseService;

    public Map<String, BigDecimal> getIncomeExpenseSummary(User user, LocalDate startDate, LocalDate endDate) {
        BigDecimal totalIncome = incomeService.calculateTotalIncome(user, startDate, endDate);
        BigDecimal totalExpenses = expenseService.calculateTotalExpenses(user, startDate, endDate);

        Map<String, BigDecimal> summary = new HashMap<>();
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpenses", totalExpenses);

        return summary;
    }

    public Map<String, BigDecimal> getCategoryBreakdown(User user, LocalDate startDate, LocalDate endDate) {
        return expenseService.calculateExpensesByCategory(user, startDate, endDate);
    }

    public Map<String, BigDecimal> getIncomeBySource(User user, LocalDate startDate, LocalDate endDate) {
        return incomeService.categorizeIncomeBySource(user, startDate, endDate);
    }

    public List<Income> getIncomeDetails(User user, LocalDate startDate, LocalDate endDate) {
        return incomeService.getIncomeDetails(user, startDate, endDate);
    }

    public List<Expense> getExpenseDetails(User user, LocalDate startDate, LocalDate endDate) {
        return expenseService.getExpenseDetails(user, startDate, endDate);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public byte[] generateIncomePieChart(User user, LocalDate startDate, LocalDate endDate) {
        Map<String, BigDecimal> incomeSources = getIncomeBySource(user, startDate, endDate);
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map.Entry<String, BigDecimal> entry : incomeSources.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart("Income Sources", dataset, true, true, false);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(out, chart, 500, 300); // Customize dimensions as needed
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating income pie chart", e);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public byte[] generateExpensePieChart(User user, LocalDate startDate, LocalDate endDate) {
        Map<String, BigDecimal> expenses = getCategoryBreakdown(user, startDate, endDate);
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map.Entry<String, BigDecimal> entry : expenses.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart("Expense Categories", dataset, true, true, false);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(out, chart, 500, 300); // Customize dimensions as needed
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating expense pie chart", e);
        }
    }


    public byte[] generatePdfReport(User user, LocalDate startDate, LocalDate endDate,
                                     List<Income> incomeDetails, List<Expense> expenseDetails) {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph("Income and Expense Summary Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            document.add(new Paragraph("Date Range: " + startDate + " to " + endDate));
            document.add(new Paragraph("\n"));

            // Income Details Table
            PdfPTable incomeTable = new PdfPTable(2);
            incomeTable.addCell("Income Source");
            incomeTable.addCell("Amount");

            for (Income income : incomeDetails) {
                incomeTable.addCell(income.getSource());
                incomeTable.addCell(income.getAmount().toString());
            }
            document.add(incomeTable);

            document.add(new Paragraph("\n"));

            // Expense Details Table
            PdfPTable expenseTable = new PdfPTable(2);
            expenseTable.addCell("Expense Category");
            expenseTable.addCell("Amount");

            for (Expense expense : expenseDetails) {
                expenseTable.addCell(expense.getCategory());
                expenseTable.addCell(expense.getAmount().toString());
            }
            document.add(expenseTable);
            
            document.add(new Paragraph("\n"));
            
            // Generate and add Income Pie Chart
            byte[] incomeChart = generateIncomePieChart(user, startDate, endDate);
            Image incomeImage = Image.getInstance(incomeChart);
            document.add(incomeImage);

            // Add space
            document.add(new Paragraph(" "));

            // Generate and add Expense Pie Chart
            byte[] expenseChart = generateExpensePieChart(user, startDate, endDate);
            Image expenseImage = Image.getInstance(expenseChart);
            document.add(expenseImage);

            document.close();
        }  catch (Exception e) {
            throw new RuntimeException("Error generating PDF report", e);
        }
        
        return outputStream.toByteArray();
    }
    

    public byte[] generateExcelReport(User user, LocalDate startDate, LocalDate endDate) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Report");

            // Report Title
            Row titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue("Income and Expense Report for " + user.getName());
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1)); // Merge cells for title

            // Income Section
            int rowIdx = 2; // Starting from the third row
            Row incomeHeader = sheet.createRow(rowIdx++);
            incomeHeader.createCell(0).setCellValue("Income Source");
            incomeHeader.createCell(1).setCellValue("Amount");

            Map<String, BigDecimal> incomeSources = getIncomeBySource(user, startDate, endDate);
            for (Map.Entry<String, BigDecimal> entry : incomeSources.entrySet()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue().doubleValue());
            }

            // Expense Section
            rowIdx += 2; // Add space before the expenses
            Row expenseHeader = sheet.createRow(rowIdx++);
            expenseHeader.createCell(0).setCellValue("Expense Category");
            expenseHeader.createCell(1).setCellValue("Amount");

            Map<String, BigDecimal> expenses = getCategoryBreakdown(user, startDate, endDate);
            for (Map.Entry<String, BigDecimal> entry : expenses.entrySet()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue().doubleValue());
            }

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel report", e);
        }
    }
}