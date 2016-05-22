import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.M5P;
import weka.core.Instances;
import weka.core.pmml.Array;
import weka.classifiers.trees.J48;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class WekaTrain {


    public void LoadTrainset(){
        try{
            ArrayList<Double> alist = new ArrayList<>();
            BufferedReader reader = new BufferedReader(
                    new FileReader("Data/down_train.arff"));
            Instances data = new Instances(reader);

            String val = data.attribute("GyrZ30").toString();
            int val2 = data.numAttributes();
         //   String val1 = data.equalHeadersMsg(data).toString();
            for(int i=0; i<data.attributeToDoubleArray(0).length; i++ ){
                System.out.println("valI:" + i);
            }

            //alist = data.attributeToDoubleArray(0);
            data.setClassIndex(0);

            reader.close();
            // setting class attribute

            System.out.println("Filed loaded");
            System.out.println("val:" + val2);
            // building a classifier

           // J48 jtree = new J48();
            //jtree.buildClassifier(data);
            NaiveBayes jtree = new NaiveBayes();
            jtree.buildClassifier(data);

            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(jtree, data, 5, new Random(1));

            System.out.println("Built classifier:");
            System.out.println(eval.toSummaryString("\nResults\n=======\n",true));
            System.out.println(eval.fMeasure(1) + " " + eval.precision(1)+ " " + eval.recall(1) );
        }
        catch (IOException IOex){
            System.out.println("Failed to load: " + IOex.getMessage().toString());
        }
        catch (Exception ex){
            System.out.println("Failed to classify:" + ex.getMessage());
        }

    }


}
