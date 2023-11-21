/**
 CP2406 Programming 3 Assignment
 Rainfall Analyser, Beta-release
 By Jay Davis
 This program visualises the data as a bar chart after reading and calculating the data through the use of the java
 class, RainfallAnalyser.
 * */

import RainfallAnalyser.RainfallAnalyser;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Group;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.File;

/**
 * A program that lets the user choose a csv file containing the records of Rainfall for a
 * specific location over a particular timeframe. The data is read, calculated and written
 * to another csv file using the RainfallAnalyser class. This class takes the written csv
 * file and graphs the written data into a bar chart. The user has the ability to click on
 * any column and see some relevant data on the selected month. This relevant data is the
 * month, year, minimum, maximum, and total rainfall for the selected month of that year.
 */


public class RainfallVisualiser extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    //-----------------------------------------------------------------------------------------------------------------

    private static double [][] data; // A 2D array that contains all the data read from the source file.

    final CategoryAxis xAxis = new CategoryAxis(); // xAxis is the month and year used in the dataset.

    final NumberAxis yAxis = new NumberAxis(); // yAxis is the total Rainfall in millimetres.

    final BarChart<String, Number> bar_chart = new BarChart<>(xAxis, yAxis); // Equals the bar chart being generated.

    final Label stats = new Label("\n" +
            "     Month's statistics                             \n\n" +
            "     Rainfall Total:          \n"  +
            "     Minimum:                \n" +
            "     Maximum:               \n" +
            "                            "); // Creates a label to show the user the selected month's other relevant data.

    final int SCENE_WIDTH = 1250;

    final int SCENE_HEIGHT = 700;

    private static double [][][] files_data; // The 3D array containing the data for all csv files from the ComboBox.

    private static ObservableList<String> csv_files; // List of available csv files for the user to choose from.

    private ComboBox<String> csv_comboBox; // ComboBox that contains the csv_files list.

    private static int file_index; // The index of the selected file from the files available in the ComboBox list.


    /**
     * Sets up the GUI. Within the stage, is a bar chart, a combobox, and a label. The checkbox and the columns in the
     * bar chart is accessible to the user.
     */
    @Override
    public void start(Stage stage)  {

        stage.setTitle("Rainfall Visualiser");

        int GRID_WIDTH;
        int GRID_HEIGHT;
        int CHART_WIDTH;
        int CHART_HEIGHT;

        // Calculates the size of the grid and the bar chart.
        GRID_WIDTH = SCENE_WIDTH / 6;
        GRID_HEIGHT = SCENE_HEIGHT / 4;
        CHART_WIDTH = SCENE_WIDTH * 5 /6;
        CHART_HEIGHT = SCENE_HEIGHT;

        Button visualiseButton = new Button("Visualise"); // Creates the button which will create the graph based on
        // the csv file selected.

        bar_chart.setTitle("Rainfall Data");
        xAxis.setLabel("Year");
        yAxis.setLabel("Total rainfall (ml)");

        // Makes appropriate configurations for the bar chart.
        bar_chart.setAnimated(true);
        bar_chart.setLegendVisible(true);
        bar_chart.setBarGap(0);
        bar_chart.setCategoryGap(0);
        bar_chart.setPrefSize(CHART_WIDTH,CHART_HEIGHT);
        bar_chart.setLayoutY(-5);

        csv_files = FXCollections.observableArrayList("MountSheridanStationCNS.csv",
                "CopperlodeDamStationCNS.csv", "KurandaRailwayStationCNS.csv", "EmptyCSV.csv", "EmptyCSVwithHeaders.csv",
                "KurandaRailwayStationCNSWithDifferentColumnHeadersNames.csv",
                "KurandaRailwayStationCNSWithDifferentColumnAllocation.csv", "NonExistantCSVFile.csv");
        // Lists all available CSV files. Includes five extra CSV files for testing exceptions. Test files exclusively
        // contain in listed order above: Empty csv without headers, empty csv with headers, different names for columns'
        // headers, different allocation for columns, no csv file created.

        files_data = new double[csv_files.size()][][];

        int count = 0;
        for (String file : csv_files)   {
            RainfallAnalyser.RAINFALL_DATA_FILE  = new File(RainfallAnalyser.FILE_PATH + File.separator + file);
            try {
                RainfallAnalyser.main(null);    // Processes and calculates the dataset of the selected csv file.
                // Reads the calculations and assigns to the 3D array, files_data.
                files_data[count] = RainfallAnalyser.readData(RainfallAnalyser.UPDATED_RAINFALL_DATA_FILE);
                count++;

            }
            catch (ArrayIndexOutOfBoundsException | NullPointerException e)   {
                count++;
                continue;
            }
        }

        csv_comboBox = new ComboBox<>(csv_files);
        csv_comboBox.setValue("Pick a CSV file...");
        csv_comboBox.setVisibleRowCount(6);
        csv_comboBox.setEditable(false);
        csv_comboBox.setOnAction(ev -> {
            file_index = csv_comboBox.getSelectionModel().getSelectedIndex(); // Assigns the index of the selected csv file.
        });

        visualiseButton.setOnAction(new EventHandler<>() {
            @Override
            /**
             * The handle function runs the generateGraph function everytime the visualiseButton is clicked.
             */
            public void handle(ActionEvent arg0) {
                generateGraph();
            }
        });

        GridPane grid = new GridPane();

        grid.add(new Label ("Data file:"), 2, 1);
        grid.add(csv_comboBox, 2, 2);
        grid.add(visualiseButton, 2, 3);
        grid.add(stats, 2, 4);

        Scene scene = new Scene(new Group(), SCENE_WIDTH, SCENE_HEIGHT);    // Adds the graph and the grid above to
                                                                            // the group.

        Group root = (Group)scene.getRoot();
        root.getChildren().add(bar_chart);
        root.getChildren().add(grid);

        // Sets the size and position of csv_comboBox and button.
        grid.setPrefSize(GRID_WIDTH,GRID_HEIGHT);
        grid.setAlignment(Pos.CENTER_RIGHT);
        grid.setLayoutX(GRID_WIDTH*5);
        grid.setLayoutY(GRID_HEIGHT);

        // Configures the layout and style of the stats Label.
        stats.setLayoutY(GRID_HEIGHT + 300);
        stats.getBorder();
        stats.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));

        // Sets the size and location of the bar graph.
        bar_chart.setPrefSize(CHART_WIDTH,CHART_HEIGHT);
        bar_chart.setLayoutY(-5);
        bar_chart.toBack();

        scene.setOnMousePressed(e -> printDataOfSelectedColumn());

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    } // ends start.


    /**
     * Finds and prints the other relevant information for the selected column in the bar graph.
     * Precondition:  The csv file that contains the data for the selected column is the most
     * recent csv file being visualised. Eitherwise, the data will not be found.
     * @return no return.
     */
    private void printDataOfSelectedColumn() {
            for (XYChart.Series<String, Number> data_series : bar_chart.getData()) {
                for (XYChart.Data<String, Number> item : data_series.getData()) {  // Checks each entry of the dataset from
                    // bar_chart to see if the index matches
                    // the selected column.
                    item.getNode().setOnMousePressed((MouseEvent event) -> {

                        // Finds the timeframe and rainfall of the selected column.
                        final String selected_month = data_series.nameProperty().getValue();
                        final int selected_year = Integer.parseInt(item.getXValue());
                        final String selected_rainfall = item.getYValue().toString();
                        final int selected_month_as_int = convertMonth(selected_month);

                        int year_index;
                        int month_index;
                        int minimum_index;
                        int maximum_index;

                        year_index = 0;
                        month_index = 1;
                        minimum_index = 3;
                        maximum_index = 4;
                        try {

                            for (double[] entry : data) {
                                // Finds the entry in the data array that matches the month and year of the selected column in
                                // the bar chart.
                                if (((int) entry[year_index] == selected_year) &&
                                        (entry[month_index] == selected_month_as_int)) {

                                    final String selected_minimum = String.valueOf(entry[minimum_index]);
                                    final String selected_maximum = String.valueOf(entry[maximum_index]);

                                    // Updates the text for the stats Label, which now contains the selected column's other information.
                                    stats.setText("\n     Month's statistics                             \n" +
                                            "     " + selected_year + ", " + selected_month + "                               \n" +
                                            "     Rainfall Total:          " + selected_rainfall + "\n" +
                                            "     Minimum:                " + selected_minimum + "\n" +
                                            "     Maximum:               " + selected_maximum + "\n" + " ");
                                    break;
                                }
                            }
                        } catch (NullPointerException e)    {
                            System.out.println("Select an appropriate csv file to check other relevant data for the selected column.");
                        }
                    });
                }
            }
    } // ends printDataOfSelectedColumn.

    /**
     * Graphs the data from the selected csv file.
     * Precondition:  The data for the selected csv file is accessible and is currently in memory.
     * @return no return.
     * @throws NullPointerException if the file is inaccessible or empty.
     */
    private void generateGraph() {

        if (csv_comboBox.getSelectionModel().isEmpty() == true) { // Checks if the user has not selected a csv file.
            System.out.println("Please select a csv file.");
        }

        else {

            String entry;
            bar_chart.setAnimated(true);    // Updates the x-axis of the graph to the x-values of the selected csv file.

            final XYChart.Series<String, Number> January = new XYChart.Series<>();
            final XYChart.Series<String, Number> February = new XYChart.Series<>();
            final XYChart.Series<String, Number> March = new XYChart.Series<>();
            final XYChart.Series<String, Number> April = new XYChart.Series<>();
            final XYChart.Series<String, Number> May = new XYChart.Series<>();
            final XYChart.Series<String, Number> June = new XYChart.Series<>();
            final XYChart.Series<String, Number> July = new XYChart.Series<>();
            final XYChart.Series<String, Number> August = new XYChart.Series<>();
            final XYChart.Series<String, Number> September = new XYChart.Series<>();
            final XYChart.Series<String, Number> October = new XYChart.Series<>();
            final XYChart.Series<String, Number> November = new XYChart.Series<>();
            final XYChart.Series<String, Number> December = new XYChart.Series<>();

            RainfallAnalyser.RAINFALL_DATA_FILE = new File(RainfallAnalyser.FILE_PATH + File.separator + csv_files.get(file_index));
            System.out.println(RainfallAnalyser.UPDATED_RAINFALL_DATA_FILE);
            try {
                data = files_data[file_index]; // Finds the 2D array in the 3D array for the selected csv file's index.

                RainfallAnalyser.countRows(RainfallAnalyser.RAINFALL_DATA_FILE);

                January.setName("January");
                February.setName("February");
                March.setName("March");
                April.setName("April");
                May.setName("May");
                June.setName("June");
                July.setName("July");
                August.setName("August");
                September.setName("September");
                October.setName("October");
                November.setName("November");
                December.setName("December");

                for (double[] month : data) {
                    entry = Integer.toString((int) month[0]);
                    switch ((int) month[1]) {
                        case 1: {
                            January.getData().add(new XYChart.Data(entry, month[2]));
                            break;
                        }
                        case 2: {
                            February.getData().add(new XYChart.Data(entry, month[2]));
                            break;
                        }
                        case 3: {
                            March.getData().add(new XYChart.Data(entry, month[2]));
                            break;
                        }
                        case 4: {
                            April.getData().add(new XYChart.Data(entry, month[2]));
                            break;
                        }
                        case 5: {
                            May.getData().add(new XYChart.Data(entry, month[2]));
                            break;
                        }
                        case 6: {
                            June.getData().add(new XYChart.Data(entry, month[2]));
                            break;
                        }
                        case 7: {
                            July.getData().add(new XYChart.Data(entry, month[2]));
                            break;
                        }
                        case 8: {
                            August.getData().add(new XYChart.Data(entry, month[2]));
                            break;
                        }
                        case 9: {
                            September.getData().add(new XYChart.Data(entry, month[2]));
                            break;
                        }
                        case 10: {
                            October.getData().add(new XYChart.Data(entry, month[2]));
                            break;
                        }
                        case 11: {
                            November.getData().add(new XYChart.Data(entry, month[2]));
                            break;
                        }
                        case 12: {
                            December.getData().add(new XYChart.Data(entry, month[2]));
                            break;
                        }
                    }
                }

                bar_chart.getData().addAll(January, February, March, April, May, June, July, August, September, October,
                        November, December);
                bar_chart.setData(FXCollections.observableArrayList(January, February, March, April, May, June, July,
                        August, September, October, November, December));
                bar_chart.setAnimated(false);
                stats.setText("\n" +
                        "     Month's statistics                             \n\n" +
                        "     Rainfall Total:          \n" +
                        "     Minimum:                \n" +
                        "     Maximum:               \n" +
                        "                            "); // Resets the stats Label.
            } catch (NullPointerException e) {
                System.out.println("\n---------------------------------------------------------------------");

                if (!RainfallAnalyser.RAINFALL_DATA_FILE.exists()) {          // Alerts the user if the csv file
                                                                              // does not exist.
                    System.out.println("This csv file does not exist.");
                }

                else if (RainfallAnalyser.number_of_rows == 0) {              // Alerts the user if the selected
                                                                              // csv file is empty.
                    System.out.println("This csv file is empty.");
                }

                else if (RainfallAnalyser.number_of_rows == 1) {              // Alerts the user if the csv file only
                                                                              // contains the column headers.
                    System.out.println("This csv file does not have any data.");
                }

                System.out.println("Please select an appropriate CSV file");

            }
        }

    } // ends generateGraph.


    /**
     * Checks the name of the month and converts it to its numerical value. E.g January = 1, December = 12.
     * Precondition:  The month currently being read is one of the available values below.
     * @param string_month the current month's name for the selected column.
     * @return the numerical value of the month for the selected column.
     */
    public static int convertMonth(String string_month)   {
        int numerical_month = 0;
        switch (string_month)   {
            case "January": {
                numerical_month = 1;
                break;
            }
            case "February": {
                numerical_month = 2;
                break;
            }
            case "March": {
                numerical_month = 3;
                break;
            }
            case "April": {
                numerical_month = 4;
                break;
            }
            case "May": {
                numerical_month = 5;
                break;
            }
            case "June": {
                numerical_month = 6;
                break;
            }
            case "July": {
                numerical_month = 7;
                break;
            }
            case "August": {
                numerical_month = 8;
                break;
            }
            case "September": {
                numerical_month = 9;
                break;
            }
            case "October": {
                numerical_month = 10;
                break;
            }
            case "November": {
                numerical_month = 11;
                break;
            }
            case "December": {
                numerical_month = 12;
                break;
            }
        }

        return numerical_month;
    } // ends convertMonth

}

