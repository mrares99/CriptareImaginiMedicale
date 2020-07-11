import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Decryption {

    public List<int[][]> generateRandomSequenceForChannels(long seed, int rows, int columns){
        int[][] redMatrixRandomSequence = new int[columns][rows];
        int[][] greenMatrixRandomSequence = new int[columns][rows];
        int[][] blueMatrixRandomSequence = new int[columns][rows];
        Random random=new Random();
        random.setSeed(seed);
        for(int i=0;i<columns;i++){
            for(int j=0;j<rows;j++){
                redMatrixRandomSequence[i][j]=random.nextInt(8);
                greenMatrixRandomSequence[i][j]=random.nextInt(8);
                blueMatrixRandomSequence[i][j]=random.nextInt(8);
            }
        }
        List<int[][]> list2d=new ArrayList<int[][]>();
        list2d.add(redMatrixRandomSequence);
        list2d.add(greenMatrixRandomSequence);
        list2d.add(blueMatrixRandomSequence);
        return list2d;
    }

    public BufferedImage doDecryption(long seed, int [][] matrix, BufferedImage colorChannel, String channel) throws StringException {
        BufferedImage outputBufferedImage=new BufferedImage(colorChannel.getWidth(), colorChannel.getHeight(),BufferedImage.TYPE_INT_RGB);
        Random random=new Random();
        random.setSeed(seed);
        int d=0;
        if(channel.equals("red")){
            d=random.nextInt(8);//initial shift
        }
        else if(channel.equals("green")){
            d=random.nextInt(8);//initial shift
            d+=random.nextInt(8);
        }
        else if(channel.equals("blue")){
            d=random.nextInt(8);//initial shift
            d+=random.nextInt(8);
            d+=random.nextInt(8);
        }
        else throw new StringException("Invalid name for color channel.The choices are:'red','green' or 'blue'.");
        d=d%8;
        for(int i=0;i<colorChannel.getWidth();i++){
            for(int j=0;j<colorChannel.getHeight();j++){
                int rgb=colorChannel.getRGB(i,j);
                rgb=rgb & 0x00ffffff;
                //trebe verificat ce culoare este si apoi convertita la byte si apoi shiftata
                byte byteRGB=0;
                byte shift=0;
                if(i==0 && j<=30) System.out.println("i="+i+"  j="+j+"  rgb="+rgb+"   d="+d);
                if((rgb & 0x00ff0000)!=0){//inseamna ca este culoarea rosie
                    byteRGB=(byte)(rgb>>16);
                    if(i==0 && j<=30) System.out.println(" rosu byteRGB="+byteRGB);
                    shift=circularLeftShift(byteRGB,d);
                    if(i==0 && j<=30) System.out.println(" rosu byteRGB="+byteRGB+" byteRGB dupa shift cu 'd'="+shift);
                    shift=circularLeftShift(shift, matrix[i][j]);
                    if(i==0 && j<=30) System.out.println(" rosu byteRGB="+byteRGB+" byteRGB dupa shift cu random sequence="+shift+" random sequence="+matrix[i][j]);
                    rgb=(int)shift<<16;
                    if(i==0 && j<=30) System.out.println("i="+i+"  j="+j+"  rgb="+(rgb & 0x00ff0000));
                    outputBufferedImage.setRGB(i, j, rgb & 0x00ff0000);
                }
                if((rgb & 0x0000ff00)!=0){//inseamna ca este culoarea verde
                    byteRGB=(byte)(rgb>>8);
                    shift=circularLeftShift(byteRGB,d);
                    shift=circularLeftShift(shift, matrix[i][j]);
                    rgb=(int)shift<<8;
                    outputBufferedImage.setRGB(i, j, rgb & 0x0000ff00);
                }
                if((rgb & 0x000000ff)!=0){//inseamna ca este culoarea albastra
                    byteRGB=(byte)rgb;
                    shift=circularLeftShift(byteRGB,d);
                    shift=circularLeftShift(shift, matrix[i][j]);
                    rgb=shift;
                    outputBufferedImage.setRGB(i, j, rgb & 0x000000ff);
                }
            }
            d++;
        }

        return outputBufferedImage;
    }

    public byte circularRightShift(byte number,int distance){
        int rotation=distance;
        rotation %= 8;
        byte num=number;
        while((rotation--)!=0)
            num = (byte) ((num >> 1) & (~(1 << 7)) | ((num & 1) << 7));

        return num;
    }

    public byte circularLeftShift(byte number,int distance){
        int rotation=distance;
        rotation %= 8;
        byte num=number;
        while((rotation--)!=0)
            num = (byte) ((num << 1) | (1 & (num >> 7)));

        return num;

        //return (byte) ((number << distance) | (number >> (8 - distance)));
    }

}
