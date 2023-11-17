import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that reads the data from a .csv file and parse it into a structure that we will use in
 * testing our trees.
 * There is only one function that takes as parameter the path to the .csv we want to parse.
 */
public class CsvReader {
    String REGEX = ",";

    public Map<Integer, List<Double>> readDataFromCsv(String path) {

        Map<Integer, List<Double>> data = new HashMap<>();
        BufferedReader reader;

        long numberOfLines;
        int counter = 1;

        try {

            reader = new BufferedReader(new FileReader(path));
            numberOfLines = Files.lines(Paths.get(path)).count() - 1;

            for(int i = 1; i <= numberOfLines; i++) {
                data.put(i, new ArrayList<>());
            }

            // It says that this is redundant, don't listen to that and don't change it :)
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] tokens = line.split(REGEX);

                for(String token : tokens) {
                    data.get(counter).add(Double.parseDouble(token));
                }
                counter++;
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;

    }
}
