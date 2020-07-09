import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Encryption {

    public List<int[][]> generateRandomSequenceForChannels(int seed,int rows,int columns){
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

    public BufferedImage doEncryption(int seed,int [][] matrix,BufferedImage colorChannel,
                                         String channel) throws StringException {
        BufferedImage outputBufferedImage=new BufferedImage(colorChannel.getWidth(),
                colorChannel.getHeight(),BufferedImage.TYPE_INT_ARGB);
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
        else throw new StringException("Invalid name for color channel.Only valid:red,green,blue.");
        d=d%8;
        for(int i=0;i<colorChannel.getWidth();i++){
            for(int j=0;j<colorChannel.getHeight();j++){
                int rgb=colorChannel.getRGB(i,j);
                int shift=circularRightShift(rgb,d);
                outputBufferedImage.setRGB(i, j,
                        circularRightShift(shift, matrix[i][j]));
            }
            d++;
        }

        return outputBufferedImage;
    }

    public int circularRightShift(int number,int distance){
        return Integer.rotateRight(number,distance);
    }

}
