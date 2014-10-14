package org.notimetoplay.voxeldesc;

import java.util.TreeMap;
import java.util.HashSet;
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
	
	private boolean modified = false;
	
	public boolean isModified() { return modified; }
	public void setModified(boolean state) { modified = state; }
	
	public VoxelScene color(final int r, final int g, final int b) {
		fillColor = new Color(r, g, b);
		return this;
	}
	
	public VoxelScene color(int r, int g, int b, int a) {
		fillColor = new Color(r, g, b, a);
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
	
	public Point3D getCursor() { return cursor; }
	
	public VoxelScene from(final byte x, final byte y, final byte z) {
		cursor.x = x;
		cursor.y = y;
		cursor.z = z;
		
		return this;
	}
	
	private final Point3D mark = new Point3D(0, 0, 0);
	
	private Point3D getMark() { return mark; }
	
	public VoxelScene to(final byte x, final byte y, final byte z) {
		mark.x = x;
		mark.y = y;
		mark.z = z;
		
		return this;
	}
	
	public VoxelScene fill(final int wx, final int wy, final int wz) {
		mark.x = (byte) (cursor.x + wx);
		mark.y = (byte) (cursor.y + wy);
		mark.z = (byte) (cursor.z + wz);

		return fill();
	}
	
	public VoxelScene fill() {
		final Point3D min = Point3D.minCoords(cursor, mark);
		final Point3D max = Point3D.maxCoords(cursor, mark);
		for (byte z = min.z; z < max.z; z++) {
			for (byte y = min.y; y < max.y; y++) {
				for (byte x = min.x; x < max.x; x++) {
					voxels.put(
						new Point3D(x, y, z),
						fillColor);
				}
			}
		}
		modified = true;
		return this;
	}
	
	public VoxelScene sphere(final byte radius) {
		final Point3D min = new Point3D(
			cursor.x - radius,
			cursor.y - radius,
			cursor.z - radius);
		final Point3D max = new Point3D(
			cursor.x + radius,
			cursor.y + radius,
			cursor.z + radius);
		for (byte z = min.z; z <= max.z; z++) {
			for (byte y = min.y; y <= max.y; y++) {
				for (byte x = min.x; x <= max.x; x++) {
					final int a = Math.abs(cursor.x - x);
					final int b = Math.abs(cursor.y - y);
					final int c = Math.abs(cursor.z - z);
					
					final double dist =
						Math.sqrt(a*a + b*b + c*c);
					if (dist <= radius)
						voxels.put(
							new Point3D(x, y, z),
							fillColor);
				}
			}
		}
		modified = true;
		return this;
	}
	
	private TreeMap<Point3D, Color> clipboard =
		new TreeMap<Point3D, Color>();
		
	public TreeMap<Point3D, Color> getClipboard() { return clipboard; }
	
	public VoxelScene cut(final int wx, final int wy, final int wz) {
		mark.x = (byte) (cursor.x + wx);
		mark.y = (byte) (cursor.y + wy);
		mark.z = (byte) (cursor.z + wz);

		return cut();
	}
	
	public VoxelScene cut() {
		final Point3D min = Point3D.minCoords(cursor, mark);
		final Point3D max = Point3D.maxCoords(cursor, mark);
		final HashSet<Point3D> selection = new HashSet<Point3D>();
		clipboard.clear();
		for (Point3D i: voxels.keySet()) {
			if (i.isBetween(min, max)) {
				final Point3D rel = new Point3D(
					i.x - min.x, i.y - min.y, i.z - min.z);
				clipboard.put(rel, voxels.get(i));
				selection.add(i);
			}
		}
		for (Point3D i: selection)
			voxels.remove(i);
		modified = true;
		return this;
	}
	
	public VoxelScene copy(final int wx, final int wy, final int wz) {
		mark.x = (byte) (cursor.x + wx);
		mark.y = (byte) (cursor.y + wy);
		mark.z = (byte) (cursor.z + wz);

		return copy();
	}
	
	public VoxelScene copy() {
		final Point3D min = Point3D.minCoords(cursor, mark);
		final Point3D max = Point3D.maxCoords(cursor, mark);
		clipboard.clear();
		for (Point3D i: voxels.keySet()) {
			if (i.isBetween(min, max)) {
				final Point3D rel = new Point3D(
					i.x - min.x, i.y - min.y, i.z - min.z);
				clipboard.put(rel, voxels.get(i));
			}
		}
		return this;
	}
	
	public VoxelScene paste(final byte x, final byte y, final byte z) {
		cursor.x = x;
		cursor.y = y;
		cursor.z = z;
		
		return paste();
	}
	
	public VoxelScene paste() {
		for (Point3D i: clipboard.keySet()) {
			final Point3D rel = new Point3D(
				i.x + cursor.x,
				i.y + cursor.y,
				i.z + cursor.z);
			voxels.put(rel, clipboard.get(i));
		}
		modified = true;
		return this;
	}
	
	public VoxelScene dot(final byte x, final byte y, final byte z) {
		voxels.put(new Point3D(x, y, z), fillColor);
		modified = true;
		return this;
	}
	
	public VoxelScene hole(final byte x, final byte y, final byte z) {
		voxels.remove(new Point3D(x, y, z));
		modified = true;
		return this;
	}
	
	public VoxelScene up(final byte steps) {
		fill(1, steps, 1);
		cursor.y = (byte) (mark.y - 1);
		return this;
	}
	
	public VoxelScene down(final byte steps) {
		fill(1, -steps + 1, 1);
		cursor.y = mark.y;
		return this;
	}
	
	public VoxelScene left(final byte steps) {
		fill(-steps + 1, 1, 1);
		cursor.x = mark.x;
		return this;
	}
	
	public VoxelScene right(final byte steps) {
		fill(steps, 1, 1);
		cursor.x = (byte) (mark.x - 1);
		return this;
	}
	
	public VoxelScene back(final byte steps) {
		fill(1, 1, -steps + 1);
		cursor.z = mark.z;
		return this;
	}
	
	public VoxelScene fore(final byte steps) {
		fill(1, 1, steps);
		cursor.z = (byte) (mark.z - 1);
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
