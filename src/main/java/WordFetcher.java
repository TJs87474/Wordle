import java.net.URL;
import java.net.URI;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class WordFetcher {

    private static final String API_KEY = "c7990710-59c5-4ec5-8523-08c28d121878"; // Your API key
    private static final String DICTIONARY_API_URL = "https://dictionaryapi.com/api/v3/references/sd4/json/";

    public static void main(String[] args) {
        String word = "example";  // Word to fetch definition for
        getWordDefinition(word);
    }

    public static void getWordDefinition(String word) {
        try {
            // Construct the API URL for the word
            String address = DICTIONARY_API_URL + word + "?key=" + API_KEY;
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

            // Check if the response contains word definitions
            if (jsonResponse.length() > 0) {
                // Get the first entry's definition
                JSONObject wordEntry = jsonResponse.getJSONObject(0);
                System.out.println("Word: " + wordEntry.getString("word"));

                if (wordEntry.has("def")) {
                    JSONArray definitions = wordEntry.getJSONArray("def");
                    for (int i = 0; i < definitions.length(); i++) {
                        JSONObject definition = definitions.getJSONObject(i);
                        System.out.println("Definition " + (i + 1) + ": " + definition.getString("dt"));
                    }
                } else {
                    System.out.println("No definitions found for this word.");
                }
            } else {
                System.out.println("No results found for the word: " + word);
            }

        } catch (Exception exception) {
            System.out.println("Error when fetching word definition: " + exception.getMessage());
        }
    }
}
