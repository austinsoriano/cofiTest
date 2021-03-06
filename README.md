# COFI Coding Test (Quandl WIKI Stock Price Program)

## Requirements
There are few requirements needed to run this program:

* Mac(Unix)/Linux
* Java 8 or Higher
* JSON.simple (External Library: jar file) File should be included in Repository [Link to Download JAR if Needed](https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple/1.1.1)

## Project Setup
To setup the program, please follow these steps:

1. Clone repository or Download Project
2. Ensure that the '**json-simple-1.1.1.jar**' (JSON.simple) external library is in the project directory where the other program files are stored (runApp.sh & Prices.java). If not, download it from the link above and place it in the project directory. The java compile command within the bash script will include the classpath of the required external library.
3. '**cd**' into project directory. Essentially where the project folder exists on your computer. 
4. If you are on the COF network, ensure that your proxy is on before running the program. If not on the COF network, do NOT turn on your proxy. Depending on the network, your data files will not be downloaded and the program will not work. 

## Running the Program
To run the program, follow these steps:
 
1. Run the command '**chmod 777 runApp.sh**' so that the program is executable
2. Run the command '**./runApp.sh**'
3. When prompted, type the Ticker Names of the securities/companies you are interested in and press enter. For this coding test, use '**cof googl msft**'.
4. When prompted, type the additional features that you would like to run and press enter. For this coding test, the choices include:
   * **--help**
   * **daily-max-profit**
   * **busy-day**
   * **biggest-loser**
   You can also hit '**Enter**' right away to just output the opening and closing average prices.
5. When prompted, type either '**yes**' or '**no**' if you would like to download the data files? If running for the first time, you should download the files. You should also re-download the files (type '**yes**') if any arguments are changed from the last run of the program.
   
NOTE: Running '**./runApp.sh**' will automatically compile the necessary Java files to run the Java portion of the program. You will NOT need to compile the program yourself.
   
## How It Works
The program will read from the bash script the user inputs (securities/companies to use, additional features to run, and download/re-download the files), then use the provided Quandl WIKI Stock Price API to download the data into JSON files if selecting to do so. From there, the script will compile and run the provided Java program, which will sort the data and make calculations on the data to provide the required output. 
