import main.java.Operations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class GenerateTrees {

    private static Logger log = LoggerFactory.getLogger(GenerateTrees.class);
    private Operations operations;

    public GenerateTrees() {
        operations = new Operations();
    }

    public double applyFunction(int nrArgs, ArrayList<Double> args, FileWriter fWriter)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Random random = new Random();
        if (nrArgs == 1) {
            String function = operations.oneArgFunctions.get(random
                    .nextInt(operations.oneArgFunctions.size()));
            fWriter.append(function).append(" ").append(String.valueOf(nrArgs)).append(" ");
            Class<?> cls = Operations.class;
            Method method = cls.getMethod(function, double.class);
            return (double) method.invoke(operations, args.get(0));
        } else if (nrArgs == 2) {
            String function = operations.twoArgFunctions.get(random
                    .nextInt(operations.twoArgFunctions.size()));
            fWriter.append(function).append(" ").append(String.valueOf(nrArgs)).append(" ");
            Class<?> cls = Operations.class;
            Method method = cls.getMethod(function, double.class, double.class);
            return (double) method.invoke(operations, args.get(0), args.get(1));
        } else {
            String function = operations.multipleArgFunctions.get(random
                    .nextInt(operations.multipleArgFunctions.size()));
            fWriter.append(function).append(" ").append(String.valueOf(nrArgs)).append(" ");
            Class<?> cls = Operations.class;
            Method method = cls.getMethod(function, ArrayList.class);
            return (double) method.invoke(operations, args);
        }
    }

    public void generateTree(ArrayList<Double> testValues, int indexFile) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, IOException {
        Random random = new Random();
        // the tree has 10 leaves
        for (int i = 0; i < 5; i++) {
            testValues.remove(random.nextInt(15 - i));
        }
        ArrayList<Double> currentLevel = new ArrayList(testValues);
        ArrayList<Double> newLevel = new ArrayList();
        FileWriter fWriter = new FileWriter("MPS_v2\\src\\main\\resources\\TreeNr" + indexFile);
        while (newLevel.size() != 1) {
            newLevel.clear();
            while (currentLevel.size() != 0) {
                int nrArgs;
                if (currentLevel.size() < 5) {
                    nrArgs = random.nextInt(currentLevel.size()) + 1;
                } else {
                    nrArgs = random.nextInt(currentLevel.size() - 4) + 1;
                }
                ArrayList<Double> args = new ArrayList<>();
                for (int i = 0; i < nrArgs; i++) {
                    args.add(currentLevel.remove(random.nextInt(currentLevel.size())));
                }
                newLevel.add(applyFunction(nrArgs, args, fWriter));
            }
            currentLevel.addAll(newLevel);
            fWriter.append("\n");
        }
        fWriter.close();
    }
}