package src;

import java.awt.*;
import javax.swing.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Stack;

class View implements Serializable {
    private Stack<Game> undo;
    private Stack<Game> redo;
    private JMenu options;
    private JMenu premiumLayout;
    private JMenu timerCount;
    private JMenuItem scores, p1, p2, p3, p4;
    private List<Point> tilesPlacedThisTurn = new ArrayList<>();
    private JPanel directionPanel;
    private int targetRow;
    private int targetCol;
    private boolean first_letter = true; // is the current player placing their first tile

    private JMenuItem exit;
    private JMenuItem reset;
    private boolean isVertical;

    private static final long serialVersionUID = 1L;

    private char direction = 'H';
    private CustomButton[][] buttons;

    public CustomButton[][] getButtons() {
        return buttons;
    }

    private JPanel skipPannel;
    private JButton skip;
    private JButton submit;
    private JButton ai_turn;
    private JPanel scoreboardPanel;
    private JLabel[] playerScores;

    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;

    private JMenuItem saveMenuItem;
    private JMenuItem loadMenuItem;
    private JMenuItem defaultLayout;
    private JMenuItem chaosLayout;
    private JMenuItem ringLayout;

    private JMenuItem timerON;
    private JMenuItem timerOFF;

    public JPanel getHandPanel() {
        return handPanel;
    }

    public String getInputWord() {
        return inputWord;
    }

    public Tile getSelectedTile() {
        return selectedTile;
    }

    private CustomButton[] handButtons;
    private JPanel handPanel;
    private Game model;
    private Word check;
    private JFrame frame;



    int clickedRow; // Since 'row' is accessible in this scope
    int clickedCol;

    String inputWord = "";

    private boolean beforeStart = true;

    private Board board;
    private Tile selectedTile; // To store the selected tile from the hand

    private JButton verticalButton;
    private JButton horizontalButton;
    private CustomButton tileButton;

    public int getTargetRow() {
        return targetRow;
    }

    public int getTargetCol() {
        return targetCol;
    }

    public boolean getFirstLetter() {
        return first_letter;
    }

    public void setFirstLetter(boolean bool) {
        first_letter = bool;
    }

    public void setTargetRow(int row) {
        targetRow = row;
    }

    public void setTargetCol(int col) {
        targetCol = col;
    }

    public Game getModel(){
        return model;
    }
    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public int getClickedRow() {
        return clickedRow;
    }

    public int getClickedCol() {
        return clickedCol;
    }
    public void setClickedRow(int row){
        clickedRow = row;
    }
    public void setClickedCol(int col){
        clickedCol = col;
    }
    public JButton getVerticalButton() {
        return verticalButton;
    }

    public JButton getAi_turn(){return ai_turn;}
    public JButton getHorizontalButton() {
        return horizontalButton;
    }
    public void setVertical(boolean vertical) {
        isVertical = vertical;
    }
    public boolean getVertical(){
        return isVertical;
    }
    public boolean getBeforeStart(){
        return beforeStart;
    }
    public void setBeforeStart(boolean input){
        beforeStart = input;
    }

    public void addInputWord(char letter){
        inputWord = inputWord + letter;
    }
    public void setSelectedTile(Tile tile){
        selectedTile = tile;
    }
    public JButton getSkip(){
        return skip;
    }
    public JButton getSubmit(){
        return submit;
    }
    public JFrame getFrame(){
        return frame;
    }
    public JMenuItem getExitMenu(){ return exit; }
    public JMenuItem getResetMenu(){
        return reset;
    }



    public void setInputWord(String inputWord) {
        this.inputWord = inputWord;
    }


    /**
     * Constructor for the View class.
     * Initializes the game model, sets up the UI components, and displays the main game window.
     *
     * @param model The Game object representing the game model.
     */


