package alm.gui;

import alm.game.Game;
import alm.world.Map;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.PerspectiveCamera;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Box;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Material;
import javafx.scene.paint.Color;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class WorldView extends Pane {
    private Game game;
    private SubScene subScene;
    private Group solidsGroup;
    private final Material redMat =
        new PhongMaterial (Color.FIREBRICK);
    private final Material whiteMat =
        new PhongMaterial (Color.WHITE);
    private final Material yellowMat =
        new PhongMaterial (Color.YELLOW);
    private final Material darkgreenMat =
        new PhongMaterial (Color.DARKGREEN);
    private final Material greenMat =
        new PhongMaterial (Color.GREEN);
    private final Material blueMat =
        new PhongMaterial (Color.BLUE);

    public WorldView() {
        super();

        this.game = null;

        /* Initialize our Group which will hold all visible meshes. */
        solidsGroup = new Group ();

        /* Initialize the SubScene we will use for 3D. */
        subScene = new SubScene ((Parent)solidsGroup, 0, 0, true, null);
        subScene.widthProperty().bind(widthProperty());
        subScene.heightProperty().bind(heightProperty());

        /* Set up the 3D camera. */
        PerspectiveCamera camera = new PerspectiveCamera (true);
        camera.setFarClip (100);
        subScene.setCamera (camera);

        /* Finally, put the SubScene into this WorldView. */
        getChildren().add (subScene);
    }

    public void setGame (Game game) {
        /* Remove old listeners if necessary. */
        if (this.game != null)
            this
              .game
              .positionProperty ()
              .removeListener ((obs, old, neu) -> {
                updateMeshes ();
              });

        /* Set the game. */
        this.game = game;

        /* Update the meshes whenever the player moves. */
        game
          .positionProperty ()
          .addListener ((obs, old, neu) -> {
            updateMeshes ();
          });

        /* Update the meshes now so we have a view of the world. */
        updateMeshes ();
    }

    private void updateMeshes() {
        if (game == null)
            return;

        System.out.println ("Updating childs.");

        solidsGroup.getChildren ().removeIf (o -> o instanceof Object);

        for (int df = 0; df < 7; df++) {
            for (int ds = -1; ds < 2; ds++) {
                if (game.getIconAt (ds, df) == '?')
                    continue;

                Box box = new Box (4, 4, 4);

                box.setTranslateZ ((df * 4) + 2);
                box.setTranslateX (ds * 4);

                if (game.getIconAt (ds, df) == '.') {
                    box.setTranslateY (3);
                    box.setMaterial (whiteMat);
                } else if (game.getIconAt (ds, df) == '#') {
                    box.setTranslateY (1);
                    box.setMaterial (redMat);
                } else if (game.getIconAt (ds, df) == 'O') {
                    box.setTranslateY (1);
                    box.setMaterial (yellowMat);
                } else if (game.getIconAt (ds, df) == '^') {
                    box.setTranslateY (1);
                    box.setMaterial (darkgreenMat);
                } else if (game.getIconAt (ds, df) == '"') {
                    box.setTranslateY (3);
                    box.setMaterial (greenMat);
                } else if (game.getIconAt (ds, df) == '~') {
                    box.setTranslateY (3);
                    box.setMaterial (blueMat);
                }

                solidsGroup.getChildren ().add (box);
            }
        }
    }
}
