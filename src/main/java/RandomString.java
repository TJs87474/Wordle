import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public final class RandomString {
    private RandomString() {}

    public static String getRandomWord(String fileName) {
        Scanner scan = null;
        String word = "";

        try {
            File inFile = new File(fileName);
            scan = new Scanner(inFile);

            Random rand = new Random();
            int randomIndex = rand.nextInt(1000);
            int i = 0;
            while (scan.hasNextLine() && i < randomIndex) {
                word = scan.nextLine();
                i++;
            }
        }
        catch (FileNotFoundException exception) {
            System.out.println("Error: word bank file not found");
        }
        catch (Exception exception) {
            System.out.println(exception);
        }
        finally {
            if (scan != null) {
                scan.close();
            }
        }

        return word;
    }
}