package src;

import java.util.*;
import java.io.*;


/**
 * Word class manages a set of valid words for the game.
 * It reads a list of words from file ("WordBank.txt")
 * and provides functionality to check if a given word is valid.
 */
public class Word implements Serializable{

    // Attributes
    Set<String> wordBank; // A set containing all valid words, stored in lowercase for easy lookup.
    private static final long serialVersionUID = 1L;
    // Constructor

    /**
     * Initializes the wordBank as a HashSet and populates it with words
     * by calling the createWordBank() method.
     */
    public Word() {
        wordBank = new HashSet<>();
        createWordBank();
    }

    // Methods

    /**
     * Reads words from a "WordBank.txt" and adds them to the wordBank set.
     * Each word is converted to lowercase and trimmed.
     *
     * If the file cannot be read, the IOException is caught and printed.
     */
    public void createWordBank(){
        try (BufferedReader reader = new BufferedReader(new FileReader("src/WordBank.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordBank.add(line.toLowerCase().trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a given word exists in wordBank.
     *
     * @param word The word to check.
     * @return true if the word exists in the wordBank, false otherwise.
     */
    public  boolean isWord(String word){
        return wordBank.contains(word);
    }
}
