package src;

import java.io.Serializable;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;

/**
 * Board class represents the Scrabble board, which is a 15x15 grid of tiles.
 * It initializes the board, displays it, and manages tiles at specific positions.
 */
public class Board implements Serializable {

    // Attributes
    private Tile[][] board = new Tile[15][15]; // A 15x15 grid of Tile objects representing the Scrabble board.
    private Map<String, List<Map<String, Integer>>> premiumSquares;
    private static final long serialVersionUID = 1L;
    private String premiumLayout;
    // Constructor

    /**
     * Initializes the board with blank tiles (' ') at all positions.
     * Sets the center position (7,7) with the specified tile.
     *
     * @param tile The Tile to be placed at the center of the board (position 7,7).
     */
    public Board(Tile tile){
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                board[row][col] = new Tile(' '); // Initialize with blank tiles
            }
        }
        board[7][7] = tile; // Place the specified tile at the center
        setPremiumLayout("resources/PremiumLayouts/premiumDefault.xml");

    }

    public String getPremiumLayout(){return premiumLayout;}

    /**
     * Displays the current state of the board in a formatted grid.
     * Each tile's letter is shown within the grid, and row/column numbers are printed for clarity.
     */
    public void displayBoard(){
        System.out.print("   ");
        // Print column numbers
        for (int i = 0; i < 15; i++) {
            if (i < 12) {
                System.out.print("   " + (i+1) + "   ");
            }
            else {
                System.out.print(" " + (i + 1) + "   ");
            }
        }

        System.out.println();

        // Print each row with its tiles and row number
        for (int row = 0; row < 15; row++) {
            System.out.print(row+1);
            if(row<9){
                System.out.print(" ");
            }
            for (int col = 0; col < 15; col++) {
                System.out.print("  |  " + board[row][col].getLetter() + " ");
            }
            System.out.println("  |");

        }
    }

    /**
     * Retrieves the tile at the specified position on the board.
     * If the position is out of bounds, a blank tile (' ') is returned.
     *
     * @param row The row index (0-14).
     * @param col The column index (0-14).
     * @return The Tile at the specified position, or a blank tile if out of bounds.
     */
    public Tile getTile(int row, int col) {
        if (row >= 0 && row < 15 && col >= 0 && col < 15) {
            return board[row][col];
        } else {
            return new Tile(' '); // Out-of-bounds positions are treated as empty
        }
    }

    /**
     * Places a tile at the specified position on the board if the position is valid.
     *
     * @param row The row index (0-14).
     * @param col The column index (0-14).
     * @param tile The Tile to place at the specified position.
     */
    public void setTile(int row, int col, Tile tile) {

        if (row >= 0 && row < 15 && col >= 0 && col < 15) {
            Tile temp = board[row][col];
            board[row][col] = tile;
            board[row][col].setBonus(temp.getBonus());
        }
    }


    public Tile[][] getTiles(){
        return board;
    }


    public void setPremiumLayout(String premiumLayout) {
        this.premiumLayout = premiumLayout;
        applyPremiumBonuses();

    }
    private void readPremiumSquaresFromXML() { // contruct a map of the premium tiles
        try {
            // create builder through factory
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            // parse the currently selected .xml file
            File xmlFile = new File(premiumLayout);
            Document doc = builder.parse(xmlFile);

            // create premiumSquares map
            premiumSquares = new HashMap<>();

            // get the types of premium squares
            NodeList premiumSquareList = doc.getElementsByTagName("premiumSquare");

            for (int i = 0; i < premiumSquareList.getLength(); i++) {
                Node node = premiumSquareList.item(i); // get the premium squares
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String type = element.getAttribute("type"); // type attribute of this premium square specifier

                    //get the coordinates specified under this type
                    NodeList coordinateList = element.getElementsByTagName("coordinate");
                    List<Map<String, Integer>> coordinates = new ArrayList<>();

                    for (int j = 0; j < coordinateList.getLength(); j++) { // go through each element under this PS element
                        Node coordinateNode = coordinateList.item(j); // get coord in this element
                        if (coordinateNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element coordinateElement = (Element) coordinateNode;
                            int row = Integer.parseInt(coordinateElement.getAttribute("row")); // grab row of coord
                            int col = Integer.parseInt(coordinateElement.getAttribute("col")); // grab col of coord

                            Map<String, Integer> coordinate = new HashMap<>(); // map with row col of this coord
                            coordinate.put("row", row);
                            coordinate.put("col", col);
                            coordinates.add(coordinate); // array list of coord maps
                        }
                    }
                    premiumSquares.put(type, coordinates); // add array list of coord maps to type map
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Applies the bonuses to the board tiles based on the premiumSquares data.
     */
    private void applyPremiumBonuses() {
        // read xml into map
        readPremiumSquaresFromXML();

        // clear old bonuses
        for(int i = 0; i < 15; i++){
            for(int j= 0; j < 15; j++){
                board[i][j].setBonus("0");
            }
        }

        // use map of coordinate data to fill in premium squares
        for (Map.Entry<String, List<Map<String, Integer>>> entry : premiumSquares.entrySet()) {
            String type = entry.getKey(); // get premium tile type
            List<Map<String, Integer>> coordinates = entry.getValue(); // map containing array list of coord maps

            // loop
            for (Map<String, Integer> coord : coordinates) { // map of the row col
                int row = coord.get("row");
                int col = coord.get("col");
                // set the bonus on the tile based on the premium square type
                switch (type) {
                    case "tripleWord":
                        board[row][col].setBonus("TW");
                        break;
                    case "doubleWord":
                        board[row][col].setBonus("DW");
                        break;
                    case "tripleLetter":
                        board[row][col].setBonus("TL");
                        break;
                    case "doubleLetter":
                        board[row][col].setBonus("DL");
                        break;
                    default:
                        break;
                }
            }
        }
    }
}