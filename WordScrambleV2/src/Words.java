import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
public class Words {
	//Data
	private String word;
	private String scrambledWord;
	private String[] wordList;
	private Random r = new Random();
	private int difficulty = 12;
	
	//Constructor
	public Words() throws FileNotFoundException {
		int listLength = 0;
		//counts how many lines are in WordList.txt
		Scanner wordCount = new Scanner(new File("WordList.txt"));
		while (wordCount.hasNextLine()) {
			listLength++;
			wordCount.nextLine();
		}
		//adds the words to array
		wordList = new String[listLength];
		Scanner wordFind = new Scanner(new File("WordList.txt"));
		for (int x = 0; x < listLength; x++) {
			wordList[x] = wordFind.nextLine();
		}		
	}
	//Methods
	
	//Generates new word
	public String newWord() {
		do {
			word = wordList[r.nextInt(wordList.length)];
		} while (word.length() < 4 || word.length() > difficulty);
		return word;
	}
	
	//Checks if word is in the wordlist
	public boolean check(String checkedWord) {
		for (int x = 0; x < wordList.length; x++) {
			if (checkedWord.equalsIgnoreCase(wordList[x])) {
				return true;
			}
		}
		return false;
	}
	
	//checks if two char arrays are equal
	public boolean arrayCheck(char arr1[], char arr2[]) {
		if (arr1.length != arr2.length)
			return false;
		
		Arrays.sort(arr1);
		Arrays.sort(arr2);
		
		for(int x = 0; x < arr1.length; x++) {
			if (arr1[x] != arr2[x])
				return false;
		}
		return true;
	}
	
	//Sets difficulty
	public void setDifficulty(String diffSetting) {
		if(diffSetting.equalsIgnoreCase("hard"))
			difficulty = 30;
		if (diffSetting.equalsIgnoreCase("easy"))
			difficulty = 8;
	}
	
}
