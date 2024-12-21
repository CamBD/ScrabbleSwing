package src;

import javax.swing.*;

/**
 * The CustomButton class extends the JButton class to include additional functionality for representing a button's
 * position on a grid (row and column).
 */
public class CustomButton extends JButton {
    private int row;
    private int col;

    /**
     * Default constructor for creating a button without a label.
     */
    public CustomButton() {
        super();
    }

    /**
     * Constructor for creating a button with a specified label.
     *
     * @param s the text to display on the button.
     */
    public CustomButton(String s) {
        super(s);
    }

    /**
     * Gets the row index of this button.
     *
     * @return the row index of the button.
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column index of this button.
     *
     * @return the column index of the button.
     */
    public int getCol() {
        return col;
    }

    /**
     * Sets the column index of this button.
     *
     * @param col the column index to set for the button.
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Sets the row index of this button.
     *
     * @param row the row index to set for the button.
     */
    public void setRow(int row) {
        this.row = row;
    }
}