package org.notimetoplay.voxeldesc;

public class Point3D implements Comparable<Point3D> {
	public byte x = 0;
	public byte y = 0;
	public byte z = 0;
	
	public Point3D(final int nx, final int ny, final int nz) {
		x = (byte) nx;
		y = (byte) ny;
		z = (byte) nz;
	}
	
	public boolean equals(Object other) {
		try {
			final Point3D o = (Point3D) other;
			return x == o.x && y == o.y && z == o.z;
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	public int hashCode() {
		return z * 65536 + y * 256 + x;
	}
	
	public String toString() {
		return String.format("Point3D(%d, %d, %d)", x, y, z);
	}
	
	public int compareTo(Point3D other) {
		if (z < other.z)
			return 1; // Sort from back to front.
		else if (z > other.z)
			return -1; // Sort from back to front.
		else if (y < other.y)
			return -1;
		else if (y > other.y)
			return 1;
		else if (x < other.x)
			return -1;
		else if (x > other.x)
			return 1;
		else
			return 0;
	}
	
	public boolean isBetween(final Point3D p1, final Point3D p2) {
		return (p1.x <= x && x < p2.x)
			&& (p1.y <= y && y < p2.y)
			&& (p1.z <= z && z < p2.z);
	}
	
	public static Point3D minCoords(final Point3D p1, final Point3D p2) {
		return new Point3D(
			Math.min(p1.x, p2.x),
			Math.min(p1.y, p2.y),
			Math.min(p1.z, p2.z));
	}
	
	public static Point3D maxCoords(final Point3D p1, final Point3D p2) {
		return new Point3D(
			Math.max(p1.x, p2.x),
			Math.max(p1.y, p2.y),
			Math.max(p1.z, p2.z));
	}
}