    public View(Game model) {
        this.model = model;
        this.check = new Word();
        this.undo = new Stack<>();
        this.redo = new Stack<>();
        model.initializeTiles();
        model.initializePlayer();
        UIManager.put("Button.disabledText", Color.DARK_GRAY);


        JMenuBar menu = new JMenuBar();
        options = new JMenu("Options");
        undoMenuItem = new JMenuItem("Undo");
        redoMenuItem = new JMenuItem("Redo");
        options.add(undoMenuItem);
        options.add(redoMenuItem);
        menu.add(options);

        saveMenuItem = new JMenuItem("Save Game");
        loadMenuItem = new JMenuItem("Load Game");
        options.add(saveMenuItem);
        options.add(loadMenuItem);

        exit = new JMenuItem("Exit");
        reset = new JMenuItem("Reset");
        options.addSeparator();
        options.add(reset);
        options.add(exit);


        premiumLayout = new JMenu("Premium Layout");
        defaultLayout = new JMenuItem("Default Scrabble Layout");
        chaosLayout = new JMenuItem("Chaos Layout");
        ringLayout = new JMenuItem("Ring Scrabble Layout");
        premiumLayout.add(defaultLayout);
        premiumLayout.add(chaosLayout);
        premiumLayout.add(ringLayout);
        menu.add(premiumLayout);


        //<bonus>
        timerCount = new JMenu("Timed Mode");
        timerCount.setOpaque(true); // Make background visible
        timerON = new JMenuItem("30s Timer");
        timerOFF = new JMenuItem("Turn off timer");
        timerCount.add(timerON);
        menu.add(timerCount);
        //</bonus>


        scores = new JMenu("Scores:     ");
        scores.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        p1 = new JMenuItem("Player 1: " + model.getPlayers()[0].getPoints());
        p1.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        p2 = new JMenuItem("Player 2: " + model.getPlayers()[1].getPoints());
        p2.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        p3 = new JMenuItem("Player 3: " + model.getPlayers()[2].getPoints());
        p3.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        p4 = new JMenuItem("Player 4: " + model.getPlayers()[3].getPoints());
        p4.setFont(new Font("Times New Roman", Font.PLAIN, 24));

        menu.add(new JMenu("                   ")); menu.add(scores);menu.add(p1); menu.add(p2); menu.add(p3); menu.add(p4);


        defaultLayout.setEnabled(false);
        // Initialize hand panel
        handPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        handPanel.setPreferredSize(new Dimension(560, 100));
        handButtons = new CustomButton[7];
        for (int i = 0; i < 7; i++) {
            CustomButton tileButton = new CustomButton(String.valueOf(model.getCurrentPlayer().getHand().get(i).getLetter()));
            tileButton.setPreferredSize(new Dimension(100, 80));
            handButtons[i] = tileButton;
            handPanel.add(handButtons[i]);
        }

        updateHandPanel();


        // initialize ai_turn button;
        ai_turn = new JButton("Play AI Turn!");
        ai_turn.setFont(new Font("Dialog", Font.PLAIN, 24));
        // Initialize the direction buttons
        directionPanel = new JPanel(new GridLayout(2, 1));
        verticalButton = new JButton("Vertical");
        verticalButton.setFont(new Font("Dialog", Font.PLAIN, 24));
        horizontalButton = new JButton("Horizontal");
        horizontalButton.setFont(new Font("Dialog", Font.PLAIN, 24));
        directionPanel.add(verticalButton);
        directionPanel.add(horizontalButton);


        buttons = new CustomButton[15][15];
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                buttons[row][col] = new CustomButton();
                buttons[row][col].setPreferredSize(new Dimension(75, 45));
                buttons[row][col].setEnabled(false);
                buttons[row][col].setRow(row);
                buttons[row][col].setCol(col);
                clickedRow = buttons[row][col].getRow();
                clickedRow = row; // Since 'row' is accessible in this scope
                clickedCol = col; // Since 'col' is accessible in this scope
            }
        }

        frame = new JFrame("Scrabble");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocation(200, 0);

        submit = new JButton("Submit");

        skipPannel = new JPanel(new GridLayout(2, 1));

        skip = new JButton("Skip");
        skip.setFont(new Font("Dialog", Font.PLAIN, 24));
        skipPannel.add(skip);
        skipPannel.add(ai_turn);
        JPanel container = new JPanel(new GridLayout(15, 15, 0, 0));
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                container.add(buttons[row][col]);
            }
        }

        frame.add(handPanel, BorderLayout.SOUTH);
        frame.add(directionPanel, BorderLayout.WEST);
        frame.add(container, BorderLayout.NORTH);
        frame.add(submit);
        submit.setPreferredSize(new Dimension(450,120));
        submit.setFont(new Font("Dialog", Font.PLAIN, 48));
        frame.add(skipPannel,BorderLayout.EAST);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setJMenuBar(menu);
        updateView();
    }

    public CustomButton getTileButton() {
        return tileButton;
    }

    /**
     * Updates the hand panel with the current player's tiles.
     */
    public void updateHandPanel() {
        // Add buttons for each tile in the current player's hand
        for (int i = 0; i < 7; i++) {
            try {
                handButtons[i].setText(Character.toString(model.getCurrentPlayer().getHand().get(i).getLetter()));
                handButtons[i].setFont(new Font("Times New Roman", Font.BOLD, 40));
                handButtons[i].setEnabled(handButtons[i].isEnabled());
            } catch (IndexOutOfBoundsException e) {
                handButtons[i].setText(" ");
                handButtons[i].setEnabled(false);
            }
        }
        handPanel.revalidate();
        handPanel.repaint();
    }

    public void refreshHandPanel(Boolean bool){
        for (int i = 0; i < 7; i++) {
            handButtons[i].setEnabled(bool);
        }
    }


    /**
     * Enables tiles for the next placement based on the selected direction.
     */
    public void updateEnabledTiles() {
        if (isVertical) {
            if (clickedRow + 1 < 15) {

                if(!buttons[clickedRow + 1][clickedCol].getText().isEmpty()){
                    buttons[clickedRow + 2][clickedCol].setEnabled(true);
                    inputWord = inputWord + buttons[clickedRow+1][clickedCol].getText();
                }
                else {buttons[clickedRow + 1][clickedCol].setEnabled(true);} // Enable tile below for vertical
            }
        } else { // Horizontal
            if (clickedCol + 1 < 15) {
                if (!buttons[clickedRow][clickedCol + 1].getText().isEmpty()) {
                    buttons[clickedRow][clickedCol + 2].setEnabled(true);
                    inputWord = inputWord + buttons[clickedRow][clickedCol + 1].getText();
                } else {
                    buttons[clickedRow][clickedCol + 1].setEnabled(true); // Enable tile to the right for horizontal}
                }
            }

        }
    }

    /**
     * Sets the board to its true model state
     */
    public void updateView() {
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                Tile tile = model.getBoard().getTile(row, col);

                if (tile != null) {
                    // Set the tile's bonus icon
                    String bonus = tile.getBonus();
                    if (!bonus.equals("0")) {
                        setBonusIcon(row, col, bonus);
                    } else {
                        buttons[row][col].setIcon(null);  // No icon for "0"
                        buttons[row][col].setBackground(new Button("").getBackground());
                    }

                    // Set the tile's letter
                    if (tile.getLetter() != ' ') {
                        buttons[row][col].setText(String.valueOf(tile.getLetter()));
                    } else {
                        buttons[row][col].setText("");
                    }
                    buttons[row][col].setFont(new Font("Times New Roman", Font.BOLD, 40));
                    buttons[row][col].setForeground(Color.BLACK);
                    // set the disabled text color to black

                }
            }
        }
        setBonusIcon(7,7,"START");
        updatePlayerScoreboard();
        frame.revalidate();
        frame.repaint();
    }

    private void updatePlayerScoreboard() {
        p1.setText("Player 1: " + model.getPlayers()[0].getPoints());
        p2.setText("Player 2: " + model.getPlayers()[1].getPoints());
        p3.setText("Player 3: " + model.getPlayers()[2].getPoints());
        p4.setText("Player 4: " + model.getPlayers()[3].getPoints());
        switch (model.getPlayerIndex()) {
            case 0:
                p4.setBackground(null);
                p1.setBackground(Color.YELLOW);
                break;
            case 1:
                p1.setBackground(null);
                p2.setBackground(Color.YELLOW);
                break;
            case 2:
                p2.setBackground(null);
                p3.setBackground(Color.YELLOW);
                break;
            case 3:
                p3.setBackground(null);
                p4.setBackground(Color.YELLOW);
                break;
        }
    }
    /**
     * Sets the bonus icon for a tile on the board.
     *
     * @param row   the row of the tile
     * @param col   the column of the tile
     * @param bonus the bonus type of the tile
     */
    private void setBonusIcon(int row, int col, String bonus) {
        String imagePath = "";

        switch (bonus) {
            case "TW":
                imagePath = "resources/PremiumTilePNG/TripleWord.png";
                break;
            case "TL":
                imagePath = "resources/PremiumTilePNG/TripleLetter.png";
                break;
            case "DW":
                imagePath = "resources/PremiumTilePNG/DoubleWord.png";
                break;
            case "DL":
                imagePath = "resources/PremiumTilePNG/DoubleLetter.png";
                break;
            case "START":
                imagePath = "resources/PremiumTilePNG/Star.png";
                break;
        }

        if (!imagePath.isEmpty()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(75, 45, Image.SCALE_SMOOTH); // Scale to fit the button
            ImageIcon scaledIcon = new ImageIcon(scaledImg);

            // Set icon and text alignment
            buttons[row][col].setIcon(scaledIcon);
            buttons[row][col].setDisabledIcon(scaledIcon);

            // Set button text and icon visibility (text will be in front of the icon)
            buttons[row][col].setText(buttons[row][col].getText());  // Refresh text display
            buttons[row][col].setFont(new Font("Times New Roman", Font.BOLD, 40));
            buttons[row][col].setForeground(Color.BLACK);
            buttons[row][col].setHorizontalTextPosition(SwingConstants.CENTER);  // Center text horizontally
            buttons[row][col].setVerticalTextPosition(SwingConstants.CENTER);    // Center text vertically
        }
    }


    /**
     * Enables all buttons on the board for tile placement.
     */
    public void enableButtons() {
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                Tile tile = model.getBoard().getTile(row, col);
                if (!(tile != null && tile.getLetter() != ' ')){
                    buttons[row][col].setEnabled(true);
                }
            }
        }
    }

    /**
     * Disables all buttons on the board after a tile is placed.
     */
    public void disableButtons(){
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                buttons[row][col].setEnabled(false);
            }
        }

    }

    /**
     * Initializes the scoreboard panel to display the scores of all players.
     *
     * @param players an array of Player objects representing all players in game
     */
    public void initializeScoreboard(Player[] players) {
        scoreboardPanel = new JPanel(new GridLayout(players.length, 1));
        playerScores = new JLabel[players.length];

        for (int i = 0; i < players.length; i++) {
            playerScores[i] = new JLabel(players[i].getName() + ": " + players[i].getPoints() + " points");
            scoreboardPanel.add(playerScores[i]);
        }

        frame.add(scoreboardPanel, BorderLayout.SOUTH);
        frame.revalidate();
    }


    /**
     * Updates the scoreboard panel to reflect the current scores of all players.
     *
     * @param players an array of Player objects representing all players in game
     */
    public void updateScoreboard(Player[] players) {
        for (int i = 0; i < players.length; i++) {
            playerScores[i].setText(players[i].getName() + ": " + players[i].getPoints() + " points");
        }
        scoreboardPanel.revalidate();
        scoreboardPanel.repaint();
    }


    public void addTilePlacedThisTurn(int row, int col) {
        tilesPlacedThisTurn.add(new Point(row, col));
    }

    public void clearTilesPlacedThisTurn() {
        tilesPlacedThisTurn.clear();
    }



    public List<Point> getTilesPlacedThisTurn() {
        return tilesPlacedThisTurn;
    }



    public CustomButton[] getHandButtons(){
        return handButtons;
    }

    public Stack<Game> getUndo() {
        return undo;
    }

    public Stack<Game> getRedo() {
        return redo;
    }
    public void addState(Game currentState) {
        undo.push(currentState);
        redo.clear();
    }
    public Game undo(Game currentState) {
        if (!undo.isEmpty()) {
            redo.push(currentState);
            return undo.pop();
        } else {
            System.out.println("No undo available!");
            return currentState;
        }
    }
    public Game redo(Game currentState) {
        if (!redo.isEmpty()) {
            undo.push(currentState);
            return redo.pop();
        } else {
            System.out.println("No redo available!");
            return currentState;
        }
    }

    public JMenuItem getUndoMenuItem() {
        return undoMenuItem;
    }
    public JMenuItem getRedoMenuItem() {
        return redoMenuItem;
    }

    public JMenuItem getSaveMenuItem() {
        return saveMenuItem;
    }

    public JMenuItem getLoadMenuItem() {
        return loadMenuItem;
    }

    public JMenuItem getDefaultLayout(){ return defaultLayout;}
    public JMenuItem getChaosLayout(){ return chaosLayout;}
    public JMenuItem getRingLayout(){ return ringLayout;}

    public JMenuItem getTimerON(){ return timerON;}
    public JMenuItem getTimerOFF(){ return timerOFF;}

    public JMenu getTimerCount(){ return timerCount;}

    public void setModel(Game model){
        this.model = model;
    }
}