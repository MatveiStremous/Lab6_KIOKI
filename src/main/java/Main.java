import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        int seed = 112;
        int n = 1;
        int y = 5;
        int startSum = getSum("src/main/resources/2.bmp", seed, n);
        makeDigital("src/main/resources/2.bmp", "src/main/resources/3.bmp", seed, n, y);
        int endSum = getSum("src/main/resources/3.bmp", seed, n);
        System.out.println("Start: "+startSum+"\nEnd: "+endSum+"\nDif: "+(endSum-startSum)+"\nn: "+(endSum-startSum)/y);
    }

    public static int checkValue(int value) {
        if (value < 0) {
            value = 0;
        } else if (value > 255) {
            value = 255;
        }
        return value;
    }

    public static int[] changeBright(int[] rgb, int y) {
        rgb[0] = checkValue(rgb[0] + y);
        rgb[1] = checkValue(rgb[1] + y);
        rgb[2] = checkValue(rgb[2] + y);
        return rgb;
    }

    public static void makeDigital(String fromFile, String toFile, int seed, int n, int y) throws IOException {
        BufferedImage image = ImageIO.read(new File(fromFile));
        int[] rgb1;
        int[] rgb2;
        Random rand = new Random(seed);
        int height = image.getHeight();
        int width = image.getWidth();
        for (int k = 0; k < n; k += 1) {
            int i = rand.nextInt(height);
            int j = rand.nextInt(width);
            rgb1 = image.getRaster().getPixel(i, j, new int[3]);
            rgb1 = changeBright(rgb1, y);
            int i1 = rand.nextInt(height);
            int j1 = rand.nextInt(width);
            rgb2 = image.getRaster().getPixel(i1, j1, new int[3]);
            rgb2 = changeBright(rgb2, -y);
            image.getRaster().setPixel(i, j, rgb1);
            image.getRaster().setPixel(i1, j1, rgb2);
        }
        ImageIO.write(image, "bmp", new File(toFile));
    }

    public static int getSum(String filePath, int seed, int n) throws IOException {
        BufferedImage image = ImageIO.read(new File(filePath));
        Random rand = new Random(seed);
        int height = image.getHeight();
        int width = image.getWidth();
        int[] rgb1;
        int[] rgb2;
        int sum = 0;
        for (int k = 0; k < n; k += 1) {
            int i = rand.nextInt(height);
            int j = rand.nextInt(width);
            rgb1 = image.getRaster().getPixel(i, j, new int[3]);
            sum+=rgb1[0]+rgb1[1]+rgb1[2];
            int i1 = rand.nextInt(height);
            int j1 = rand.nextInt(width);
            rgb2 = image.getRaster().getPixel(i1, j1, new int[3]);
            sum-=rgb2[0]-rgb2[1]-rgb2[2];
        }
        return sum;
    }
}
