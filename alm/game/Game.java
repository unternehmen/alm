package alm.game;

import alm.world.Map;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;

public class Game {
    private IntegerProperty x, y;
    private IntegerProperty direction;
    private ObjectBinding position;
    private Map map;
    
    public Game() {
        position = new ObjectBinding() {
            {
                super.bind(x, y);
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
     * @return 
     */
    public String getIconAt(int strafe, int farness) {
        
    }
    
    public void goForward() {
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
        }
    }
    
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
}
