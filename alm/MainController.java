/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alm;

import alm.world.Map;
import alm.world.MapLoader;
import alm.gui.WorldView;
import alm.game.Game;
import alm.game.Position;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;

/**
 * FXML Controller class
 *
 * @author murphy249
 */
public class MainController implements Initializable {
    private Game game;

    @FXML
    private WorldView worldView;

    @FXML
    private void handleQuit() {
        Platform.exit();
    }

    @FXML
    private void handleForward() {
        if (!game.goForward ()) {
            System.out.println ("Bump!");
        }
    }

    @FXML
    private void handleLeft() {
        System.out.println ("Left");
        game.turnLeft ();
    }

    @FXML
    private void handleRight() {
        System.out.println ("Right");
        game.turnRight ();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Map map;

        try {
            map = MapLoader.load("data/town.map");

            if (map == null) {
                System.out.println("Map weren't loaded!!!");
            } else if (map.getTileAt(0, 0).isSolid()) {
                System.out.println("Is solid.");
            } else {
                System.out.println("Is NOT solid.");
            }

            game = new Game (map, 1, 2, Position.EAST);

            worldView.setGame (game);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    
}
