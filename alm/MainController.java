/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alm;

import alm.world.Map;
import alm.world.MapLoader;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author murphy249
 */
public class MainController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Map map;

        try {
            map = MapLoader.load("C:\\\\Users\\murphy249\\Downloads\\alm\\alm\\data\\town.map");

            if (map == null) {
                System.out.println("Map weren't loaded!!!");
            } else if (map.getTileAt(0, 0).isSolid()) {
                System.out.println("Is solid.");
            } else {
                System.out.println("Is NOT solid.");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    
}
