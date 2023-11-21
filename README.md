# Rainfall Visualiser
 
## Description
This project is a JavaFX program that retrieves data from a chosen csv file and displays the data via a bar graph after the data is cleaned and processed. The data being processed in this project is based around the rainfall amounts received in multiple locations in Queensland. Each location has its own csv file to hold their data and each csv can be selected from the drop-down menu. 

The bar graph is able to be clicked on and show the data for the clicked-on month and year. Once the user selects a location and clicks on the 'Visualise' button, the graph will be generated. If the user clicks on a bar, the following data will be printed for that selected bar: month, year, minimum, maximum and total rainfall. The last 3 bits of data are recorded in millimetres. 

To show the additional error checking and functionality of the app, I included several more csv files to show the program's ability to prevent errors such as: incorrect header names in the csv files, incorrect order of the headers, missing csv files. 

This project was assigned by James Cook University as part of my Bachelor of Information Technlogy degree during the second semester of my second year.

## Development and Timeframe
This program was created using IntelliJ and JavaFX. There were some initial difficulties with setting up JavaFX, but these issues only lasted roughly a few hours. 

If I were to continue working on this project, I would like to implement the following additions and modifications:
1. Allow the program to read fully dynamic data using keywords to look for in the columns' header names. 
2. Update the design and look.

The timeframe for this project was 2 weeks where I assigned 1-2 hours a day. This timeframe included pseudo-coding, designing, coding, troubleshooting and refining. Data cleaning and data procesing is done automatically with the RainfallAnalyser class. 

## How to run
1. Install IntelliJ from [here](https://www.jetbrains.com/idea/download/?section=windows)
2. Install JavaFX from [here](https://gluonhq.com/products/javafx/)
3. Open the 'src' folder in IntelliJ.
4. Ensure the following has been set.
   i. Configure IntelliJ to use JavaFX.
      a. Go to "File" > "Project Structure."
      b. Under "Project," set the "Project SDK" to JDK 19.
      c. Under "Project," set the "Project language level" to the appropriate level for your code.
      d. Under "Libraries," click the "+" button to add a new library.
      e. Choose "Java" and add the lib directory from the extracted JavaFX SDK.
   ii. Configure VM Options (for JDK 11 and later)
      a. In the Run Configuration, go to the "Configuration" tab.
      b. In the "VM options" field, add the following (replace the path with the actual path to your JavaFX SDK):
         --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
5. Run RainfallVisualiser.java.

## Contributions 
This project was solely programmed and completed by me. 

## Where to find more
To find out more on this project, please find the project here on [LinkedIn.](https://www.linkedin.com/in/jay-davis-261738277/details/projects/)
