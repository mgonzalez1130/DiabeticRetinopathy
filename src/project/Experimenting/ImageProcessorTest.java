package project.Experimenting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import project.ImageProcessor;

public class ImageProcessorTest {

    public static void main(String args[]) {
        long startTime = System.currentTimeMillis();

        String loadDirectory = "C:/Users/Moses/Desktop/data/sample_train";
        String saveFile = "C:/Users/Moses/Desktop/data/sampleTrainData.csv";
        File trainLabels = new File(
                "C:/Users/Moses/Desktop/data/sampleTrainLabels.csv");

        ImageProcessor iProcessor = new ImageProcessor(loadDirectory, saveFile);

        try (BufferedReader br = new BufferedReader(new FileReader(trainLabels))) {
            String line = br.readLine(); // skip the header line
            while ((line = br.readLine()) != null) {
                String[] splitLine = line.split(",");
                System.out.println("Processing image: " + splitLine[0]);
                iProcessor.processImage(splitLine[0] + ".jpeg",
                        Integer.parseInt(splitLine[1]), true);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Finished processing images");
        System.out.println("Runtime: "
                + (System.currentTimeMillis() - startTime));

    }
}
