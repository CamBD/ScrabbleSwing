package src;

import java.io.Serializable;
import java.util.*;

/**
 * Player class represents a player in Scrabble.
 * It manages the player's hand of tiles, points, and actions related to gameplay,
 * such as adding and removing tiles, checking for valid moves, and displaying the hand.
 */
public class Player implements Serializable {

    // Attributes
    final private ArrayList<Tile> hand; // A list of tiles in the player's hand.
    private int points;
    final private String name;
    private static final long serialVersionUID = 1L;
    // Constructor

    /**
     * Initializes the player's hand, points, and assigns a name based on the player number.
     *
     * @param number The player number used to assign a name (e.g., Player1, Player2).
     */
    public Player(int number){
        this.hand = new ArrayList<>();
        this.points = 0;
        this.name  = ("Player"+(number+1));
    }

    // Methods

    /**
     * Adds a tile to the player's hand.
     *
     * @param tile The tile to be added to the player's hand.
     */
    public void addTile(Tile tile){
        this.hand.add(tile);
    }

    /**
     * Checks if the player has the specified tile in their hand.
     *
     * @param tile The tile to check for.
     * @return true if the tile exists in the player's hand, false otherwise.
     */
    public boolean hasTile(Tile tile) {
        // Check if the player has the specified tile in their hand
        for (Tile handTile : hand) {
            if (handTile.getLetter() == tile.getLetter()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the specified tile from the player's hand and returns it.
     *
     * @param tile The tile to be removed.
     * @return The removed tile if it exists in the hand, null otherwise.
     */
    public Tile removeTile(Tile tile) {
        // Remove and return the specified tile if it exists in the player's hand
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getLetter() == tile.getLetter()) {
                return hand.remove(i);
            }
        }
        return null;
    }

    /**
     * Checks if the player has all the required tiles to form the given word on the board.
     *
     * @param word The word to be placed.
     * @param board The game board represented as a 2D array of tiles.
     * @param row The starting row position for the word.
     * @param col The starting column position for the word.
     * @param isVertical Whether the word is placed vertically (true) or horizontally (false).
     * @return true if the player has all required tiles to form the word, false otherwise.
     */
    public boolean hasAllTiles(String word, Tile[][] board, int row, int col, boolean isVertical) {
        // Check if the player has all the required tiles to form the word
        ArrayList<Character> requiredTiles = new ArrayList<>();

        for (int i = 0; i < word.length(); i++) {
            char boardLetter = isVertical ? board[row + i][col].getLetter() : board[row][col + i].getLetter();
            char wordLetter = word.charAt(i);

            if (boardLetter == ' ') {
                // If the board space is empty, the player must have the corresponding tile
                requiredTiles.add((Character) wordLetter);
            } else if (boardLetter != wordLetter) {
                // If there's a mismatch between the word and the board, the word is invalid
                return false;
            }
        }

        // Check if the player has all the required tiles in their hand
        for (char tileLetter : requiredTiles) {
            Tile tile = new Tile(tileLetter); // Create a Tile object to check
            if (!hasTile(tile)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Gets the player's name.
     *
     * @return The name of the player.
     */
    public String getName(){
        return this.name;
    }

    /**
     * Retrieves the list of tiles in the player's hand.
     *
     * @return An ArrayList of tiles representing the player's hand.
     */
    public ArrayList<Tile> getHand() {
        return hand;
    }

    /**
     * Displays the player's hand by printing the tiles to the console.
     */
    public void displayHand(){
        System.out.print(name + " has tiles: ");
        for (Tile tile : hand) {
            System.out.print(tile.getLetter() + " ");
        }
        System.out.println();
    }

    /**
     * Displays the player's hand by printing the tiles to the console.
     */
    public void clearHand(){
        hand.clear();
    }

    /**
     * Adds points to the player's total score.
     *
     * @param add The number of points to add to the player's score.
     */
    public void addPoints(int add) {
        this.points += add;
    }

    /**
     *
     * @return this player's point count
     */
    public int getPoints() {
        return points;
    }

    /**
     *
     * @return this player's point count
     */
    public void setPoints(int set) {
        this.points = set;
    }
}
