import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Wordle {

	public static void main(String[] args) {
		Wordle wordle = new Wordle();
		if (wordle.play(false)) {
			wordle.println("You Win!!");
		} else {
			wordle.println("You Lose!!");
		}
		System.out.println("GoodBye!");
	}

	// Implementation of the game Wordle

	private final String dirname;
	private final Scanner scanner;
	private final Set<String> words;
	private final Set<String> allowable;
	private final int AT_CORRECT_INDEX = 2, AT_WRONG_INDEX = 1, DOES_NOT_EXIST = 0;

	public Wordle() {
		this.dirname = "";// "C:\\Users\\20705251\\OneDrive- Maynooth University\\Desktop\\Wordle\\";
		this.allowable = new HashSet<>(readFileLines(dirname + "allowable.txt"));
		this.words = new HashSet<>(readFileLines(dirname + "wordlewords.txt"));
		this.scanner = new Scanner(System.in);
	}

	public boolean play(boolean visible) {
		int random = new Random().nextInt(words.size());
		String picked = words.toArray(new String[words.size()])[random];
		String guess = null;
		int[] response = null;
		int trials = 0;
		boolean correct = false;
		println("Word picked: " + (visible ? picked : "[ word is hidden ]"));
		do {
			println("Trials left: " + (6 - trials));
			print("Enter a word: ");
			guess = scanner.nextLine();
			if (guess.length() == 5 && words.contains(guess) && allowable.contains(guess)) {
				response = checkUserGuessAccuracy(guess, picked);
				correct = isCorrectGuess(response);
				println(Arrays.toString(response) + "\n");
				trials++;
			} else {
				println("Invalid Word Entered\n");
			}
		} while (!correct && trials != 6);
		scanner.close();
		return correct;
	}

	private boolean isCorrectGuess(int[] response) {
		if (response == null)
			return false;
		for (int i : response) {
			if (i != 2)
				return false;
		}
		return true;
	}

	private int[] checkUserGuessAccuracy(String guess, String word) {
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
			while (sc.hasNextLine()) {
				lines.add(sc.nextLine());
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
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
