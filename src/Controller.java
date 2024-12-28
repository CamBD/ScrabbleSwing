package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Stack;

/**
 * The Controller class manages user interactions and game logic for Scrabble. It listens to user actions, updates the view,
 * and modifies the model accordingly.
 *
 * ALL CODE REVELANT TO BONUS MILESTONE WILL BE ANNOTATED BY leading //<bonus> and terminating //</bonus>
 * Thank you.
 *
 */
public abstract class Controller implements ActionListener {
    private static Game model;
    // The game model representing the state and logic of the game.

    private Stack<byte[]> undoStack = new Stack<>();
    private Stack<byte[]> redoStack = new Stack<>();
    private Timer timer;
    private static View view; // The game view that displays the board and UI components.
    CustomButton storedButton; // Stores the button selected by the player.

    /**
     * Constructor for the {@code Controller} class.
     * Initializes the game model, view, and action listeners for all UI components.
     */
    public Controller() {
        model = new Game();
        this.view = model.getView();

        timer = new Timer(0, null);

        view.getVerticalButton().addActionListener(e-> verticalButton());
        view.getHorizontalButton().addActionListener(e->horizontalButton());
        CustomButton[][] button = view.getButtons();
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                button[row][col].addActionListener(this::clickedBoard);
            }
        }
        view.getSubmit().addActionListener(this::submitButton);
        view.getSkip().addActionListener(this::skip);
        view.getAi_turn().addActionListener(this::ai_turn);
        for (int i = 0; i < 7; i++) {
            view.getHandButtons()[i].addActionListener(this::handButton);
        }
        view.getUndoMenuItem().addActionListener(e-> undoButton());
        view.getRedoMenuItem().addActionListener(e->redoButton());

        view.getSaveMenuItem().addActionListener(e -> saveGame());
        view.getLoadMenuItem().addActionListener(e -> loadGame());

        view.getExitMenu().addActionListener(e -> System.exit(0));
        view.getResetMenu().addActionListener(ActionListener -> {
            view.getFrame().dispose();
            Controller controller = new Controller() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("g");
                }
            };
        });
        
        view.getDefaultLayout().addActionListener(e->defaultLayout());
        view.getChaosLayout().addActionListener(e->chaosLayout());
        view.getRingLayout().addActionListener(e->ringLayout());

        //<bonus>
        view.getTimerON().addActionListener(e->timerMode());
        view.getTimerOFF().addActionListener(e->timerOff());
        //</bonus>

        view.getHandPanel().revalidate();
        view.getHandPanel().repaint();
