package lab2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class WordleHelper {

	public static void main(String[] args) {
		if (new WordleHelper().play(false)) { 
			System.out.println("You Win!!");
		} else {
			System.out.println("You Lose!!");
		}
		System.out.println("GoodBye!");
	}

	
	// Implementation of the game Wordle
	
	private Set<String> words;
	private Set<String> allowable;
	private final String dirname;
	private final Scanner scanner;
	private final int AT_CORRECT_INDEX = 2, AT_WRONG_INDEX = 1, DOES_NOT_EXIST = 0;
	
	public WordleHelper() {
		this.dirname = "/Users/syed/eclipse-workspace/CS211/src/lab1/";
		this.allowable = new HashSet<>(readFileLines(dirname + "allowable.txt"));
		this.words = new HashSet<>(readFileLines(dirname + "wordlewords.txt"));
		this.scanner = new Scanner(System.in);
	}
	
	public boolean play(boolean visible) {
		int random = new Random().nextInt(words.size()), trials = 0;
		String picked = words.toArray(new String[words.size()])[random], guess = null;
		int[] feedback = null; 
		boolean correct = false;		
		println("Word picked: " + (visible ? picked : "[ word is hidden ]"));
		do {
			println("Trials left: " + (6 - trials));			
			print("Enter a word: ");			
			guess = scanner.nextLine();
			if (guess.length() == 5 && words.contains(guess) && allowable.contains(guess)) {
				feedback = getFeedBack(guess, picked);
				if (!(correct = isCorrectGuess(feedback))) {
					allowable = filter(feedback, guess, picked);
					println(Arrays.toString(feedback));
					println("Try: " + allowable.toString());
				}
				trials++;
			} else {
				println("Invalid Word Entered");
			}
			println("");
		} while (!(correct || trials == 6));
		scanner.close();
		return correct;
	}
	
	public Set<String> filter(int[] feedback, String guess, String picked) {
		List<String> temp = new ArrayList<>();
		for (String s : allowable) {
			boolean match = true;
			for (int i = 0; i < s.length() && match; i++) {
				match = !(feedback[i] == 2 && picked.charAt(i) != s.charAt(i) ||
					feedback[i] == 1 && !s.contains(String.valueOf(guess.charAt(i))) ||
					feedback[i] == 0 && s.contains(String.valueOf(guess.charAt(i))));
			}
			if (match) { temp.add(s); }
		}
		temp.removeIf(e -> !words.contains(e));
		return new HashSet<>(temp);
	}
	
	private boolean isCorrectGuess(int[] response) {
		if (response == null) return false;
		for (int i : response) { if (i != 2) return false; }
		return true;
	} 
	
	private int[] getFeedBack(String guess, String word) {
		int[] response = { -1, -1, -1, -1, -1 };
		char[] chars = guess.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (guess.charAt(i) == word.charAt(i)) {
				response[i] = AT_CORRECT_INDEX;
				continue;
			} else if (word.indexOf(String.valueOf(chars[i])) > -1) {
				response[i] = AT_WRONG_INDEX;
				continue;
			} else {
				response[i] = DOES_NOT_EXIST;
			}
		}
		return response;
	}

	private List<String> readFileLines(String filename) {
		final List<String> lines = new ArrayList<>();
		try {
			Scanner sc = new Scanner(new File(filename));
			while (sc.hasNextLine()) { lines.add(sc.nextLine()); }
			sc.close();
		} catch (FileNotFoundException e) {
			println(e.getMessage());
			System.exit(0x2f); // non zero termination
		}
		return lines;
	}

	private void println(Object o) {
		System.out.println(String.valueOf(o));
	}
	
	private void print(Object o) {
		System.out.print(String.valueOf(o));
	}
	
}
