package main.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Testing
{
    public static void CheckOptimization(String path) throws IOException {
        Map<Integer, Double> globalTrain = readGlobalTrainData(path);
        processGlobalTrain(globalTrain);

        String LUTTrain = "MPS_v2\\src\\main\\resources\\LuTTrain.csv";
        main.java.CsvReader csvReader = new main.java.CsvReader();
        Map<Integer, List<Double>> lutTrain = csvReader.readDataFromCsv(LUTTrain);

        List<Double> foundValues = new ArrayList<>();


        for (Map.Entry<Integer, Double> entry : globalTrain.entrySet()) {
            int key = entry.getKey();
            double value = entry.getValue();
            if (lutTrain.containsKey(key)) {
                List<Double> lutValues = lutTrain.get(key);
                int index = (int) value - 1;
                if (index >= 0 && index < lutValues.size()) {
                    double foundValue = lutValues.get(index);
                    foundValues.add(foundValue);
                }
            }
        }

        Path path_object = Paths.get(path);

        String fileName = path_object.getFileName().toString();

        if (!foundValues.isEmpty()) {
            double sum = 0;
            for (double foundValue : foundValues) {
                sum += foundValue;
            }
            double average = sum / foundValues.size();

            System.out.println("The score for the file " + fileName + " is: " + average);
        } else {
            System.out.println("A score could not be calculated for the file " + fileName);
        }
    }

    public static Map<Integer, Double> readGlobalTrainData(String filePath) throws IOException {
        Map<Integer, Double> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                int key = Integer.parseInt(parts[0]);
                double value = Double.parseDouble(parts[1]);
                map.put(key, value);
            }
        }
        return map;
    }

    public static void processGlobalTrain(Map<Integer, Double> globalTrain) {
        for (Map.Entry<Integer, Double> entry : globalTrain.entrySet()) {
            double roundedValue = Math.round(entry.getValue() * 255);
            globalTrain.put(entry.getKey(), roundedValue);
        }
    }
}


