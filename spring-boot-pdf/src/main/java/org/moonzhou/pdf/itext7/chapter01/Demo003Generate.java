package org.moonzhou.pdf.itext7.chapter01;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfChoiceFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.IOException;

/**
 * @author moon zhou
 * @description 生成带有radio/checkbox等复杂元素的pdf
 * @email ayimin1989@163.com
 * @date 2022/5/12 22:39
 **/
public class Demo003Generate {

    public static final String DEST = "/Users/xxx/tmp/pdf/demo003.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Demo003Generate().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException {

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        PageSize ps = PageSize.A4;
        pdf.setDefaultPageSize(ps);

        // Initialize document
        Document document = new Document(pdf);

        Demo003Generate.addAcroForm(document);

        //Close document
        document.close();

    }

    public static PdfAcroForm addAcroForm(Document doc) {

        Paragraph title = new Paragraph("Application for employment")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16);
        doc.add(title);
        doc.add(new Paragraph("Full name:").setFontSize(12));
        doc.add(new Paragraph("Native language:      English         French       German        Russian        Spanish").setFontSize(12));
        doc.add(new Paragraph("Experience in:       cooking        driving           software development").setFontSize(12));
        doc.add(new Paragraph("Preferred working shift:").setFontSize(12));
        doc.add(new Paragraph("Additional information:").setFontSize(12));

        //Add acroform
        PdfAcroForm form = PdfAcroForm.getAcroForm(doc.getPdfDocument(), true);

        //Create text field
        PdfTextFormField nameField = PdfTextFormField.createText(doc.getPdfDocument(),
                new Rectangle(99, 753, 425, 15), "name", "");
        form.addField(nameField);

        //Create radio buttons
        PdfButtonFormField group = PdfFormField.createRadioGroup(doc.getPdfDocument(), "language", "");
        PdfFormField.createRadioButton(doc.getPdfDocument(), new Rectangle(130, 728, 15, 15), group, "English");
        PdfFormField.createRadioButton(doc.getPdfDocument(), new Rectangle(200, 728, 15, 15), group, "French");
        PdfFormField.createRadioButton(doc.getPdfDocument(), new Rectangle(260, 728, 15, 15), group, "German");
        PdfFormField.createRadioButton(doc.getPdfDocument(), new Rectangle(330, 728, 15, 15), group, "Russian");
        PdfFormField.createRadioButton(doc.getPdfDocument(), new Rectangle(400, 728, 15, 15), group, "Spanish");
        form.addField(group);

        form.getField("language").setValue("German");

        //Create checkboxes
        for (int i = 0; i < 3; i++) {
            PdfButtonFormField checkField = PdfFormField.createCheckBox(doc.getPdfDocument(), new Rectangle(119 + i * 69, 701, 15, 15),
                    "experience".concat(String.valueOf(i+1)), "Off", PdfFormField.TYPE_CHECK);
            form.addField(checkField);
        }

        //Create combobox
        String[] options = {"Any", "6.30 am - 2.30 pm", "1.30 pm - 9.30 pm"};
        PdfChoiceFormField choiceField = PdfFormField.createComboBox(doc.getPdfDocument(), new Rectangle(163, 676, 115, 15),
                "shift", "Any", options);
        form.addField(choiceField);

        //Create multiline text field
        PdfTextFormField infoField = PdfTextFormField.createMultilineText(doc.getPdfDocument(),
                new Rectangle(158, 625, 366, 40), "info", "");
        form.addField(infoField);

        //Create push button field
        PdfButtonFormField button = PdfFormField.createPushButton(doc.getPdfDocument(),
                new Rectangle(479, 594, 45, 15), "reset", "RESET");
        button.setAction(PdfAction.createResetForm(new String[] {"name", "language", "experience1", "experience2", "experience3", "shift", "info"}, 0));
        form.addField(button);

        return form;

    }
}
