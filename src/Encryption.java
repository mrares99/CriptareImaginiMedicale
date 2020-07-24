import java.awt.image.BufferedImage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Encryption {

    public List<int[][]> generateRandomSequenceForChannels(long seed,int rows,int columns) throws NoSuchAlgorithmException {
        int[][] redMatrixRandomSequence = new int[rows][columns];
        int[][] greenMatrixRandomSequence = new int[rows][columns];
        int[][] blueMatrixRandomSequence = new int[rows][columns];
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG") ;
        random.setSeed(seed);
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
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

    public BufferedImage doEncryption(long seed,int [][] matrix,BufferedImage colorChannel, String channel) throws StringException, NoSuchAlgorithmException {
        BufferedImage outputBufferedImage=new BufferedImage(colorChannel.getWidth(), colorChannel.getHeight(),BufferedImage.TYPE_INT_RGB);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG") ;
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
        for(int i=0;i<colorChannel.getHeight()-1;i++){
            for(int j=0;j<colorChannel.getWidth()-1;j++){
                int rgb=colorChannel.getRGB(j,i);
                rgb=rgb & 0x00ffffff;
                //trebe verificat ce culoare este si apoi convertita la byte si apoi shiftata
                byte byteRGB=0;
                byte shift=0;
                //am facut AND cu 0x00ff0000 sa elimin partea alfa,adica transparenta
                if((rgb & 0x00ff0000)!=0){//inseamna ca este culoarea rosie
                    byteRGB=(byte)(rgb>>16);
                    shift=circularRightShift(byteRGB,d);
                    shift=circularRightShift(shift, matrix[i][j]);
                    rgb=(int)shift<<16;
                    outputBufferedImage.setRGB(j, i, rgb & 0x00ff0000);
                }
                if((rgb & 0x0000ff00)!=0){//inseamna ca este culoarea verde
                    byteRGB=(byte)(rgb>>8);
                    shift=circularRightShift(byteRGB,d);
                    shift=circularRightShift(shift, matrix[i][j]);
                    rgb=(int)shift<<8;
                    outputBufferedImage.setRGB(j, i, rgb & 0x0000ff00);
                }
                if((rgb & 0x000000ff)!=0){//inseamna ca este culoarea albastra
                    byteRGB=(byte)rgb;
                    shift=circularRightShift(byteRGB,d);
                    shift=circularRightShift(shift, matrix[i][j]);
                    rgb=(int)shift;
                    outputBufferedImage.setRGB(j, i, rgb & 0x000000ff);
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

}
