package com.example.mypc.uco;

import com.github.barteksc.pdfviewer.PDFView;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

class ThriftInterestCertificate {
    public void createPDF(JSONArray jsonArray, PDFView pdfView, File file, String dest, ImageData imageData, String nameStr, String memberNoStr, String branch_name, String zone, String fromYearStr, String toYearStr) throws IOException {
        PdfWriter writer = null;
        try {
            writer = new PdfWriter(dest);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Creating a PdfDocument
        PdfDocument pdf = new PdfDocument(writer);

        // Creating a Document
        Document document = new Document(pdf);

        // Creating an Image object
        Image image = new Image(imageData);

        // Setting the position of the image to the center of the page
        image.setFixedPosition(247, 705);

        // Scaling the image
        image.scale(0.08f,0.08f);

        // Adding image to the document
        document.add(image);

        // Creating Paragraphs
        PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

        // Title
        String titleStr = "UCO BANK";
        Paragraph title = new Paragraph(titleStr);
        title.setFont(bold);
        title.setFixedPosition(260, 680, 400);

        // Heading
        String headingStr = "UCO BANK EMPLOYEES' CO-OP THRIFT & CREDIT SOCIETY LTD., MSCS/CR/42/94";
        Paragraph heading = new Paragraph(headingStr);
        heading.setFont(bold);
        heading.setFixedPosition(70, 660, 500);

        // SubHeading
        String subHeadingStr = "NO.328, THAMBU CHETTY STREET, CHENNAI 600 001, PHONE : 044-25331230 ";
        Paragraph subHeading = new Paragraph(subHeadingStr);
        subHeading.setFont(bold);
        subHeading.setFixedPosition(80, 640, 500);

        // Title2
        String title2Str = "THRIFT INTEREST CERTIFICATE";
        Paragraph title2 = new Paragraph(title2Str);
        title2.setFont(bold);
        title2.setUnderline(1.5f, -3f);
        title2.setFixedPosition(210, 600, 500);

        // Date
        String dateInString =new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String dateStr = "Date : " + dateInString;
        Paragraph date = new Paragraph(dateStr);
        date.setFixedPosition(450, 580, 100);

        // MainParagraph
        Text name = new Text(nameStr).setFont(bold);

        Text memberNo = new Text(memberNoStr).setFont(bold);

        Text fromYear = new Text(fromYearStr).setFont(bold);

        Text toYear = new Text(toYearStr).setFont(bold);

        String mainParagraphStr = "The following is the thrift interest for Shri/Smt. ";

        String para2Str = " Member No. ";
        Text para2 = new Text(para2Str);

        String para3Str;
        if(zone.isEmpty()) para3Str = ", UCO BANK, " + branch_name;
        else para3Str = ", UCO BANK, " + branch_name + " " + zone;
        Text para3 = new Text(para3Str);

        String para4Str = ", for the financial year April ";
        Text para4 = new Text(para4Str);

        String para5Str = " to March ";
        Text para5 = new Text(para5Str);

        String para6Str = ".";
        Text para6 = new Text(para6Str);

        Paragraph mainParagraph = new Paragraph(mainParagraphStr);
        mainParagraph.setFontSize(12f);
        mainParagraph.setFirstLineIndent(20f);
        mainParagraph.setTextAlignment(TextAlignment.JUSTIFIED);
        mainParagraph.setFixedPosition(60, 520, 500);

        mainParagraph.add(name)
                .add(para2)
                .add(memberNo)
                .add(para3)
                .add(para4)
                .add(fromYear)
                .add(para5)
                .add(toYear)
                .add(para6);

        // Creating a table
        float [] pointColumnWidths = {150F, 150F, 150F, 150F, 150F};
        Table table = new Table(pointColumnWidths);
        table.setFixedPosition(60, 450, 500);

        // Adding cells to the table
        table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setFont(bold).add("Member No."));
        table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setFont(bold).add("Member Name"));
        table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setFont(bold).add("Date"));
        table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setFont(bold).add("Account No."));
        table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setFont(bold).add("Interest Amount"));

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(memberNoStr).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(nameStr).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add("31-03-" + toYearStr).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(jsonObject.get("accountNo").toString()).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(jsonObject.get("interestAmount").toString()).setTextAlignment(TextAlignment.CENTER));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        // Signature
        String signatureStr = "This is a system generated certificate and needs no signature.";
        Paragraph signature = new Paragraph(signatureStr);
        signature.setFixedPosition(140, 410, 400);

        // Adding paragraphs to document
        document.add(title);
        document.add(heading);
        document.add(subHeading);
        document.add(title2);
        document.add(date);
        document.add(mainParagraph);
        document.add(table);
        document.add(signature);

        // Closing the document
        document.close();

        pdfView.fromFile(file).load();
    }
}