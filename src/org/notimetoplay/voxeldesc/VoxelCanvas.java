package org.notimetoplay.voxeldesc;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import java.util.List;
import java.lang.Math;

public class VoxelCanvas extends Canvas {
	public static final int MAXMSG = 16;
	
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
		camera.f = getHeight();
		
		int midX = getWidth() / 2;
		// Optimize for high/low camera angles.
		int midY = (camera.y > 0) ? 0 : getHeight();
		
		g.translate(midX, midY);
		paintGuides(g);
		
		for (Point3D i: scene.getVoxels().keySet())
			paintVoxel(g, i, scene.getVoxels().get(i));
		
		g.translate(-midX, -midY);

		paintMessages(g);
		g.setColor(scene.getFillColor());
		g.fillOval(getWidth() - 32, 8, 24, 24);
	}
	
	private void paintGuides(final Graphics g) {
		g.setColor(Color.BLACK);
		for (int x = -128; x <= 128; x+=16)
			paintGuideZ(g, x, 0);
		for (int z = -128; z <= 128; z+=16)
			paintGuideX(g, 0, z);
		
		g.setColor(Color.BLUE);
		paintGuideZ(g, 0, 0);
		g.setColor(Color.GREEN);
		paintGuideY(g, 0, 0);
		g.setColor(Color.RED);
		paintGuideX(g, 0, 0);
	}
	
	private void paintGuideX(Graphics g, int y, int z) {
		if (z <= camera.z)
			return;
		
		camera.project(-128, y, z);
		int x1 = (int) Math.floor(camera.px);
		int y1 = (int) Math.floor(camera.py);
		camera.project(127, y, z);
		int x2 = (int) Math.floor(camera.px);
		int y2 = (int) Math.floor(camera.py);
		g.drawLine(x1, -y1, x2, -y2);
	}
	
	private void paintGuideY(Graphics g, int x, int z) {
		if (z <= camera.z)
			return;

		camera.project(x, -128, z);
		int x1 = (int) Math.floor(camera.px);
		int y1 = (int) Math.floor(camera.py);
		camera.project(x, 127, z);
		int x2 = (int) Math.floor(camera.px);
		int y2 = (int) Math.floor(camera.py);
		g.drawLine(x1, -y1, x2, -y2);
	}
	
	private void paintGuideZ(Graphics g, int x, int y) {
		final int z1 = Math.max(-128, (int) camera.z + 1);
		final int z2 = 127;

		camera.project(x, y, z1);
		final int x1 = (int) Math.floor(camera.px);
		final int y1 = (int) Math.floor(camera.py);
		camera.project(x, y, z2);
		final int x2 = (int) Math.floor(camera.px);
		final int y2 = (int) Math.floor(camera.py);
		g.drawLine(x1, -y1, x2, -y2);
	}
	
	private void paintVoxel(Graphics g, Point3D p, Color c) {
		if (p.z <= camera.z)
			return;
		
		camera.project(p.x, p.y, p.z);
		final short x = (short) Math.floor(camera.px);
		final short y = (short) Math.floor(camera.py);
		final short side = (short) Math.ceil(camera.scale);
		g.setColor(c);
		g.fillRect(x, -y, side, side);
		g.setColor(Color.GRAY);
		g.drawRect(x, -y, side, side);
	}
	
	private void paintMessages(final Graphics g) {
		final List<String> lastN;
		
		if (messages == null || messages.size() < 1) {
			return;
		} else if (messages.size() < MAXMSG) {
			lastN = messages;
		} else {
			lastN = messages.subList(
				messages.size() - MAXMSG, messages.size());
		}
		
		for (byte i = 0; i < lastN.size(); i++) {
			if (lastN.get(i).startsWith("Error"))
				g.setColor(Color.RED);
			else
				g.setColor(Color.BLACK);
			g.drawString(lastN.get(i), 16, (i + 1) * 16 + 16);
		}
	}
}
