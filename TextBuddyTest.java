import static org.junit.Assert.*;
import java.io.IOException;

import org.junit.Test;

public class TextBuddyTest {
	
	String textFileName = "mytextfile.txt";
	
	@Test
	//This test file tests the Add, Display, Delete, Clear, Sort and Search commands.
	public void testExecuteCommand() throws IOException {
		
		testDisplayCommand("null is empty", "display");
		testAddCommand("added to null: \"Kitty\"", "Kitty");
		testDisplayCommand("Kitty", "display");	
		
		testAddCommand("added to null: \"Cat Island\"", "Cat Island");
		testDisplayCommand("KittyCat Island", "display");
		
		
		//-----------------------------FOR CE2--------------------------------
		//Note: Unable to access mytextfile.txt, so I used null instead.
		//--------------------------------------------------------------------
		
		//With 4 inputs, the Search command is used here to search for "Cat"
		testAddCommand("added to null: \"Cat Maniac\"", "Cat Maniac");
		testAddCommand("added to null: \"Sheep Girl\"", "Sheep Girl");
		testDisplayCommand("KittyCat IslandCat ManiacSheep Girl", "display");
		testSearchCommand("Cat IslandCat Maniac", "cat");
	
		//With 4 inputs, the Sort command is used here to sort the list alphabetically
		testSortCommand("list sorted", "sort");
		testDisplayCommand("Cat IslandCat ManiacKittySheep Girl", "display");
		
		//--------------------------------------------------------------------
		//--------------------------------------------------------------------
		
		
		testDeleteCommand("deleted from null: \"Cat Island\"", 1);
		testDisplayCommand("Cat ManiacKittySheep Girl", "display");
		
		testClearCommand("all content deleted from null", "clear");
		testDisplayCommand("null is empty", "display");
	}
	
	private void testDisplayCommand(String expected, String command) {
		assertEquals(expected, TextBuddy.displayAllTexts()); 
	}
	
	private void testAddCommand(String expected, String toAdd) {
		assertEquals(expected, TextBuddy.executeAddCommand(textFileName, toAdd)); 
	}
	
	private void testDeleteCommand(String expected, int indexToDelete) {
		assertEquals(expected, TextBuddy.executeDeleteCommand(textFileName, indexToDelete)); 
	}
	
	private void testClearCommand(String expected, String command) {
		assertEquals(expected, TextBuddy.executeClearCommand(textFileName)); 
	}
	
	private void testSearchCommand(String expected, String toSearch) {
		assertEquals(expected, TextBuddy.executeSearchCommand(textFileName, toSearch)); 
	}
	
	private void testSortCommand(String expected, String command) {
		assertEquals(expected, TextBuddy.executeSortCommand(textFileName)); 
	}
	
}
