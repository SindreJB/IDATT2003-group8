package edu.ntnu.idi.idatt.ui.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ntnu.idi.idatt.model.Player;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Manages the visual representation of player pieces on the game board
 */
public class GamePiece {
  private final int tileSize;
  private final List<Player> players;
  
  // Colors for fallback pieces
  private static final Map<String, Color> COLOR_MAP = new HashMap<>();
  
  static {
    // Initialize color map
    COLOR_MAP.put("Pink star", Color.PINK);
    COLOR_MAP.put("Yellow square", Color.YELLOW);
    COLOR_MAP.put("blue circle", Color.BLUE);
    COLOR_MAP.put("green triangle", Color.GREEN);
    COLOR_MAP.put("red pentagon", Color.RED);
  }

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
   * @param position The board position to check
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
   * Adds a single player piece to the container
   * 
   * @param container The container to add the piece to
   * @param player The player to add
   * @param playerIndex The index of this player among those at this position
   * @param playerCount The total number of players at this position
   */
  private void addPlayerPieceToContainer(StackPane container, Player player, int playerIndex, int playerCount) {
    String pieceType = player.getPieceType();
    
    // Check if piece type indicates an image file
    if (pieceType.endsWith(".png")) {
      try {
        // Try to load the image
        String imagePath = "/boardPieces/" + pieceType;
        Image pieceImage = new Image(getClass().getResourceAsStream(imagePath));
        
        // Create image view
        ImageView pieceView = new ImageView(pieceImage);
        
        // Size and position
        double size = calculatePieceSize(playerCount);
        pieceView.setFitWidth(size);
        pieceView.setFitHeight(size);
        pieceView.setPreserveRatio(true);
        
        Insets margin = calculatePieceMargin(playerIndex, playerCount);
        StackPane.setMargin(pieceView, margin);
        
        container.getChildren().add(pieceView);
        return;
      } catch (Exception e) {
        System.err.println("Error loading image " + pieceType + ": " + e.getMessage());
        // Fall through to shape creation
      }
    }
    
    // For non-image pieces or if image loading failed, create appropriate shapes
    createShapePiece(container, player, playerIndex, playerCount);
  }
  
  /**
   * Creates a shape-based piece for a player
   * 
   * @param container The container to add the piece to
   * @param player The player to create a piece for
   * @param playerIndex The index of this player among those at this position
   * @param playerCount The total number of players at this position
   */
  private void createShapePiece(StackPane container, Player player, int playerIndex, int playerCount) {
    String pieceType = player.getPieceType();
    
    // Get color for this piece type
    Color pieceColor = COLOR_MAP.getOrDefault(pieceType, 
        getDefaultColor(playerIndex));
    
    // Create circle
    double size = calculatePieceSize(playerCount) * 0.4;
    Circle circle = new Circle(size);
    circle.setFill(pieceColor);
    circle.setStroke(Color.BLACK);
    circle.setStrokeWidth(1);
    
    // Calculate position
    Insets margin = calculatePieceMargin(playerIndex, playerCount);
    StackPane.setMargin(circle, margin);
    
    // Add to container
    container.getChildren().add(circle);
  }
  
  /**
   * Calculates the appropriate piece size based on how many players are on a tile
   * 
   * @param playerCount The number of players on the tile
   * @return The appropriate size for each piece
   */
  private double calculatePieceSize(int playerCount) {
    switch (playerCount) {
      case 1: return tileSize * 0.45; // Single player - larger piece
      case 2: return tileSize * 0.35; // Two players - medium pieces
      case 3: return tileSize * 0.3;  // Three players
      case 4: return tileSize * 0.25; // Four players
      case 5: return tileSize * 0.2;  // Five players - smallest pieces
      default: return tileSize * 0.3; // Default size
    }
  }
  
