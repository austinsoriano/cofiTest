# COFI Coding Test (Quandl WIKI Stock Price Program)

## Requirements
There are few requirements needed to run this program:

* Mac(Unix)/Linux
* Java 8
* JSON.simple (External Library: jar file) [Link to Download JAR](https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple/1.1.1)

## Project Setup
To setup the program, please follow these steps:

1. Clone repository or Download Project
2. Ensure that the 'json-simple-1.1.1.jar' (JSON.simple) external library is in the project directory. If not, download it from the link above and place it in the project directory. The compile command will compile with the classpath of the required external library.
3. '**cd**' into project directory

## Running the Program
To run the program, follow these steps:
 
1. Run the command '**./runApp.sh**'
2. When prompted, type the Ticker Names of the securities/companies you are interested in and press enter. For this coding test, use 'cof googl msft'.
3. When prompted, type the additional features that you would like to run and press enter. For this coding test, the choices include:
   * **--help**
   * **daily-max-profit**
   * **busy-day**
   * **biggest-loser**
   
NOTE: Running '**./runApp.sh**' will automatically compile the necessary Java files to run the Java portion of the program. You will NOT need to compile the program yourself.
   
## How It Works
The program will read from the bash script the user inputs (securities/companies to use and additional features to run), then use the provided Quandl WIKI Stock Price API to download the data into JSON files. From there, the script will compile and run the provided Java program, which will sort the data and make calculations on the data to provide the required output. 
