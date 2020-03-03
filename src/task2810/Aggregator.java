package task2810;

import task2810.model.HHStrategy;
import task2810.model.Model;
import task2810.model.MoikrugStrategy;
import task2810.model.Provider;
import task2810.view.HtmlView;

import java.io.IOException;

public class Aggregator {
    public static void main(String[] args) throws IOException {
        final Provider provider = new Provider(new HHStrategy());
        final HtmlView view = new HtmlView();
        final Model model = new Model(view, provider, new Provider(new MoikrugStrategy()));
        final Controller controller = new Controller(model);
        view.setController(controller);
        view.userCitySelectEmulationMethod();
    }
}
