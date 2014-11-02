JAVAC = javac

JAR = voxeldesc.jar
MAIN = org.notimetoplay.voxeldesc.VoxelGUI
OBJ = 	org/notimetoplay/voxeldesc/ScriptConsole.class \
	org/notimetoplay/voxeldesc/Point3D.class \
	org/notimetoplay/voxeldesc/VoxelScene.class \
	org/notimetoplay/voxeldesc/Camera.class \
	org/notimetoplay/voxeldesc/VoxelCanvas.class \
	org/notimetoplay/voxeldesc/VoxelGUI.class
DOCS =	app.html camera.html canvas.html console.html example.html \
	features.html index.html objects.html scene.html docs/voxeldesc.css

vpath %.class bin
vpath %.java src
vpath %.html docs

all: $(JAR)

clean:
	rm -rf bin/org $(JAR)

$(JAR): $(OBJ) $(DOCS)
	jar cfe $(JAR) $(MAIN) LICENSE.txt docs -C bin org

%.class: %.java
	$(JAVAC) -d bin -sourcepath src -source 6 -target 6 src/$*.java
