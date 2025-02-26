import java.util.Scanner;
public class Main {
  public static void main(String[] args) {

    double guessAvg;
    String guess;
    int guessLength = 0;

    Scanner keyboard = new Scanner(System.in);
      
    System.out.println("Welcome to Wordle Two!");
      
    while(guessLength < 5 || guessLength > 8) {
      System.out.print("Please enter a number between 5 and 8: ");
      guessLength = keyboard.nextInt();
    }

    System.out.println("You will have " + (guessLength + 1) + " attempts to guess a " + guessLength + " letter word.");
      
    Wordle wordle = new Wordle(guessLength);

    //Loops until the game ends
    while(!wordle.isGameOver()) {
      System.out.print("Please enter a guess: ");
      guess = keyboard.nextLine(); 

      //Checks for the validity of the users inputted word
      while (!wordle.isValidGuess(guess)){
        System.out.print("This word is not valid. Please enter a another word: ");
        guess = keyboard.nextLine();
      }

      
      wordle.setGuess(guess);
      wordle.addGuessTotal();
      wordle.printAttempt();
      
     
      
    }

  }
}
