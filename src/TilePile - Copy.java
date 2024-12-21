package src;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * TilePile class represents a collection of tiles in Scrabble.
 * It manages adding, removing, and retrieving tiles from the pile.
 */
public class TilePile implements Serializable {

    // Attributes
    ArrayList<Tile> tiles; // A list to store the Tile objects in the pile.
    private static final long serialVersionUID = 1L;
    // Constructor

    /**
     * Initializes an empty list to hold Tile objects.
     */
    TilePile(){
        this.tiles = new ArrayList<>();
    }

    /**
     * Retrieves the current list of tiles in the pile.
     *
     * @return An ArrayList of Tile objects representing the current pile.
     */
    public ArrayList<Tile> getPile(){
        return tiles;
    }

    /**
     * Adds a specific letter tile to the pile multiple times.
     * Each tile will have the same letter, and the number of tiles added is
     * determined by the count parameter.
     *
     * @param letter The letter to be added to the pile.
     * @param count The number of times the letter tile should be added.
     */
    public void addTile(char letter, int count){
        for (int i = 0; i < count; i++) {
            this.tiles.add(new Tile(letter));
        }
    }

    /**
     * Removes and returns the first tile from the pile.
     * If the pile is empty, it returns null.
     *
     * @return The first Tile from the pile, or null if the pile is empty.
     */
    public Tile deleteTile() {
        if (!tiles.isEmpty()){
            return tiles.remove(0);
        }
        return null;
    }
}
