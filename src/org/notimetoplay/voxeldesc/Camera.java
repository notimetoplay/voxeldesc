package org.notimetoplay.voxeldesc;

public class Camera {
	public float x;
	public float y;
	public float z;
	public float f;
	
	public float px;
	public float py;
	public float scale;
	
	public Camera(float nx, float ny, float nz, float nf) {
		x = nx;
		y = ny;
		z = nz;
		f = nf;
	}
	
	public void project(float nx, float ny, float nz) {
		nx -= x;
		ny -= y;
		nz -= z;

		scale = f / nz;
		px = nx * scale;
		py = ny * scale;
	}
	
	// Convenience methods (for the scripting engine).
	
	public Camera moveTo(final float nx, final float ny, final float nz) {
		x = nx;
		y = ny;
		z = nz;
		return this;
	}
	
	public Camera moveBy(final float nx, final float ny, final float nz) {
		x += nx;
		y += ny;
		z += nz;
		return this;
	}
}
