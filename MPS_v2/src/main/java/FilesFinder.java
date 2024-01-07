import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FilesFinder {

    private static Logger log = LoggerFactory.getLogger(FilesFinder.class);

    public ArrayList<String> getTreeFiles(String directoryPath) {
        File directory = new File(directoryPath);
        ArrayList<String> treeFilesPaths = new ArrayList<>();
        // check if given path is a directory
        if (directory.isDirectory()) {
            File[] treeFiles = directory.listFiles();

            if (treeFiles != null) {
                for (File treeFile : treeFiles) {
                    if (treeFile.isDirectory() || !treeFile.getName().contains("TreeNr")) {
                        continue;
                    }
                    treeFilesPaths.add(treeFile.getAbsolutePath());
                }
                return treeFilesPaths;
            } else {
                log.error("Directory is empty");
                return null;
            }
        } else {
            log.error("Given path does not indicate to a directory");
            return null;
        }
    }


    public Map<String, ArrayList<Pixel>> getCsvFilesData(String directoryPath) {
        File directory = new File(directoryPath);
        Map<String, ArrayList<Pixel>> csvFilesData = new HashMap<>();
        // check if given path is a directory
        if (directory.isDirectory()) {
            File[] csvFiles = directory.listFiles();

            if (csvFiles != null) {
                for (File csvFile : csvFiles) {
                    PixelReader pixelReader = new PixelReader();
                    ArrayList<Pixel> image = (ArrayList<Pixel>) pixelReader
                            .readPixelFromCsv(csvFile.getAbsolutePath());
                    csvFilesData.put(csvFile.getAbsolutePath(), image);
                }
                return csvFilesData;
            } else {
                log.error("Directory is empty");
                return null;
            }
        } else {
            log.error("Given path does not indicate to a directory");
            return null;
        }
    }
}
