import java.util.Scanner;
import java.net.URL;
import java.net.URI;

public class Wordle extends PlayerInfo implements Resetable {
    private final int MAX_ATTEMPTS;
    private final String path = "src/main/java/";
    private int attemptsUsed;
    private int guessTotal;
    private String guess;
    private String word;

    public Wordle(int wordLength) {
        super();
        this.attemptsUsed = 0;
        this.guessTotal = 0;
        this.guess = "xxxxx";
        this.MAX_ATTEMPTS = wordLength + 1;
        this.word = RandomString.getRandomWord(this.path + wordLength + ".txt");
    }

    public boolean isValidGuess(String guess) {
        boolean validWord = false;
        Scanner scan = null;
        try {
            String address = "https://dictionaryapi.com/api/v3/references/collegiate/json/" + guess + "?key=ee1420bc-cd86-4dca-bcaa-4e92442b0305";
            URL pageLocation = new URI(address).toURL();

            scan = new Scanner(pageLocation.openStream());
            scan.next();

            if (scan.hasNext()) {
                validWord = true;
            }
        }
        catch (Exception exception){
            System.out.println("Error when checking for guess validity.\n" + exception);
        }
        finally {
            if (scan != null) {
                scan.close();
            }
        }
        return guess.length() == this.word.length() && validWord;
    }

    public void setGuess(String guess) {
        if (this.isValidGuess(guess)) {
            this.guess = guess.toLowerCase();
        }
        else {
            this.guess = "xxxxx";
        }
    }

    public void addGuessTotal() {
        this.guessTotal++;
    }

    public double averageGuesses() {
        if (super.getTotalPlays() != 0) {
        return (double) this.guessTotal / super.getTotalPlays();
        }
        return 0.0;
    }

    public boolean isGuessCorrect() {
        return this.guess.equals(this.word);
    }

    public boolean isGameOver() {
        return this.isGuessCorrect() || this.attemptsUsed == MAX_ATTEMPTS;
    }

    private int countCharApperance(char c) {
        int count = 0;
        for (int i = 0; i < this.word.length(); i++) {
            if (this.word.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    private boolean greenCheck(int[] charCount, int i) {
        if ((this.guess.charAt(i) == this.word.charAt(i)) && (charCount[i] < countCharApperance(this.word.charAt(i)))){
            ColorString.print(this.guess.substring(i, i + 1), "green");
            charCount[i]++;
            return true;
        }
        return false;
    }

    private boolean yellowCheck(int[] charCount, int i) {
        for (int j = 0; j < this.guess.length(); j++){
            if ((this.guess.charAt(i) == this.word.charAt(j)) && (charCount[j] < countCharApperance(this.word.charAt(j)))) {
                ColorString.print(this.guess.substring(i, i + 1), "yellow");
                charCount[j]++;
                return true;
            }
        }
        return false;
    }

    public void printAttempt() {
        attemptsUsed++;

        if (this.isGuessCorrect()) {
            ColorString.print(guess, "green");
            return;
        }

        int[] charCount = new int[this.word.length()];
        for (int i = 0; i < this.guess.length(); i++) {
            if (!this.greenCheck(charCount, i) && !this.yellowCheck(charCount, i)) {
                ColorString.print(this.guess.substring(i, i + 1), "red");
            }
        }
    }

    public void resetGame() {
        this.attemptsUsed = 0;
        this.guess = "xxxxx";
        this.word = RandomString.getRandomWord(this.path + this.word.length() + ".txt");
    }



    public String toString() {
        String gameOver = isGameOver() ? "over" : "not over";
        return String.format(
            "%s Word: %s, Current Guess: %s, Attempts Used: %d, Total guesses: %d, This game is %s", super.toString(), this.word, this.guess, this.attemptsUsed, this.guessTotal, gameOver);
    }
}