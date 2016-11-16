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
        game.goForward ();
    }

    @FXML
    private void handleLeft() {
        game.turnLeft ();
    }

    @FXML
    private void handleRight() {
        game.turnRight ();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Map map;

        try {
            map = MapLoader.load(getClass()
                                   .getClassLoader ()
                                   .getResource ("data/town.map"));

            game = new Game (map, 1, 2, Position.EAST);

            worldView.setGame (game);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}
