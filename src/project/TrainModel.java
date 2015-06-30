package project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.meta.LogitBoost;
import weka.classifiers.trees.DecisionStump;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;

public class TrainModel {

    private static Instances trainData;
    private static Remove rm;
    private static ArrayList<Classifier> voter;
    private static String trainDataFile;
    private static String testDataFile;
    private static String saveFolder;

    /**
     * Trains the classifiers using the given train data that will be used to
     * classify the test data
     *
     * argument 1: the path to the file containing the train data (in .arff
     * format) <br>
     * argument 2: the path to the file containing the test data (in .arff
     * format)<br>
     * argument 3: the path to the folder where the results file will be saved
     *
     * @param args
     */
    public static void main(String[] args) {

        if (args.length != 3) {
            throw new RuntimeException("Incorrect number of arguments!");
        }

        // String trainDataFile =
        // "C:/Users/Moses/Desktop/data/processedTrainData.arff";
        // String testDataFile = "C:/Users/Moses/Desktop/data/testData.arff";
        // String saveFolder = "C:/Users/Moses/Desktop/data/";

        trainDataFile = args[0];
        testDataFile = args[1];
        saveFolder = args[2];

        System.out.println("Loading data...");
        try {
            trainData = new Instances(new BufferedReader(new FileReader(
                    trainDataFile)));
            trainData.setClassIndex(trainData.numAttributes() - 1);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // filter
        rm = new Remove();
        rm.setAttributeIndices("1");

        // Vote classifier
        voter = new ArrayList<Classifier>();

        // train classifier
        System.out.println("Training AdaBoost:");
        AdaBoostM1 ada = new AdaBoostM1();
        build(ada);
        write("adaBoost.model", ada);

        System.out.println("Training Bagging:");
        Bagging bag = new Bagging();
        build(bag);
        write("bagging.model", bag);

        System.out.println("Training BayesNet:");
        BayesNet bn = new BayesNet();
        build(bn);
        write("BayesNet.model", bn);

        System.out.println("Training DecisionStump:");
        DecisionStump ds = new DecisionStump();
        build(ds);
        write("DecisionStump.model", ds);

        System.out.println("Training LogitBoost:");
        LogitBoost lb = new LogitBoost();
        build(lb);
        write("logitBoost.model", lb);

        // print results to .csv file
        TestModel tm = new TestModel(testDataFile, saveFolder);
        tm.testModel(voter, "results.csv");

    }

    /**
     * Writes the given classifier to a file with the given filename and place
     * it in a folder called models that will be placed in the specified save
     * folder.
     *
     * @param fileName
     *            the name to give the file that will be created
     * @param cls
     *            the classifier that is being written to file
     */
    private static void write(String fileName, Classifier cls) {
        // Serialize classifier for future use
        try {
            weka.core.SerializationHelper.write(saveFolder + "/" + fileName,
                    cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Builds the given classifier. Filters out the first column (which is the
     * id column). The classifier is then added to the the list of classifiers
     * that will be used in a voting scheme to determine the label of an image.
     * Finally, the classifier is evaluated using 10-fold cross validation.
     *
     * @param cls
     *            the classifier that needs to be built
     */
    private static void build(Classifier cls) {
        FilteredClassifier fc = new FilteredClassifier();
        fc.setFilter(rm);
        fc.setClassifier(cls);
        buildHelper(fc);
        voter.add(fc);
        evaluate(fc);
    }

    /**
     * Builds the given classifier
     *
     * @param fc
     *            a filtered classifier that needs to be built
     */
    private static void buildHelper(FilteredClassifier fc) {
        try {
            fc.buildClassifier(trainData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Evaluates the given classifier using 10-fold cross validation and prints
     * the result statistics to the console.
     *
     * @param fc
     *            the classifiers that needs to be evaluated.
     */
    private static void evaluate(Classifier fc) {
        System.out.println("Evaluating classifier");
        try {
            Evaluation eval = new Evaluation(trainData);
            eval.crossValidateModel(fc, trainData, 10, new Random(1));
            System.out.println(eval.toSummaryString());
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
