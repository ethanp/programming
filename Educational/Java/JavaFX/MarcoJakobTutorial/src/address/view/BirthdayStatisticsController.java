package address.view;

import address.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Ethan Petuchowski 1/7/15
 */
public class BirthdayStatisticsController {

    @FXML private BarChart<String, Integer> barChart;
    @FXML private CategoryAxis xAxis;

    private ObservableList<String> monthNames = FXCollections.observableArrayList();

    // automatically called after the fxml file has been loaded
    @FXML private void initialize() {
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        monthNames.addAll(Arrays.asList(months));
        xAxis.setCategories(monthNames);
    }

    /** set which persons to show statistics for */
    public void setPersonData(List<Person> persons) {
        int[] monthCounter = new int[12];
        for (Person p : persons) {
            int month = p.getBirthday().getMonthValue()-1;
            monthCounter[month]++;
        }
        XYChart.Series<String, Integer> series = new XYChart.Series<>();

        // add a new XYChart.Data object to the series for each month
        for (int i = 0; i < monthCounter.length; i++) {
            String monthKey = monthNames.get(i);
            int monthValue = monthCounter[i];
            XYChart.Data<String, Integer> dataPoint = new XYChart.Data<>(monthKey, monthValue);
            series.getData().add(dataPoint);
        }

        barChart.getData().add(series);
    }
}
