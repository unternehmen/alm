package alm.world;

import java.util.HashMap;

public class TileKit {
    private static final HashMap < String, Tile > tiles
        = new HashMap < > ();

    public static Tile getTile (String name) {
        Tile tile = (Tile)tiles.get (name);

        if (tile == null) {
            switch (name) {
            case "grass":
                tile = new Tile (false, '"');
                break;
            case "floor":
                tile = new Tile (false, '.');
                break;
            case "wall":
                tile = new Tile (true, '#');
                break;
            case "water":
                tile = new Tile (true, '~');
                break;
            case "tree":
                tile = new Tile (true, '^');
                break;
            case "door":
                tile = new Tile (true, 'O');
                break;
            default:
                throw new IllegalArgumentException ("name != any tile");
            }

            tiles.put (name, tile);
        }

        return tile;
    }
}
