package project;

import ij.ImagePlus;
import ij.gui.HistogramWindow;
import ij.io.Opener;
import ij.measure.ResultsTable;
import ij.plugin.ContrastEnhancer;
import ij.process.ImageConverter;
import imagescience.feature.Laplacian;
import imagescience.feature.Statistics;
import imagescience.image.ByteImage;
import imagescience.image.FloatImage;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ImageProcessor {

    // Each image is converted to grayscale, and 256 corresponds to the number
    // of distinct colors that each image will have afterwards
    private static final int START_INDEX = 256;

    // Fields for all of the different objects that will be used by the image
    // processor
    private Opener opener;
    private ImageConverter converter;
    private Laplacian laplacian;
    private ContrastEnhancer enhancer;
    private Statistics stats;

    private String loadDirectory; // the directory containing all the images
    private File saveFile; // the file where the result data will be stored

    /**
     * Constructor for an ImageProcessor. Handles the instantiation of all the
     * objects that the ImageProcessor will need in processing a given image.
     *
     * @param loadDirectory
     *            the directory containing all of the images to be processed
     * @param saveFileName
     *            the name of the file where the result data will be stored
     */
    public ImageProcessor(String loadDirectory, String saveFileName) {
        this.opener = new Opener();
        this.laplacian = new Laplacian();
        this.enhancer = new ContrastEnhancer();
        this.stats = new Statistics();
        this.loadDirectory = loadDirectory;
        this.saveFile = new File(saveFileName);
    }

    /**
     * Constructor for an ImageProcessor. Handles the instantiation of all the
     * objects that the ImageProcessor will need in processing a given image.
     *
     * @param saveFile
     *            The file where the result data will be stored
     */
    public ImageProcessor(File saveFile) {
        this.laplacian = new Laplacian();
        this.enhancer = new ContrastEnhancer();
        this.stats = new Statistics();
        this.saveFile = saveFile;
    }

    /**
     * Handles the process of converting the specified image to a list of values
     * that will be used for classification. The list of values is represented
     * by a String of comma-separated values, and is appended to the save file
     * immediately. All inputs must be non-null.
     *
     * @param fileName
     *            the filename of the image being processed
     * @param label
     *            the classification of the image being processed if it exists.
     *            It will be appended to the end of the list of values generated
     *            by the image processing if and only if the trainImage field is
     *            true
     * @param trainImage
     *            true if the given image is one that will be used for training,
     *            false otherwise
     */
    public void processImage(String fileName, int label, boolean trainImage) {
        ImagePlus image = opener.openImage(loadDirectory, fileName);

        // convert to grayscale
        converter = new ImageConverter(image);
        converter.convertToGray8();

        // apply FeatureJ Laplacian
        ImagePlus lapImage = laplacian.run(new ByteImage(image), 10)
                .imageplus();

        // Enhance contrast
        enhancer.stretchHistogram(lapImage, 5);

        // Get histogram
        HistogramWindow hw = new HistogramWindow(fileName, image, 200, -0.5,
                0.5);
        ResultsTable results = hw.getResultsTable();
        hw.close();

        // Get FeatureJ image statistics
        stats.run(new FloatImage(lapImage));

        // Add stats to results table
        int counter = START_INDEX;
        for (int stat = 1; stat <= 8192; stat *= 2) {
            results.setValue(0, counter, counter);
            results.setValue(1, counter, stats.get(stat));
            counter++;
        }

        // Add label to results table
        results.setValue(0, counter, counter);
        results.setValue(1, counter, label);

        // Add row to result file
        StringBuffer sb = new StringBuffer();
        sb.append(fileName.replace(".jpeg", ""));

        for (int row = 0; row < results.getCounter(); row++) {
            sb.append(",");
            sb.append(results.getValueAsDouble(1, row));
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile,
                true))) {
            bw.write(sb.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the process of converting the specified image to a list of values
     * that will be used for classification. The list of values is represented
     * by a String of comma-separated values, and is appended to the save file
     * immediately. All inputs must be non-null.
     *
     * @param fileName
     *            the filename of the image being processed
     * @param label
     *            the classification corresponding to the image being processed.
     *            It will be appended to the end of the list of values generated
     *            by the image processing if and only if the trainImage field is
     *            true
     * @param trainImage
     *            a boolean specifying whether or not the given image is one
     *            that will be used for training
     * @param buffImage
     *            the image to be processed
     */
    public void processImage(String fileName, int label, boolean trainImage,
            BufferedImage buffImage) {
        ImagePlus image = new ImagePlus();
        image.setImage(buffImage);

        // convert to grayscale
        converter = new ImageConverter(image);
        converter.convertToGray8();

        // apply FeatureJ Laplacian
        ImagePlus lapImage = laplacian.run(new ByteImage(image), 10)
                .imageplus();

        // Enhance contrast
        enhancer.stretchHistogram(lapImage, 5);

        // Get histogram
        HistogramWindow hw = new HistogramWindow(fileName, image, 200, -0.5,
                0.5);
        ResultsTable results = hw.getResultsTable();
        hw.close();

        // Get FeatureJ image statistics
        stats.run(new FloatImage(lapImage));

        // Add stats to results table. The constants that correspond to each
        // statistic start at 1 and increase by powers of 2 up to 8192
        int counter = START_INDEX;
        for (int stat = 1; stat <= 8192; stat *= 2) {
            results.setValue(0, counter, counter);
            results.setValue(1, counter, stats.get(stat));
            counter++;
        }

        // Add label to results table
        if (trainImage) {
            results.setValue(0, counter, counter);
            results.setValue(1, counter, label);
        }

        // Add row to result file
        StringBuffer sb = new StringBuffer();
        sb.append(fileName.replace(".jpeg", ""));

        for (int row = 0; row < results.getCounter(); row++) {
            sb.append(",");
            sb.append(results.getValueAsDouble(1, row));
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile,
                true))) {
            bw.write(sb.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
