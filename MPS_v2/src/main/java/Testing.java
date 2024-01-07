import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Testing {

    public int getNrCores() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.availableProcessors();
    }

    public void check() {
        int nrCores = getNrCores();

        AtomicInteger inQueue = new AtomicInteger(0);
        ExecutorService tpe = Executors.newFixedThreadPool(nrCores);

        for (int i = 1; i <= 100; i++) {
            inQueue.incrementAndGet();
            tpe.submit(new ProcessFile(tpe, inQueue, i));
        }
    }


}

class ProcessFile implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(GenerateTrees.class);
    private ExecutorService tpe;
    private AtomicInteger inQueue;
    private int indexFile;
    private static Map<String, Double> fileAverages = new HashMap<>();


    public ProcessFile(ExecutorService tpe, AtomicInteger inQueue, int indexFile) {
        this.tpe = tpe;
        this.inQueue = inQueue;
        this.indexFile = indexFile;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    "MPS_v2\\src\\main\\resources\\TreeNr" + indexFile + "Values"));
            checkOptimization(reader, "MPS_v2\\src\\main\\resources\\TreeNr" + indexFile);
            int left = inQueue.decrementAndGet();
            if (left == 0) {
                reader.close();
                tpe.shutdown();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        printAveragesAboveMean();
    }


    public void printAveragesAboveMean() {
        if (fileAverages.isEmpty()) {
            System.out.println("No scores available.");
            return;
        }

        double overallMean = fileAverages.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        
        Map<String, Double> filteredAverages = fileAverages.entrySet().stream()
                .filter(entry -> entry.getValue() > overallMean)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Double> sortedAverages = filteredAverages.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        System.out.println("\nThe top 10 files with the highest scores above the mean are:\n");
        int count = 0;
        List<String> top10Files = new ArrayList<>();
        for (Map.Entry<String, Double> entry : sortedAboveMeanAverages.entrySet()) {
            try {
                Files.deleteIfExists("MPS_v2\\src\\main\\resources\\TreeNr" + entry.getValues() + "Values");
                System.out.println("File " + entry.getKey() + " has been deleted.");
            } catch (IOException e) {
                System.out.println("Unable to delete file " + entry.getKey() + ": " + e.getMessage());
            }

            if (count < 10) {
                System.out.println("File: " + entry.getKey() + ", Average: " + entry.getValue());
                top10Files.add(entry.getKey());
                count++;
            } else {
                break;
            }
        }

        for (Map.Entry<String, Double> entry : aboveMeanAverages.entrySet()) {
            if (!top10Files.contains(entry.getKey())) {
                try {
                    Files.deleteIfExists("MPS_v2\\src\\main\\resources\\TreeNr" + entry.getValues());
                    System.out.println("File " + entry.getKey() + " has been deleted.");
                } catch (IOException e) {
                    System.out.println("Unable to delete file " + entry.getKey() + ": " + e.getMessage());
                }
            }
        }
    }


    public void checkOptimization(BufferedReader reader, String pathName) throws IOException {
        Map<Integer, Double> globalTrain = readGlobalTrainData(reader);
        processGlobalTrain(globalTrain);

        String LUTTest = "MPS_v2\\src\\main\\resources\\LuTTest.csv";
        main.java.CsvReader csvReader = new main.java.CsvReader();
        Map<Integer, List<Double>> lutTrain = csvReader.readDataFromCsv(LUTTest);

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

        Path path_object = Paths.get(pathName);

        String fileName = path_object.getFileName().toString();

        if (!foundValues.isEmpty()) {
            double sum = 0;
            for (double foundValue : foundValues) {
                sum += foundValue;
            }
            double average = sum / foundValues.size();
            fileAverages.put(fileName, average);

            System.out.println("The score for the file " + fileName + " is: " + average);
        } else {
            System.out.println("A score could not be calculated for the file " + fileName);
        }
    }

    public Map<Integer, Double> readGlobalTrainData(BufferedReader reader) throws IOException {
        Map<Integer, Double> map = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            int key = Integer.parseInt(parts[0]);
            double value = Double.parseDouble(parts[1]);
            map.put(key, value);
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

