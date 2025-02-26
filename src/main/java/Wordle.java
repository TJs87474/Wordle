import java.util.Scanner;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;

public class Wordle extends PlayerInfo implements Resetable {
    private final int MAX_ATTEMPTS;
    private final String MERRIAM_WEBSTER_API_KEY = "ca1c447b-d172-44fd-83bf-c9a82d4bd315";  // Replace with your actual API key
    private final String MERRIAM_WEBSTER_API_URL = "https://dictionaryapi.com/api/v3/references/";
    private final String path = "src/main/java/";
    private int attemptsUsed;
    private int guessTotal;
    private String guess = "";  // Empty string instead of "xxxxx"

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
        if (guess == null || guess.isEmpty()) {
            return false;
        }
    
        if (guess.equals("xxxxx")) {
            System.out.println("Skipping API call: Default guess.");
            return false;
        }
    
        boolean validWord = false;
        try {
            String urlStr = "https://dictionaryapi.com/api/v3/references/learners/json/" + guess + "?key=" + "ca1c447b-d172-44fd-83bf-c9a82d4bd315";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
    
            int statusCode = conn.getResponseCode();
            if (statusCode != 200) {
                System.out.println("Error: Received HTTP " + statusCode);
                return false;
            }
    
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
    
            // Log the raw response for debugging
          //  System.out.println("Raw API Response: " + response.toString());
    
            // Check if the response is a valid JSON array
            if (!response.toString().startsWith("[")) {
                System.out.println("Invalid API response format.");
                return false;
            }
    
            JSONArray jsonResponse = new JSONArray(response.toString());
            if (jsonResponse.length() > 0 && jsonResponse.get(0) instanceof org.json.JSONObject) {
                org.json.JSONObject firstEntry = jsonResponse.getJSONObject(0);
                if (firstEntry.has("meta")) {
                    validWord = true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error when checking for guess validity: " + e.getMessage());
        }
    
        return guess.length() == this.word.length() && validWord;
    }
      
    

    private boolean firstGuess = true;

    public void setGuess(String guess) {
        if (firstGuess) {
            firstGuess = false; // Allow valid guesses after this
        }
    
        if (guess == null || guess.isEmpty() || guess.equals("xxxxx")) {
            System.out.println("Invalid input: Guess is empty or uninitialized.");
            return;
        }
    
        if (this.isValidGuess(guess)) {
            this.guess = guess.toLowerCase();
        } else {
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
                System.out.println();
                charCount[j]++;
                return true;
            }
        }
        return false;
    }

    public void printAttempt() {
        attemptsUsed++;
    
        // If the guess is correct, print the whole word in green and exit
        if (this.isGuessCorrect()) {
            ColorString.print(guess, "green");
            System.out.println(); // Optionally print a new line after a correct guess
            return;
        }
    
        // Initialize an array to track character counts for color coding
        int[] charCount = new int[this.word.length()];
        
        // Print the guess in one go on the same line
        for (int i = 0; i < this.guess.length(); i++) {
            // Check for 'green' first (exact match with the word)
            if (!this.greenCheck(charCount, i)) {
                // Check for 'yellow' (correct letter, but wrong position)
                if (!this.yellowCheck(charCount, i)) {
                    // If neither, print 'red' (wrong letter)
                    ColorString.print(this.guess.substring(i, i + 1), "red");
                }
            }
        }
        
        // Ensure output stays on the same line
        System.out.print(" ");  // Adds a space between attempts but keeps it on the same line
        System.out.println();
        // Optionally, if you want to add a newline after each guess, use `System.out.println()` here.
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