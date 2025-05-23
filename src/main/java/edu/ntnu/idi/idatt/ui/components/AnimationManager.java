package edu.ntnu.idi.idatt.ui.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ntnu.idi.idatt.model.Player;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Manages animations in the Snakes and Ladders game.
 * Responsible for animating player movement between tiles and handling
 * animation completion events.
 */
public class AnimationManager {

  private final int TILE_SIZE;
  private final BorderPane root;
  private final GamePiece gamePiece;
  private final Map<Integer, StackPane> tilesMap;
  private final InfoTable infoTable;
  private int millis = 500;

  /**
   * Creates a new AnimationManager.
   *
   * @param root      The root BorderPane of the game UI
   * @param gamePiece The GamePiece component for creating player pieces
   * @param tilesMap  Map of tile positions to StackPane UI elements
   * @param infoTable The InfoTable for UI updates
   * @param tileSize  The size of each tile
   */
  public AnimationManager(BorderPane root, GamePiece gamePiece,
      Map<Integer, StackPane> tilesMap, InfoTable infoTable, int tileSize) {
    this.root = root;
    this.gamePiece = gamePiece;
    this.tilesMap = tilesMap;
    this.infoTable = infoTable;
    this.TILE_SIZE = tileSize;
  }

  /**
   * Creates a new AnimationManager.
   *
   * @param root      The root BorderPane of the game UI
   * @param gamePiece The GamePiece component for creating player pieces
   * @param tilesMap  Map of tile positions to StackPane UI elements
   * @param infoTable The InfoTable for UI updates
   * @param tileSize  The size of each tile
   */
  public AnimationManager(BorderPane root, GamePiece gamePiece,
      Map<Integer, StackPane> tilesMap, InfoTable infoTable, int tileSize, int millis) {
    this.millis = millis;
    this.root = root;
    this.gamePiece = gamePiece;
    this.tilesMap = tilesMap;
    this.infoTable = infoTable;
    this.TILE_SIZE = tileSize;
  }

  /**
   * Animates player movement from one tile to another.
   * This is purely a UI function that visualizes movement decided by the
   * controller.
   *
   * @param player       The player being moved
   * @param players      All players in the game for checking overlapping
   *                     positions
   * @param fromPosition The starting position
   * @param toPosition   The ending position
   * @param checkVictory Whether to check for victory after animation
   */
  public void animatePlayerMove(Player player, List<Player> players,
      int fromPosition, int toPosition, boolean checkVictory) {
    int playerIndex = players.indexOf(player);

    // Get tiles for animation
    StackPane fromTile = tilesMap.get(fromPosition);
    StackPane toTile = tilesMap.get(toPosition);

    // Remove player from old position
    removePlayerFromTile(fromTile);

    // Check if other players were on the same tile and add them back
    for (Player otherPlayer : players) {
      if (otherPlayer != player && otherPlayer.getTileId() == fromPosition) {
        gamePiece.addPlayerToTile(otherPlayer, fromPosition, fromTile);
      }
    }

    // Create animating piece using GamePiece
    ImageView playerPiece = gamePiece.createAnimationPiece(playerIndex);
    if (playerPiece == null) {
      // Skip animation if piece creation fails, but still handle the move
      gamePiece.addPlayerToTile(player, toPosition, toTile);

      // Re-enable roll button
      infoTable.setRollEnabled(true);
      return;
    }

    // Create animation container
    StackPane animationPane = new StackPane(playerPiece);
    animationPane.setMaxSize(TILE_SIZE * 0.5, TILE_SIZE * 0.5);

    // Get coordinates
    Bounds fromBounds = fromTile.localToScene(fromTile.getBoundsInLocal());
    Bounds toBounds = toTile.localToScene(toTile.getBoundsInLocal());

    // Add to scene
    root.getChildren().add(animationPane);

    // Set start position
    double startX = fromBounds.getMinX() + (fromBounds.getWidth() - playerPiece.getFitWidth()) / 2;
    double startY = fromBounds.getMinY() + (fromBounds.getHeight() - playerPiece.getFitHeight()) / 2;
    animationPane.setTranslateX(startX);
    animationPane.setTranslateY(startY);

    // Set end position
    double endX = toBounds.getMinX() + (toBounds.getWidth() - playerPiece.getFitWidth()) / 2;
    double endY = toBounds.getMinY() + (toBounds.getHeight() - playerPiece.getFitHeight()) / 2;

    // Create animation
    TranslateTransition transition = new TranslateTransition(Duration.millis(millis), animationPane);
    transition.setToX(endX);
    transition.setToY(endY);

    // When animation completes
    transition.setOnFinished(e -> {
      root.getChildren().remove(animationPane);

      // Add player to new position using GamePiece
      gamePiece.addPlayerToTile(player, toPosition, toTile);

    });

    transition.play();
  }

  /**
   * Removes all player pieces from the given tile.
   * The player pieces are removed from the tile to prepare for the next move.
   *
   * @param tile The tile from which player pieces are to be removed
   */
  private void removePlayerFromTile(StackPane tile) {
    // We only want to remove the player pieces (StackPane containers),
    // but keep the background rectangle and label
    List<Node> nodesToRemove = new ArrayList<>();

    for (Node node : tile.getChildren()) {
      // Only remove StackPane containers (which contain player pieces)
      // and preserve everything else (rectangle background and label)
      if (node instanceof StackPane) {
        nodesToRemove.add(node);
      }
    }

    // Remove only the player containers
    tile.getChildren().removeAll(nodesToRemove);
  }
}