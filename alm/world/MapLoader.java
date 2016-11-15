package alm.world;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.util.Scanner;
import java.text.ParseException;
import java.util.StringTokenizer;

public class MapLoader {
    // public MapLoader () { }

    public static Map load (String path) throws ParseException, IOException {
        Scanner scanner;
        String line;
        int width = -1, height = -1;
        Map map;

        if (path == null)
            throw new IllegalArgumentException ("path == null");

        if (path.isEmpty ())
            throw new IllegalArgumentException ("path == \"\"");


        BufferedInputStream in =
          new BufferedInputStream (
            Files.newInputStream (
              FileSystems
                .getDefault ()
                .getPath (path)));

        scanner = new Scanner (in);

        if (!scanner.hasNextLine())
            throw new ParseException ("Premature end in " + path, 0);
            
        if (!scanner.nextLine ().equals("[Alm Map]"))
            throw new ParseException ("No header in " + path, 0);

        if (!scanner.hasNextLine())
            throw new ParseException ("Premature end in " + path, 0);
            
        line = scanner.nextLine ();
        while (!line.equals ("%")) { 
            StringTokenizer tokenizer = new StringTokenizer(line, ": ");
            int count = tokenizer.countTokens();
                
            if (count == 2) {
                String field = tokenizer.nextToken();
                    
                switch (field) {
                    case "Width":
                        try {
                            width = Integer.parseInt(
                                      tokenizer.nextToken());
                        } catch (NumberFormatException e) {
                            throw new ParseException ("Width field "
                                                      + "expects a "
                                                      + "whole number.", 0);
                        }
                            
                        if (width < 0)
                            throw new ParseException ("Width field "
                                                      + "expects a "
                                                      + "whole number.", 0);
                            
                        break;
                    case "Height":
                        try {
                            height = Integer.parseInt(
                                       tokenizer.nextToken());
                        } catch (NumberFormatException e) {
                            throw new ParseException ("Height field "
                                                      + "expects a "
                                                      + "whole number.", 0);
                        }
                            
                        if (height < 0)
                            throw new ParseException ("Height field "
                                                      + "expects a "
                                                      + "whole number.", 0);
                            
                        break;
                    default:
                        throw new ParseException("Bad field name \""
                                                 + field + "\"", 0);
                }
            } else {
                throw new ParseException("Bad line format \""
                                         + line + "\"", 0);
            }
                
            if (!scanner.hasNextLine())
                throw new ParseException ("Premature end in " + path, 0);
                
            line = scanner.nextLine ();
        }
            
        if (width == -1)
            throw new ParseException ("No \"Width\" given.", 0);
            
        if (height == -1)
            throw new ParseException ("No \"Height\" given.", 0);
            
        map = new Map(width, height);
            
        /* We have read the "%" line.  Now read the map data... */
        for (int i = 0; i < height; i++) {
            if (!scanner.hasNextLine())
                throw new ParseException ("Not enough rows", 0);
                
            line = scanner.nextLine();
                
            if (line.length() < width)
                throw new ParseException ("Underfull row", 0);
            else if (line.length() > width)
                throw new ParseException ("Overfull row", 0);
                
            for (int j = 0; j < width; j++) {
                Tile tile;
                    
                switch (line.charAt(j)) {
                    case '"':
                        tile = TileKit.getTile("grass");
                        break;
                    case '.':
                        tile = TileKit.getTile("floor");
                        break;
                    case '#':
                        tile = TileKit.getTile("wall");
                        break;
                    case '~':
                        tile = TileKit.getTile("water");
                        break;
                    case '^':
                        tile = TileKit.getTile("tree");
                        break;
                    case 'O':
                        tile = TileKit.getTile("door");
                        break;
                    default:
                        throw new ParseException ("Unknown tile glyph \""
                                                  + line.charAt(j)
                                                  + "\"", 0);
                }
                    
                map.putTileAt (tile, j, i);
            }
        }

        in.close ();

        return map;
    }
}
