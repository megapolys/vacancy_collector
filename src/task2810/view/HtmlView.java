package task2810.view;

import task2810.Controller;
import task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HtmlView implements View {

    private Controller controller;
    private final String filePath = "./src/" + this.getClass().getPackage().getName().replace('.', '/') + "/vacancies.html";

    @Override
    public void update(List<Vacancy> vacancies) {
        try {
            updateFile(getUpdatedFileContent(vacancies));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {

        final Document document;
        try {
            document = getDocument();
            final Elements templateOrig = document.getElementsByClass("template");
            final Elements template = templateOrig.clone();
            document.getElementsByClass("vacancy").remove();
            template.removeAttr("style");
            template.removeClass("template");
            for (Vacancy vacancy : vacancies) {
                final Elements clone = template.clone();
                clone.select(".city").append(vacancy.getCity());
                clone.select(".companyName").append(vacancy.getCompanyName());
                clone.select(".salary").append(vacancy.getSalary());
                clone.select("a[href]").append(vacancy.getTitle());
                clone.select("a[href]").attr("href", vacancy.getUrl());
                document.getElementsByTag("table").append(clone.outerHtml());
            }
            for (Element tbody : document.getElementsByTag("tbody")) {
                if (tbody.html().isEmpty()) {
                    tbody.remove();
                }
            }
            document.getElementsByTag("table").append(templateOrig.outerHtml());
            return document.html();
        } catch (IOException e) {
            e.printStackTrace();
            return "Some exception occurred";
        }
    }

    protected Document getDocument() throws IOException {
        return Jsoup.parse(new File(filePath), String.valueOf(StandardCharsets.UTF_8));
    }

    private void updateFile(String content) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(content.getBytes());
        }
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void userCitySelectEmulationMethod() {
        controller.onCitySelect("Odessa");
    }
}
