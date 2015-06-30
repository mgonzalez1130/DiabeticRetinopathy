package project;

/**
 * A class to run multiple image processing threads
 *
 * @author Moses
 *
 */
public class MuliThreaded implements Runnable {

    private String[] args;

    /**
     * If the last argument is the string "train", then ProcessTrainImages will
     * be run, and the arguments proceeding the string "train" should correspond
     * to the arguments that would be passed to the ProcessTrainImages main
     * method. If the last argument is the string "test", then ProcessTestImages
     * will be run, and the arguments proceeding the string "test" should
     * correspond to the arguments that would be passed to the ProcessTestImages
     * main method.
     *
     * @param arg1
     *            The path to the directory containing all of the images to be
     *            processed
     * @param arg2
     *            The path to the file where the results of the image processing
     *            will be stored
     * @param arg3
     *            The path to the file containing the labels for all of the
     *            train images. Necessary if "train" is specified, otherwise
     *            should not be given
     * @param arg4
     *            Either "train" or "test", depending on whether the thread
     *            should run ProcessTrainImages or ProcessTestImages on the
     *            given inputs
     *
     */
    public MuliThreaded(String... strings) {
        if (strings[strings.length - 1].equals("train")) {
            if (strings.length != 4) {
                throw new RuntimeException(
                        "Incorrect number of arguments for processing train images");
            } else {
                args = new String[3];
                args[0] = strings[0];
                args[1] = strings[1];
                args[2] = strings[2];
            }
        } else if (strings[strings.length - 1].equals("test")) {
            if (strings.length != 3) {
                throw new RuntimeException(
                        "Incorrect number of arguments for processing test images");
            } else {
                args = new String[2];
                args[0] = strings[0];
                args[1] = strings[1];
            }
        } else {
            throw new RuntimeException(
                    "The last argument should be either train or test");
        }
    }

    @Override
    public void run() {
        if (args.length == 3) {
            ProcessTrainImages.main(args);
        } else {
            ProcessTestImages.main(args);
        }
    }

    /**
     * The train and test data had been split into smaller, more manageable
     * pieces, and so a separate thread was made for each one of those piece.
     *
     * @param args
     */
    public static void main(String[] args) {
        // (new Thread(new MuliThreaded("C:/Users/Moses/Desktop/data/train",
        // "C:/Users/Moses/Desktop/data/sampleTrainData.csv",
        // "C:/Users/Moses/Desktop/data/sampleTrainLabels.csv", "train")))
        // .start();
        //
        // (new Thread(new MuliThreaded("C:/Users/Moses/Desktop/data/train",
        // "C:/Users/Moses/Desktop/data/trainData-1.csv",
        // "C:/Users/Moses/Desktop/data/trainLabels-1.csv", "train")))
        // .start();
        //
        // (new Thread(new MuliThreaded("C:/Users/Moses/Desktop/data/train",
        // "C:/Users/Moses/Desktop/data/trainData-2.csv",
        // "C:/Users/Moses/Desktop/data/trainLabels-2.csv", "train")))
        // .start();
        //
        // (new Thread(new MuliThreaded("C:/Users/Moses/Desktop/data/train",
        // "C:/Users/Moses/Desktop/data/trainData-3.csv",
        // "C:/Users/Moses/Desktop/data/trainLabels-3.csv", "train")))
        // .start();
        //
        // (new Thread(new MuliThreaded("C:/Users/Moses/Desktop/data/train",
        // "C:/Users/Moses/Desktop/data/trainData-4.csv",
        // "C:/Users/Moses/Desktop/data/trainLabels-4.csv", "train")))
        // .start();
        //
        // (new Thread(new MuliThreaded("C:/Users/Moses/Desktop/data/train",
        // "C:/Users/Moses/Desktop/data/trainData-5.csv",
        // "C:/Users/Moses/Desktop/data/trainLabels-5.csv", "train")))
        // .start();
        //
        // (new Thread(new MuliThreaded("C:/Users/Moses/Desktop/data/train",
        // "C:/Users/Moses/Desktop/data/trainData-6.csv",
        // "C:/Users/Moses/Desktop/data/trainLabels-6.csv", "train")))
        // .start();
        //
        // (new Thread(new MuliThreaded("C:/Users/Moses/Desktop/data/test1",
        // "C:/Users/Moses/Desktop/data/testData1.csv", "test"))).start();
        //
        // (new Thread(new MuliThreaded("C:/Users/Moses/Desktop/data/test2",
        // "C:/Users/Moses/Desktop/data/testData2.csv", "test"))).start();
        //
        // (new Thread(new MuliThreaded("C:/Users/Moses/Desktop/data/test3",
        // "C:/Users/Moses/Desktop/data/testData3.csv", "test"))).start();
        //
        // (new Thread(new MuliThreaded("C:/Users/Moses/Desktop/data/test4",
        // "C:/Users/Moses/Desktop/data/testData4.csv", "test"))).start();
        //
        // (new Thread(new MuliThreaded("C:/Users/Moses/Desktop/data/test5",
        // "C:/Users/Moses/Desktop/data/testData5.csv", "test"))).start();
        //
        // (new Thread(new MuliThreaded("C:/Users/Moses/Desktop/data/test6",
        // "C:/Users/Moses/Desktop/data/testData6.csv", "test"))).start();
        //
        // (new Thread(new MuliThreaded("C:/Users/Moses/Desktop/data/test7",
        // "C:/Users/Moses/Desktop/data/testData7.csv", "test"))).start();
        //
        // (new Thread(new MuliThreaded("C:/Users/Moses/Desktop/data/test8",
        // "C:/Users/Moses/Desktop/data/testData8.csv", "test"))).start();
        //
        // (new Thread(new MuliThreaded("C:/Users/Moses/Desktop/data/test9",
        // "C:/Users/Moses/Desktop/data/testData9.csv", "test"))).start();
    }
}