//        view.initializeScoreboard(model.player);
        view.refreshHandPanel(false);
    }

    private void saveGame() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("resources/savedGame.txt"))) {
            oos.writeObject(model);
            JOptionPane.showMessageDialog(view.getFrame(),
                    "Game saved successfully!", "Save Game", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view.getFrame(), "Error saving game: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("resources/savedGame.txt"))) {
            model = (Game) ois.readObject(); //restore the saved game object
            view.setModel(model); //set the view back to this model

            //refresh view to account for current game state
            view.updateView();
            view.updateHandPanel();
            JOptionPane.showMessageDialog(view.getFrame(), "Game loaded successfully!", "Load Game", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view.getFrame(), "Error loading game: " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void timerOff() {//<bonus>
        timer.stop();
        view.getTimerCount().setBackground(new JMenu().getBackground());
        view.getTimerCount().setForeground(Color.black);
        view.getTimerCount().remove(view.getTimerOFF());
        view.getTimerOFF().setEnabled(false);
        view.getTimerCount().add(view.getTimerON());
        view.getTimerON().setEnabled(true);
        view.getTimerCount().setText("Timed Mode");
    } //</bonus>

    private void timerMode() {//<bonus>
        timer.stop();
        view.getTimerCount().setBackground(new JMenu().getBackground());
        view.getTimerCount().setForeground(new JMenu().getForeground());
        view.getTimerCount().remove(view.getTimerON());
        view.getTimerON().setEnabled(false);
        view.getTimerOFF().setEnabled(true);
        view.getTimerCount().add(view.getTimerOFF());
        final int[] remainingTime = {30}; // must use a final single integer array for timer to access
        view.getTimerCount().setText("Time remaining: " + remainingTime[0]);
        timer = new Timer(1000, new ActionListener() {
            // timer listener responsible for changing the color scheme of the timer in application
            // based on time remaining
            @Override
            public void actionPerformed(ActionEvent e) {
                if (remainingTime[0] > 1) {
                    remainingTime[0]--;
                    view.getTimerCount().setText("Time remaining: " + remainingTime[0]);
                    if(remainingTime[0] <= 15 && remainingTime[0] > 5){ // less than 15 flash orange / default white
                        if (!view.getTimerCount().getBackground().equals(Color.ORANGE)){
                            view.getTimerCount().setBackground(Color.ORANGE);
                        } else{
                            view.getTimerCount().setBackground(new JMenu().getBackground());
                        }
                    }
                    if(remainingTime[0] <= 5){ // less than 5 flash red and orange
                        if (!view.getTimerCount().getBackground().equals(Color.RED)) {
                            view.getTimerCount().setBackground(Color.RED);
                            view.getTimerCount().setForeground(Color.WHITE);
                        } else if (!view.getTimerCount().getBackground().equals(Color.ORANGE)) {
                            view.getTimerCount().setBackground(Color.ORANGE);
                            view.getTimerCount().setForeground(new JMenu().getForeground());
                        }
                    }
                } else {
                    ((Timer) e.getSource()).stop(); // stop the timer when it reaches 0
                    view.getTimerCount().setText("Time's up!");
                    view.getTimerCount().setBackground(Color.RED);
                    view.getTimerCount().setForeground(Color.WHITE);
                    JOptionPane.showMessageDialog(view.getFrame(), "Time's up! Your turn is being skipped.");

                    skip(e);
                    //skip();
                    view.getTimerCount().setBackground(new JMenu().getBackground());
                    view.getTimerCount().setForeground(new JMenu().getForeground());

                }
            }
        });
        // Start the timer
        timer.start();
    }//</bonus>

    public Game getModel(){ return model;} // testing
    public View getView(){ return model.getView();}

    private void ringLayout() {
        if(model.start()) {
            model.getBoard().setPremiumLayout("resources/PremiumLayouts/premiumRing.xml");
            view.getChaosLayout().setEnabled(true);
            view.getDefaultLayout().setEnabled(true);
            view.getRingLayout().setEnabled(false);

            view.getHorizontalButton().setEnabled(true);
            view.getVerticalButton().setEnabled(true);
            view.updateHandPanel();
            view.setFirstLetter(true);
            view.setBeforeStart(true);
            view.updateView();
            view.setInputWord("");
            view.refreshHandPanel(false);
        }else{
            JOptionPane.showMessageDialog(view.getFrame(), "You may only change the premium square layout on the first turn.");
        }
    }

    private void chaosLayout() {
        if(model.start()) {
            model.getBoard().setPremiumLayout("resources/PremiumLayouts/premiumChaos.xml");
            view.getChaosLayout().setEnabled(false);
            view.getDefaultLayout().setEnabled(true);
            view.getRingLayout().setEnabled(true);

            view.getHorizontalButton().setEnabled(true);
            view.getVerticalButton().setEnabled(true);
            view.updateHandPanel();
            view.setFirstLetter(true);
            view.setBeforeStart(true);
            view.updateView();
            view.setInputWord("");
            view.refreshHandPanel(false);
        }else{
            JOptionPane.showMessageDialog(view.getFrame(), "You may only change the premium square layout on the first turn.");
        }
    }

    private void defaultLayout() {
        if(model.start()) {
            model.getBoard().setPremiumLayout("resources/PremiumLayouts/premiumDefault.xml");
            view.getChaosLayout().setEnabled(true);
            view.getDefaultLayout().setEnabled(false);
            view.getRingLayout().setEnabled(true);

            view.getHorizontalButton().setEnabled(true);
            view.getVerticalButton().setEnabled(true);
            view.updateHandPanel();
            view.setFirstLetter(true);
            view.setBeforeStart(true);
            view.updateView();
            view.setInputWord("");
            view.refreshHandPanel(false);
        }else{
            JOptionPane.showMessageDialog(view.getFrame(), "You may only change the premium square layout on the first turn.");
        }
    }

    /**
     * Handles the "Vertical" button click event.
     * Enables vertical word placement mode and disables further direction changes.
     */
    private void verticalButton(){
        view.setVertical(true);
        view.getVerticalButton().setEnabled(false); // Disable after selecting vertical
        view.getHorizontalButton().setEnabled(false); // Disable horizontal as well
        view.setDirection('V');
        view.disableButtons();
        view.refreshHandPanel(true);
    }

    /**
     * Handles the "Horizontal" button click event.
     * Enables horizontal word placement mode and disables further direction changes.
     */
    private void horizontalButton() {
        view.setVertical(false);
        view.getVerticalButton().setEnabled(false); // Disable after selecting horizontal
        view.getHorizontalButton().setEnabled(false); // Disable vertical as well
        view.setDirection('H');
        view.disableButtons();
        view.refreshHandPanel(true);
    }

    /**
     * Handles the player's hand button click event.
     * Stores the selected tile and updates the hand and board buttons accordingly.
     *
     * @param e the action event triggered by clicking a hand button.
     */
    public void handButton (ActionEvent e){
        CustomButton button = (CustomButton) e.getSource();
        storedButton = (CustomButton) e.getSource();
        view.setSelectedTile(new Tile(button.getText().charAt(0)));// Store the selected tile
        if(view.getSelectedTile().getLetter() == '*'){
            blankSelector();
            return;
        }
        if (view.getBeforeStart()) {
            view.enableButtons();
        } else {
            //view.disableButtons();
            view.updateEnabledTiles();
        }
        view.setBeforeStart(false);
        view.updateHandPanel();
    }

    /**
     * Handles the board button click event.
     * Places the selected tile on the board and updates the game state.
     *
     * @param e the action event triggered by clicking a board button.
     */
    public void clickedBoard (ActionEvent e) {
        CustomButton clickedButton = (CustomButton) e.getSource();
        CustomButton[][] button = view.getButtons();
        int row = clickedButton.getRow();
        int col = clickedButton.getCol();
        if (view.getSelectedTile() != null) {
            view.setClickedRow(clickedButton.getRow());
            view.setClickedCol(clickedButton.getCol());
            button[view.getClickedRow()][view.getClickedCol()].setText(String.valueOf(view.getSelectedTile().getLetter()));
            view.addTilePlacedThisTurn(row,col);
            if (view.getFirstLetter()) { // if this is the first letter added
                view.setTargetRow(clickedButton.getRow());
                view.setTargetCol(clickedButton.getCol());
                if (view.getVertical()) {
                    if (model.getBoard().getTile(view.getTargetRow() - 1, view.getTargetCol()).getLetter() != ' ') {
                        String temp = Character.toString(view.getSelectedTile().getLetter());
                        view.setTargetRow(view.getTargetRow() - 1);
                        view.setInputWord(model.getBoard().getTile(view.getTargetRow(), view.getTargetCol()).getLetter() + temp);
                    }else {
                        view.addInputWord(view.getSelectedTile().getLetter());

                    }
                } else {
                    if (model.getBoard().getTile(view.getTargetRow(), view.getTargetCol() - 1).getLetter() != ' ') {
                        String temp = Character.toString(view.getSelectedTile().getLetter());
                        view.setTargetCol(view.getTargetCol() - 1);
                        view.setInputWord(model.getBoard().getTile(view.getTargetRow(), view.getTargetCol()).getLetter() + temp);
                    }else {
                        view.addInputWord(view.getSelectedTile().getLetter());
                    }
                }
                view.setFirstLetter(false);
            } else {
                view.addInputWord(view.getSelectedTile().getLetter());
            }
            view.setSelectedTile(null);
            //view.updateHandPanel();
            view.disableButtons();
            storedButton.setEnabled(false);
        }
    }

    /**
     * Handles the "Submit" button click event.
     * Validates the word placement, updates the board, and transitions to the next player.
     *
     * @param e the action event triggered by clicking the submit button.
     */
    public void submitButton(ActionEvent e) {
        Game currentState = model;
        view.addState(currentState);
        saveState();
        boolean isTouchingExistingLetter = false;

        // Check adjacency for each newly placed tile
        for (Point p : view.getTilesPlacedThisTurn()) {
            int row = p.x;
            int col = p.y;

            // Check adjacent tiles
            if ((row > 0 && model.getBoard().getTile(row - 1, col).getLetter() != ' ') || // Above
                    (row < 14 && model.getBoard().getTile(row + 1, col).getLetter() != ' ') || // Below
                    (col > 0 && model.getBoard().getTile(row, col - 1).getLetter() != ' ') || // Left
                    (col < 14 && model.getBoard().getTile(row, col + 1).getLetter() != ' ') ||
                    (row == 7 && col == 7)) { // Right
                System.out.println("workn");
                isTouchingExistingLetter = true;
                break;
            }
        }
        if (!view.getVertical()) {
            while (model.getBoard().getTile(view.getTargetRow(), view.getTargetCol() + view.getInputWord().length()).getLetter() != ' ') {
                view.addInputWord(model.getBoard().getTile(view.getTargetRow(), view.getTargetCol() + view.getInputWord().length()).getLetter());
            }
        } else {
            while (model.getBoard().getTile(view.getTargetRow() + view.getInputWord().length(), view.getTargetCol()).getLetter() != ' ') {
                view.addInputWord(model.getBoard().getTile(view.getTargetRow() + view.getInputWord().length(), view.getTargetCol()).getLetter());
            }
        }

        if (isTouchingExistingLetter) {
            if (model.play(view.getInputWord().toLowerCase(), view.getDirection(), view.getTargetRow(), view.getTargetCol())) {
                timer.stop();
                System.out.println("Input word " + view.getInputWord() + " Row: " + view.getTargetRow() + " Col: " + view.getTargetCol() + " Dir:" + view.getDirection());

                if(!view.getTimerON().isEnabled()){
                    timerMode();
                }
            } else {
                JOptionPane.showMessageDialog(view.getFrame(), "Tried to submit word: " + view.getInputWord() + "\nInvalid word. Please try again.");
            }
        } else {
            JOptionPane.showMessageDialog(view.getFrame(), "Word must touch an existing letter on the board!");
        }

        view.getHorizontalButton().setEnabled(true);
        view.getVerticalButton().setEnabled(true);
        if(view.getVertical()){
            view.setDirection('V');
        }else{
            view.setDirection('H');
        }

        view.updateHandPanel();
        view.setFirstLetter(true);
        view.setBeforeStart(true);
        view.updateView();
        view.setInputWord("");
        view.clearTilesPlacedThisTurn();
//        view.updateScoreboard(model.player);
        view.refreshHandPanel(false);
    }

    /**
     * Handles the "Skip" button click event.
     * Moves to the next player's turn and refreshes the game view.
     *
     * @param e the action event triggered by clicking the "Skip" button.
     */
    public void skip(ActionEvent e) {
        Game currentState = model;
        saveState();
        timer.stop();
        view.addState(currentState);
        model.nextPlayer();

        view.setBeforeStart(true);
        view.setInputWord("");
        view.getHorizontalButton().setEnabled(true);
        view.getVerticalButton().setEnabled(true);
        view.updateView();
        view.updateHandPanel();
        if(!view.getTimerON().isEnabled()){
            timerMode();
        }
        view.refreshHandPanel(false);
    }

    /**
     * Handles the "AI Turn" button click event.
     * Allows the AI to make a move, validates the move, and updates the game view.
     *
     * @param e the action event triggered by clicking the "AI Turn" button.
     */
    public void ai_turn(ActionEvent e){
        timer.stop();
        Game currentState = model;
        view.addState(currentState);
        saveState();
        model.aiPlay();
        view.getHorizontalButton().setEnabled(true);
        view.getVerticalButton().setEnabled(true);
        view.setFirstLetter(true);
        view.setBeforeStart(true);
        view.updateView();
        view.setInputWord("");

        view.updateHandPanel();
        if(!view.getTimerON().isEnabled()){
            timerMode();
        }
//        view.updateScoreboard(model.player);
        view.refreshHandPanel(false);


    }

    public void undoButton(){
        if (!undoStack.isEmpty()) {
            redoStack.push(serializeCurrentState()); // save current state for redo
            byte[] previousState = undoStack.pop();
            restoreState(previousState);
            view.updateView();
            view.updateHandPanel();
            if(!view.getTimerON().isEnabled()){
                timerMode();
            }
        } else {
            System.out.println("No undo available!");
        }
        view.updateView();
    }

    public void redoButton(){
        if (!redoStack.isEmpty()) {
            undoStack.push(serializeCurrentState()); // resave current state for undo
            byte[] nextState = redoStack.pop();
            restoreState(nextState);
            view.updateView();
            view.updateHandPanel();
            if(!view.getTimerON().isEnabled()){
                timerMode();
            }
        } else {
            System.out.println("No redo available!");
        }
    }
    /**
     * Displays a dialog box for selecting a replacement letter when a blank tile is used.
     */
    public static void blankSelector() {
        // create the temporary frame
        JFrame frame = new JFrame("Which letter would you like?");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // panel to hold the letter selection buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 6, 10, 10)); // 5 rows x 6 columns with gaps

        // create an ActionListener to handle button clicks in the secondary menu
        ActionListener buttonClickListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource(); // get the clicked button
                String letter = source.getText();         // get the button text
                model.getCurrentPlayer().removeTile(new Tile('*')); // remove the blank
                Tile temp = new Tile(letter.charAt(0)); // add the new tile
                temp.setPoints(0); // blank tiles are still worth 0
                model.getCurrentPlayer().addTile(temp);
                view.updateHandPanel();
                frame.dispose();
            }
        };

        // create buttons A-Z and add them to the temporary panel
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            JButton letterButton = new JButton(String.valueOf(letter));
            letterButton.addActionListener(buttonClickListener); // use the above listener
            buttonPanel.add(letterButton); // add to temp panel
        }

        // add the panel to the frame
        frame.add(buttonPanel);

        // display the frame
        frame.setLocationRelativeTo(null); // center on screen
        frame.setVisible(true);
    }

    public void saveState() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(model);
            undoStack.push(baos.toByteArray()); // save serialized state on undoStack
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restoreState(byte[] state) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(state);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            model = (Game) ois.readObject();
            view.setModel(model);
            view.updateView(); // update the view to reflect the restored state
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private byte[] serializeCurrentState() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(model);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Controller controller = new Controller() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("g");
            }
        };
    }
}
