package org.notimetoplay.voxeldesc;

import java.util.TreeMap;
import java.awt.Color;
import java.io.Writer;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;

public class VoxelScene {
	private TreeMap<Point3D, Color> voxels = new TreeMap<Point3D, Color>();
	
	public TreeMap<Point3D, Color> getVoxels() {
		return voxels;
	}

	private Color fillColor = Color.BLACK;
	
	public Color getFillColor() {
		return fillColor;
	}
	
	public void setFillColor(final Color c) {
		if (c == null) throw new NullPointerException(
			"Non-null color expected");
		fillColor = c;
	}
	
	public VoxelScene color(final int r, final int g, final int b) {
		fillColor = new Color(r, g, b);
		return this;
	}
	
	public VoxelScene color(final int rgb) {
		fillColor = new Color(rgb);
		return this;
	}
	
	public VoxelScene color(final Color c) {
		setFillColor(c);
		return this;
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
	
	public VoxelScene fill(final byte wx, final byte wy, final byte wz) {
		mark.x = (byte) (cursor.x + wx - 1);
		mark.y = (byte) (cursor.y + wy - 1);
		mark.z = (byte) (cursor.z + wz - 1);
		fill();
		return this;
	}
	
	public VoxelScene fill() {
		for (byte z = cursor.z; z <= mark.z; z++) {
			for (byte y = cursor.y; y <= mark.y; y++) {
				for (byte x = cursor.x; x <= mark.x; x++) {
					voxels.put(
						new Point3D(x, y, z),
						fillColor);
				}
			}
		}
		return this;
	}
	
	public VoxelScene dot(final byte x, final byte y, final byte z) {
		voxels.put(new Point3D(x, y, z), fillColor);
		return this;
	}
	
	public String toString() {
		return String.format(
			"Scene: %d voxels OK", voxels.size());
	}
	
	public void serialize(final Writer out) throws IOException {
		for (Point3D i: voxels.keySet()) {
			final Color c = voxels.get(i);
			if (c.getAlpha() < 255) {
				out.write(String.format(
					"%d %d %d %d %d %d %d\n",
					i.x, i.y, i.z,
					c.getRed(), c.getGreen(), c.getBlue(),
					c.getAlpha()));
			} else {
				out.write(String.format(
					"%d %d %d %d %d %d\n",
					i.x, i.y, i.z,
					c.getRed(), c.getGreen(), c.getBlue()));
			}
		}
	}
	
	public void deserialize(final Reader in) throws IOException {
		final BufferedReader buffer = new BufferedReader(in);
		String line;
		while ((line = buffer.readLine()) != null) {
			final String[] numbers = line.split("\\s");
			if (numbers.length == 7) {
				final Point3D p = new Point3D(
					Integer.parseInt(numbers[0]),
					Integer.parseInt(numbers[1]),
					Integer.parseInt(numbers[2]));
				final Color c = new Color(
					Integer.parseInt(numbers[3]),
					Integer.parseInt(numbers[4]),
					Integer.parseInt(numbers[5]),
					Integer.parseInt(numbers[6]));
				voxels.put(p, c);
			} else if (numbers.length == 6) {
				final Point3D p = new Point3D(
					Integer.parseInt(numbers[0]),
					Integer.parseInt(numbers[1]),
					Integer.parseInt(numbers[2]));
				final Color c = new Color(
					Integer.parseInt(numbers[3]),
					Integer.parseInt(numbers[4]),
					Integer.parseInt(numbers[5]));
				voxels.put(p, c);
			} else {
				throw new IOException("Short input line");
			}
		}
	}
}
