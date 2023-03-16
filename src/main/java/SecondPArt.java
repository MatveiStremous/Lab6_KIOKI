import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import javax.sound.sampled.*;

public class SecondPArt {

    public static void main(String[] args) throws IOException {
        String message = "Hello ahahahahahahahhahahahahahahahahaha";
        //System.out.println(getMessageFromFile("src/main/resources/test.wav"));
        injectMessage(message, "src/main/resources/test.wav", "src/main/resources/test2.wav");
        System.out.println(getMessageFromFile("src/main/resources/test2.wav").substring(0, message.length()));
    }

    public static void injectMessage(String message, String fromFile, String toFile) throws IOException {
        byte[] array = getAllBytes(fromFile);
        //checkArray(array);
        StringBuilder binMsg = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            StringBuilder s = new StringBuilder(Integer.toBinaryString(message.charAt(i)));
            while (s.length() < 8) {
                s.insert(0, "0");
            }
            binMsg.append(s);
        }

        for (int i = 0, j = 0; i < array.length && j + 1 < binMsg.length(); i++, j += 2) {
            StringBuilder s = new StringBuilder(Integer.toBinaryString((array[i] + 256) % 256));
            while (s.length() < 8) {
                s.insert(0, "0");
            }
            s = new StringBuilder(s.substring(0, 6)).append(binMsg.charAt(j)).append(binMsg.charAt(j + 1));
            if (Objects.equals(s.toString(), "00000000")) {
                s = new StringBuilder("0");
            } else {
                while (Objects.equals(s.charAt(0), '0')) {
                    s = new StringBuilder(s.substring(1));
                }
            }
            array[i] = (byte) Integer.parseInt(String.valueOf(s), 2);
        }
        //checkArray(array);
        writeToFile(array, toFile);
    }

    public static String getMessageFromFile(String fromFile) throws IOException {
        byte[] array = getAllBytes(fromFile);
        StringBuilder binMsg = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            StringBuilder s = new StringBuilder(Integer.toBinaryString((array[i] + 256) % 256));
            while (s.length() < 8) {
                s.insert(0, "0");
            }
            binMsg.append(s.substring(6, 8));
        }

        StringBuilder msg = new StringBuilder();

        for (int i = 0; i < binMsg.length() / 8; i++) {
            byte a = (byte) Integer.parseInt(binMsg.substring(i * 8, (i + 1) * 8), 2);
            msg.append((char) a);
        }

        return msg.toString();
    }

    public static byte[] getAllBytes(String fromFile) throws IOException {
        int unusefulDataLength = 44;
        byte[] array = Files.readAllBytes(Paths.get(fromFile));
        byte[] newArray = new byte[array.length - unusefulDataLength];
        System.arraycopy(array, unusefulDataLength, newArray, 0, newArray.length);
        return newArray;
    }

    public static void writeToFile(byte[] array, String toFile) throws IOException {
        AudioFormat format = new AudioFormat(44100.0f, 32, 1, true, false);
        ByteArrayInputStream bytes = new ByteArrayInputStream(array);
        AudioInputStream audioInputStream = new AudioInputStream(bytes, format, array.length / 2);
        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(toFile));
        audioInputStream.close();
    }
}
