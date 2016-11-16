JAVA  := /home/cam/Downloads/jdk1.8.0_111/bin/java
JAVAC := /home/cam/Downloads/jdk1.8.0_111/bin/javac

SRC := ./alm/game/Game.java ./alm/game/Position.java ./alm/world/Map.java ./alm/world/Tile.java ./alm/world/MapLoader.java ./alm/world/TileKit.java ./alm/gui/WorldView.java ./alm/Main.java ./alm/MainController.java
OBJ := $(patsubst %.java,%.class,$(SRC))
MAIN := alm.Main

all: $(OBJ)

$(OBJ): $(SRC)
	$(JAVAC) $^

run:
	$(JAVA) $(MAIN)

clean:
	rm -f $(OBJ)

.PHONY: all clean run
