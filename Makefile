JAVAC = javac

JAR = voxeldesc.jar
MAIN = org.notimetoplay.voxeldesc.VoxelGUI
OBJ = 	org/notimetoplay/voxeldesc/ScriptConsole.class \
	org/notimetoplay/voxeldesc/Point3D.class \
	org/notimetoplay/voxeldesc/VoxelScene.class \
	org/notimetoplay/voxeldesc/Camera.class \
	org/notimetoplay/voxeldesc/VoxelCanvas.class \
	org/notimetoplay/voxeldesc/VoxelGUI.class

vpath %.class bin
vpath %.java src

all: $(JAR)

clean:
	rm -rf bin/org $(JAR)

$(JAR): $(OBJ)
	jar cfe $(JAR) $(MAIN) LICENSE.txt docs -C bin org

%.class: %.java
	$(JAVAC) -d bin -sourcepath src src/$*.java
