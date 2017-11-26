import java.util.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;

class Prices {
    public static void main(String[] args) throws Exception {
        run(args);
    }

    public static void run(String[] args) throws Exception {
        File[] files = grabFiles();
        JSONParser parser = new JSONParser();
        Map<String, Map<String, Map<String, Double>>> companyData = 
            new TreeMap<String, Map<String, Map<String, Double>>>();
        for (File file : files) {
            if (file.getAbsolutePath().contains(".json")) {
                JSONObject dataset = grabDataSet(parser, file);
                String tickerName = (String) dataset.get("dataset_code");
                String columnName = (String) ((JSONArray) dataset.get("column_names")).get(1);
                JSONArray dataArray = (JSONArray) dataset.get("data");
                if (!companyData.containsKey(tickerName)) {
                    companyData.put(tickerName, new HashMap<String, Map<String, Double>>());
                }
                Map<String, Double> data = getData(dataArray);
                if (!companyData.get(tickerName).containsKey(columnName)) {
                    companyData.get(tickerName).put(columnName, data);
                }
            }
        }
        getAverages(companyData);
        List<String> features = Arrays.asList(args);
        runAdditionalFeatures(companyData, features);
    }

    private static File[] grabFiles() {
        File directory = null;
        File[] files = null;
        try {
            String workingDir = System.getProperty("user.dir");
            directory = new File(workingDir + "/json");
            files = directory.listFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    private static JSONObject grabDataSet(JSONParser parser, File file) throws Exception {
        Object obj = parser.parse(new FileReader(file.getAbsolutePath()));
        JSONObject jsonObject = (JSONObject) obj;
        return (JSONObject) jsonObject.get("dataset");
    }

    private static Map<String, Double> getData(JSONArray dataArray) {
        Map<String, Double> prices = new TreeMap<String, Double>();
        for (int i = 0; i < dataArray.size(); i++) {
            JSONArray dateAndPrice = (JSONArray) dataArray.get(i);
            prices.put((String) dateAndPrice.get(0), (double) dateAndPrice.get(1));
        }
        return prices;
    }

    private static void getAverages(Map<String, Map<String, Map<String, Double>>> companyData) {
        for (String company : companyData.keySet()) {
            double[] openAverages = calculateAverages(companyData.get(company).get("Open"));
            double[] closeAverages = calculateAverages(companyData.get(company).get("Close"));
            printAverages(company, openAverages, closeAverages);
        }
    }

    private static double[] calculateAverages(Map<String, Double> columnData) {
        double[] prices = { 0, 0, 0, 0, 0, 0, 0 };
        double[] counts = { 0, 0, 0, 0, 0, 0, 0 };
        for (String date : columnData.keySet()) {
            String[] splitDate = date.split("-");
            double priceForTheDay = columnData.get(date);
            counts[Integer.parseInt(splitDate[1])]++;
            prices[Integer.parseInt(splitDate[1])] += priceForTheDay;
        }
        for (int i = 1; i < prices.length; i++) {
            prices[i] /= counts[i];
        }
        return prices;
    }

private static void printAverages(String company, double[] open, double[] close) {
    String[] months = {"", "2017-01", "2017-02", "2017-03", "2017-04", "2017-05", "2017-06"};
    System.out.println(company);
    for(int i = 1; i < months.length; i++) {
        String openAvg = String.format("%.2f",
        Math.round(open[i] * 100.00) / 100.00);
        String closeAvg = String.format("%.2f",
        Math.round(close[i] * 100.00) / 100.00);
        System.out.println("Month:" + months[i] + " | average_open: $" + openAvg + 
            " | average_close: $" + closeAvg);
    }
    System.out.println();
}

    private static void findBiggestLoser(Map<String, Map<String, Map<String, Double>>> companyData) {
        String biggestLoser = "";
        int currentCompany = 0;
        int currentLoserCount = 0;
        for (String company : companyData.keySet()) {
            for (String date : companyData.get(company).get("Close").keySet()) {
                if (companyData.get(company).get("Open").containsKey(date)) {
                    if (companyData.get(company).get("Close").get(date) < 
                            companyData.get(company).get("Open").get(date)) {
                        currentCompany++;
                    }
                }
            }
            if (currentCompany > currentLoserCount) {
                currentLoserCount = currentCompany;
                biggestLoser = company;
            }
            currentCompany = 0;
        }
        System.out.println("Biggest Loser: " + biggestLoser + " w/ " + currentLoserCount + " days");
        System.out.println();
    }

    private static void maxDailyProfit(Map<String, Map<String, Map<String, Double>>> companyData) {
        for (String company : companyData.keySet()) {
            String maxProfitDate = "";
            double maxProfit = 0.0;
            for (String date : companyData.get(company).get("High").keySet()) {
                if (companyData.get(company).get("Low").containsKey(date)) {
                    double diff = companyData.get(company).get("High").get(date)
                            - companyData.get(company).get("Low").get(date);
                    if (diff > maxProfit) {
                        maxProfit = diff;
                        maxProfitDate = date;
                    }
                }
            }
            String max = String.format("%.2f", Math.round(maxProfit * 100.00) / 100.00);
            System.out.println("Max Daily Profit for " + company + ": $" + max + 
                " on " + maxProfitDate);
        }
        System.out.println();
    }

    private static void busyDays(Map<String, Map<String, Map<String, Double>>> companyData) {
        for (String company : companyData.keySet()) {
            long companyAverageVolume = 0;
            long tenPercentMore = 0;
            Map<String, Double> busyDays = new TreeMap<String, Double>();
            companyAverageVolume = (long) getAverageVolume(companyData, company, companyAverageVolume);
            tenPercentMore = (long) (companyAverageVolume + (companyAverageVolume * 0.1));
            busyDays = findBusyDays(companyData, busyDays, company, tenPercentMore);
            printOutBusyDays(busyDays, company,
                    String.format("%.2f", Math.round(companyAverageVolume * 100.00) / 100.00));
            System.out.println();
            busyDays.clear();
        }
    }

    private static double getAverageVolume(Map<String, Map<String, Map<String, Double>>> companyData, 
            String company, long companyAverageVolume) {
        int totalDays = 0;
        long cumulative = 0;
        for (String date : companyData.get(company).get("Volume").keySet()) {
            cumulative += companyData.get(company).get("Volume").get(date);
            totalDays++;
        }
        companyAverageVolume = cumulative / totalDays;
        return companyAverageVolume;
    }

    private static Map<String, Double> findBusyDays(
            Map<String, Map<String, Map<String, Double>>> companyData,
            Map<String, Double> busyDays, String company, long tenPercentMore) {
        for (String date : companyData.get(company).get("Volume").keySet()) {
            if (companyData.get(company).get("Volume").get(date) > tenPercentMore) {
                busyDays.put(date, companyData.get(company).get("Volume").get(date));
            }
        }
        return busyDays;
    }

    private static void printOutBusyDays(Map<String, Double> busyDays, String company, 
            String averageVolume) {
        if (!busyDays.isEmpty()) {
            System.out.println(company + " Busy Days");
            System.out.println("Average Volume: " + averageVolume);
            System.out.print("Days: {");
            for (String date : busyDays.keySet()) {
                System.out.print(" [" + date + ", " + busyDays.get(date) + "] ");
            }
            System.out.println("}");
        }
    }

    private static void runAdditionalFeatures(
            Map<String, Map<String, Map<String, Double>>> companyData, List<String> features) {
        if (features.contains("max-daily-profit")) {
            maxDailyProfit(companyData);
        }
        if (features.contains("busy-day")) {
            busyDays(companyData);
        }
        if (features.contains("biggest-loser")) {
            findBiggestLoser(companyData);
        }
    }
}