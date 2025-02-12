package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.controller.GameController;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        GameController gameController = new GameController();
        gameController.initializeLadderGame();
        gameController.playLadderGame();
    }
}
