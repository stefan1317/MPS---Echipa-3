import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TestingLocal {
    public void checkLocal() {
        String filePath = "MPS_v2\\src\\main\\resources\\FMeasures";

        Map<String, Double> treeData = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    String fileName = parts[0];
                    double value = Double.parseDouble(parts[1]);
                    treeData.put(fileName, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(treeData.entrySet());
//        sortedEntries.sort(Comparator.comparing(Map.Entry::getValue).reversed());
        sortedEntries.sort(Comparator.comparing(Map.Entry<String, Double>::getValue).reversed());


        System.out.println("\nThe best three trees are:\n");
        int count = 0;
        for (Map.Entry<String, Double> entry : sortedEntries) {
            System.out.println(entry.getKey() + " " + entry.getValue());
            count++;
            if (count == 3) {
                break;
            }
        }

        for (int i = 3; i < sortedEntries.size(); i++) {
            String fileNameToDelete = sortedEntries.get(i).getKey();
            File fileToDelete = new File(fileNameToDelete);

            if (fileToDelete.exists()) {
                if (fileToDelete.delete()) {
                    System.out.println("File " + fileNameToDelete + " has been successfully deleted.");
                } else {
                    System.out.println("Error deleting file " + fileNameToDelete);
                }
            }
        }
    }
}