  /**
   * Calculates the margin for a piece based on its position among others
   * 
   * @param playerIndex The index of this player on the tile (0-based)
   * @param playerCount Total number of players on this tile
   * @return The appropriate margin to position this piece
   */
  private Insets calculatePieceMargin(int playerIndex, int playerCount) {
    if (playerCount == 1) {
      // Single player - center
      return new Insets(0, 0, 0, 0);
    } else if (playerCount == 2) {
      // Two players - side by side
      return new Insets(0, 
          playerIndex == 0 ? 10 : -10, 0, 
          playerIndex == 0 ? -10 : 10);
    } else if (playerCount == 3) {
      // Three players - triangle formation
      switch (playerIndex) {
        case 0: return new Insets(-10, 0, 0, 0);    // Top
        case 1: return new Insets(5, -10, 0, 0);    // Bottom left
        case 2: return new Insets(5, 10, 0, 0);     // Bottom right
        default: return new Insets(0, 0, 0, 0);
      }
    } else if (playerCount == 4) {
      // Four players - diamond formation
      switch (playerIndex) {
        case 0: return new Insets(-10, 0, 0, 0);    // Top
        case 1: return new Insets(0, -10, 0, 0);    // Left
        case 2: return new Insets(0, 10, 0, 0);     // Right
        case 3: return new Insets(10, 0, 0, 0);     // Bottom
        default: return new Insets(0, 0, 0, 0);
      }
    } else {
      // Five players - spread across the tile
      switch (playerIndex) {
        case 0: return new Insets(-10, 0, 0, 0);    // Top center
        case 1: return new Insets(-5, -10, 0, 0);   // Top left
        case 2: return new Insets(-5, 10, 0, 0);    // Top right
        case 3: return new Insets(10, -10, 0, 0);   // Bottom left
        case 4: return new Insets(10, 10, 0, 0);    // Bottom right
        default: return new Insets(0, 0, 0, 0);
      }
    }
  }
  
  /**
   * Gets a default color based on player index
   * 
   * @param playerIndex Player index
   * @return A default color
   */
  private Color getDefaultColor(int playerIndex) {
    switch (playerIndex % 8) {
      case 0: return Color.RED;
      case 1: return Color.BLUE;
      case 2: return Color.GREEN;
      case 3: return Color.PURPLE;
      case 4: return Color.ORANGE;
      case 5: return Color.BROWN;
      case 6: return Color.GRAY;
      case 7: return Color.YELLOW;
      default: return Color.BLACK;
    }
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
   * For both image-based and shape-based pieces
   *
   * @param playerIndex the index of the player
   * @return the created ImageView or null if creation fails
   */
  public ImageView createAnimationPiece(int playerIndex) {
    Player player = players.get(playerIndex);
    String pieceType = player.getPieceType();
    
    // Check if piece type indicates an image file
    if (pieceType.endsWith(".png")) {
      try {
        String imagePath = "/boardPieces/" + pieceType;
        Image playerImage = new Image(getClass().getResourceAsStream(imagePath));
        ImageView playerPiece = new ImageView(playerImage);
        
        // Use a fixed size for animation
        double size = tileSize * 0.35;
        playerPiece.setFitHeight(size);
        playerPiece.setFitWidth(size);
        playerPiece.setPreserveRatio(true);
        
        return playerPiece;
      } catch (Exception e) {
        System.err.println("Error loading player image: " + e.getMessage());
        e.printStackTrace();
        // Fall through to shape creation
      }
    }
    
    // For non-image pieces, create a temporary ImageView with a snapshot of the shape
    try {
      // Create a shape equivalent to what would be displayed on the board
      Circle circle = new Circle(tileSize * 0.15);
      Color pieceColor = COLOR_MAP.getOrDefault(pieceType, getDefaultColor(playerIndex));
      circle.setFill(pieceColor);
      circle.setStroke(Color.BLACK);
      circle.setStrokeWidth(1);
      
      // Create a StackPane to hold the circle with transparent background
      StackPane shapeContainer = new StackPane(circle);
      shapeContainer.setPrefSize(tileSize * 0.35, tileSize * 0.35);
      shapeContainer.setBackground(javafx.scene.layout.Background.EMPTY);  // Set transparent background
      
      // Create snapshot parameters with transparent background
      javafx.scene.SnapshotParameters params = new javafx.scene.SnapshotParameters();
      params.setFill(javafx.scene.paint.Color.TRANSPARENT);  // Make snapshot background transparent
      
      // Take a snapshot of the shape with transparent background
      javafx.scene.image.WritableImage snapshot = shapeContainer.snapshot(params, null);
      
      // Create an ImageView from the snapshot
      ImageView shapeView = new ImageView(snapshot);
      shapeView.setFitWidth(tileSize * 0.35);
      shapeView.setFitHeight(tileSize * 0.35);
      
      return shapeView;
    } catch (Exception e) {
      System.err.println("Error creating shape animation: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Preserves labels in a tile by saving and restoring them
   *
   * @param tile the tile to preserve labels in
   */
  private void preserveLabels(StackPane tile) {
    java.util.List<Label> labels = new java.util.ArrayList<>();

    // Find and save all labels
    for (javafx.scene.Node node : tile.getChildren()) {
      if (node instanceof Label) {
        labels.add((Label) node);
      }
    }

    // Clear tile completely
    tile.getChildren().clear();

    // Add back labels
    tile.getChildren().addAll(labels);
  }
}