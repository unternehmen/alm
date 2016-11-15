package alm.world;

public class Tile {
    private final boolean solid;
    private final char icon;

    public Tile (boolean solid, char icon) {
        this.solid = solid;
        this.icon = icon;
    }

    public boolean isSolid () {
        return solid;
    }

    public char getIcon () {
        return icon;
    }
}
