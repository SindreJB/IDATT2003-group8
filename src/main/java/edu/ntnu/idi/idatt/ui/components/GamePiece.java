package edu.ntnu.idi.idatt.ui.components;

import java.util.List;

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
    try {
      // Get images for player pieces
      Image player1Image = new Image(getClass().getResourceAsStream("/boardPieces/SindreImage.png"));
      Image player2Image = new Image(getClass().getResourceAsStream("/boardPieces/StianImage.png"));

      // Create ImageViews for player pieces
      ImageView player1Piece = new ImageView(player1Image);
      ImageView player2Piece = new ImageView(player2Image);

      // Scale images appropriately - make them smaller
      player1Piece.setFitHeight(tileSize * 0.35);
      player1Piece.setFitWidth(tileSize * 0.35);
      player1Piece.setPreserveRatio(true);

      player2Piece.setFitHeight(tileSize * 0.35);
      player2Piece.setFitWidth(tileSize * 0.35);
      player2Piece.setPreserveRatio(true);

      // Container for player pieces
      StackPane playerContainer = new StackPane();

      // Add padding around the pieces to offset them slightly
      StackPane.setMargin(player1Piece, new Insets(0, 5, 0, -5)); // Right padding
      StackPane.setMargin(player2Piece, new Insets(0, -5, 0, 5)); // Left padding

      playerContainer.getChildren().addAll(player1Piece, player2Piece);
      playerContainer.setMaxSize(tileSize, tileSize); // Limit the container size

      // Add to the existing tile without stretching it
      startTile.getChildren().add(playerContainer);

    } catch (Exception e) {
      System.err.println("Error loading player pieces: " + e.getMessage());
      e.printStackTrace();

      // Fallback to colored circles if images aren't available
      createFallbackPlayerPieces(startTile);
    }
  }

  /**
   * Creates colored circles as fallback player pieces
   *
   * @param tile the tile to add player pieces to
   */
  private void createFallbackPlayerPieces(StackPane tile) {
    // Create colored circles as fallback pieces
    Circle player1Piece = new Circle(tileSize * 0.12);
    player1Piece.setFill(Color.RED);
    player1Piece.setStroke(Color.BLACK);
    player1Piece.setStrokeWidth(1);

    Circle player2Piece = new Circle(tileSize * 0.12);
    player2Piece.setFill(Color.BLUE);
    player2Piece.setStroke(Color.BLACK);
    player2Piece.setStrokeWidth(1);

    // Use StackPane to overlap player pieces slightly
    StackPane playerContainer = new StackPane();

    // Add padding around the pieces to offset them slightly
    StackPane.setMargin(player1Piece, new Insets(0, 5, 0, -5)); // Right padding
    StackPane.setMargin(player2Piece, new Insets(0, -5, 0, 5)); // Left padding

    playerContainer.getChildren().addAll(player1Piece, player2Piece);
    playerContainer.setMaxSize(tileSize, tileSize); // Limit the container size

    // Add to the existing tile
    tile.getChildren().add(playerContainer);
  }

  /**
   * Adds a player piece to a specific tile
   *
   * @param player   the player to add
   * @param position the position to add the player at
   * @param tile     the tile corresponding to the position
   */
  public void addPlayerToTile(Player player, int position, StackPane tile) {
    int playerIndex = players.indexOf(player);
    Player otherPlayer = players.get(1 - playerIndex);

    // Preserve labels first
    preserveLabels(tile);

    // Check if both players on same tile
    boolean bothPlayersOnTile = (otherPlayer.getTileId() == position);

    // Create container for player pieces
    StackPane playerContainer = new StackPane();
    playerContainer.setMaxSize(tileSize, tileSize);

    try {
      // Add current player
      String imagePath = playerIndex == 0 ? "/boardPieces/SindreImage.png" : "/boardPieces/StianImage.png";
      Image playerImage = new Image(getClass().getResourceAsStream(imagePath));
      ImageView playerPiece = new ImageView(playerImage);
      playerPiece.setFitHeight(tileSize * 0.35);
      playerPiece.setFitWidth(tileSize * 0.35);
      playerPiece.setPreserveRatio(true);

      // Offset position if both on same tile
      if (bothPlayersOnTile) {
        StackPane.setMargin(playerPiece, new Insets(0,
            playerIndex == 0 ? 10 : -10, 0, playerIndex == 0 ? -10 : 10));
      }

      playerContainer.getChildren().add(playerPiece);

      // Add other player if they're on same tile
      if (bothPlayersOnTile) {
        String otherImagePath = (1 - playerIndex) == 0 ? "/boardPieces/SindreImage.png"
            : "/boardPieces/StianImage.png";
        Image otherImage = new Image(getClass().getResourceAsStream(otherImagePath));
        ImageView otherPiece = new ImageView(otherImage);
        otherPiece.setFitHeight(tileSize * 0.35);
        otherPiece.setFitWidth(tileSize * 0.35);
        otherPiece.setPreserveRatio(true);

        StackPane.setMargin(otherPiece, new Insets(0,
            (1 - playerIndex) == 0 ? 10 : -10, 0, (1 - playerIndex) == 0 ? -10 : 10));

        playerContainer.getChildren().add(otherPiece);
      }

      // Add container to tile
      tile.getChildren().add(playerContainer);
    } catch (Exception e) {
      System.err.println("Error loading player images: " + e.getMessage());
      e.printStackTrace();
      addFallbackPieces(playerIndex, bothPlayersOnTile, playerContainer, tile);
    }
  }

  /**
   * Adds fallback circle pieces when images fail to load
   */
  private void addFallbackPieces(int playerIndex, boolean bothPlayersOnTile,
      StackPane playerContainer, StackPane tile) {
    if (bothPlayersOnTile) {
      // Create circles for both players
      Circle player1Circle = new Circle(tileSize * 0.12);
      player1Circle.setFill(Color.RED);
      player1Circle.setStroke(Color.BLACK);
      player1Circle.setStrokeWidth(1);

      Circle player2Circle = new Circle(tileSize * 0.12);
      player2Circle.setFill(Color.BLUE);
      player2Circle.setStroke(Color.BLACK);
      player2Circle.setStrokeWidth(1);

      StackPane.setMargin(player1Circle, new Insets(0, 10, 0, -10));
      StackPane.setMargin(player2Circle, new Insets(0, -10, 0, 10));

      playerContainer.getChildren().addAll(player1Circle, player2Circle);
    } else {
      // Just create circle for current player
      Circle playerCircle = new Circle(tileSize * 0.12);
      playerCircle.setFill(playerIndex == 0 ? Color.RED : Color.BLUE);
      playerCircle.setStroke(Color.BLACK);
      playerCircle.setStrokeWidth(1);

      playerContainer.getChildren().add(playerCircle);
    }

    tile.getChildren().add(playerContainer);
  }

  /**
   * Creates an ImageView for a player piece for animation
   *
   * @param playerIndex the index of the player
   * @return the created ImageView or null if creation fails
   */
  public ImageView createAnimationPiece(int playerIndex) {
    try {
      String imagePath = playerIndex == 0 ? "/boardPieces/SindreImage.png" : "/boardPieces/StianImage.png";
      Image playerImage = new Image(getClass().getResourceAsStream(imagePath));
      ImageView playerPiece = new ImageView(playerImage);
      playerPiece.setFitHeight(tileSize * 0.35);
      playerPiece.setFitWidth(tileSize * 0.35);
      playerPiece.setPreserveRatio(true);
      return playerPiece;
    } catch (Exception e) {
      System.err.println("Error loading player image: " + e.getMessage());
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