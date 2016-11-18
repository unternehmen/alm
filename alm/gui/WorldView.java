package alm.gui;

import alm.game.Game;
import alm.game.Position;
import alm.world.Map;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.PerspectiveCamera;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Box;
import javafx.scene.AmbientLight;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Material;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.binding.ObjectBinding;

public class WorldView extends StackPane {
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
    private static Material floorMat = null;
    private ImageView skyboxView;
    private ImageView overlay;

    public WorldView() {
        super();

        this.game = null;

        /* Initialize Image textures. */
        if (grassMat == null) {
            grassMat = new PhongMaterial ();
            ((PhongMaterial)grassMat)
              .setDiffuseMap (
                new Image (
                  getClass ().getClassLoader ()
                    .getResource ("data/grass_big.png")
                    .toExternalForm ()));
        }

        if (wallMat == null) {
            wallMat = new PhongMaterial ();
            ((PhongMaterial)wallMat)
              .setDiffuseMap (
                new Image (
                  getClass ().getClassLoader ()
                    .getResource ("data/wall_big.png")
                    .toExternalForm ()));
        }

        if (doorMat == null) {
            doorMat = new PhongMaterial ();
            ((PhongMaterial)doorMat)
              .setDiffuseMap (
                new Image (
                  getClass ().getClassLoader ()
                    .getResource ("data/door_big.png")
                    .toExternalForm ()));
        }

        if (floorMat == null) {
            floorMat = new PhongMaterial ();
            ((PhongMaterial)floorMat)
              .setDiffuseMap (
                new Image (
                  getClass ().getClassLoader ()
                    .getResource ("data/floor_big.png")
                    .toExternalForm ()));
        }

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

        /* Set up the lighting. */
        AmbientLight light = new AmbientLight ();
        light.setColor (Color.WHITE);
        solidsGroup.getChildren ().add (light);

        /* Make the backdrop. */
        skyboxView = new ImageView ();
        skyboxView.setImage (
          new Image (
            getClass ().getClassLoader ()
              .getResource ("data/sky.png")
              .toExternalForm ()));
        Rectangle2D r = new Rectangle2D (0, 0, 400, 400);
        skyboxView.setViewport (r);
        skyboxView.fitWidthProperty ().bind (widthProperty ());
        skyboxView.fitHeightProperty ().bind (heightProperty ());
        
        /* Make the overlay for being in buildings. */
        overlay = new ImageView ();
        overlay.fitWidthProperty ().bind (widthProperty ());
        overlay.fitHeightProperty ().bind (heightProperty ());

        /* Finally, put the SubScene into this WorldView. */
        getChildren().addAll (skyboxView, subScene, overlay);
    }
    
    public void startOverlay (String name) {
        if (name.equals ("tavern")) {
            overlay.setImage (
              new Image (
                getClass ().getClassLoader ()
                  .getResource ("data/tavern.png")
                  .toExternalForm ()));
        }
    }
    
    public void stopOverlay () {
        overlay.setImage (null);
    }

    public void setGame (Game game) {
        /* Remove old listeners if necessary. */
        if (this.game != null) {
            this
              .game
              .positionProperty ()
              .removeListener ((obs, old, neu) -> {
                updateMeshes ();
              });
            
            game.overlayNameProperty ().removeListener ((obs, old, neu) -> {
                if (neu.isEmpty ()) stopOverlay ();
                else startOverlay (neu);
            });
        }

        /* Set the game. */
        this.game = game;
        
        game.overlayNameProperty ().addListener ((obs, old, neu) -> {
            if (neu.isEmpty ()) stopOverlay ();
            else startOverlay (neu);
        });

        /* Update the meshes whenever the player moves. */
        game
          .positionProperty ()
          .addListener ((obs, old, neu) -> {
            updateMeshes ();
          });

        game.directionProperty ().addListener ((obs, old, neu) -> {
            int xoff;

            switch (neu.intValue ()) {
            case Position.EAST:
                xoff = 400;
                break;
            case Position.WEST:
                xoff = 800;
                break;
            case Position.NORTH:
                xoff = 1200;
                break;
            default:
                xoff = 0;
                break;
            }

            Rectangle2D r = new Rectangle2D (xoff, 0, 400, 400);
            skyboxView.setViewport (r);
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
                    box.setMaterial (floorMat);
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
