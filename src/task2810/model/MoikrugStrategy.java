package task2810.model;

import task2810.vo.Vacancy;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoikrugStrategy implements Strategy {

    private static final String URL_FORMAT = "https://moikrug.ru/vacancies?q=java+%s&page=%d";
    private static final String URL_FORMAT_ROOT = "https://moikrug.ru";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        final List<Vacancy> vacancies = new ArrayList<>();
        Document document;

        for (int i = 0;; i++) {
            try {
                document = getDocument(searchString, i);
            } catch (HttpStatusException http) {
                break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            final Elements pageElements = document.getElementsByClass("job");
            if (pageElements.size() == 0) {
                break;
            }
            for (Element element : pageElements) {
                final Vacancy vacancy = new Vacancy();
                final Elements title = element.getElementsByClass("title");
                vacancy.setTitle(title.text());
                final Elements salary = element.getElementsByClass("salary");
                vacancy.setSalary(salary.text());
                final Elements city = element.getElementsByClass("location");
                vacancy.setCity(city.text());
                final Elements companyName = element.getElementsByClass("company_name");
                vacancy.setCompanyName(companyName.text().trim());
                vacancy.setUrl(URL_FORMAT_ROOT + title.select("a").attr("href"));
                vacancy.setSiteName(URL_FORMAT);
                vacancies.add(vacancy);
            }
        }


        return vacancies;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        return Jsoup.connect(String.format(URL_FORMAT, searchString, page))
                .userAgent("Mozilla/5.0 (Windows NT 10.0; …) Gecko/20100101 Firefox/73.0")
                .referrer("")
                .get();
    }

}
