package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ProcessTrainImages {

    private static final int ID_INDEX = 0;
    private static final int LABEL_INDEX = 1;

    /**
     * Takes three arguments.<br>
     * First: The path to the directory containing all of the images to be
     * processed<br>
     * Second: The path to the file where the results of the image processing
     * will be stored<br>
     * Third: The path to the file containing the labels for all of the train
     * images
     *
     * @param args
     *            command line arguments
     */
    public static void main(String args[]) {

        if (args.length != 3) {
            throw new Error("Incorrect number of arguments");
        }

        // String imageDirectory = "C:/Users/Moses/Desktop/data/train";
        // String trainDataFile =
        // "C:/Users/Moses/Desktop/data/sampleTrainData.csv";
        // File trainLabels = new File(
        // "C:/Users/Moses/Desktop/data/sampleTrainLabels.csv");

        String imageDirectory = args[0];
        String trainDataFile = args[1];
        File trainLabels = new File(args[2]);

        // delete and recreate the trainDataFile so that no old data is kept
        File tdf = new File(trainDataFile);
        if (tdf.exists()) {
            tdf.delete();
            try {
                tdf.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // process the images in the specified directory
        ImageProcessor iProcessor = new ImageProcessor(imageDirectory,
                trainDataFile);

        try (BufferedReader br = new BufferedReader(new FileReader(trainLabels))) {
            String line = br.readLine(); // skip the header line
            while ((line = br.readLine()) != null) {
                String[] splitLine = line.split(",");
                System.out.println("Processing image: " + splitLine[ID_INDEX]);
                iProcessor.processImage(splitLine[ID_INDEX] + ".jpeg",
                        Integer.parseInt(splitLine[LABEL_INDEX]), true);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Finished processing images");

    }

}
