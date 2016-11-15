package alm.world;

public class Map {
    private final Tile[] tileMatrix;
    private final int width;
    private final int height;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;

        tileMatrix = new Tile[width * height];

        for (int i = 0; i < tileMatrix.length; i++) {
            tileMatrix[i] = TileKit.getTile("grass");
        }
    }

    public Tile getTileAt(int x, int y) {
        if (x < 0)
            throw new IllegalArgumentException("x < 0");

        if (y < 0)
            throw new IllegalArgumentException("y < 0");

        if (x >= width)
            throw new IllegalArgumentException("x >= width");

        if (y >= height)
            throw new IllegalArgumentException("y >= height");

        return tileMatrix[y * width + x];
    }
    
    public void putTileAt(Tile tile, int x, int y) {
        if (x < 0)
            throw new IllegalArgumentException("x < 0");

        if (y < 0)
            throw new IllegalArgumentException("y < 0");

        if (x >= width)
            throw new IllegalArgumentException("x >= width");

        if (y >= height)
            throw new IllegalArgumentException("y >= height");
        
        if (tile == null)
            throw new IllegalArgumentException("tile == null");
        
        tileMatrix[y * width + x] = tile;
    }
}
