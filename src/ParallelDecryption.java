import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ParallelDecryption extends Thread{

    private long seedKey;
    private int [][] randomSequenceMatrix;
    private BufferedImage colorChannel;
    private String stringChannel;
    private Thread thread;
    private String threadName;
    private Decryption decryption;
    private static List<BufferedImage> outputDecryptedImageList;
    private BufferedImage rezultatDecriptare;

    public ParallelDecryption(){
        this.thread=new Thread();
        this.decryption=new Decryption();
        outputDecryptedImageList=new ArrayList<BufferedImage>();
    }

    public void run(){
        try{
            BufferedImage outputEncryptedImage=decryption.doDecryption(seedKey,randomSequenceMatrix, colorChannel,stringChannel);
            rezultatDecriptare=outputEncryptedImage;
            addDecryptedImageInList(outputEncryptedImage);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void addDecryptedImageInList(BufferedImage bufferedImage){
        outputDecryptedImageList.add(bufferedImage);
    }

    public  List<BufferedImage> getOutputDecryptedImageList() {
        return outputDecryptedImageList;
    }

    public void setSeedKey(long seedKey) {
        this.seedKey = seedKey;
    }

    public void setRandomSequenceMatrix(int[][] randomSequenceMatrix) {
        this.randomSequenceMatrix = randomSequenceMatrix;
    }

    public void setColorChannel(BufferedImage colorChannel) {
        this.colorChannel = colorChannel;
    }

    public void setStringChannel(String stringChannel) {
        this.stringChannel = stringChannel;
    }

}
