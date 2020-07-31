package main.java;

import java.awt.image.BufferedImage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Decryption {

    public List<int[][]> generateRandomSequenceForChannels(long seed, int rows, int columns) throws NoSuchAlgorithmException {
        int[][] redMatrixRandomSequence = new int[rows][rows];
        int[][] greenMatrixRandomSequence = new int[rows][rows];
        int[][] blueMatrixRandomSequence = new int[rows][rows];
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG") ;
        random.setSeed(seed);
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                redMatrixRandomSequence[i][j]=random.nextInt(columns);
                greenMatrixRandomSequence[i][j]=random.nextInt(columns);
                blueMatrixRandomSequence[i][j]=random.nextInt(columns);
            }
        }
        List<int[][]> list2d=new ArrayList<int[][]>();
        list2d.add(redMatrixRandomSequence);
        list2d.add(greenMatrixRandomSequence);
        list2d.add(blueMatrixRandomSequence);
        return list2d;
    }

    public BufferedImage doDecryption(long seed, int [][] matrix, BufferedImage colorChannel, String channel) throws StringException, NoSuchAlgorithmException {
        int width=colorChannel.getWidth(),height= colorChannel.getHeight();
        BufferedImage outputBufferedImage=new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG") ;
        random.setSeed(seed);
        List<List<Integer>> bitsFromRows;
        if(channel.equals("red")){
            bitsFromRows=getRowBitsFromImage(colorChannel,"red");
        }
        else if(channel.equals("green")){
            bitsFromRows=getRowBitsFromImage(colorChannel,"green");
        }
        else if(channel.equals("blue")){
            bitsFromRows=getRowBitsFromImage(colorChannel,"blue");
        }
        else throw new StringException("Invalid name for color channel.The choices are:'red','green' or 'blue'.");
        int distance=0;
        List<Integer> auxiliaryList;
        for(int i=0;i<height;i++){
            distance=0;
            for(int j=0;j<width;j++){
                distance+=matrix[i][j];
            }
            auxiliaryList=doCircularLeftShift(bitsFromRows.get(i),distance % width);
            bitsFromRows.set(i,auxiliaryList);
        }
        outputBufferedImage=recreateImageFromList(bitsFromRows,width,height,channel);
        return outputBufferedImage;
    }

    public List<List<Integer>> getRowBitsFromImage(BufferedImage inputBufferedImage,String channel){
        List<List<Integer>> outputRows= new ArrayList<List<Integer>>();//lista de randuri din imagine/matrice
        int height=inputBufferedImage.getHeight(),width=inputBufferedImage.getWidth();
        for(int i=0;i<height;i++){
            List<Integer> row=new ArrayList<Integer>();//rand din imagine
            for(int j=0;j<width;j++){
                int rgb=inputBufferedImage.getRGB(j,i);
                rgb=rgb & 0x00ffffff;
                byte byteRGB=0;
                String bitRepresentaton="";
                if(channel.equals("red")){
                    byteRGB=(byte)(rgb>>16);
                    bitRepresentaton=String.format("%8s", Integer.toBinaryString(byteRGB & 0xFF)).replace(' ', '0');
                    for(int counter=0;counter<8;counter++){
                        row.add(Integer.parseInt(String.valueOf(bitRepresentaton.charAt(counter))));
                    }
                }
                if(channel.equals("green")){
                    byteRGB=(byte)(rgb>>8);
                    bitRepresentaton=String.format("%8s", Integer.toBinaryString(byteRGB & 0xFF)).replace(' ', '0');
                    for(int counter=0;counter<8;counter++){
                        row.add(Integer.parseInt(String.valueOf(bitRepresentaton.charAt(counter))));
                    }
                }
                if(channel.equals("blue")){
                    byteRGB=(byte)rgb;
                    bitRepresentaton=String.format("%8s", Integer.toBinaryString(byteRGB & 0xFF)).replace(' ', '0');
                    for(int counter=0;counter<8;counter++){
                        row.add(Integer.parseInt(String.valueOf(bitRepresentaton.charAt(counter))));
                    }
                }
            }
            outputRows.add(row);
        }
        return outputRows;
    }

    public List<Integer> doCircularLeftShift(List<Integer> inputList,int distance){
        List<Integer> outputList=inputList;
        int range=outputList.size();
        for(int counter=0;counter<distance;counter++){
            int aux=outputList.get(0);
            for(int i=0;i<range-1;i++){
                outputList.set(i,outputList.get(i+1));
            }
            outputList.set(range-1,aux);
        }
        return outputList;
    }

    public BufferedImage recreateImageFromList(List<List<Integer>> inputList,int width,int height,String color){
        BufferedImage outputBufferedImage=new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        int nrRows=inputList.size(),nrBitsInARow=inputList.get(0).size(),counter=0,columnNumber=0;
        String result="";
        for(int i=0;i<nrRows;i++){
            result="";
            columnNumber=0;
            for(int j=0;j<nrBitsInARow;j++){
                result+=inputList.get(i).get(j);
                counter++;
                if(counter==8){
                    byte byteRGB= (byte) Integer.parseInt(result,2);
                    result="";
                    counter=0;
                    if(color.equals("red")){
                        outputBufferedImage.setRGB(columnNumber,i,((int)byteRGB<<16) & 0x00ff0000);
                    }
                    if(color.equals("green")){
                        outputBufferedImage.setRGB(columnNumber,i,((int)byteRGB<<8)  & 0x0000ff00);
                    }
                    if(color.equals("blue")){
                        outputBufferedImage.setRGB(columnNumber,i,((int)byteRGB) & 0x000000ff);
                    }
                    columnNumber++;
                }
            }
        }
        return outputBufferedImage;
    }

}
