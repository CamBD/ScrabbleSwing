# Scrabble 

This project is a simplified implementation of the Scrabble game using **Java**. It supports multiplayer gameplay with word validation and scoring mechanisms based on Scrabble rules.

---

## **How to Play**

1. **Start the Game**  
   - Launch the game, and players will take turns entering words to play on the board.

2. **Word Validation**  
   - Words are checked against a predefined list stored in `WordBank.txt`.
   - If the word is invalid, the player must try again.

3. **Placing Words**  
   - Players select a letter from their hand.
   - Players select the **direction** of the word: Horizontal or Vertical.
   - Players place tile selected on board

4. **End of Turn**  
   - If the move is valid, the board updates with the new word, and the next player’s turn begins.
   - If the move is invalid, the player retries the turn.
   - If player wants they  can skip

5. **Winning the Game**  
   - The game continues until all tiles are used or no valid moves remain.
   - The player with the **highest score** at the end wins.

---

## Timer Mode
This mode is for players who want a challenge.  
Play your turn within 30 seconds or get skipped!  
Timer mode can be activated & deactivated at any  
time in the menu bar.   

---

## AI Strategy
The Scrabble game features an AI player that follows a strategic decision-making process. Here’s how the AI works:

### AI Logic
1. **Word Generation**
- The AI scans the board for existing tiles.
- It combines the board's letters with its own hand tiles to generate potential words of lengths 2-5.

2. **Word Validation**
-Each generated word is validated against the word dictionary (WordBank.txt) to ensure it’s legal.
-The AI considers both horizontal and vertical placements.

3. **Score Calculation**
-The AI calculates scores for all valid words, considering Scrabble's point rules and bonus tiles (e.g., double word and triple letter).

4. **Optimal Move Selection**
-The AI selects the highest-scoring valid word.
-It prioritizes moves that leverage bonus tiles or create high-value combinations.

5. **Execution**
-The AI places its chosen word on the board and updates its hand.
-If no valid moves are available, the AI skips its turn.

## **Features**
- **Multiplayer Support**  
  - Allows up to **4 players**, each starting with 7 tiles.

- **Word Validation**  
  - Words are validated against a dictionary file (`WordBank.txt`).
  - Invalid words prompt the player to try again.
- **Score Calculation**  
  - Tiles have point values according to **Scrabble rules**.
  - Bonus points are awarded for placing all 7 tiles in one turn (Bingo).

- **Board Management**  
  - A **15x15 board** is displayed and updated with each move.

- **Tile Management**  
  - Tiles are drawn from a shared tile pile.
  - Players manage their hand by adding and removing tiles.

- **Save & Load**
    - A save file can store a game file for later use
    - Save files can be loaded at any time to overwrite a game

- **Undo & Redo**
    - The undo feature allows a player to erase as many sequential  
    moves as desired
    - Redo does the opposite and retrieves the undone moves.


---

## **Premium Tiles**
Premium Tiles are represented in the application as 4 colors.  
**Red** Tiles represent a Triple Word bonus  
**Pink** Tiles represent a Double Word bonus  
**Blue** Tiles represent a Triple Letter bonus  
**Cyan** Tiles represent a Double Letter bonus  
Players can select from a variety of premium tile layout  
at the start of the game.

---

## **Technologies Used**
- **Java**  
  - Core programming language for the project.

- **Collections Framework**  
  - `ArrayList`, `HashSet`, and `Arrays` for tile and word management.

---

## **Copyright Disclaimer**
This project is a **non-commercial educational project** created for learning purposes.  
**Scrabble®** is a registered trademark of **Hasbro, Inc.** (in the U.S. and Canada) and **Mattel** (elsewhere).  
This implementation is not affiliated with or endorsed by the official owners of the Scrabble game.  
All copyrights, trademarks, and intellectual property related to **Scrabble** belong to their respective owners.

---

## **Contributors**
 - Primary author: Cameron Bodkin-Dick  
Special thanks to classmates:  
 - Jad Hamzeh  
 - Jared St. Louis  
 - Jake Swann  
Who helped with several of the game functions, specifically with the AI turn progression,
save/load from file, action listener logic, and many design choices.  
---