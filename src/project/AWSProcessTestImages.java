package project;

import java.io.File;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AWSProcessTestImages {
    /**
     * Processes the images that the classifier will be tested on by retrieving
     * the images from the bucket they are stored in on S3 and passing it to the
     * ImageProcessor class. All necessary files must be stored in S3, and any
     * output generated will be uploaded to S3.
     *
     * /** Takes two arguments.<br>
     * First: The bucket containing the images to processed. Images must be
     * stored at the highest level (i.e. not in any sub-folders)<br>
     * Second: The name of the file where the results of the image processing
     * will be stored<br>
     *
     * @param args
     *            command line arguments
     */
    public static void main(String args[]) {

        long startTime = System.currentTimeMillis();

        if (args.length != 2) {
            throw new Error("Incorrect number of arguments");
        }

        // String bucketName = "projectsampletestdata";
        // String testDataFileName = "sampleTestData.csv";

        String bucketName = args[0];
        String testDataFileName = args[1];

        S3 s3Client = new S3(bucketName);

        // delete the existing the trainDataFile so that no old data is kept and
        // new one can be made
        try {
            s3Client.deleteObject(bucketName, testDataFileName);
        } catch (AmazonServiceException ase) {
            ase.printStackTrace();
        }

        // create the file the test data will be stored in
        File testDataFile = new File(testDataFileName);

        // process the images in the specified directory
        ImageProcessor iProcessor = new ImageProcessor(testDataFile);

        ObjectListing objectListing = s3Client.getS3().listObjects(
                new ListObjectsRequest().withBucketName(bucketName));

        while (true) {
            for (S3ObjectSummary objectSummary : objectListing
                    .getObjectSummaries()) {
                String imageName = objectSummary.getKey();
                System.out.println("Processing image: " + imageName);
                iProcessor.processImage(imageName, -1, false,
                        s3Client.getImage(imageName));
                System.out.println();
            }
            if (objectListing.isTruncated()) {
                s3Client.getS3().listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }

        // Upload the test data that was just generated (in a .csv file) to S3
        s3Client.uploadFile("testData.csv", testDataFile);
        System.out.println("Finished processing images");

        long runtime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("Runtime: " + runtime + "seconds");

    }
}
