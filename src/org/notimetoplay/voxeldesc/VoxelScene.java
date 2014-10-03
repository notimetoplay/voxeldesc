package org.notimetoplay.voxeldesc;

import java.util.TreeMap;
import java.awt.Color;

public class VoxelScene {
	private TreeMap<Point3D, Color> voxels = new TreeMap<Point3D, Color>();
	
	public TreeMap<Point3D, Color> getVoxels() {
		return voxels;
	}

	private Color drawingColor = Color.BLACK;
	
	public Color getDrawingColor() {
		return drawingColor;
	}
	
	public void setDrawingColor(final Color c) {
		if (c == null) throw new NullPointerException(
			"Non-null color expected");
		drawingColor = c;
	}
	
	private final Point3D cursor = new Point3D(0, 0, 0);
	
	public VoxelScene from(final byte x, final byte y, final byte z) {
		cursor.x = x;
		cursor.y = y;
		cursor.z = z;
		
		return this;
	}
	
	private final Point3D mark = new Point3D(0, 0, 0);
	
	public VoxelScene to(final byte x, final byte y, final byte z) {
		mark.x = x;
		mark.y = y;
		mark.z = z;
		
		return this;
	}
	
	public VoxelScene box(final byte wx, final byte wy, final byte wz) {
		mark.x = (byte) (cursor.x + wx);
		mark.y = (byte) (cursor.y + wy);
		mark.z = (byte) (cursor.z + wz);
		box();
		return this;
	}
	
	public VoxelScene box() {
		for (byte z = cursor.z; z <= mark.z; z++) {
			for (byte y = cursor.y; y <= mark.y; y++) {
				for (byte x = cursor.x; x <= mark.x; x++) {
					voxels.put(
						new Point3D(x, y, z),
						drawingColor);
				}
			}
		}
		return this;
	}
}
