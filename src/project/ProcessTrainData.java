package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The raw train data was composed of approximately 75% images with a label of
 * 0, so to equalize the distribution of labels, the train data was reprocessed
 * so that the number of images with a label of 0 was limited to 5000. 5000 was
 * chosen because it is the next highest number of images belonging to a
 * particular class.
 *
 * @author Moses
 *
 */
public class ProcessTrainData {

    private static final int MAX_ZERO_COUNT = 5000;
    private static int zeroCount;

    public static void main(String[] args) {

        zeroCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(
                "C:/Users/Moses/Desktop/data/trainData.csv"));
                BufferedWriter writer = new BufferedWriter(new FileWriter(
                        "C:/Users/Moses/Desktop/data/processedTrainData.csv"))) {

            // Writer the first line which is the header
            String line = reader.readLine();
            writer.write(line);

            while ((line = reader.readLine()) != null) {
                int label = processLine(line);
                if (label == 0) {
                    if (zeroCount > MAX_ZERO_COUNT) {
                        continue;
                    } else {
                        writer.newLine();
                        writer.write(line);
                    }
                } else {
                    writer.newLine();
                    writer.write(line);
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Takes a line from the train data, parses it, and returns the label
     * associated with that line. Also increments the zero count (number of
     * images with a label of zero seen so far) by one.
     *
     * @param line
     *            a line from the train data file
     * @return the label associated with that line
     */
    private static int processLine(String line) {
        String[] splitLine = line.split(",");
        int label = Integer.parseInt(splitLine[splitLine.length - 1]);
        if (label == 0) {
            zeroCount++;
        }
        return label;
    }

}
