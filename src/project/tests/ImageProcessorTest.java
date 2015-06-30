package project.tests;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import project.ImageProcessor;

public class ImageProcessorTest {

    private ImageProcessor ip1;
    private ImageProcessor ip2;
    private BufferedImage trainImage;
    private BufferedImage testImage;

    @Before
    public void setUp() throws Exception {
        ip1 = new ImageProcessor("SampleData/sample_train",
                "ImageProcessorTest1");
        ip2 = new ImageProcessor(new File("ImageProcessorTest2"));
        trainImage = ImageIO.read(new File(
                "SampleData/sample_train/10_left.jpeg"));
        testImage = ImageIO
                .read(new File("SampleData/sample_test/1_left.jpeg"));
    }

    // Note: this test will take ~30 seconds to run
    @Test
    public void testProcessImage() {
        ip1.processImage("13_right.jpeg", 0, true, trainImage);
        ip1.processImage("13_right.jpeg", 0, true, trainImage);

        ip1.processImage("2_left.jpeg", -1, false, testImage);
        ip1.processImage("2_left.jpeg", -1, false, testImage);

        ip2.processImage("13_right.jpeg", 0, true, trainImage);
        ip2.processImage("13_right.jpeg", 0, true, trainImage);

        // read in each pair of lines and make sure that they're the same

        try (BufferedReader reader = new BufferedReader(new FileReader(
                "ImageProcessorTest1"));
                BufferedReader reader2 = new BufferedReader(new FileReader(
                        "ImageProcessorTest2"))) {

            String line1 = reader.readLine();
            String line2 = reader.readLine();
            String line3 = reader2.readLine();
            String line4 = reader2.readLine();
            String line5 = reader2.readLine();
            String line6 = reader2.readLine();

            assertEquals(line1, line2);
            assertEquals(line3, line4);
            assertEquals(line5, line6);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
