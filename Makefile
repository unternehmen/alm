JAVA  := /home/cam/Downloads/jdk1.8.0_111/bin/java
JAVAC := /home/cam/Downloads/jdk1.8.0_111/bin/javac

SRC := alm/Main.java alm/world/Tile.java alm/world/TileKit.java alm/world/Map.java alm/world/MapLoader.java
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
