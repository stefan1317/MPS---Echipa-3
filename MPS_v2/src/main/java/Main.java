import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        CsvReader csvReader = new CsvReader();

        String FILE_PATH = "src/main/resources/GlobalTest.csv";

        /*
            HashMap where the key is the number of the line (for each picture) and the value is a
            List where:
                1) index 0 is the binarization threshold after applying the OtsuT algorithm
                2) index 0 is the binarization threshold after applying the KittlerT algorithm
                ... and so on.
         */
        Map<Integer, List<Double>> dataFromCsv = csvReader.readDataFromCsv(FILE_PATH);

        System.out.println(dataFromCsv);
    }
}
