package dk.itu.ubicomp.ubipunchreader;

import java.util.Date;

/**
 * Created by Eiler on 28/04/2016.
 */
public class Data {

    public String timestamp;

    public double AccX = 0;
    public double AccY = 0;
    public double AccZ = 0;

    public double GyrX = 0;
    public double GyrY = 0;
    public double GyrZ = 0;

    public Data(String timestamp, double AccX, double AccY, double AccZ, double GyrX, double GyrY, double GyrZ) {
        this.AccX = AccX;
        this.AccY = AccY;
        this.AccZ = AccZ;
        this.GyrX = GyrX;
        this.GyrY = GyrY;
        this.GyrZ = GyrZ;
    }

    public static String getHeader()
    {
        return "Time stamp, AccX, AccY, AccZ, GyrX, GyrY, GyrZ";
    }

    @Override
    public String toString() {
        return timestamp + ", " + AccX + ", " + AccY + ", " + AccZ + ", " + GyrX + ", " + GyrY + ", " + GyrZ;
    }
}
