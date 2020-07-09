import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ParallelEncryption extends Thread{

    private long seedKey;
    private int [][] randomSequenceMatrix;
    private BufferedImage colorChannel;
    private String stringChannel;
    private Thread thread;
    private String threadName;
    private Encryption encryption;
    private static List<BufferedImage> outputEncryptedImageList;
    private ViewImage viewImage=new ViewImage();

    public ParallelEncryption(){
        this.thread=new Thread();
        this.encryption=new Encryption();
        outputEncryptedImageList=new ArrayList<BufferedImage>();
    }

    public void run(){
        try{
            BufferedImage outputEncryptedImage=encryption.doEncryption(seedKey,randomSequenceMatrix, colorChannel,stringChannel);
            //viewImage.displayImage(outputEncryptedImage,"imgErr",outputEncryptedImage.getWidth(),outputEncryptedImage.getHeight());
            addEncryptedImageInList(outputEncryptedImage);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public  List<BufferedImage> getOutputEncryptedImageList() {
        return outputEncryptedImageList;
    }

    public static void setOutputEncryptedImageList(List<BufferedImage> outputEncryptedImageList) {
        ParallelEncryption.outputEncryptedImageList = outputEncryptedImageList;
    }

    public synchronized void addEncryptedImageInList(BufferedImage bufferedImage){
        outputEncryptedImageList.add(bufferedImage);
    }

    public long getSeedKey() {
        return seedKey;
    }

    public void setSeedKey(long seedKey) {
        this.seedKey = seedKey;
    }

    public int[][] getRandomSequenceMatrix() {
        return randomSequenceMatrix;
    }

    public void setRandomSequenceMatrix(int[][] randomSequenceMatrix) {
        this.randomSequenceMatrix = randomSequenceMatrix;
    }

    public BufferedImage getColorChannel() {
        return colorChannel;
    }

    public void setColorChannel(BufferedImage colorChannel) {
        this.colorChannel = colorChannel;
    }

    public String getStringChannel() {
        return stringChannel;
    }

    public void setStringChannel(String stringChannel) {
        this.stringChannel = stringChannel;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public Encryption getEncryption() {
        return encryption;
    }

    public void setEncryption(Encryption encryption) {
        this.encryption = encryption;
    }
}
