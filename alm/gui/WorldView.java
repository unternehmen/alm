package alm.gui;

import alm.game.Game;
import alm.world.Map;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.PerspectiveCamera;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Box;
import javafx.scene.AmbientLight;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Material;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
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
    private static Material grassMat = null;
    private static Material wallMat = null;
    private static Material doorMat = null;

    public WorldView() {
        super();

        this.game = null;

        /* Initialize Image textures. */
        if (grassMat == null) {
            grassMat = new PhongMaterial ();
            ((PhongMaterial)grassMat)
              .setDiffuseMap (
                new Image (
                  getClass ()
                    .getResource ("../../data/grass.png")
                    .toExternalForm ()));
        }

        if (wallMat == null) {
            wallMat = new PhongMaterial ();
            ((PhongMaterial)wallMat)
              .setDiffuseMap (
                new Image (
                  getClass ()
                    .getResource ("../../data/wall.png")
                    .toExternalForm ()));
        }

        if (doorMat == null) {
            doorMat = new PhongMaterial ();
            ((PhongMaterial)doorMat)
              .setDiffuseMap (
                new Image (
                  getClass ()
                    .getResource ("../../data/door.png")
                    .toExternalForm ()));
        }

        /* Initialize our Group which will hold all visible meshes. */
        solidsGroup = new Group ();

        /* Initialize the SubScene we will use for 3D.
         * Also, disable anti-aliasing so the textures are sharp. */
        subScene = new SubScene ((Parent)solidsGroup, 0, 0, true, SceneAntialiasing.DISABLED);
        subScene.widthProperty().bind(widthProperty());
        subScene.heightProperty().bind(heightProperty());

        /* Set up the 3D camera. */
        PerspectiveCamera camera = new PerspectiveCamera (true);
        camera.setFarClip (100);
        subScene.setCamera (camera);

        /* Set up the lighting. */
        AmbientLight light = new AmbientLight ();
        light.setColor (Color.WHITE);
        solidsGroup.getChildren ().add (light);

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

        solidsGroup.getChildren ().removeIf (o -> o instanceof Box);

        for (int df = 0; df < 7; df++) {
            for (int ds = -1 * (df + 1); ds < df + 2; ds++) {
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
                    box.setMaterial (wallMat);
                } else if (game.getIconAt (ds, df) == 'O') {
                    box.setTranslateY (1);
                    box.setMaterial (doorMat);
                } else if (game.getIconAt (ds, df) == '^') {
                    box.setTranslateY (1);
                    box.setMaterial (darkgreenMat);
                } else if (game.getIconAt (ds, df) == '"') {
                    box.setTranslateY (3);
                    box.setMaterial (grassMat);
                } else if (game.getIconAt (ds, df) == '~') {
                    box.setTranslateY (3.1);
                    box.setMaterial (blueMat);
                }

                solidsGroup.getChildren ().add (box);
            }
        }
    }
}
