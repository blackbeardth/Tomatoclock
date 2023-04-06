package application;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ReportController implements Initializable {
	@FXML
	private LineChart<String, Number> lineChart;

	@FXML
	private CategoryAxis x;

	@FXML
	private NumberAxis y;

	@FXML
	private ImageView timerImageView;
	
	@FXML
	private Label totalHoursLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		timerImageView.setImage(new Image(System.getProperty("user.dir") + "/resources/timer_icon.png"));

		// Removing the symbols of the line chart
		lineChart.setCreateSymbols(false);
		
		// totalHoursLabel.setText(Integer.toString(Session.getSession().getTotalHoursFocused()));

		LinkedHashMap<LocalDate, Integer> hmap = Session.getSession().getHoursFocusedMap();
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		if (hmap.isEmpty()) {
			for (int i = 0; i < 7; i++)
				series.getData().add(new XYChart.Data<String, Number>(LocalDate.now().plusDays(i).format(formatter), 0));
			
			lineChart.getData().add(series);
			System.out.println("Empty");
			return;
		}
		
		int i = 0;
		Map.Entry<LocalDate, Integer> entry = null;

		for (Iterator<Entry<LocalDate, Integer>> itr = hmap.entrySet().iterator(); itr.hasNext() && i < 7; i++) {
			entry = itr.next();
			series.getData().add(new XYChart.Data<String, Number>(entry.getKey().format(formatter), entry.getValue()));
		}
		if (i < 7) {
			for (int j = i, k = 1; j < 7; j++, k++) 
				series.getData().add(new XYChart.Data<String, Number>(entry.getKey().plusDays(k).format(formatter), 0));
		}
		lineChart.getData().add(series);
	}

}
