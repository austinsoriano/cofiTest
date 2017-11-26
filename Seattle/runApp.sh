#!/bin/bash

echo

BASE_DIR=$(pwd)

echo "Welcome to Austin Soriano's Stock Price Lookup Application!"

echo

read -p "Please enter the securities/companies you would like stock prices for (please separate each company by spaces and capitlize): " -a companies

echo

read -p "What additional feature(s) would you like? (Enter '--help' for list of acceptable arguments. No arguments prints averages): " -a features

echo

while [ "${features[0]}" == '--help' ]
do
    echo "Additional Feature Arguments: "
    echo
    echo -e "max-daily-profit\t Prints out the highest amount of profit for each security/company"
    echo -e "busy-day\t\t Prints out all days with their volumes of unusual high activitiy for each security/company"
    echo -e "biggest-loser\t\t Prints out the security/company with the most days where the closing price was lower than the opening price"
    echo
    read -p "What additional feature(s) would you like? (Enter '--help' for list of acceptable arguments. No arguments prints averages): " -a features
    echo
done

read -p "Would you like to download the data files? Must re-download if changing/removing any previous securities/companies from the last run. (Enter 'yes' or 'no'): " answer

echo

if [ $answer == 'yes' ]; then
    rm -rf $BASE_DIR/json

    mkdir $BASE_DIR/json

    for i in "${companies[@]}"
    do
        echo "We are grabbing ${i} data for you, please wait..."
        echo
        echo ${i}
        echo
        curl "https://www.quandl.com/api/v3/datasets/WIKI/${i}.json?column_index=1&start_date=2017-01-01&end_date=2017-06-30&collapse=daily&api_key=s-GMZ_xkw6CrkGYUWs1p" >> json/${i}Open.json
        curl "https://www.quandl.com/api/v3/datasets/WIKI/${i}.json?column_index=4&start_date=2017-01-01&end_date=2017-06-30&collapse=daily&api_key=s-GMZ_xkw6CrkGYUWs1p" >> json/${i}Close.json
        curl "https://www.quandl.com/api/v3/datasets/WIKI/${i}.json?column_index=2&start_date=2017-01-01&end_date=2017-06-30&collapse=daily&api_key=s-GMZ_xkw6CrkGYUWs1p" >> json/${i}High.json
        curl "https://www.quandl.com/api/v3/datasets/WIKI/${i}.json?column_index=3&start_date=2017-01-01&end_date=2017-06-30&collapse=daily&api_key=s-GMZ_xkw6CrkGYUWs1p" >> json/${i}Low.json
        curl "https://www.quandl.com/api/v3/datasets/WIKI/${i}.json?column_index=5&start_date=2017-01-01&end_date=2017-06-30&collapse=daily&api_key=s-GMZ_xkw6CrkGYUWs1p" >> json/${i}Volume.json
        echo
    done
fi

javac -classpath $BASE_DIR/json-simple-1.1.1.jar Prices.java

java -classpath .:$BASE_DIR/json-simple-1.1.1.jar Prices "${features[@]}"

