//package com.info6250.bts.analytics;
//
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.pdf.PdfWriter;
//import org.springframework.web.servlet.view.AbstractView;
//import org.springframework.web.servlet.view.document.AbstractPdfView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.ByteArrayOutputStream;
//import java.io.OutputStream;
//import java.util.Map;
//
//public abstract class AbstractITextPdfView extends AbstractView {
//
//    public AbstractITextPdfView(){
//        setContentType("application/pdf");
//    }
//
//    @Override
//    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
//                                           HttpServletResponse response) throws Exception {
//        ByteArrayOutputStream baos = createTemporaryOutputStream();
//        Document document = newDocument();
//        PdfWriter writer = newWriter(document, baos);
//        prepareWriter(model, writer, request);
//        buildPdfMetadata(model, document, request);
//
//    }
//    protected Document newDocument() {
//        return new Document(PageSize.A4);
//    }
//
//    protected PdfWriter newWriter(Document document, OutputStream os) throws DocumentException {
//        return PdfWriter.getInstance(document, os);
//    }
//
//    protected void prepareWriter(Map<String, Object> model, PdfWriter writer, HttpServletRequest request)
//            throws DocumentException {
//
//        writer.setViewerPreferences(getViewerPreferences());
//    }
//
//    protected int getViewerPreferences() {
//        return PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage;
//    }
//
//    protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request) {
//    }
//
//    protected abstract void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
//                                             HttpServletRequest request, HttpServletResponse response) throws Exception;
//}
