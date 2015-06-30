package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public class TestModel {

    private Instances testData;
    // Corresponds to the index of the column that represents the id of each row
    private static final int ID_ATTRIBUTE = 0;
    // The number of distinct labels an image can have (0, 1, 2, 3, or 4)
    private static final int NUM_LABELS = 5;
    private String resultFilePath;

    /**
     * Constructor for the TestModel class. Reads in the test data that will a
     * given model will be tested on.
     *
     * @param filePath
     *            the path to the file containing the test data
     * @param resultFilePath
     *            the path to the directory where the test results will be saved
     */
    public TestModel(String filePath, String resultFilePath) {
        this.resultFilePath = resultFilePath;

        try {
            testData = new Instances(new BufferedReader(
                    new FileReader(filePath)));
            testData.setClassIndex(testData.numAttributes() - 1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the given classifier and generates a .csv file where each line
     * corresponds to one item in the test data. Each line will be of the
     * format: imageID,classification
     *
     * @param model
     *            the model that is being tested
     * @param fileName
     *            the name of the file where the result classifications will be
     *            stored
     */
    public void testModel(Classifier model, String fileName) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                resultFilePath + "/" + fileName))) {

            String line = testData.instance(0).toString(ID_ATTRIBUTE) + ","
                    + model.classifyInstance(testData.instance(0));
            writer.write(line);

            for (int i = 1; i < testData.numInstances(); i++) {
                writer.newLine();
                line = testData.instance(i).toString(ID_ATTRIBUTE)
                        + ","
                        + testData.classAttribute().value(
                                (int) model.classifyInstance(testData
                                        .instance(i)));
                writer.write(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * Tests the given classifiers and generates a .csv file where each line
     * corresponds to one item in the test data. Each line will be of the
     * format: imageID,classification
     *
     * @param models
     *            An list of classifiers that are being tested. The
     *            classification that will be written to file is determined by
     *            majority vote.
     * @param fileName
     *            The name of the file where the result classifications will be
     *            stored
     */
    public void testModel(ArrayList<Classifier> models, String fileName) {
        System.out.println("Testing model...");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                resultFilePath + "/" + fileName))) {

            String line = testData.instance(0).toString(ID_ATTRIBUTE) + ","
                    + classifyInstance(testData.instance(0), models);
            writer.write(line);

            for (int i = 1; i < testData.numInstances(); i++) {
                writer.newLine();
                line = testData.instance(i).toString(ID_ATTRIBUTE) + ","
                        + classifyInstance(testData.instance(0), models);
                writer.write(line);
            }
            System.out.println("Finished testing model. See results.csv file");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Determines the classification of the given instance according to a
     * majority vote of the given list of models. In the case of a tie, the
     * smallest label is used.
     *
     * @param instance
     *            an instance corresponding to one image
     * @param models
     *            the list of models being used for classification
     * @return
     */
    private int classifyInstance(Instance instance, ArrayList<Classifier> models) {
        int[] labelCount = new int[NUM_LABELS];

        for (Classifier cls : models) {
            int label = 0;
            try {
                label = (int) cls.classifyInstance(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
            labelCount[label] = labelCount[label] + 1;
        }

        int maxLabelIndex = 0;
        for (int i = 0; i < labelCount.length; i++) {
            if (labelCount[i] > labelCount[maxLabelIndex]) {
                maxLabelIndex = i;
            }
        }

        return maxLabelIndex;
    }
}
