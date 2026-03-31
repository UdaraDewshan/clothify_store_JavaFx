package util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.tm.CartTm;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PdfGenerator {

    public static void generateBill(String orderId, List<CartTm> cartList, double subTotal, double discount, double netTotal) {
        String folderPath = "bills";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        String filePath = folderPath + "/" + orderId + ".pdf";

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.BLACK);
            Paragraph title = new Paragraph("CLOTHIFY POS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Font subFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.GRAY);
            Paragraph subTitle = new Paragraph("No 123, Galle Road, Colombo 03\nTel: 011 234 5678\n\n", subFont);
            subTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subTitle);

            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = formatter.format(new Date());

            document.add(new Paragraph("Order ID : " + orderId, normalFont));
            document.add(new Paragraph("Date       : " + date, normalFont));
            document.add(new Paragraph("-----------------------------------------------------------------------------------------"));
            document.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4f, 1.5f, 2f, 2.5f});

            // Table Headers
            String[] headers = {"Item Name", "Qty", "Unit Price", "Total (LKR)"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setPadding(8);
                table.addCell(cell);
            }

            for (CartTm tm : cartList) {
                table.addCell(new PdfPCell(new Phrase(tm.getName())));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(tm.getQty()))));
                table.addCell(new PdfPCell(new Phrase(String.format("%.2f", tm.getUnitPrice()))));
                table.addCell(new PdfPCell(new Phrase(String.format("%.2f", tm.getTotal()))));
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Paragraph totals = new Paragraph();
            totals.setAlignment(Element.ALIGN_RIGHT);
            totals.add("Sub Total : LKR " + String.format("%.2f", subTotal) + "\n");
            totals.add("Discount (5%) : LKR " + String.format("%.2f", discount) + "\n");
            totals.add(new Chunk("Net Total : LKR " + String.format("%.2f", netTotal), boldFont));

            document.add(totals);

            document.add(new Paragraph("\n-----------------------------------------------------------------------------------------"));
            Paragraph thankYou = new Paragraph("Thank you for shopping with us!", normalFont);
            thankYou.setAlignment(Element.ALIGN_CENTER);
            document.add(thankYou);

            document.close();

            File pdfFile = new File(filePath);
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}