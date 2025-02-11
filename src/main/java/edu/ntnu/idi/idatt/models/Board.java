package edu.ntnu.idi.idatt.models;

import java.util.HashMap;
import java.util.Map;

public class Board {
  private Map<Integer, Tile> tiles = new HashMap<>();

  public Board() {
    initializeBoard();
  }

  public void initializeBoard() {
    tiles = java.util.stream.IntStream.rangeClosed(1, 90)
        .boxed()
        .collect(java.util.stream.Collectors.toMap(i -> i, Tile::new));
    linkTiles();
  }

  private void linkTiles() {
    tiles.values().forEach(currentTile -> {
      int tileId = currentTile.getTile();
      java.util.stream.IntStream.rangeClosed(1, 12)
          .filter(j -> tileId + j <= 90)
          .forEach(j -> currentTile.setNextTile(tiles.get(tileId + j)));
    });
  }
}