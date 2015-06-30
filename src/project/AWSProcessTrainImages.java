package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.amazonaws.AmazonServiceException;

public class AWSProcessTrainImages {

    /**
     * Processes the images that the classifier will be trained on. It works by
     * reading in a csv file that maps each image to a classification, and
     * passing each image to the image processor. All files specified must be
     * stored in S3.
     *
     * Takes three arguments.<br>
     * First: The bucket name containing the images to be processed<br>
     * Second: The name of the file where the results of the image processing
     * will be stored<br>
     * Third: The name of the file containing the labels for all of the train
     * images. This file must be found in the top level of the bucket, not in
     * any sub-folders<br>
     * Fourth (optional): If a specific path must be followed to find the
     * directory containing the train images, specify the path. Start the path
     * with the folder name (not a "/"), and do not end the path with a "/".
     *
     * @param args
     *            command line arguments
     */
    public static void main(String args[]) {

        long startTime = System.currentTimeMillis();

        if ((args.length < 3) || (args.length > 4)) {
            throw new Error("Incorrect number of arguments");
        }

        // String bucketName = "projectsampledata";
        // String trainDataFileName = "sampleTrainData.csv";
        // String trainLabels = "sampleTrainLabels.csv";
        // String path = "sample_train";

        String bucketName = args[0];
        String trainDataFileName = args[1];
        String trainLabels = args[2];
        String path = "";
        if (args.length == 4) {
            path = args[3];
        }

        S3 s3Client = new S3(bucketName);

        // delete the existing the trainDataFile so that no old data is kept and
        // new one can be made
        try {
            s3Client.deleteObject(bucketName, trainDataFileName);
        } catch (AmazonServiceException ase) {
            System.out
                    .println("Caught an AmazonServiceException, which means your request made it "
                            + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        }

        // Create the file that the train data will be stored in
        File trainDataFile = new File(trainDataFileName);

        // download the trainLabels file
        File trainLabelsFile = s3Client.getFile(bucketName, trainLabels);

        // process the images in the specified directory
        ImageProcessor iProcessor = new ImageProcessor(trainDataFile);

        try (BufferedReader br = new BufferedReader(new FileReader(
                trainLabelsFile))) {
            String line = br.readLine(); // skip the header line
            while ((line = br.readLine()) != null) {
                String[] splitLine = line.split(",");
                System.out.println("Processing image: " + splitLine[0]);
                String imageName = splitLine[0] + ".jpeg";
                iProcessor.processImage(imageName,
                        Integer.parseInt(splitLine[1]), true,
                        s3Client.getImage(path + "/" + imageName));
                System.out.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // upload the train data file (.csv) that was just generated to S3
        s3Client.uploadFile("sampleTrainData.csv", trainDataFile);
        System.out.println("Finished processing images");

        long runtime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("Runtime: " + runtime + "seconds");
    }
}
