package org.notimetoplay.voxeldesc;

public class Point3D implements Comparable<Point3D> {
	public byte x = 0;
	public byte y = 0;
	public byte z = 0;
	
	public Point3D(final byte nx, final byte ny, final byte nz) {
		x = nx;
		y = ny;
		z = nz;
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
}
