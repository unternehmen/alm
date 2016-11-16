package alm.game;

import alm.world.Map;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * A Game manages the data of a running game, such as player position.
 */
public class Game {
    private IntegerProperty x, y;
    private IntegerProperty direction;
    private ObjectBinding position;
    private Map map;
    
    /**
     * Instantiate a new Game.
     *
     * @param map the map to start in.
     * @param startX the X coordinate the player should start at. 
     * @param startY the Y coordinate the player should start at.
     * @param startDir the direction the player face at first.
     */
    public Game(Map map, int startX, int startY, int startDir) {
        this.map = map;

        this.x = new SimpleIntegerProperty(startX);
        this.y = new SimpleIntegerProperty(startY);
        this.direction = new SimpleIntegerProperty(startDir);

        position = new ObjectBinding() {
            {
                super.bind(x, y, direction);
            }
            
            @Override
            protected Object computeValue() {
                return new Position(x.get(), y.get(), direction.get());
            }
        };
    }
    
    /**
     * Returns the icon of the tile at a relative position from the player.
     * 
     * @param strafe the leftness or rightness of the tile.
     * @param farness how far the tile is.
     * @return the icon of the tile, or '?' if out of bounds.
     */
    public char getIconAt(int strafe, int farness) {
        int dx, dy, rx, ry, cx, cy;
        
        switch (direction.get ()) {
            case Position.NORTH:
                dx = 0;
                dy = -1;
                rx = 1;
                ry = 0;
                break;
            case Position.WEST:
                dx = -1;
                dy = 0;
                rx = 0;
                ry = -1;
                break;
            case Position.SOUTH:
                dx = 0;
                dy = 1;
                rx = -1;
                ry = 0;
                break;
            default:
                dx = 1;
                dy = 0;
                rx = 0;
                ry = 1;
                break;
        }

        cx = farness * dx + x.get() + (rx * strafe);
        cy = farness * dy + y.get() + (ry * strafe);

        try {
            return map.getTileAt (cx, cy).getIcon ();
        } catch (IllegalArgumentException e) {
            return '?';
        }
    }
    
    /**
     * Attempt to move the player character forward.
     *
     * @return true if successful, otherwise false.
     */
    public boolean goForward() {
        int dx, dy;
        
        switch (direction.get()) {
            case Position.NORTH:
                dx = 0;
                dy = -1;
                break;
            case Position.WEST:
                dx = -1;
                dy = 0;
                break;
            case Position.SOUTH:
                dx = 0;
                dy = 1;
                break;
            default:
                dx = 1;
                dy = 0;
                break;
        }
        
        if (!map.getTileAt(x.get() + dx, y.get() + dy).isSolid()) {
            x.set(x.get() + dx);
            y.set(y.get() + dy);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Turn the player to the left.
     */
    public void turnLeft() {
        switch (direction.get()) {
            case Position.NORTH:
                direction.set(Position.WEST);
                break;
            case Position.WEST:
                direction.set(Position.SOUTH);
                break;
            case Position.SOUTH:
                direction.set(Position.EAST);
                break;
            case Position.EAST:
                direction.set(Position.NORTH);
                break;
        }
    }
    
    /**
     * Turn the player to the right.
     */
    public void turnRight() {
        switch (direction.get()) {
            case Position.NORTH:
                direction.set(Position.EAST);
                break;
            case Position.WEST:
                direction.set(Position.NORTH);
                break;
            case Position.SOUTH:
                direction.set(Position.WEST);
                break;
            case Position.EAST:
                direction.set(Position.SOUTH);
                break;
        }
    }

    public boolean isWithinBounds (int x, int y) {
        return map.isWithinBounds (x, y);
    }

    public ObjectBinding positionProperty () { return position; }
}
