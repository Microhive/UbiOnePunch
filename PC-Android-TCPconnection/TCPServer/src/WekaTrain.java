import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.*;
import weka.classifiers.trees.J48;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils;
import weka.filters.unsupervised.attribute.Remove;

import java.io.*;
import java.util.*;
import java.util.Queue;


public class WekaTrain {


    private File file = null;
    private Queue<Data> queue = null;
    private int n;
    private Attribute[] Attributes = new Attribute[181];


    public WekaTrain(){
try{
// Create empty instance with three attribute values
    createInstance();

    // load data
    ArffLoader loader = new ArffLoader();
    loader.setFile(new File("Data/tt.arff"));
    Instances structure = loader.getStructure();
    structure.setClassIndex(0);

    // train NaiveBayes
    NaiveBayesUpdateable nb = new NaiveBayesUpdateable();
    nb.buildClassifier(structure);
    Instance current;
    while ((current = loader.getNextInstance(structure)) != null)
        nb.updateClassifier(current);

    // output generated model
    System.out.println(nb);
}catch (IOException ex){
    System.out.println("failed to load arff" + ex.getMessage());
}
catch (Exception ex){
    System.out.println("failed to something" + ex.getMessage());
}



        TimerTask timerTask = new CustomTask();
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 3000);
        System.out.println("TimerTask started");
    }



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

            breader = new BufferedReader(new FileReader("Data/input.csv"));
            Instances testData = new Instances(breader);
            testData.setClassIndex(0);

/*            ConverterUtils.DataSource source = new ConverterUtils.DataSource("Data/input.csv");
            Instances testData = source.getDataSet();
            testData.setClassIndex(0);*/

            breader = new BufferedReader(new FileReader("Data/tt.arff"));
            //Instance inst = new DenseInstance(3);

            // Example
/*            Data data = new Data(1,1,1,1,1,1);

            inst.setValue(Attributes[0], "idle");
            for(int i = 1; i< 181; i++) {
                inst.setValue(Attributes[i], 5.5);
                System.out.print("Number: " + i);
            }

            testData.add(inst);
            testData.setClassIndex(0);*/
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

                //System.out.println(", data vals: " + testData.classAttribute().value());
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

    private void read() {

        try(FileWriter fw = new FileWriter(file.getAbsolutePath(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println("string");

        } catch (IOException e) {
            System.out.println("Something went wrong");
        }
    }

    public void routine(/*Data raw*/) {

        Random rand = new Random();

        if (queue.size() >= n) {
            queue.remove();
        }

        Data raw = new Data(
                rand.nextInt(50) + 1,
                rand.nextInt(50) + 1,
                rand.nextInt(50) + 1,
                rand.nextInt(50) + 1,
                rand.nextInt(50) + 1,
                rand.nextInt(50) + 1);

        queue.add(raw);

        if (queue.size() < n) {
            return;
        }

/*        Data refined = averageQueueData();
        writeStringToFile(refined.toString());*/
    }

    public class CustomTask extends TimerTask {

        public CustomTask() {

            //Constructor

        }

        public void run() {
            try {
                System.out.println("READING FILE! ");
                readCSV();

            } catch (Exception ex) {

                System.out.println("error running thread " + ex.getLocalizedMessage());
            }
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

    public void readCSV(){
        String csvFile =  "Data/input.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        ArrayList<String> GestureQ;

        try {
            GestureQ = new ArrayList<>();
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] dataFromFile = line.split(cvsSplitBy);

                for(int i = 0; i<=30;i++){
                    GestureQ.add(dataFromFile[i]);
                   // System.out.println("Data code: for"+i+" :" + GestureQ.get(i));
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Done");
    }
    public void csvtoarff(){
String CSVSource = "Data/input.csv";
        String arffSource = "Data/inputC";
        try{
        // load CSV
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(CSVSource));
        Instances data = loader.getDataSet();

        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(arffSource));
        saver.setDestination(new File(arffSource));
        saver.writeBatch();
        }
        catch (IOException Ioex) {
            System.out.print("Error in converting..." + Ioex.getMessage());
        }
    }

    public void createInstance(){

        try{
        BufferedReader breader = null;
        breader = new BufferedReader(new FileReader("Data/tt.arff"));
        Instances trainData = new Instances(breader);
        trainData.setClassIndex(0);

        queue = new LinkedList();
        n = 30;

        FastVector fvNominalVal = new FastVector(3);
        fvNominalVal.addElement("up");
        fvNominalVal.addElement("down");
        fvNominalVal.addElement("left");
        fvNominalVal.addElement("right");
        Attribute Lable = new Attribute("Lable", fvNominalVal);

        Attributes[0] = Lable;

        for(int i = 1; i< 181;){
            Attributes[i] = new Attribute("AccX" + (i));
            Attributes[i+1] = new Attribute("AccY" + (i+1));
            Attributes[i+2] = new Attribute("AccZ" + (i+2));
            Attributes[i+3] = new Attribute("GyrX" + (i+3));
            Attributes[i+4] = new Attribute("GyrY" + (i+4));
            Attributes[i+5] = new Attribute("GyrZ" + (i+5));
            i = i + 6;
            System.out.print("i is: " + i);
            Instance inst = new DenseInstance(3);

// Set instance's values for the attributes "length", "weight", and "position"
            inst.setValue(Attributes[i], 5.3);
            inst.setValue(Attributes[i+1], 300);
            inst.setValue(Attributes[i+2], 454);

// Set instance's dataset to be the dataset "race"
            inst.setDataset(trainData);

// Print the instance
            System.out.println("The instance: " + inst);
        }

        }
        catch (IOException ioex){
            System.out.print("ioex:" + ioex.getMessage());
        }
    }
}
