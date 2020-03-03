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

// Этот класс будет реализовывать конкретную стратегию работы с сайтом ХэдХантер (http://hh.ua/ и http://hh.ru/)
public class HHStrategy implements Strategy {

    private static final String URL_FORMAT = "http://hh.ua/search/vacancy?text=java+%s&page=%d";
    private static final String URL_FORMAT_REAL = "https://hh.ru/search/vacancy?L_is_autosearch=false&area=1&clusters=true&enable_snippets=true&text=java+%s&page=%d";

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
            final Elements pageElements = document.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy");
            if (pageElements.size() == 0) {
                break;
            }
            for (Element element : pageElements) {
                final Vacancy vacancy = new Vacancy();
                final Elements title = element.getElementsByAttributeValueContaining("data-qa", "title");
                vacancy.setTitle(title.text());
                final Elements salary = element.getElementsByAttributeValueContaining("data-qa", "compensation");
                vacancy.setSalary(salary.text());
                final Elements city = element.getElementsByAttributeValueContaining("data-qa", "address");
                vacancy.setCity(city.text());
                final Elements companyName = element.getElementsByAttributeValueContaining("data-qa", "vacancy-serp__vacancy-employer");
                vacancy.setCompanyName(companyName.text().trim());
                vacancy.setUrl(title.attr("href"));
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
