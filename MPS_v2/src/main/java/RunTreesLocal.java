import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class GroundTruth {
    public int truePositive;
    public int falsePositive;
    public int trueNegative;
    public int falseNegative;
}

public class RunTreesLocal {

    private static Logger log = LoggerFactory.getLogger(RunTreesLocal.class);

    public int getNrCores() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.availableProcessors();
    }

    public GroundTruth runTreesLocal(String treePath, ArrayList<Pixel> image) {
        int nrCores = getNrCores();

        log.info("Running trees with pixels values...");
        AtomicInteger inQueuePixel = new AtomicInteger(0);
        ExecutorService tpePixel = Executors.newFixedThreadPool(nrCores);
        GroundTruth groundTruths = new GroundTruth();

        for (Pixel pixel : image) {
            inQueuePixel.incrementAndGet();
            tpePixel.submit(new RunTreeLocal(tpePixel, inQueuePixel, treePath, pixel, groundTruths));
        }
        while(!tpePixel.isShutdown()) {}
        return groundTruths;
    }
}

class RunTreeLocal implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(GenerateTrees.class);
    private ExecutorService tpe;
    private AtomicInteger inQueue;
    private final String treePath;
    private final Pixel pixel;
    private final GroundTruth groundTruths;

    public RunTreeLocal(ExecutorService tpe, AtomicInteger inQueue, String treePath,
                        Pixel pixel, GroundTruth groundTruths) {
        this.tpe = tpe;
        this.inQueue = inQueue;
        this.treePath = treePath;
        this.pixel = pixel;
        this.groundTruths = groundTruths;
    }

    public double applyFunction(int nrArgs, ArrayList<Double> args, String function)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (nrArgs == 1) {
            Class<?> cls = main.java.Operations.class;
            Method method = cls.getMethod(function, double.class);
            return (double) method.invoke(new main.java.Operations(), args.get(0));
        } else if (nrArgs == 2) {
            Class<?> cls = main.java.Operations.class;
            Method method = cls.getMethod(function, double.class, double.class);
            return (double) method.invoke(new main.java.Operations(), args.get(0), args.get(1));
        } else {
            Class<?> cls = main.java.Operations.class;
            Method method = cls.getMethod(function, ArrayList.class);
            return (double) method.invoke(new main.java.Operations(), args);
        }
    }

    public double calculateTreeValue(BufferedReader reader) throws IOException,
            InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String line;
        Random random = new Random();
        ArrayList<Double> currentLevel = new ArrayList<>();
        currentLevel.addAll(pixel.pixelMap);
        ArrayList<Double> newLevel = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            newLevel.clear();
            String[] tokens = line.split(" ");

            for (int i = 0; i < tokens.length; i += 2) {
                int nrArgs = Integer.parseInt(tokens[i + 1]);
                ArrayList<Double> args = new ArrayList<>();
                for (int j = 0; j < nrArgs; j++) {
                    args.add(currentLevel.remove(random.nextInt(currentLevel.size())));
                }
                newLevel.add(applyFunction(nrArgs, args, tokens[i]));
            }
            currentLevel.addAll(newLevel);
        }
        return newLevel.get(0);
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(treePath));
            double value = calculateTreeValue(reader);
            int color = (pixel.value > value) ? 1 : 0;

            synchronized (groundTruths) {
                if (pixel.groundTruth == 1 && pixel.groundTruth == color) {
                    groundTruths.truePositive++;
                } else if (pixel.groundTruth == 1 && pixel.groundTruth != color) {
                    groundTruths.falsePositive++;
                } else if (pixel.groundTruth == 0 && pixel.groundTruth == color) {
                    groundTruths.trueNegative++;
                } else {
                    groundTruths.falseNegative++;
                }
            }
            int left = inQueue.decrementAndGet();
            if (left == 0) {
                tpe.shutdown();
                reader.close();
            }
        } catch (IOException | InvocationTargetException
                 | NoSuchMethodException | IllegalAccessException e) {
            log.error("Can not write, read or close a file or wrong call of a function");
            throw new RuntimeException(e);
        }
    }
}
