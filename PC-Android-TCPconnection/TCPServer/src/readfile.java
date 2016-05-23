import java.io.IOException;

/**
 * Created by JonathanLab on 23-05-2016.
 */
public class readfile {

    public void readfile(){
        try{
            java.io.FileInputStream in = new java.io.FileInputStream("Data/out.txt");

            java.nio.channels.FileChannel fc = in.getChannel();
            java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(10);

            while(fc.read(bb) >= 0){
                bb.flip();
                while(bb.hasRemaining()) {
                    System.out.println((char)bb.get());
                }
                bb.clear();
            }
        }
        catch (IOException IOex){
            System.out.println("failed: " + IOex.getMessage());
        }
    }
}
