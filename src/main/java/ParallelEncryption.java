package main.java;

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

    public ParallelEncryption(){
        this.thread=new Thread();
        this.encryption=new Encryption();
        outputEncryptedImageList=new ArrayList<BufferedImage>();
    }

    public void run(){
        try{
            //BufferedImage outputEncryptedImage=encryption.doEncryption(seedKey,randomSequenceMatrix, colorChannel,stringChannel);
            //addEncryptedImageInList(outputEncryptedImage);
            addEncryptedImageInList(encryption.doEncryption(seedKey,randomSequenceMatrix, colorChannel,stringChannel));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public  List<BufferedImage> getOutputEncryptedImageList() {
        return outputEncryptedImageList;
    }

    public synchronized void addEncryptedImageInList(BufferedImage bufferedImage){
        outputEncryptedImageList.add(bufferedImage);
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
