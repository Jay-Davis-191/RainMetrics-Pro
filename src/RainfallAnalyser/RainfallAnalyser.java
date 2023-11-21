/**
 CP2406 Programming 3 Assignment
 Rainfall Analyser, Beta-release
 By Jay Davis
 This program reads data from a csv file chosen by the user and makes multiple calculations, before writing the data to
 a different csv file.
 */

package RainfallAnalyser;

import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * This program reads a csv file containing rainfall data and performs multiple calculations with the data. The
 * calculations including the minimum and maximum rainfall, as well as the total rainfall, for each month is written
 * to a different csv file.
 */


public class RainfallAnalyser {

    public static String FILE_PATH = System.getProperty("user.dir");    // File path to subdirectory that contains
                                                                        // multiple csv files.

    public static File RAINFALL_DATA_FILE; // Location of csv file being read.

    public static File UPDATED_RAINFALL_DATA_FILE = new File("RainfallData.txt"); // Location of csv file being
                                                                                      // written to.

    public static int number_of_columns; // Equals the number of columns in RAINFALL_DATA_FILE.

    public static int NUMBER_OF_REQUIRED_VALUES = 5; // The appropriate number of values necessary from each row to be
                                                     // written to UPDATED_RAINFALL_DATA_FILE.

    public static int number_of_rows; // Equals the number of rows (excluding headers) from RAINFALL_DATA_FILE.

    public static int day_position;               // The index of the "Day" column in RAINFALL_DATA_FILE.
    public static int initial_year_position;      // The index of the "Year" column in RAINFALL_DATA_FILE.
    public static int initial_month_position;     // The index of the "Month" column in RAINFALL_DATA_FILE.
    public static int initial_rainfall_position;  // The index of the "Rainfall Total" column in RAINFALL_DATA_FILE.
    public static int initial_time_position;      // The index of the Timeframe column in RAINFALL_DATA_FILE.
    public static int initial_quality_position;   // The index of the "Quality" column in RAINFALL_DATA_FILE.

    public static int UPDATED_YEAR_POSITION = 0;     // The index of the "Year" column for the calculations array.
    public static int UPDATED_MONTH_POSITION = 1;    // The index of the "Month" column for the calculations array.
    public static int UPDATED_DAY_POSITION = 2;      // The index of the "Year" column for the calculations array.
    public static int UPDATED_RAINFALL_POSITION = 3; // The index of the "Rainfall Amount" for the calculations array.
    public static int UPDATED_TIME_POSITION = 4;     // The index of the Timeframe column for the calculations array.
    public static int AVERAGE_POSITION = 5;          // The index of the average (based on Rainfall Amount / Timeframe)
                                                     // for the calculations array.

    public static int current_row; // The number of the current row from RAINFALL_DATA_FILE being processed.

    public static int updated_number_of_rows; // Represents the number of rows being written to RainfallData.txt based
                                              // on the length of the array being written to RainfallData.txt.


    public static void main(String[] args) {
        current_row = 0;

        countRows(RAINFALL_DATA_FILE); // Counts the number of rows in RAINFALL_DATA_FILE.

        double[][] rainfall_data; // 2D array that contains all data from RAINFALL_DATA_FILE.

        double[][] updated_data; // 2D array that contains all the calculations made.

        rainfall_data = readData(RAINFALL_DATA_FILE); // Reads all rows from RAINFALL_DATA_FILE.

        updated_data = calculateInfo(rainfall_data); // Makes appropriate calculations for all data from rainfall_data.

        writeToFile(updated_data); // Writes the updated data to RainfallData.txt.
    } // ends main ()


    /**
     * Reads the csv file, RAINFALL_DATA_FILE, and counts the number of rows used in RAINFALL_DATA_FILE.
     * Precondition:  The csv file is accessible and readable.
     * @param filename the csv file being read.
     * @return the total number of rows in the csv file, excluding the column headers that it assigns
     * to the public variable, number_of_rows.
     * @throws IOException if the file, filename, is not accessible or readable. Exceptions are used
     * more in RainfallVisualiser.
     */
    public static void countRows(File filename) {
        number_of_rows = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            while (reader.readLine() != null)   {
                number_of_rows++;
            }
        }

