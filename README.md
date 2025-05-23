# Snakes and Ladders Game

A JavaFX implementation of the classic Snakes and Ladders board game with modern features and extensibility.

## Overview

This project is a comprehensive implementation of the Snakes and Ladders game with additional features like wormholes, custom boards, and animated gameplay. The application follows clean architecture principles and provides a polished user interface.

## Features

- **Multiple Board Types**:

  - Standard (classic Snakes and Ladders layout)
  - Empty (board with no snakes or ladders)
  - Custom (user-defined board configurations)
  - Wormhole (extended gameplay with wormhole teleportation)

- **Player Management**:

  - Support for 1-5 players
  - Custom player creation
  - Player piece customization
  - CSV import/export of player profiles

- **Game Mechanics**:

  - Animated player movement
  - Snake, ladder, and wormhole interactions
  - Turn-based gameplay
  - Victory detection

- **User Interface**:
  - Clean, modern UI with Material Design influences
  - Game information panel
  - Dice rolling visualization
  - Board state visualization with colored tiles

## Getting Started

### Prerequisites

The games are only guaranteed to work on the following specs:

- Java 21
- JavaFX 17
- Maven
- Windows system
- 100% scaling for screen

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Build the project using Maven:
   ```
   mvn clean install
   ```
4. Run the application:
   ```
   mvn javafx:run
   ```

## How to Play

<details>
<summary><b>Starting a New Game</b></summary>

1. **Launch the application**

- The main menu will appear with options for "New Game", "Create new Players", and "Exit"

2. **Select "New Game"**

- This opens the Games Menu with different board types

3. **Choose a Board Type**

- **Standard**: Classic Snakes and Ladders board
- **Treasure Hunt**: Find the treasure on the board
- **Wormhole**: Board with extra wormholes that teleport you to random locations

4. **Player Selection**

- The Player Selection modal will appear
- Select the number of players (1-5) using the spinner
- Click "Next" to proceed to player piece selection
- Select player pieces from the dropdown menus
- Click "Start Game" to begin playing
  </details>
<details>
<summary><b>Creating New Players</b></summary>

1. **From the Main Menu, select "Create new Players"**

- This opens the Create Player dialog

2. **Enter Player Details**

- Type a name in the "Player Name" field
- Select a color using the color picker
- Click "Save" to create the player

3. **Player Storage**

- Players are automatically saved to the application's data directory
- The new player will be available for selection in future games
  </details>
<details>
<summary><b>Loading Players from CSV</b></summary>

1. **During Player Selection**

- In the player selection screen, click the "Load Players from CSV" button
- A file chooser dialog will appear

2. **Select a CSV File**

- Navigate to your CSV file containing player data
- File format should be: name,color_hex_code (e.g., "Player1,#FF0000")
- Click "Open" to load the players

3. **Using Loaded Players**

- Loaded players will appear in the dropdown menus
- Select the players you want to use in your game
- Click "Start Game" to begin
  </details>
  <details>
<summary><b>In-Game Controls</b></summary>

- Click the "Roll" button to roll the dice and move your piece
- For the Treasure Hunt game, use arrow keys to navigate after rolling
- Game information is displayed in the right panel
- When a player wins, a victory dialog appears with options to:

  - Restart Game (play again with same settings)
  - New Game (return to game selection)
  - Exit (close the application)
      </details>

        <details>

    <summary><b>Known issues</b></summary>

- Game sometimes places board in the top left when restarting laddergames after each other.
- Game might not start om Macbooks with apple silicon chips.
  </details>

## Project Structure

- **Model**: Game entities and business logic

  - `Boards`: Base class for all game boards
  - `Tiles`: Class for tiles in game
  - `Dice`: Simualtes dice
  - `Player`: Player information and state

- **Controller**: Game flow and rule management

  - `GameController`: Base game controller with common functionality
  - `BoardManager`: Board configuration and loading

- **UI**: User interface components

  - `Menus`: Main menu and game menu are navigation interfaces
  - Components: `GamePiece`, `AnimationManager`, `InfoTable`, etc. Components are objects called by the navigation pages

- **Persistence**: Data storage and retrieval
  - `JsonHandler`: Board configuration storage
  - `CsvHandler`: Player profile management

## Design Patterns

- **MVC Architecture**: Separation of model, view, and controller
- **Observer Pattern**: Game events notification system
- **Factory Method**: Board creation

## Contributors

- IDATT2003 Group 8
