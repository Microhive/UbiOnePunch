import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.*;
import weka.classifiers.trees.J48;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.unsupervised.attribute.Remove;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;


public class WekaTrain {

    private File file = null;
    private List<Data> queue = null;
    private int n;
    private Attribute[] attributeList = new Attribute[181];

    public WekaTrain() {
        try {
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
        }
        catch (IOException ex) {
            System.out.println("failed to load arff" + ex.getMessage());
        }
        catch (Exception ex) {
            System.out.println("failed to something" + ex.getMessage());
        }
        try {
            TimerTask timerTask = new CustomTask();
            Timer timer = new Timer(true);
            timer.scheduleAtFixedRate(timerTask, 0, 1000);
            System.out.println("TimerTask started");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
            Instances testData = insertIntoWeka(queue, "he");
            testData.setClassIndex(0);

//            ConverterUtils.DataSource source = new ConverterUtils.DataSource("Data/input.csv");
//            Instances trainData = insertIntoWeka(queue, "he");
//            trainData.setClassIndex(0);

//            breader = new BufferedReader(new FileReader("Data/tt.arff"));
            //Instance inst = new DenseInstance(3);

            // Example
/*            Data data = new Data(1,1,1,1,1,1);

            inst.setValue(attributeList[0], "idle");
            for(int i = 1; i< 181; i++) {
                inst.setValue(attributeList[i], 5.5);
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
    // List <Point3d> points
    public Instances insertIntoWeka(final List<Data> queue, final String name)
    {
        // Create numeric attributes "x" and "y" and "z"
        // Create vector of the above attributes
        FastVector attributes = new FastVector(3);

        for (int i = 0; i < attributeList.length; i++)
            attributes.addElement(attributeList[i]);

        // Create the empty datasets "wekaPoints" with above attributes
        Instances wekaPoints = new Instances(name, attributes, 0);

        int counter = 0;
        for (Iterator<Data> i = queue.iterator(); i.hasNext();)
        {
            // Create empty instance with three attribute values
            Instance inst = new weka.core.DenseInstance(5);
            // get the point3d
            Data p = i.next();

            if (counter == 0)
            {
                inst.setValue(attributeList[0], "idle");
            }
            // Set instance's values for the attributes "x", "y", and "z"
            inst.setValue(attributeList[counter+1], p.AccX);
            inst.setValue(attributeList[counter+2], p.AccY);
            inst.setValue(attributeList[counter+3], p.AccZ);
            inst.setValue(attributeList[counter+4], p.GyrX);
            inst.setValue(attributeList[counter+5], p.GyrY);
            inst.setValue(attributeList[counter+6], p.GyrZ);

            // Set instance's dataset to be the dataset "wekaPoints"
            inst.setDataset(wekaPoints);

            // Add the Instance to Instances
            wekaPoints.add(inst);
            counter += 6;
        }

        return wekaPoints;
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
            queue.remove(0);
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

        public CustomTask() throws IOException {
        }

        boolean running = true;
        boolean reachedEndOnce = false;
//        BufferedInputStream reader = new BufferedInputStream(new FileInputStream( "Data/log.csv" ) );
//        BufferedReader br = new BufferedReader(new FileReader("Data/log.csv"));
        Path path = Paths.get("Data/imu_Lab/log.csv");
//        InputStream is = Files.newInputStream(path, StandardOpenOption.READ);
//        InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
//        BufferedReader lineReader = new BufferedReader(reader);
        final String cvsSplitBy = ",";
        int lineCount = 0;
        int lastCount = 0;
        String line = "";

        public void run() {

            while( running ) {
                try (InputStream is = Files.newInputStream(path, StandardOpenOption.READ)) {
                    InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                    BufferedReader lineReader = new BufferedReader(reader);

                    while ((line = lineReader.readLine()) != null) {



                        if (reachedEndOnce && lineCount > lastCount)
                        {
                            String[] dataFromFile = line.split(cvsSplitBy);
                            Data d = new Data(
                                    Double.parseDouble(dataFromFile[1]),
                                    Double.parseDouble(dataFromFile[2]),
                                    Double.parseDouble(dataFromFile[3]),
                                    Double.parseDouble(dataFromFile[4]),
                                    Double.parseDouble(dataFromFile[5]),
                                    Double.parseDouble(dataFromFile[6]));

                            PushQueue(d);
                            System.out.println( line );
                        }

                        lineCount++;
                    }

                    lastCount = lineCount;
                    lineCount = 0;

                    reachedEndOnce = true;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void PushQueue(Data d)
    {
        queue.add(d);

        if (queue.size() > 30)
            queue.remove(0);
    }

//    public void readCSV(){
//        String csvFile =  "Data/input.csv";
//        BufferedReader br = null;
//        String line = "";
//        String cvsSplitBy = ",";
//        ArrayList<String> GestureQ;
//
//        try {
//            GestureQ = new ArrayList<>();
//            br = new BufferedReader(new FileReader(csvFile));
//            while ((line = br.readLine()) != null) {
//
//                // use comma as separator
//                String[] dataFromFile = line.split(cvsSplitBy);
//
//                for(int i = 0; i<=30;i++){
//                    GestureQ.add(dataFromFile[i]);
//                   // System.out.println("Data code: for"+i+" :" + GestureQ.get(i));
//                }
//
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        System.out.println("Done");
//    }

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

        queue = new LinkedList();
        n = 30;

        FastVector fvNominalVal = new FastVector(3);
        fvNominalVal.addElement("up");
        fvNominalVal.addElement("down");
        fvNominalVal.addElement("left");
        fvNominalVal.addElement("right");
        fvNominalVal.addElement("till");
        fvNominalVal.addElement("tilr");
        fvNominalVal.addElement("idle");
        Attribute Lable = new Attribute("Lable", fvNominalVal);

        attributeList[0] = Lable;

        int index = 1;
        for(int i = 0; i <= 181;){
            attributeList[i+1] = new Attribute("AccX" + (index));
            attributeList[i+2] = new Attribute("AccY" + (index));
            attributeList[i+3] = new Attribute("AccZ" + (index));
            attributeList[i+4] = new Attribute("GyrX" + (index));
            attributeList[i+5] = new Attribute("GyrY" + (index));
            attributeList[i+6] = new Attribute("GyrZ" + (index));
            System.out.print("i is: " + i);
            i += 6;
            index++;
        }
    }
}