        catch (IOException e) {
        }

    } // ends countRows


    /**
     * Reads all data from RAINFALL_DATA_FILE and assigns each data row to a 1D array in a 2D array.
     * Precondition:  The csv file is accessible and readable.
     * @param filename the csv file being read.
     * @return the 2D array containing all the relevant values necessary for calculations and
     * graphing as doubles or integers.
     * @throws Exception if there are any other relevant issues with reading the file, filename.
     */
    public static double[][] readData(File filename) {

        String current_line;
        int current_data_entry;
        double[][] data_list_as_doubles;

        current_data_entry = 0;
        data_list_as_doubles = new double[number_of_rows+1][NUMBER_OF_REQUIRED_VALUES + 1];

        try (BufferedReader file = new BufferedReader(new FileReader(filename))) {
            current_line = file.readLine();
            findColumns(current_line); // Finds the index of the columns containing the data findings and time period.

            while ((current_line = file.readLine()) != null) { // While loop that continues until it reaches an empty row.
                String[] data_of_current_entry;
                data_of_current_entry = current_line.split(",");

                // Updates data to check for empty values.
                data_list_as_doubles[current_data_entry] = convertToDouble(updateData(data_of_current_entry));

                current_data_entry++;
            }
        }

        catch (Exception e) {
        }

        return data_list_as_doubles;
    } // ends readData


    /**
     * Finds the index of each column based on the column's header. This is useful for csv files with
     * different arrangements for their columns.
     * Precondition:  The csv file is accessible and readable and the required columns are present in
     * the csv file.
     * @param current_line the current row being read from the file, RAINFALL_DATA_FILE.
     * @return no return.
     * @throws ArrayIndexOutOfBoundsException if the required data columns or the required number of
     * columns in RAINFALL_DATA_FILE are not present.
     */
    public static void findColumns(String current_line) {

        String[] example;

        example = current_line.split(",");
        number_of_columns = example.length;

        try { //
            initial_year_position = Arrays.asList(example).indexOf("Year");
            initial_month_position = Arrays.asList(example).indexOf("Month");
            day_position = Arrays.asList(example).indexOf("Day");

            for (int column_index = 0; column_index < example.length; column_index++) {
                if (example[column_index].contains("Rainfall") && example[column_index].contains("amount") &&
                        example[column_index].contains("millimetres")) {
                    initial_rainfall_position = column_index;

                } else if (example[column_index].contains("Period") && example[column_index].contains("measured") &&
                        example[column_index].contains("(days)")) {
                    initial_time_position = column_index;

                } else if (example[column_index].contains("Quality")) {
                    initial_quality_position = column_index;
                }
            }

        } catch (ArrayIndexOutOfBoundsException e) { // Alerts the user if the required data columns are not present.
            System.out.println("ArrayIndexOutOfBoundsException");
        }
    } // ends findColumns


    /**
     * Checks if there are any empty values in the findings of the current row being processed.
     * Precondition:  The number of required columns are present in the csv file.
     * @param entry A 1D array that contains the values for the current row being read from
     *              RAINFALL_DATA_FILE, which is split by each occurrence of ",".
     * @return the updated array for the current row's values without any occurrences of null
     * or NaN values.
     */
    public static String[] updateData(String[] entry) {

        String[] updated_entry;

        updated_entry = new String[number_of_columns];

        for (int data_cell = 0; data_cell < entry.length; data_cell++) {
            updated_entry[data_cell] = entry[data_cell];
            if (updated_entry[data_cell] == "") {       // Checks for any empty data cells. The default value for
                                                        // any empty value is 0.
                updated_entry[data_cell] = "0";
            }
        }

        if (entry.length < number_of_columns) {         // Checks if all of the last three findings of the current
                                                        // row are missing. If so, default value is assigned.
            updated_entry[initial_rainfall_position] = "0.0";
            updated_entry[initial_time_position] = "0";
            updated_entry[initial_quality_position] = "0";
        }

        return updated_entry;

    } // ends updateData


    /**
     * Converts the String array to a double array with the same data.
     * Precondition:  The updated values for the current row being read from RAINFALL_DATA_FILE have
     * been updated successfully in the updateData method.
     * @param entry the current entry from the updated 1D array data_of_current_entry.
     * @return the 1D array that contains the updated findings as either integers or doubles.
     */
    public static double[] convertToDouble(String[] entry) {
        double[] list;

        if (entry.length == 8) {
            list = new double[NUMBER_OF_REQUIRED_VALUES + 1];

            list[UPDATED_YEAR_POSITION] = Integer.parseInt(entry[initial_year_position]);
            list[UPDATED_MONTH_POSITION] = Integer.parseInt(entry[initial_month_position]);
            list[UPDATED_DAY_POSITION] = Integer.parseInt(entry[day_position]);
            list[UPDATED_RAINFALL_POSITION] = Double.parseDouble(entry[initial_rainfall_position]);
            list[UPDATED_TIME_POSITION] = Integer.parseInt(entry[initial_time_position]); // Assigns the entry's time
            // period of last time a
            // record was taken in days.

            // Assigns the average rainfall based on the amount of days since the previous test was performed. Prevents
            // the rainfall amount from a 10 day timeframe since the last test was taken being assigned as the minimum
            // and maximum being assigned.
            list[AVERAGE_POSITION] = list[UPDATED_RAINFALL_POSITION] / list[UPDATED_TIME_POSITION];

        }
        // else statement below is used by Rainfall Visualiser when you click "Visualise", as it reads the array
        // containing the calculations.
        else    {
            list = new double[NUMBER_OF_REQUIRED_VALUES];

            // All assignments below match the same description as the similar assignments above.
            list[UPDATED_YEAR_POSITION] = Double.parseDouble(entry[UPDATED_YEAR_POSITION]);
            list[UPDATED_MONTH_POSITION] = Double.parseDouble(entry[UPDATED_MONTH_POSITION]);
            list[UPDATED_DAY_POSITION] = Double.parseDouble(entry[UPDATED_DAY_POSITION]);
            list[UPDATED_RAINFALL_POSITION] = Double.parseDouble(entry[UPDATED_RAINFALL_POSITION]);
            list[UPDATED_TIME_POSITION] = Double.parseDouble(entry[UPDATED_TIME_POSITION]);
        }

        return list;
    } // ends convertToDouble


    /**
     * Calculates the info to find the total rainfall, minimum and maximum for each month.
     * Precondition:  All values can be converted to doubles and do not include any letters or
     * special characters.
     * @param record a 2D array containing the updated findings for each data row from
     *               RAINFALL_DATA_FILE as doubles and integers.
     * @return the 2D array that contains the calculations for each 1D array from the 2D array,
     * record. The calculations for each month for each row is: Year, Month, Rainfall total, Rainfall
     * minimum, and Rainfall maximum.
     */
    public static double[][] calculateInfo(double[][] record) {
        double[][] calculations;
        Double min_of_current_month; // Using Double instead of double allows to check if value is NaN.
        Double max_of_current_month; // Using Double instead of double allows to check if value is NaN.
        double rainfall_total;
        Double current_month;
        Double current_year;
        double next_month;
        double next_year;
        double rounded_total;
        double rounded_min;
        double rounded_max;
        int next_row;

        calculations = new double[number_of_rows+1][NUMBER_OF_REQUIRED_VALUES];
        rainfall_total = 0.0;
        min_of_current_month = record[0][AVERAGE_POSITION];
        max_of_current_month = record[0][AVERAGE_POSITION];

        // 'for' loop that checks all data from the 2D array, record.
        for (int current_row_being_calculated = 0; current_row_being_calculated <= number_of_rows; current_row_being_calculated++) {
            next_row = current_row_being_calculated+1;
            current_month = record[current_row_being_calculated][UPDATED_MONTH_POSITION];
            current_year = record[current_row_being_calculated][UPDATED_YEAR_POSITION];


            if (current_row_being_calculated == number_of_rows) { // Checks if the number of the current is the last
                                                                  // row of data.
                rainfall_total += record[current_row_being_calculated-1][UPDATED_RAINFALL_POSITION];
                next_month = record[current_row_being_calculated][UPDATED_MONTH_POSITION];
                next_year = record[current_row_being_calculated][UPDATED_YEAR_POSITION];
            }
            else    {   // All other instances require the program to operate as normal.
                rainfall_total += record[current_row_being_calculated][3];
                next_month = record[next_row][UPDATED_MONTH_POSITION];
                next_year = record[next_row][UPDATED_YEAR_POSITION];
            }

            if (record[current_row_being_calculated][AVERAGE_POSITION] < min_of_current_month) {
                min_of_current_month = record[current_row_being_calculated][AVERAGE_POSITION];
            }

            if (record[current_row_being_calculated][AVERAGE_POSITION] > max_of_current_month) {
                max_of_current_month = record[current_row_being_calculated][AVERAGE_POSITION];
            }

            if (current_month != next_month || current_year != next_year)   { // Checks if the month or year of the
                                                                              // current month match the next row's.
                // Rounds all values directly below to 1 decimal place.
                rounded_total = Math.round(rainfall_total * 10.0) / 10.0;
                rounded_min = Math.round(min_of_current_month * 10.0) / 10.0;
                rounded_max = Math.round(max_of_current_month * 10.0) / 10.0;

                calculations[current_row] = new double[]{current_year, current_month, rounded_total, rounded_min, rounded_max};

                rainfall_total = 0;

                // Ensures no new additions or calculations are made for any empty rows after all rows of data.
                if (next_row >= number_of_rows-1) {
                    min_of_current_month = 0.0;
                    max_of_current_month = 0.0;
                }
                else    {
                    min_of_current_month = record[next_row][AVERAGE_POSITION]; // Assigns the next month's first
                    // entry as the min.
                    max_of_current_month = record[next_row][AVERAGE_POSITION]; // Assigns the next month's first
                    // entry as the max.
                }

                while (min_of_current_month.isNaN())    {
                    if (record[next_row][UPDATED_MONTH_POSITION] == record[next_row + 1][UPDATED_MONTH_POSITION]) { // Checks if the next
                        // entry of the same month and year has an valid number to use as the min for further comparison.
                        min_of_current_month = record[next_row + 1][AVERAGE_POSITION];
                        next_row++;
                    }
                    else    { // Prevents the current row with the invalid entry to use a future row that has a 
                        // different month or year to use as the min, otherwise it would affect the integrity of
                        // the data.
                        min_of_current_month = 0.0;
                    }
                }
                // While statement follows same principle and reasoning as while statement directly above.
                while (max_of_current_month.isNaN())    {
                    if (record[next_row][UPDATED_MONTH_POSITION] == record[next_row + 1][UPDATED_MONTH_POSITION]) {
                        max_of_current_month = record[next_row + 1][AVERAGE_POSITION];
                        next_row++;
                    }
                    else    {
                        max_of_current_month = 0.0;
                    }
                }

                current_row++;
                updated_number_of_rows++;
            }
        }
        return calculations;
    } // ends calculateInfo


    /**
     * Writes the results to UPDATED_RAINFALL_DATA_FILE.
     * Precondition:  The file the data is being written to is accessible and editable.
     * @param data the 2D array containing the calculations for each month from RAINFALL_DATA_FILE.
     * @return no return.
     */
    public static void writeToFile(double[][] data) {
        File write_file = UPDATED_RAINFALL_DATA_FILE;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(write_file))) {
            StringBuilder sb = new StringBuilder(); // sb represents the values being written to UPDATED_RAINFALL_DATA_FILE.
            // Integer used for checking the total number of rows that have been written to UPDATED_RAINFALL_DATA_FILE.

            sb.append("Year, Month, Rainfall total, Min, Max\n");

            for (int data_cell = 0; data_cell < updated_number_of_rows; data_cell++) {
                double[] row = data[data_cell]; // For loop for each row of data.
                for (double entry : row) { // For loop for each value within the first array of the 2D array.
                    sb.append(entry);
                    sb.append(",");
                }
                sb.append("\n");
            }
            writer.write(sb.toString()); // Writes sb to UPDATED_RAINFALL_DATA_FILE.

        } catch (IOException e)   { // Alerts user if there is an issue writing data
            System.out.println("Data could not be written to a file.");
        }
    } // ends writeToFile
}