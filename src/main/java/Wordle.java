import java.util.Scanner;
import java.net.URL;
import java.net.URI;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class Wordle extends PlayerInfo implements Resetable {
    private final int MAX_ATTEMPTS;
    private final String API_KEY = "c7990710-59c5-4ec5-8523-08c28d121878";  // Replace with your actual API key
    private final String DICTIONARY_API_URL = "https://dictionaryapi.com/api/v3/references/sd4/json/";
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
            try {
                // Construct the API URL for the word
                String address = DICTIONARY_API_URL + guess + "?key=" + API_KEY;
                URL pageLocation = new URI(address).toURL();

                // Open connection and read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(pageLocation.openStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response
                JSONArray jsonResponse = new JSONArray(response.toString());

                // Check if the response contains word definitions (indicating a valid word)
                if (jsonResponse.length() > 0 && jsonResponse.getJSONObject(0).has("meta")) {
                    validWord = true;
                }
            } catch (Exception exception) {
                System.out.println("Error when checking for guess validity.\n" + exception);
            }

            // The guess is valid if it's the correct length and found in the dictionary
            return guess.length() == this.word.length() && validWord;
        }

    public String getRandomWord(int wordLength) {
        // You can implement this similar to how you validate the guess
        return "sample"; // Placeholder, replace with actual logic to fetch a word based on length
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