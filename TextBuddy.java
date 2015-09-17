import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class TextBuddy {

	private static Scanner sc = new Scanner(System.in);
	private static ArrayList<String> textArray = new ArrayList<String>();
	public static String textFileName;
	private static String command = "";
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy.";
	private static final String MESSAGE_FILE_READY = " is ready for use";
	private static final String MESSAGE_COMMAND_ERROR = "such command "
														+ "does not exist";
	private static final String MESSAGE_CLEAR = "all content deleted from ";
	private static final String MESSAGE_SORTED = "list sorted";
	private static final String MESSAGE_SORTERROR = "there is nothing in the list to sort";
	private static final String MESSAGE_SEARCHNOTFOUND = "was not found in ";

	public static void main(String[] args) {
		initializeFileName(args);
		printWelcomeText();
		readTextFile(textFileName);
		receiveCommands();
	}

	private static void receiveCommands() {
		while (commandIsNotExit()) { // user does not exit
			printPromptForCommand();
			command = receiveCommand();
			makeCommandNotCaseSensitive();

			if (commandIsAdd()) {
				String newText = receiveTextToAdd();
				executeAddCommand(textFileName, newText);
			} else if (commandIsDisplay()) {
				displayAllTexts();
			} else if (commandIsDelete()) {
				int indexToDelete = sc.nextInt();
				executeDeleteCommand(textFileName, indexToDelete);
			} else if (commandIsClear()) {
				executeClearCommand(textFileName);
			} else if (commandIsSort()) {
				executeSortCommand(textFileName);
			} else if (commandIsSearch()) {
				String whatToSearch = receiveTextToSearch();
				executeSearchCommand(textFileName, whatToSearch);
			} else {
				if (commandIsNotExit()) {
					printCommandErrorMessage();
				}
			}
		}
		System.exit(0);
	}

	//----------------------------------------------------------------------
	// The following methods execute the commands accordingly
	// Display, Add, Delete, Clear, Sort, Search
	//----------------------------------------------------------------------

	static String displayAllTexts() {
		String textDisplayed = "";
		if (hasNoText()) {
			return printEmptyMessage();
		} else {
			for (int i = 1; i <= textArray.size(); i++) {
				printLabelledTextLines(i);
				textDisplayed = textDisplayed + textArray.get(i-1);
			}
		}
		return textDisplayed;
	}

	static String executeAddCommand(String textFileName, String newText) {
		saveTextToArray(newText);
		saveToTextFile(textFileName);
		return printAddTextComplete(newText); 
	}

	static String executeDeleteCommand(String textFileName, int indexToDelete) {
		String deletedLine = deleteSelectedLine(indexToDelete);
		saveToTextFile(textFileName);
		return printDeleteTextComplete(deletedLine);
	}

	static String executeClearCommand(String textFileName) {
		clearAllText();
		saveToTextFile(textFileName);
		return printClearAllTextComplete();
	}

	static String executeSortCommand(String textFileName) {
		if (hasNoText()) {
			return printSortErrorMessage();
		} else {
			sortArray();
			return printSortComplete();
		}
	}

	static String executeSearchCommand(String textFileName, String whatToSearch) {
		ArrayList<String> tempArray = new ArrayList<String>();
		
		for (int i = 0; i < textArray.size(); i++) {
			String searchWord = makeTextNotCaseSensitive(whatToSearch);
			String textToSearch = makeTextNotCaseSensitive(textArray.get(i));
			if (textToSearch.contains(searchWord)) {
				addToListContainingLinesOfSearchWord(tempArray, i);
			}
		}
		return printSearchOutcome(whatToSearch, tempArray);
	}

	//----------------------------------------------------------------------
	// The following methods are linked to accessing the text file.
	// They initialize the text file, BufferedReader and BufferedWriter
	// This enables reading and writing to the text file 
	//----------------------------------------------------------------------

	private static void createFile(File file) throws IOException {
		if (!file.exists()) {
			file.createNewFile();
		}
	}

	private static File initializeFile(String textFileName) {
		File file = new File(textFileName);
		return file;
	}
	
	private static void makeNewFile(File file) throws IOException {
		file.delete();
		file.createNewFile();
	}

	private static void initializeFileName(String[] args) {
		textFileName = args[0];
	}

	private static BufferedReader initializeBufferedReader(String textFileName) 
			throws FileNotFoundException {
		FileReader fR = new FileReader(textFileName);
		BufferedReader bR = new BufferedReader(fR);
		return bR;
	}

	private static BufferedWriter initializeBufferedWriter(File file) 
	throws IOException {
		FileWriter fW = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bW = new BufferedWriter(fW);
		return bW;
	}
	
	private static void closeBufferedWriter(BufferedWriter bW) throws IOException {
		bW.close();
	}

	private static void readTextFile(String textFileName) { // throws
															// IOException {
		try {
			BufferedReader bR = initializeBufferedReader(textFileName);
			insertTextLineByLine(bR);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void saveToTextFile(String textFileName) { //throws IOException {
		try {
			File file = initializeFile(textFileName);
			// if file doesn't exists, then create it
			createFile(file);
			BufferedWriter bW = initializeBufferedWriter(file);

			if (textArray.size() == 0) {
				makeNewFile(file);
			} else {
				for (int i = 0; i < textArray.size(); i++) {
					saveTextToFileLineByLine(bW, i);
				}
				closeBufferedWriter(bW);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//----------------------------------------------------------------------
	// The following methods are the methods within the execution methods
	// Arranged in terms of alphabetical order
	//----------------------------------------------------------------------
	
	static boolean addToListContainingLinesOfSearchWord(ArrayList<String> tempArray, int i) {
		return tempArray.add(textArray.get(i));
	}

	private static void clearAllText() {
		textArray.clear();
	}
	
	private static boolean commandIsAdd() {
		return command.equals("add");
	}
	
	private static boolean commandIsClear() {
		return command.equals("clear");
	}

	private static boolean commandIsDelete() {
		return command.equals("delete");
	}

	private static boolean commandIsDisplay() {
		return command.equals("display");
	}

	private static boolean commandIsNotExit() {
		return !command.equals("exit");
	}

	private static boolean commandIsSearch() {
		return command.equals("search");
	}

	static boolean commandIsSort() {
		return command.equals("sort");
	}
	
	private static String deleteSelectedLine(int indexToDelete) {
		String deletedLine = textArray.remove(indexToDelete - 1);
		return deletedLine;
	}

	private static boolean hasNoText() {
		return textArray.isEmpty();
	}

	private static void insertTextLineByLine(BufferedReader bR) 
			throws IOException {
		String currentLine;
		while ((currentLine = bR.readLine()) != null) {
			saveTextToArray(currentLine);
		}
	}

	private static void makeCommandNotCaseSensitive() {
		command = command.toLowerCase();
	}

	static String makeTextNotCaseSensitive(String text) {
		String notCaseSensitiveText = text;
		notCaseSensitiveText = notCaseSensitiveText.toLowerCase();
		return notCaseSensitiveText;
	}

	private static String receiveCommand() {
		command = sc.next();
		return command;
	}

	private static String receiveTextToAdd() {
		String newText = sc.nextLine();
		newText = newText.trim();
		return newText;
	}
	
	private static String receiveTextToSearch() {
		String searchText = sc.nextLine();
		searchText = searchText.trim();
		return searchText;
	}

	static void saveTextToArray(String newText) {
		textArray.add(newText);
	}
	
	private static void saveTextToFileLineByLine(BufferedWriter bW, int i) 
	throws IOException {
		bW.write(textArray.get(i));
		bW.newLine();
	}

	private static void sortArray() {
		Collections.sort(textArray);
	}
	
	//----------------------------------------------------------------------
	// The following methods are the possible print outputs
	// Arranged in terms of alphabetical order
	//----------------------------------------------------------------------
	
	private static String printAddTextComplete(String newText) {
		String addMessage = "added to " + textFileName + ": \"" 
				+ newText + "\"";
		System.out.println(addMessage);
		return addMessage;
	}

	private static String printClearAllTextComplete() {
		System.out.println(MESSAGE_CLEAR + textFileName);
		return MESSAGE_CLEAR + textFileName;
	}

	private static void printCommandErrorMessage() {
		System.out.println(MESSAGE_COMMAND_ERROR);
	}

	private static String printDeleteTextComplete(String deletedLine) {
		String deleteMessage = "deleted from " + textFileName + ": \"" 
				   + deletedLine + "\"";
		System.out.println(deleteMessage);
		return deleteMessage;
	}
	
	private static String printEmptyMessage() {
		String emptyMessage = textFileName + " is empty";
		System.out.println(emptyMessage);
		return emptyMessage;
	}

	private static void printLabelledTextLines(int i) {
		System.out.println(i + ". " + textArray.get(i - 1));
	}
	
	private static void printLabelledTextLinesForSearch(int i, ArrayList<String> tempArray) {
		System.out.println(i + ". " + tempArray.get(i - 1));
	}

	private static String printListOfMatches(ArrayList<String> tempArray) {
		String textDisplayedForSearch = "";
		for (int i = 1; i <= tempArray.size(); i++) {
			printLabelledTextLinesForSearch(i, tempArray);
			textDisplayedForSearch = textDisplayedForSearch 
									+ tempArray.get(i-1);
		}
		return textDisplayedForSearch;
	}
	private static String printNoMatchForSearch(String whatToSearch) {
		String searchNotFoundMessage = "\"" + whatToSearch +"\" "  
							+ MESSAGE_SEARCHNOTFOUND + textFileName;
		System.out.println(searchNotFoundMessage);
		return searchNotFoundMessage;
	}

	private static void printPromptForCommand() {
		System.out.print("command: ");
	}

	static String printSearchOutcome(String whatToSearch, ArrayList<String> tempArray) {
		if (tempArray.isEmpty()) {
			return printNoMatchForSearch(whatToSearch);
		} else {
			return printListOfMatches(tempArray);
		}
	}
	private static String printSortComplete() {
		System.out.println(MESSAGE_SORTED);
		return MESSAGE_SORTED;
	}

	private static String printSortErrorMessage() {
		System.out.println(MESSAGE_SORTERROR);
		return MESSAGE_SORTERROR;
	}

	private static void printWelcomeText() {
		System.out.println(MESSAGE_WELCOME);
		System.out.println(textFileName + MESSAGE_FILE_READY);
	}
}
