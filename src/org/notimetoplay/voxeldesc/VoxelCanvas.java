package org.notimetoplay.voxeldesc;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import java.util.List;

public class VoxelCanvas extends Canvas {
	private Color drawingColor = Color.BLACK;
	
	public Color getDrawingColor() {
		return drawingColor;
	}
	
	public void setDrawingColor(final Color c) {
		if (c == null) throw new NullPointerException(
			"Non-null color expected");
		drawingColor = c;
	}
	
	private VoxelScene scene;
	private Camera camera;
	
	public VoxelScene getScene() {
		return scene;
	}
	
	public void setScene(final VoxelScene s) {
		if (s == null) throw new NullPointerException(
			"Non-null scene expected");
		scene = s;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public void setCamera(final Camera c) {
		if (c == null) throw new NullPointerException(
			"Non-null camera expected");
		camera = c;
	}
	
	public VoxelCanvas(final VoxelScene s, final Camera c) {
		setScene(s);
		setCamera(c);
	}

	private List<String> messages = null;
	public List<String> getMessages() { return messages; }
	public void setMessages(List<String> m) { messages = m; }
	
	public void paint(Graphics g) {
		paintGrid(g);
		
		// TODO: Paint the scene proper.
		
		paintMessages(g);
		
		g.setColor(drawingColor);
		g.fillOval(getWidth() - 32, 8, 24, 24);
	}
	
	private void paintGrid(Graphics g) {
		
	}
	
	private void paintMessages(final Graphics g) {
		final List<String> last10;
		
		if (messages == null || messages.size() < 1) {
			return;
		} else if (messages.size() < 10) {
			last10 = messages;
		} else {
			last10 = messages.subList(
				messages.size() - 10, messages.size());
		}
		
		for (byte i = 0; i < last10.size(); i++) {
			g.drawString(last10.get(i), 16, (i + 1) * 16);
		}
	}
}
