project-mgonza11
================

*Name*: Moses Gonzalez

*Email*: mgonzalez1130@gmail.com

*Github*: https://github.ccs.neu.edu/cs8674sp15-seattle/project-mgonza11


Material Submitted:
-------------------
- Meeting notes
- Design document
- Source code
- Necessary jar files, except for the AWS sdk (in the lib folder)
- Sample data to run program on
    - 10 train images (used for training the ML algorithms) and 10 test images (images that need to be labeled by the ML algorithms for the competition)
	- sampleTrainLabels.csv -> a file that contains the labels that correspond to the 10 train images
	- sampleTrainData and sampleTestData files that show what the data generated from the image processing looks like
	- sampleTrainData and sampleTestData in .arff file format, which Weka uses and which the source code expects
	- testResults file that shows what the file that is generated for the competition looks like
	- sampleClassifier.model, a file containing the serialized version of a classifier, which weka is able to read in and use

To build source code:
---------------------
1. Clone repository from github
2. Donwload AWS sdk
3. Download these jar files and place them in the build path (4 total):
	- [Weka](http://www.cs.waikato.ac.nz/ml/weka/downloading.html) (jar file can be found in the installation folder)
	- [ImageJ](http://rsb.info.nih.gov/ij/download.html)
	- [FeatureJ and imagescience](http://www.imagescience.org/meijering/software/featurej/)
4. Build the following executable jar files from project source code:
	- ProcessTrainImages.jar
		- Classes to include: ProcessTrainImages.java, ImageProcessor.java
		- Number of arguments: 3
	- ProcessTestImages.jar
		- Classes to include: ProcessTestImages.java, ImageProcessor.java
		- Number of arguments: 2
	- TrainModel.jar
		- Classes to include: TrainModel.java, TestModel.java
		- Number of arguments: 3
5. Place the ProcessTrainImages.jar, ProcessTestImages.jar, and TrainModel.jar files in the SampleData folder

To run source code:
-------------------
1. While in the SampleData folder, run the following commands:
	- java -jar ProcessTrainImages.jar ./sample_train trainDataJar.csv sampleTrainLabels.csv
	- java -jar ProcessTestImages.jar ./sample_test testDataJar.csv
	- java -jar TrainModel.jar sampleTrainData.arff sampleTestData.arff .

_OR_

1. Download files and images from: https://www.kaggle.com/c/diabetic-retinopathy-detection/data (or use the sample data provided)
2. Run the ProcessTrainImages class with the following arguments: 
	- Path to the folder containing the images that will be processed
	- Path to the file where the results of the image processing will be stored
	- Path to the file containing the labels for the images being processed
3. Run the ProcessTestImages class with the following arguments:
	- Path to the folder containing the images that will be processed
	- Path to the file where the results of the image processing will be stored
4. Optionally run the ProcessTrainData class with the following arguments (for a more even distribution of data): 
	- Path to the file that contains the train data that needs processing
	- Path to the file where the results of the data processing will be stored
5. Use the Weka GUI (download at: http://www.cs.waikato.ac.nz/ml/weka/downloading.html) to convert the .csv files to .arff files. Ensure that the label column (the last column) has a nominal type, not numeric
6. Run the TrainModel class with the following arguments:
	- Path to the file containing the train data (in .arff file format)
	- Path to the file containing the test data (in .arff file format)
	- Path to the folder where the results should be saved

Project Summary: 
----------------
The goal of the project was to produce something that could be submitted to the competition found at: [www.kaggle.com](https://www.kaggle.com/c/diabetic-retinopathy-detection)

Diabetic retinopathy is an eye disease associated with long-standing diabetes, and affects 40%-45% of American with diabetes. Progression of the disease can lead to vision impairment, but can be slowed or averted if detected in time. Currently, detecting the disease is a time-consuming and manual process that requires a trained specialist to examine photos taken of the retina. A more efficient way of detecting the disease is needed, especially as more and more Americans become affected by the disease. That is the goal of this competition - to produce an automated detection system for Diabetic Retinopathy.

To accomplish this, there are two major pieces that were needed: first, an algorithm to process the images that were provided and turn them into numerical "feature" vectors, which could then be used in the second piece, which was to train various Machine Learning classifiers. Those classifiers could then be used to classify an image based on how far the disease had progressed. 

While some powerful tools were very helpful in putting together these pieces, the biggest hurdle was scaling up the image processing. It would have taken up to 90 hours to finish processing all 80 gbs worth of images that were provided if run one by one on my personal computer. A few different solutions were tried to get around this problem. The first was to upload the images to AWS and run the image processing in the cloud. Though the code necessary to accomplish this was successfully produced, uploading the images to Amazon's S3 service proved to be too time consuming. Ultimately the fastest solution turned out to be running the image processing on my personal computer and using multiple threads to speed up the process. 

With all of the images processed, the only thing left to do was to train the machine learning algorithms. Weka made adding and maniuplating a wide variety of machine learning algorithms pretty straightfoward, so this part was not difficult. Finding the optimal combination of machine learning algorithms and algorithm configurations, however, has proven difficult, and is likely a task that will require further experimenting into the summer.

Bugs/Limitations
----------------
- The main limitation is the time required to run the entire program.
- A second limitation is the difficulty involved in ensuring a particular output is correct. 
- Converting trainDataFile and testDataFile from .csv file format to .arff file format was not automated. Instead of writing code, the Weka GUI was used to accomplish this. It was a change that was made while trying to track down a bug, and the change was never reverted. It did have the benefit of making the code easier to follow/read.