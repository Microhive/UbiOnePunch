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
            fc.buildClassifier(train);
            for (int i = 0; i < test.numInstances(); i++) {
                double pred = fc.classifyInstance(test.instance(i));
                System.out.print("ID: " + test.instance(i).value(0));
                System.out.print(", actual: " + test.classAttribute().value((int) test.instance(i).classValue()));
                System.out.println(", predicted: " + test.classAttribute().value((int) pred));
            }
        }
        catch (IOException IOex){
            System.out.println("Failed to load: " + IOex.getMessage().toString());
        }
        catch (Exception ex){
            System.out.println("Failed to classify:" + ex.getMessage());
        }

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
