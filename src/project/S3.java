package project;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class S3 {

    private String bucketName;
    private AmazonS3 s3Client;

    /**
     * Constructor for the S3 class. Handles the various tasks associated with
     * connecting to, downloading, and uploading objects from S3.
     *
     * @param bucketName
     *            the name of the bucket that this instance of S3 will connect
     *            to.
     */
    public S3(String bucketName) {
        System.out.println("Connecting to S3...");
        this.bucketName = bucketName;
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. "
                            + "Please make sure that your credentials file is at the correct "
                            + "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        s3Client = new AmazonS3Client(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3Client.setRegion(usWest2);
    }

    /**
     * Returns the name of the bucket this instance of S3 is connected to.
     *
     * @return the bucket name
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * Retrieves the AmazonS3 instance used to connect, download, and upload
     * objects from S3.
     *
     * @return the AmazonS3 client used to connect, download, and upload objects
     *         from S3
     */
    public AmazonS3 getS3() {
        return this.s3Client;
    }

    /**
     * Retrieves the image from S3 with the given filename.
     *
     * @param imageFileName
     *            the name of the file to retrieve from S3
     * @return the specified image
     */
    public BufferedImage getImage(String imageFileName) {
        System.out.println("Getting image: " + imageFileName);
        BufferedImage image = null;
        System.out.println("Downloading image: " + imageFileName);
        S3Object object = s3Client.getObject(new GetObjectRequest(bucketName,
                imageFileName));
        InputStream stream = object.getObjectContent();

        try {
            image = ImageIO.read(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Uploads the given file to S3 in the bucket used in the instantiation of
     * this instance.
     *
     * @param fileName
     *            the name to give the file being uploaded to S3
     * @param file
     *            the file that will be uploaded to S3
     */
    public void uploadFile(String fileName, File file) {
        System.out.println("Uploading file: " + fileName);
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
    }

    /**
     * Uploads the given file to the specified bucket
     *
     * @param fileName
     *            the name to give the file being uploaded to S3
     * @param file
     *            the file to be uploaded to S3
     * @param bucketName
     *            the name of the bucket the file should be placed in
     */
    public void uploadFile(String fileName, File file, String bucketName) {
        System.out.println("Uploading file: " + fileName);
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
    }

    /**
     * Creates a new bucket with the given bucket name
     *
     * @param bucketName
     *            the name of the bucket to create
     */
    public void createBucket(String bucketName) {
        System.out.println("Creating bucket: " + bucketName);
        s3Client.createBucket(bucketName);
    }

    /**
     * Deletes the specified file from the specified bucket
     *
     * @param bucketName
     *            the name of the bucket where the specified object is stored
     * @param fileName
     *            the name of the file to be deleted
     */
    public void deleteObject(String bucketName, String fileName) {
        System.out.println("Deleting file: " + fileName);
        s3Client.deleteObject(bucketName, fileName);
    }

    /**
     * Retrieves the specified file from the specified bucket and returns it
     *
     * @param bucketName
     *            the name of the bucket that the file resides in
     * @param fileName
     *            the name of the file to be retrieved
     * @return the specified file
     */
    public File getFile(String bucketName, String fileName) {
        System.out.println("Retrieving file: " + fileName);
        File file = new File(fileName);
        S3Object object = s3Client.getObject(new GetObjectRequest(bucketName,
                fileName));

        try (InputStream is = object.getObjectContent()) {
            Files.copy(is, file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
