import java.net.URL;
import java.net.URI;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class Wordle extends PlayerInfo implements Resetable {
    private final int MAX_ATTEMPTS;
    private final String API_KEY = "c7990710-59c5-4ec5-8523-08c28d121878";  // Your API key
    private final String DICTIONARY_API_URL = "https://dictionaryapi.com/api/v3/references/sd4/json/";
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
        this.word = getWordFromAPI(wordLength);  // Fetch word from API based on wordLength
    }

    // Fetch a word from the Merriam-Webster API based on word length
    public String getWordFromAPI(int wordLength) {
        try {
            String address = DICTIONARY_API_URL + "example" + "?key=" + API_KEY; // Example word, update to fetch a word with the length you need
            URL pageLocation = new URI(address).toURL();

            // Open connection and read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(pageLocation.openStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Print the raw API response for debugging
            System.out.println("Raw API Response: " + response.toString());

            // Parse the JSON response
            JSONArray jsonResponse = new JSONArray(response.toString());

            // If the response contains word definitions, fetch the first word's definition
            if (jsonResponse.length() > 0) {
                JSONObject wordEntry = jsonResponse.getJSONObject(0);
                // Assuming the word we get is valid, assign it to the target word
                if (wordEntry.has("word")) {
                    return wordEntry.getString("word");
                }
            }
            return "sample";  // Default word if no valid word found
        } catch (Exception exception) {
            System.out.println("Error when fetching word from API: " + exception.getMessage());
        }
        return "sample";  // Default word if an error occurs
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

    // Other existing methods remain unchanged...

    public void resetGame() {
        this.attemptsUsed = 0;
        this.guess = "xxxxx";
        this.word = getWordFromAPI(this.word.length());  // Fetch a new word based on the word length
    }

    public void setGuess(String guess) {
    this.guess = guess.toLowerCase();
    this.attemptsUsed++;
}

public void addGuessTotal() {
    this.guessTotal++;
}

public boolean isGameOver() {
    return attemptsUsed >= MAX_ATTEMPTS || guess.equals(word);
}

public void printAttempt() {
    System.out.println("Attempt " + attemptsUsed + " of " + MAX_ATTEMPTS);
    for (int i = 0; i < word.length(); i++) {
        if (i < guess.length()) {
            if (guess.charAt(i) == word.charAt(i)) {
                System.out.print("🟩"); // Correct letter, correct position
            } else if (word.contains(String.valueOf(guess.charAt(i)))) {
                System.out.print("🟨"); // Correct letter, wrong position
            } else {
                System.out.print("⬜"); // Letter not in word
            }
        }
    }
    System.out.println();
}

public String toString() {
        String gameOver = isGameOver() ? "over" : "not over";
        return String.format(
            "%s Word: %s, Current Guess: %s, Attempts Used: %d, Total guesses: %d, This game is %s", super.toString(), this.word, this.guess, this.attemptsUsed, this.guessTotal, gameOver);
    }
}