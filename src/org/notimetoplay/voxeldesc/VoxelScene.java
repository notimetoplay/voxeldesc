package org.notimetoplay.voxeldesc;

import java.util.TreeMap;
import java.awt.Color;

public class VoxelScene {
	private TreeMap<Point3D, Color> voxels = new TreeMap<Point3D, Color>();
	
	public TreeMap<Point3D, Color> getVoxels() {
		return voxels;
	}
}
