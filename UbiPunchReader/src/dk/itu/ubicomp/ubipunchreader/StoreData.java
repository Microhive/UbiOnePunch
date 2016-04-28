package dk.itu.ubicomp.ubipunchreader;

/**
 * Created by Eiler on 28/04/2016.
 */

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class StoreData {

    private File file = null;
    private FileWriter fileWriter = null;
    private Queue<Data> queue = null;
    private int n;

    public StoreData() {

        queue = new LinkedList();
        n = 20;

        System.out.println("Creating File");
        createFile();
        System.out.println("File Path " + file.getAbsolutePath());

        // Test data
        System.out.println("Writing test set");
        for (int i = 0; i < 100; i++)
        {
            routine();
        }
    }

    private void createFile()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDateTime = dateTime.format(formatter);
        System.out.println("Getting current date: " + formattedDateTime);

        File dir = new File("data");
        dir.mkdirs();
        file = new File(dir, formattedDateTime + ".csv");

        // Feed data here
        writeStringToFile(Data.getHeader());
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

        writeStringToFile(averageQueueData().toString());
    }

    private Data averageQueueData()
    {
        Data refined = new Data(0, 0, 0, 0, 0, 0);

        for (Data item: queue) {
            refined.AccX += item.AccX;
            refined.AccY += item.AccY;
            refined.AccZ += item.AccZ;
            refined.GyrX += item.GyrX;
            refined.GyrY += item.GyrY;
            refined.GyrZ += item.GyrZ;
        }

        refined.AccX /= queue.size();
        refined.AccY /= queue.size();
        refined.AccZ /= queue.size();
        refined.GyrX /= queue.size();
        refined.GyrY /= queue.size();
        refined.GyrZ /= queue.size();

        return refined;
    }

    private void writeStringToFile(String string) {

        try(FileWriter fw = new FileWriter(file.getAbsolutePath(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(string);

        } catch (IOException e) {
            System.out.println("Something went wrong");
        }
    }
}
