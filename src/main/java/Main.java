import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        double guessAvg;
        String guess = "";
        int guessLength = 0;

        Scanner keyboard = new Scanner(System.in);
        
        System.out.println("Welcome to Wordle Two!");
        
        // Ask for guess length, between 5 and 8
        while(guessLength < 5 || guessLength > 8) {
            System.out.print("Please enter a number between 5 and 8: ");
            guessLength = keyboard.nextInt();
        }

        // Consume the newline character left by nextInt()
        keyboard.nextLine();
        
        System.out.println("You will have " + (guessLength + 1) + " attempts to guess a " + guessLength + " letter word.");
        
        // Create the Wordle game object
        Wordle wordle = new Wordle(guessLength);

        // Loop until the game ends
        while(!wordle.isGameOver()) {
            System.out.print("Please enter a guess: ");
            guess = keyboard.nextLine();  // Read user guess
            
            // Check for validity of the user's input word
            while (!wordle.isValidGuess(guess)){
                System.out.print("This word is not valid. Please enter another word: ");
                guess = keyboard.nextLine();  // Re-read input if invalid
            }

            // Set the valid guess and update game state
            wordle.setGuess(guess);
            wordle.addGuessTotal();
            wordle.printAttempt();  // Print attempt (e.g., with colors)
        }

        // Optionally, display some game over message or stats
       // System.out.println("Game Over! Final word was: " + wordle.getWord());
        guessAvg = wordle.averageGuesses();
        System.out.println("Average guesses per game: " + guessAvg);
    }
}
