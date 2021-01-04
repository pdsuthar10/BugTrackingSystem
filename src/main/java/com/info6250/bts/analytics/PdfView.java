package com.info6250.bts.analytics;

import com.info6250.bts.pojo.Issue;
import com.info6250.bts.pojo.Project;
//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfView extends AbstractPdfView {

    private static Font catFont = new Font(Font.HELVETICA, 18,
            Font.BOLD);
    private static Font smallBold = new Font(Font.HELVETICA, 12,
            Font.BOLD);

    protected void writeChartToPdf(JFreeChart chart, int width, int height, Document document, PdfWriter writer){
        PdfContentByte contentByte = writer.getDirectContent();
        PdfTemplate template = contentByte.createTemplate(width, height);
        Graphics2D graphics2d = template.createGraphics(width, height,
                new DefaultFontMapper());
        Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width,
                height);

        chart.draw(graphics2d, rectangle2d);

        graphics2d.dispose();
        contentByte.addTemplate(template, 50, 100);

    }

    public static JFreeChart generateBarChart(Map<String, Integer> result, String filter) {
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        if(filter.equals("status")){
            dataSet.setValue(result.get("open"), "Issues", "Open");
            dataSet.setValue(result.get("closed"), "Issues", "Closed");

            JFreeChart chart = ChartFactory.createBarChart(
                    "Issues", "Status", "Number",
                    dataSet, PlotOrientation.VERTICAL, false, true, false);

            return chart;
        }else{
            dataSet.setValue(result.get("Bug"), "Issues", "Bug");
            dataSet.setValue(result.get("Task"), "Issues", "Task");
            dataSet.setValue(result.get("Error"), "Issues", "Error");

            JFreeChart chart = ChartFactory.createBarChart(
                    "Distribution of Issues by Type", "Issue Type", "Number",
                    dataSet, PlotOrientation.VERTICAL, false, true, false);

            return chart;
        }

    }

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<Issue> issues = (List<Issue>) model.get("issues");
        List<Project> projects = (List<Project>) model.get("projects");
        document.open();
        Paragraph title = new Paragraph();
        addEmptyLine(title, 1);
        title.add(new Paragraph("Analysis of your projects and issues", catFont));
        title.setAlignment(Element.ALIGN_CENTER);

        addEmptyLine(title, 1);
        document.add(title);
        Table table = new Table(3);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);


        // write table header

        table.addCell(new Paragraph("Project ID", smallBold));

        table.addCell(new Paragraph("Open Issues", smallBold));

        table.addCell(new Paragraph("Closed Issues", smallBold));

        for (Project project : projects) {
            table.addCell(String.valueOf(project.getId()));
            table.addCell(String.valueOf(getNumberOfIssuesByStatus(project, "open")));
            table.addCell(String.valueOf(getNumberOfIssuesByStatus(project, "closed")));
        }
        table.setPadding(4);
        document.add(table);
        writeChartToPdf(generateBarChart(getIssuesStatusAll(issues), "status"), 500, 400, document, writer);


        document.newPage();

        writeChartToPdf(generateBarChart(getIssueTypeAll(issues), "issueType"), 500, 400, document, writer);
        document.close();

    }

    protected int getNumberOfIssuesByStatus(Project project, String status){
        int result = 0;
        for(Issue issue : project.getIssues()){
            if(issue.getStatus().getName().equals(status))
                result++;
        }
        return result;
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private Map<String, Integer> getIssuesStatusAll(List<Issue> issues){
        Map<String, Integer> result = new HashMap<>();
        for(Issue issue: issues){
            result.put(issue.getStatus().getName(), result.getOrDefault(issue.getStatus().getName(), 0) + 1);
        }
        return result;
    }

    private Map<String, Integer> getIssueTypeAll(List<Issue> issues){
        Map<String, Integer> result = new HashMap<>();
        for(Issue issue: issues){
            result.put(issue.getIssueType(), result.getOrDefault(issue.getIssueType(), 0) + 1);
        }
        return result;
    }
}
