
package main.subCommponents.body.chart;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class ChartController {

    @FXML
    private LineChart<String, Integer> stockChart;

    @FXML
    private Label nameOfCurrStock;

    @FXML
    private Label chart;

    private String currentStock;

    private SimpleStringProperty currStock ;

    private Map<String, Integer> symbol2Price;

    public ChartController() {
        currStock = new SimpleStringProperty();
    }

    @FXML
    private void initialize() {
        stockChart.setLegendVisible(false);
        stockChart.setCreateSymbols(false);
        nameOfCurrStock.textProperty().bind(currStock);
    }


    public void setMapForChar(List<Pair<String,Integer>> chartData, String currStock) {
        this.currentStock = currStock;
        this.currStock.set(currStock);
        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data("StartingPoint", symbol2Price.get(currentStock)));
        for (Pair<String,Integer> currPair : chartData) {
            series.getData().add(new XYChart.Data(currPair.getKey(), currPair.getValue()));
        }
        stockChart.getData().addAll(series);
    }


    public void setSymbol2Price(Map<String, Integer> initPrice2Symbol) {
        symbol2Price = initPrice2Symbol;
    }

}
