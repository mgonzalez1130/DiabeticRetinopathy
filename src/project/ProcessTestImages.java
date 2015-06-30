package project;

import java.io.File;
import java.io.IOException;

public class ProcessTestImages {
    /**
     * Takes three arguments.<br>
     * First: The path to the directory containing all of the images to be
     * processed<br>
     * Second: The name of the file where the results of the image processing
     * will be stored<br>
     *
     * @param args
     *            command line arguments
     */
    public static void main(String args[]) {

        if (args.length != 2) {
            throw new Error("Incorrect number of arguments");
        }

        // first argument: "C:/Users/Moses/Desktop/data/sampleTest"
        // second argument: "C:/Users/Moses/Desktop/data/sampleTestData.csv"

        String imageDirectory = args[0];
        String testDataFile = args[1];

        // delete and recreate the trainDataFile so that no old data is kept
        File tdf = new File(testDataFile);
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
                testDataFile);

        File folder = new File(imageDirectory);
        File[] files = folder.listFiles();

        for (File image : files) {
            System.out.println("Processing test image: " + image.getName());
            iProcessor.processImage(image.getName(), -1, false);
        }

        System.out.println("Finished processing images");

    }
}
