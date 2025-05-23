package edu.ntnu.idi.idatt.ui.components;

import java.util.List;

import edu.ntnu.idi.idatt.model.Player;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Manages the visual representation of player pieces on the game board
 */
public class GamePiece {
  private final int tileSize;
  private final List<Player> players;

  // Default colors if hex parsing fails
  private static final Color[] DEFAULT_COLORS = {
      Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PURPLE
  };

  /**
   * Creates a GamePiece manager for visualizing players
   *
   * @param tileSize the size of each tile
   * @param players  the list of players
   */
  public GamePiece(int tileSize, List<Player> players) {
    this.tileSize = tileSize;
    this.players = players;
  }

  /**
   * Creates the initial visual representation of all players on the start tile
   *
   * @param startTile the tile where all players start
   */
  public void setupPlayerPieces(StackPane startTile) {
    // Create a container for all player pieces
    StackPane playerContainer = new StackPane();
    playerContainer.setMaxSize(tileSize, tileSize);

    // Add all players to the start tile
    addAllPlayersToContainer(playerContainer, 1);

    // Add container to the start tile
    startTile.getChildren().add(playerContainer);
  }

  /**
   * Adds all players at the specified position to a container
   * 
   * @param container The container to add pieces to
   * @param position  The board position to check
   */
  private void addAllPlayersToContainer(StackPane container, int position) {
    int playerCount = 0;

    // Count players at this position
    for (Player player : players) {
      if (player.getTileId() == position) {
        playerCount++;
      }
    }

    // Add each player at this position
    int currentIndex = 0;
    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);
      if (player.getTileId() == position) {
        addPlayerPieceToContainer(container, player, currentIndex, playerCount);
        currentIndex++;
      }
    }
  }

  /**
   * Gets the color for a player from their piece type (hex string)
   * 
   * @param player      The player to get color for
   * @param playerIndex Fallback index for default color
   * @return The parsed Color object
   */
  private Color getPlayerColor(Player player, int playerIndex) {
    // Debug the color value we're trying to parse

    String colorHex = player.getPieceType();

    // If null or empty, use default
    if (colorHex == null || colorHex.isEmpty()) {
      return getDefaultColor(playerIndex);
    }

    try {
      // Make sure the color has a proper hex format
      if (!colorHex.startsWith("#")) {
        colorHex = "#" + colorHex;
      }

      Color parsedColor = Color.web(colorHex);
      return parsedColor;
    } catch (Exception e) {
      // Log the error and use default
      return getDefaultColor(playerIndex);
    }
  }

  /**
   * Adds a single player piece to the container
   * 
   * @param container   The container to add the piece to
   * @param player      The player to add
   * @param playerIndex The index of this player among those at this position
   * @param playerCount The total number of players at this position
   */
  private void addPlayerPieceToContainer(StackPane container, Player player, int playerIndex, int playerCount) {
    // Get color from player piece type
    Color pieceColor = getPlayerColor(player, playerIndex);

    // Create circle with the color
    double size = calculatePieceSize(playerCount) * 0.4;
    Circle circle = new Circle(size);

    // Set color
    circle.setFill(pieceColor);
    circle.setStroke(Color.BEIGE);
    circle.setStrokeWidth(2);

    // Calculate position
    Insets margin = calculatePieceMargin(playerIndex, playerCount);
    StackPane.setMargin(circle, margin);

    // Add to container
    container.getChildren().add(circle);
  }

  /**
   * Get a default color based on player index
   * 
   * @param playerIndex The index of the player
   * @return A default color
   */
  private Color getDefaultColor(int playerIndex) {
    return DEFAULT_COLORS[playerIndex % DEFAULT_COLORS.length];
  }

  /**
   * Calculates the appropriate piece size based on how many players are on a tile
   * 
   * @param playerCount The number of players on the tile
   * @return The appropriate size for each piece
   */
  private double calculatePieceSize(int playerCount) {
    return switch (playerCount) {
      case 1 -> tileSize * 0.45; // Single player - larger piece
      case 2 -> tileSize * 0.35; // Two players - medium pieces
      case 3 -> tileSize * 0.3; // Three players
      case 4 -> tileSize * 0.25; // Four players
      case 5 -> tileSize * 0.2; // Five players - smallest pieces
      default -> tileSize * 0.3; // Default size
    };
  }

  /**
   * Calculates the margin for a piece based on its position among others
   * 
   * @param playerIndex The index of this player on the tile (0-based)
   * @param playerCount Total number of players on this tile
   * @return The appropriate margin to position this piece
   */
  private Insets calculatePieceMargin(int playerIndex, int playerCount) {
    return switch (playerCount) {
      case 1 ->
        // Single player - center
        new Insets(0, 0, 0, 0);
      case 2 ->
        // Two players - side by side
        new Insets(0,
            playerIndex == 0 ? 10 : -10, 0,
            playerIndex == 0 ? -10 : 10);
      case 3 -> {
        // Three players - triangle formation
        yield switch (playerIndex) {
          case 0 -> new Insets(-10, 0, 0, 0); // Top
          case 1 -> new Insets(5, -10, 0, 0); // Bottom left
          case 2 -> new Insets(5, 10, 0, 0); // Bottom right
          default -> new Insets(0, 0, 0, 0);
        };
      }
      case 4 -> {
        // Four players - diamond formation
        yield switch (playerIndex) {
          case 0 -> new Insets(-10, 0, 0, 0); // Top
          case 1 -> new Insets(0, -10, 0, 0); // Left
          case 2 -> new Insets(0, 10, 0, 0); // Right
          case 3 -> new Insets(10, 0, 0, 0); // Bottom
          default -> new Insets(0, 0, 0, 0);
        };
      }
      default -> {
        // Five players - spread across the tile
        yield switch (playerIndex) {
          case 0 -> new Insets(-10, 0, 0, 0); // Top center
          case 1 -> new Insets(-5, -10, 0, 0); // Top left
          case 2 -> new Insets(-5, 10, 0, 0); // Top right
          case 3 -> new Insets(10, -10, 0, 0); // Bottom left
          case 4 -> new Insets(10, 10, 0, 0); // Bottom right
          default -> new Insets(0, 0, 0, 0);
        };
      }
    };
  }

  /**
   * Adds a player to a tile at a specific position
   *
   * @param player   the player to add
   * @param position the position to add the player at
   * @param tile     the tile corresponding to the position
   */
  public void addPlayerToTile(Player player, int position, StackPane tile) {
    // Preserve labels first
    preserveLabels(tile);

    // Create container for player pieces
    StackPane playerContainer = new StackPane();
    playerContainer.setMaxSize(tileSize, tileSize);

    // Add all players at this position
    addAllPlayersToContainer(playerContainer, position);

    // Only add if there are pieces to display
    if (!playerContainer.getChildren().isEmpty()) {
      tile.getChildren().add(playerContainer);
    }
  }

  /**
   * Creates an ImageView for a player piece for animation
   *
   * @param playerIndex the index of the player
   * @return the created ImageView or null if creation fails
   */
  public ImageView createAnimationPiece(int playerIndex) {
    Player player = players.get(playerIndex);

    try {
      // Use the same color method to ensure consistency
      Color pieceColor = getPlayerColor(player, playerIndex);

      // Create a shape equivalent to what would be displayed on the board
      Circle circle = new Circle(tileSize * 0.15);
      circle.setFill(pieceColor);
      circle.setStroke(Color.BLACK);
      circle.setStrokeWidth(1);

      // Create a StackPane with transparent background
      StackPane shapeContainer = new StackPane(circle);
      shapeContainer.setPrefSize(tileSize * 0.35, tileSize * 0.35);
      shapeContainer.setBackground(javafx.scene.layout.Background.EMPTY);

      // Create snapshot parameters with transparent background
      SnapshotParameters params = new javafx.scene.SnapshotParameters();
      params.setFill(javafx.scene.paint.Color.TRANSPARENT);

      // Take a snapshot
      WritableImage snapshot = shapeContainer.snapshot(params, null);

      // Create an ImageView from the snapshot
      ImageView shapeView = new javafx.scene.image.ImageView(snapshot);
      shapeView.setFitWidth(tileSize * 0.35);
      shapeView.setFitHeight(tileSize * 0.35);

      return shapeView;
    } catch (Exception e) {

      return null;
    }
  }

  /**
   * Preserves background and labels in a tile by removing only player pieces
   *
   * @param tile the tile to clean of player pieces
   */
  private void preserveLabels(StackPane tile) {
    // Remove any StackPane containers (which contain player pieces)
    // but keep the background rectangle and labels
    java.util.List<javafx.scene.Node> nodesToRemove = new java.util.ArrayList<>();

    for (javafx.scene.Node node : tile.getChildren()) {
      if (node instanceof StackPane && node != tile) {
        nodesToRemove.add(node);
      }
    }

    // Remove only the player containers
    tile.getChildren().removeAll(nodesToRemove);
  }
}