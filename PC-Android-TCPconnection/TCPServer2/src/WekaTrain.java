import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.HoeffdingTree;
import weka.core.*;
import weka.classifiers.trees.J48;
import weka.filters.unsupervised.attribute.Remove;
import java.util.Timer;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

import static java.lang.System.out;


public class WekaTrain {

    private File file = null;
    private List<Data> queue = null;
    private int n;
    private Attribute[] attributeList = new Attribute[181];
    private TCPServer LhServer;
    private boolean processingGesture = false;

    public WekaTrain() {
        startServer();
        listNets();

        queue = new ArrayList();
        n = 100;

        createInstance();

        try {
            TimerTask timerTask = new readFileTask();
            Timer timer = new Timer(true);
            timer.scheduleAtFixedRate(timerTask, 0, 500);
            System.out.println("TimerTask started");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public int sendGesture(){
        int GestureID = 0;

        if(processingGesture){
            return GestureID;
        }

        try {
            BufferedReader breader = null;
            breader = new BufferedReader(new FileReader("Data/combined_train.arff"));
            Instances trainData = new Instances(breader);
            trainData.setClassIndex(0);
            breader.close();

            Instances testData = insertIntoWeka(queue, "TestSet");
            testData.setClassIndex(0);

            // filter
            Remove rm = new Remove();
            //rm.setAttributeIndices("1");  // remove 1st attribute
            // classifier
//            HoeffdingTree ht = new HoeffdingTree();

            J48 j48 = new J48();
            j48.setUnpruned(true);        // using an unpruned J48
//             meta-classifier
            FilteredClassifier fc = new FilteredClassifier();
            fc.setFilter(rm);
            fc.setClassifier(j48);
            // train and make predictions
            fc.buildClassifier(trainData);

            for (int i = 0; i < testData.numInstances(); i++) {
                double pred = fc.classifyInstance(testData.instance(i));
                System.out.print("ID: " + testData.instance(i).value(0));
//                System.out.print(", actual: " + testData.classAttribute().value((int) testData.instance(i).classValue()));
                System.out.println(", predicted: " + testData.classAttribute().value((int) pred));

//                System.out.println(", data vals: " + testData.classAttribute().value());
                String left = "left";
                String right = "right";
                String up = "up";
                String down = "down";
//                String tiltl = "tiltl";
//                String tiltr = "tiltr";
                String idle = "idle";

                if(testData.classAttribute().value((int)pred).equals(left)){
                    System.out.print("Predicted: " + left);
                    GestureID = 1;
                    processingGesture = true;
                }
                else if(testData.classAttribute().value((int)pred).equals(right)){
                    System.out.print("Predicted: " + right);
                    GestureID = 2;
                    processingGesture = true;
                }
                else if(testData.classAttribute().value((int)pred).equals(up)){
                    System.out.print("Predicted: " + up);
                    GestureID = 3;
                    processingGesture = true;
                }
                else if(testData.classAttribute().value((int)pred).equals(down)){
                    System.out.print("Predicted: " + down);
                    GestureID = 4;
                    processingGesture = true;
                }
                else if(testData.classAttribute().value((int)pred).equals(idle)){
                    System.out.print("Predicted: " + idle);
                    GestureID = 5;
//                    processingGesture = false;
                }
                else{
                    System.out.print("failed to predict or not found");
                    GestureID = 0;
//                    processingGesture = false;
                }
            }
        }
        catch (IOException IOex){
            System.out.println("Failed to load: " + IOex.getMessage().toString());
        }
        catch (Exception ex){
            System.out.println("Failed to classify:" + ex.getMessage());
        }
        final String messageText = ""+ GestureID;
//        System.out.print("\n" + "input to android: (" + messageText +")");
        //messagesArea.append("\n" + "input to android: (" + messageText +")");

        if(GestureID != 0 && GestureID != 5){

            LhServer.sendMessage(messageText);
            Timer timer1 = new Timer();
            timer1.schedule(new TimerTask() {
                @Override
                public void run() {

                    if (processingGesture)
                    {
                        queue.clear();
                        processingGesture = false;
                        System.out.print("Gathering measurements again...!");
                    }
                }
            }, 4000);
        }

        return GestureID;
    }
    // List <Point3d> points
    public Instances insertIntoWeka(final List<Data> queue, final String name)
    {
        // Create numeric attributes "x" and "y" and "z"
        // Create vector of the above attributes
        FastVector attributes = new FastVector(0);

        for (int i = 0; i < attributeList.length; i++)
            attributes.addElement(attributeList[i]);

        // Create the empty datasets "wekaPoints" with above attributes
        Instances wekaPoints = new Instances(name, attributes, 0);

        // Create empty instance
        Instance inst = new weka.core.DenseInstance(181);
        inst.setValue(attributeList[0], "idle");

        int counter = 1;
        for (Iterator<Data> i = queue.iterator(); i.hasNext();)
        {
            // get the point3d
            Data p = i.next();

            // Set instance's values for the attributes "x", "y", and "z"
            inst.setValue(attributeList[counter++], p.AccX);
            inst.setValue(attributeList[counter++], p.AccY);
            inst.setValue(attributeList[counter++], p.AccZ);
            inst.setValue(attributeList[counter++], p.GyrX);
            inst.setValue(attributeList[counter++], p.GyrY);
            inst.setValue(attributeList[counter++], p.GyrZ);

        }

        // Set instance's dataset to be the dataset "wekaPoints"
        inst.setDataset(wekaPoints);

        // Add the Instance to Instances
        wekaPoints.add(inst);

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

    public class readFileTask extends TimerTask {


        public readFileTask() throws IOException {
        }
//        boolean running = true;
        boolean reachedEndOnce = false;
        Path path = Paths.get("Data/imu_Lab/log.csv");
        final String cvsSplitBy = ",";
        int lineCount = 0;
        int lastCount = 0;
        String line = "";

        public void run() {

            try (InputStream is = Files.newInputStream(path, StandardOpenOption.READ)) {
                InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader lineReader = new BufferedReader(reader);

                while ((line = lineReader.readLine()) != null) {

                    if (reachedEndOnce && lineCount > lastCount && !processingGesture)
                    {
                        String[] dataFromFile = line.split(cvsSplitBy);
                        Data d = new Data(
                                dataFromFile[0],
                                Double.parseDouble(dataFromFile[1]),
                                Double.parseDouble(dataFromFile[2]),
                                Double.parseDouble(dataFromFile[3]),
                                Double.parseDouble(dataFromFile[4]),
                                Double.parseDouble(dataFromFile[5]),
                                Double.parseDouble(dataFromFile[6]));

                        PushQueue(d);

                        if (queue.size() >= 30 && reachedEndOnce && !processingGesture && (lineCount+60) > lastCount)
                        {
                            sendGesture();
                        }
                    }

                    if (processingGesture)
                    {
                        queue.clear();
                    }

                    lineCount++;
                }

                lastCount = lineCount - 1;
                lineCount = 0;

                reachedEndOnce = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.print("called gesture");
            //sendGesture();
//            if (queue.size() >= 30 && reachedEndOnce)
//            {
//                // System.out.print("called gesture");
//                sendGesture();
//                // System.out.println( queue.get(queue.size()-1) );
//            }
        }
    }

    public void PushQueue(Data d)
    {
        queue.add(d);

        if (queue.size() > 30)
            queue.remove(0);
    }

    public void createInstance(){

        FastVector fvNominalVal = new FastVector(5);
        fvNominalVal.addElement("left");
        fvNominalVal.addElement("right");
        fvNominalVal.addElement("up");
        fvNominalVal.addElement("down");
        fvNominalVal.addElement("idle");
        Attribute Lable = new Attribute("Lable", fvNominalVal);

        attributeList[0] = Lable;

        int index = 1;
        for(int i = 0; i < 180;){
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
    public void startServer(){
        LhServer = new TCPServer(new TCPServer.OnMessageReceived() {
            @Override
            //this method declared in the interface from TCPServer class is implemented here
            //this method is actually a callback method, because it will run every time when it will be called from
            //TCPServer class (at while)
            public void messageReceived(String message) {
                System.out.print("\n "+message);
                //messagesArea.append("\n "+message);
            }
        });
        LhServer.start();

    }
    public void listNets(){
        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();

            for (NetworkInterface netint : Collections.list(nets))
                displayInterfaceInformation(netint);


        } catch (SocketException Se) {
            return;
        }
    }

    public void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        out.printf("Display name: %s\n", netint.getDisplayName());
        out.printf("Name: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {

            out.printf("InetAddress: %s\n", inetAddress);
            System.out.print("InetAddress: %s\n" + inetAddress.toString());
            //messagesArea.append("InetAddress: %s\n" + inetAddress.toString());
        }
        out.printf("\n");
    }
}
