/* @author Silas Smith
 * Midterm project
 * CIS-2271
 * A game that scrambles a random word and has the player guess it.
 */
import java.io.*;
import java.util.Scanner;
import java.util.Random;
import java.util.InputMismatchException;
public class WordScramble {
	static int score;
	static Scanner input = new Scanner(System.in);
	static Random r = new Random();
	static int totalScore = 0;
	public static void main(String[] args) {
		String word;
		String scrambledWord;
		int roundsWanted = 0;
		String playAgain = "no";
		intro();
		//Do while loop so you can play again
		do {
			//resets score if you play again
			score = 0;
			totalScore = 0; 
			
			System.out.println("How many rounds would you like to play?");
			//Prevents error if user inputs the wrong data type
			while (true) {
				try {
					roundsWanted = input.nextInt();
					input.nextLine();
					break;
				} catch (InputMismatchException e) {
					System.out.println("Invalid input. Try again:");
					input.nextLine();
				}

			}
			//Runs the game for the chosen amount of rounds
			for (int round = 1; round <= roundsWanted; round++) {

				System.out.printf("\nRound %d/%d\n", round, roundsWanted);
				word = randomWord();

				//ends the program if random word returns an error
				if (word.equals("error"))
					return;

				scrambledWord = wordScramble(word);
				totalScore = totalScore + word.length();
				//Prevents scrambled word from matching the word
				while (word.equals(scrambledWord)) {
					scrambledWord = wordScramble(word);
				}
				
				game(word, scrambledWord);
				
				//prints current score unless its the last round
				if (round < roundsWanted)
					System.out.printf("your current score is: %d/%d\n\n", score, totalScore);
			}
			//prints total score on the last round
			System.out.printf("Your total score is %d/%d\n", score, totalScore);
			accuracy();
			System.out.println("\nWould you like to play again? yes/no:");
			playAgain = input.nextLine();		
		} while (playAgain.equalsIgnoreCase("yes"));
		System.out.println("Thanks for playing!");
	}
	//Chooses a random word by reading WorlList.txt line by line a random amount of times
	public static String randomWord() {
		try {
			String randomWord = null;
			/*Before adding this line I generated the random number inside the parameters of the for loop, 
			 * which created a fun bug where the method would almost always return a word starting with "a"
			 * because the random number changed with every loop and normally while it was still in the a's it 
			 * would hit a low enough number to end the loop.*/ 

			int randomLine = r.nextInt(998) + 1;
			//creates a scanner that scans the text file WordList
			Scanner wordFind = new Scanner(new File("src/WordList.txt"));

			//Loops a random amount of times in order to select a random line 
			for (int line = 1; line <= randomLine; line++) {
				randomWord = wordFind.nextLine();				
			}

			/*Prevents especially easy words from getting picked. I thought about having the user choose a difficulty 
			 * and limiting the word length based on that, but I realized that that would cause certain words to have 
			 * a much higher probability of being picked unless I made some significant changes to how this method works.*/ 

			while (randomWord.length() < 4 && randomLine < 999) {
				randomWord = wordFind.nextLine();
			}
			return randomWord;
			//catches the error if WordList.txt is not found	
		} catch (FileNotFoundException e) {
			System.out.println("\nUh oh, something went wrong.\nError: FileNotFoundException\nWordList.txt was not found.");
			return "error";
		}
	}
	
	//I got this method from Stack Overflow. I understand how it works but I did not write the code.
	public static String wordScramble(String word) {
		//converts the String word into an array of char variables so that you can easily move around the letters.
		char a[] = word.toCharArray();
		//Fisher-Yates shuffle
		for(int i = 0; i < a.length; i++) {
			int j = r.nextInt(a.length);
			char temp = a[i];
			a[i] = a[j];
			a[j] = temp;
			
		}
		//converts the char array back into a String and returns it
		return new String(a);
	}

	public static void game(String word, String scrambledWord) {
		boolean correct = false;
		//your score each round is based on the length of the scrambled word
		int tempScore = word.length();
		int hints = 0;
		System.out.printf("For %d Points\n", tempScore);
		System.out.println("Here is your scrambled word:\n" + scrambledWord + "\nUnscramble the word:");
		do {
			String guess = input.nextLine();
			/*checking the answer this way creates an issue where the user can enter a real word using the scrambled letters but 
			 * still get the the word wrong because it doesn't match the random word chosen. A better way to do this would be to
			 * check if the user used the right letters than to check the input against a large database of words.  
			*/
			if (guess.equalsIgnoreCase(word)) {
				correct = true;
				System.out.printf("Correct! %d points!\n", tempScore);
			}
			//Lets user ask for hint
			else if (guess.equalsIgnoreCase("hint")){
				//prevents error if user asks for to many hints
				if (tempScore == 1) {
					System.out.printf("\nThe answer was: %s\nPoints this round = %d\n", word, --tempScore); 
					break;
				}

				System.out.printf("\nFor %d Points\n",--tempScore);
				hints(hints, word);
				hints++;
				System.out.println(scrambledWord + "\nUnscramble the word:");
			}
			//lets user give up
			else if (guess.equalsIgnoreCase("I give up")) {
				tempScore = 0;
				System.out.printf("\nThe answer was: %s\nPoints this round = %d\n", word, tempScore); 
				break;
			}
			
			else {
				System.out.println("\nWrong answer.");
				tempScore--;
				//Ends the round if the user runs out of points
				if (tempScore == 0) {
					System.out.printf("The answer was: %s\nPoints this round = %d\n", word, tempScore); 
					break;
				}
				else {
					System.out.printf("For %d Points\n", tempScore);
					//shows previous hints. -1 so it doesn't create a new hint.
					if (hints > 0)
						hints(hints-1, word);
					System.out.printf("%s\nTry Again:\n", scrambledWord);
				}

			}

		} while (correct == false);
		score = score + tempScore;
	}
	public static void intro() {
		System.out.println("Welcome to Word Scramble!");
		System.out.println("The goal of the game is to unscamble the scrambled word.");
		System.out.println("Sometimes even if you enter a real word from the scrambled letters you will still get the word wrong because it does not match the original word.");
		System.out.println("If at any point you want a hint simply type \"hint\"");
		System.out.println("You will then be given the first letter of the word and will lose one point off your score.");
		System.out.println("You can use \"hint\" multiple times to recieve more letters of the word.");
		System.out.println("If you give up type \"I give up\" you will recieve a zero for that round. \nPress enter to continue... ");
		input.nextLine();
	}
	
	//shows previous hints
	public static void hints(int hints, String word) {
		for (int hintLoops = 0; hintLoops <= hints; hintLoops++) {
			System.out.printf("Letter %d is : %c\n",hintLoops + 1, word.charAt(hintLoops));
		}
	}

	public static void accuracy() {
		double accuracy = ((double)score / totalScore) * 100;
		System.out.println("Accuracy: " + Math.round(accuracy) + "%");
	}
}
/*I enjoyed working on this project and I will probably continue to work on it after submitting it. 
 * I was thinking of adding a leaderboard by sorting the scores with an array and storing them in a text file, and
 * implementing the improved method of checking the users guess like I mentioned above. 
 */
