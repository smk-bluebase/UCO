package com.example.mypc.uco;

import com.github.barteksc.pdfviewer.PDFView;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoanInterestCertificate {
    public void createPDF (PDFView pdfView, File file, String dest, ImageData imageData, String nameStr, String memberNoStr, String moneyStr, String fromDateStr, String toDateStr, String interestRecoveredNo, String interestToBeRecoveredNo) throws IOException {
        // Creating a PdfWriter
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
        String title2Str = "INTEREST CERTIFICATE";
        Paragraph title2 = new Paragraph(title2Str);
        title2.setFont(bold);
        title2.setUnderline(1.5f, -3f);
        title2.setFixedPosition(230, 600, 500);

        // Date
        String dateInString = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String dateStr = "Date : " + dateInString;
        Paragraph date = new Paragraph(dateStr);
        date.setFixedPosition(450, 580, 100);

        // MainParagraph
        Text name = new Text(nameStr).setFont(bold);

        Text memberNo = new Text(memberNoStr).setFont(bold);

        Text money = new Text(moneyStr).setFont(bold);

        NumberToWords numToWords = new NumberToWords();

        String moneyInWordsStr = numToWords.numberToWord(Integer.parseInt(moneyStr)).toUpperCase();
        Text moneyInWords = new Text(moneyInWordsStr).setFont(bold);

        Text fromDate = new Text(fromDateStr).setFont(bold);

        Text toDate = new Text(toDateStr).setFont(bold);

        String mainParagraphStr = "Certified that Shri/Smt. ";

        Paragraph mainParagraph = new Paragraph(mainParagraphStr);
        mainParagraph.setFontSize(12f);
        mainParagraph.setFirstLineIndent(20f);
        mainParagraph.setTextAlignment(TextAlignment.JUSTIFIED);
        mainParagraph.setFixedPosition(60, 460, 500);

        String para2Str = " Mem.No. ";
        Text para2 = new Text(para2Str);

        String para3Str = " UCO BANK, PURASAWALKAM CHENNAI, has been charged a sum of Rs. ";
        Text para3 = new Text(para3Str);

        String para4Str = " ( Rupees ";
        Text para4 = new Text(para4Str);

        String para5Str = " ONLY ) towards interest amount during the period from ";
        Text para5 = new Text(para5Str);

        String para6Str = " to ";
        Text para6 = new Text(para6Str);

        String para7Str = ". The reason for availing the loan mentioned in his/her loan application is for the purpose of dwellings / completion of house construction.";
        Text para7 = new Text(para7Str);

        mainParagraph.add(name)
                .add(para2)
                .add(memberNo)
                .add(para3)
                .add(money)
                .add(para4)
                .add(moneyInWords)
                .add(para5)
                .add(fromDate)
                .add(para6)
                .add(toDate)
                .add(para7);

        // Interest Recovered
        String interestRecoveredStr = "Interest Recovered : ";
        Paragraph interestRecoveredPara = new Paragraph(interestRecoveredStr);
        interestRecoveredPara.setFont(bold);
        interestRecoveredPara.setFixedPosition(60, 420, 200);

        Paragraph interestRecovered = new Paragraph(interestRecoveredNo);
        interestRecovered.setFixedPosition(170, 420, 200);

        //Interest to be Recovered
        String interestToBeRecoveredStr = "Interest To Be Recovered : ";
        Paragraph interestToBeRecoveredPara = new Paragraph(interestToBeRecoveredStr);
        interestToBeRecoveredPara.setFont(bold);
        interestToBeRecoveredPara.setFixedPosition(60, 400, 200);

        Paragraph interestToBeRecovered = new Paragraph(interestToBeRecoveredNo);
        interestToBeRecovered.setFixedPosition(203, 400, 400);

        // Total
        String totalStr = "Total : ";
        Paragraph totalPara = new Paragraph(totalStr);
        totalPara.setFont(bold);
        totalPara.setFixedPosition(60, 380, 200);

        Paragraph total = new Paragraph(moneyStr);
        total.setFixedPosition(100, 380, 200);

        // Signature
        String signatureStr = "This is a system generated certificate and needs no signature.";
        Paragraph signature = new Paragraph(signatureStr);
        signature.setFixedPosition(140, 340, 400);

        // To Address
        String toStr = "To";
        Paragraph to = new Paragraph(toStr);
        to.setFont(bold);
        to.setFixedPosition( 60, 280, 200);

        String addressStr1 = "Shri/Smt. ";
        Paragraph address1 = new Paragraph(addressStr1);
        address1.setFixedPosition(60,260, 200);
        address1.add(name).add(",");
        address1.setFontSize(10f);

        String addressStr2 = "UCO BANK,\n" +
                "OLD NO. 285, NEW NO. 14,\n" +
                "PURASAWALKAM, HIGH ROAD,\n" +
                "PURASAWALKAM, CHENNAI,\n" +
                "CHENNAI TAMILNADU,\n" +
                "PIN - 600007.";

        Paragraph address2 = new Paragraph(addressStr2);
        address2.setFixedPosition(60, 170, 300);
        address2.setFontSize(10f);


        // Adding paragraphs to document
        document.add(title);
        document.add(heading);
        document.add(subHeading);
        document.add(title2);
        document.add(date);
        document.add(mainParagraph);
        document.add(interestRecoveredPara);
        document.add(interestRecovered);
        document.add(interestToBeRecoveredPara);
        document.add(interestToBeRecovered);
        document.add(totalPara);
        document.add(total);
        document.add(signature);
        document.add(to);
        document.add(address1);
        document.add(address2);

        // Closing the document
        document.close();

        pdfView.fromFile(file).load();
    }
}