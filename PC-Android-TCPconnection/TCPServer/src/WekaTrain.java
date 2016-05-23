import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.M5P;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.pmml.Array;
import weka.classifiers.trees.J48;
import weka.core.pmml.jaxbbindings.Attribute;
import weka.filters.unsupervised.attribute.Remove;

import javax.swing.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class WekaTrain {


    public void LoadTrainset(){
        try{
            //ArrayList<Double> alist = new ArrayList<>();
            BufferedReader breader = null;
            breader = new BufferedReader(new FileReader("Data/combined_train.arff"));
            Instances train = new Instances(breader);
            train.setClassIndex(0);

            //breader = new BufferedReader(new FileReader("Data/test_train.arff"));
            breader = new BufferedReader(new FileReader("Data/tt.arff"));
            Instances test = new Instances(breader);
            test.setClassIndex(0);
            breader.close();

            // train classifier
            Classifier cls = new J48();
            cls.buildClassifier(train);
            // evaluate classifier and print some statistics
            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(cls, test);
            System.out.println(eval.toSummaryString("\nResults\n======\n", false));
            // Declare the class attribute along with its values

/*            FastVector fvClassVal = new FastVector(2);
            fvClassVal.addElement("up");
            fvClassVal.addElement("down");
            Attribute ClassAttr = new Attribute("Lable",fvClassVal);
            Attribute ClassAttribute = new Attribute("Lable", fvClassVal);*/
            System.out.println("Filed loaded");
            //System.out.println("val:" + val2);



/*            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(jtree, data, 5, new Random(1));

            System.out.println("Built classifier:");
            System.out.println(eval.toSummaryString("\nResults\n=======\n",true));
            System.out.println(eval.fMeasure(1) + " " + eval.precision(1)+ " " + eval.recall(1) );*/

        }
        catch (IOException IOex){
            System.out.println("Failed to load: " + IOex.getMessage().toString());
        }
        catch (Exception ex){
            System.out.println("Failed to classify:" + ex.getMessage());
        }

    }

    public int sendGesture(){
        int GestureID = 0;
        try{
            BufferedReader breader = null;
            breader = new BufferedReader(new FileReader("Data/combined_train.arff"));
            Instances trainData = new Instances(breader);
            trainData.setClassIndex(0);

            breader = new BufferedReader(new FileReader("Data/tt.arff"));
            Instances testData = new Instances(breader);
            testData.setClassIndex(0);
            breader.close();
            // filter
            Remove rm = new Remove();
            //rm.setAttributeIndices("1");  // remove 1st attribute
            // classifier
            J48 j48 = new J48();
            j48.setUnpruned(true);        // using an unpruned J48
            // meta-classifier
            FilteredClassifier fc = new FilteredClassifier();
            fc.setFilter(rm);
            fc.setClassifier(j48);
            // train and make predictions
            fc.buildClassifier(trainData);
            for (int i = 0; i < testData.numInstances(); i++) {
                double pred = fc.classifyInstance(testData.instance(i));
                System.out.print("ID: " + testData.instance(i).value(0));
                System.out.print(", actual: " + testData.classAttribute().value((int) testData.instance(i).classValue()));
                System.out.println(", predicted: " + testData.classAttribute().value((int) pred));
                String up = "up";
                String down = "down";
                String left = "left";
                String right = "right";
                String tiltr = "tiltr";
                String tiltl = "tiltl";
                String idle = "idle";

                if(testData.classAttribute().value((int)pred).equals(left)){
                    System.out.print("Predicted: " + left);
                    GestureID = 1;
                }
                else if(testData.classAttribute().value((int)pred).equals(right)){
                    System.out.print("Predicted: " + right);
                    GestureID = 2;
                }
                else if(testData.classAttribute().value((int)pred).equals(up)){
                    System.out.print("Predicted: " + up);
                    GestureID = 3;
                }
                else if(testData.classAttribute().value((int)pred).equals(down)){
                    System.out.print("Predicted: " + down);
                    GestureID = 4;
                }
                else if(testData.classAttribute().value((int)pred).equals(tiltr)){
                    System.out.print("Predicted: " + tiltr);
                    GestureID = 5;
                }
                else if(testData.classAttribute().value((int)pred).equals(tiltl)){
                    System.out.print("Predicted: " + tiltl);
                    GestureID = 6;
                }
                else if(testData.classAttribute().value((int)pred).equals(idle)){
                    System.out.print("Predicted: " + idle);
                    GestureID = 7;
                }
                else{
                    System.out.print("failed to predict or not found");
                    GestureID = 0;
                }
            }

        }
        catch (IOException IOex){
            System.out.println("Failed to load: " + IOex.getMessage().toString());
        }
        catch (Exception ex){
            System.out.println("Failed to classify:" + ex.getMessage());
        }
return GestureID;
    }

/*    BufferedInputStream reader = null;
    boolean running = true;

public boolean isReadingfile(){
    running = true;
    try {
        reader = new BufferedInputStream(new FileInputStream( "out.txt" ) );
        running = true;
    }
    catch (FileNotFoundException Fex){
        System.out.print("file not found: " + Fex.getMessage().toString());
        running = false;
    }
    return running;
}
    public void run() {
        while( running ) {
            try {
                if (reader.available() > 0) {
                    System.out.print((char) reader.read());
                } else {
                    try {
                        //sleep(500);
                    } catch (InterruptedException ex) {
                        running = false;
                    }
                }
            }
            catch (IOException Ioex){

            }
        }
    }*/


}
