package project.Experimenting;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;

public class TestAWSEC2 {

    private static String imageId = "ami-e7527ed7";
    private static String instanceType = "t2.micro";
    private static String keyName = "default";
    private static int minCount = 1;
    private static int maxCount = 1;

    public static void main(String[] args) {
        // Create credentials
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

        // Create ec2 client
        AmazonEC2Client ec2 = new AmazonEC2Client(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        ec2.setRegion(usWest2);

        // Create ec2 instances
        RunInstancesRequest request = new RunInstancesRequest();
        request.withImageId(imageId).withInstanceType(instanceType)
                .withMinCount(minCount).withMaxCount(maxCount)
                .withKeyName(keyName);

        RunInstancesResult run = ec2.runInstances(request);

    }
}
