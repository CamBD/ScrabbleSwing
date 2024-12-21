
package src;

/**
 * Represents information about a word that can be played on the Scrabble board.
 * Stores the word itself, the position of the tile on the board, and related metadata.
 */
public class WordInfo {
    private String word;      // The word formed.
    private char boardTile;   // The specific letter on the board contributing to the word.
    private int row;          // The row index where the word starts.
    private int col;          // The column index where the word starts.

    /**
     * Constructs a WordInfo object with details about the word, its position, and its associated board tile.
     *
     * @param word      the word being represented.
     * @param row       the row index of the starting position of the word.
     * @param col       the column index of the starting position of the word.
     * @param boardTile the letter on the board that is part of the word.
     */
    public WordInfo(String word, int row, int col, char boardTile) {
        this.word = word;
        this.row = row;
        this.col = col;
        this.boardTile = boardTile;
    }

    /**
     * Retrieves the specific letter on the board associated with this word.
     *
     * @return the letter on the board.
     */
    public char getBoardTile() {
        return boardTile;
    }

    /**
     * Retrieves the word represented by this WordInfo object.
     *
     * @return the word.
     */
    public String getWord() {
        return word;
    }

    /**
     * Retrieves the row index where the word starts.
     *
     * @return the row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Retrieves the column index where the word starts.
     *
     * @return the column index.
     */
    public int getCol() {
        return col;
    }

    /**
     * Finds the index of the letter in the word that corresponds to the board tile.
     *
     * @return the index of the board tile letter in the word, or -1 if the letter is not found.
     */
    public int getLetterIndex() {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == boardTile) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Retrieves the last letter of the word.
     *
     * @return the last letter of the word.
     * @throws IllegalArgumentException if the word is null or empty.
     */
    public char getLastLetter() {
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("Word cannot be null or empty");
        }
        return word.charAt(word.length() - 1);
    }
}
