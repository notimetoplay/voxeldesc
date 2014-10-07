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
	
	public Camera zoomIn(final float factor) {
		x /= factor;
		y /= factor;
		z /= factor;
		
		return this;
	}
	
	public Camera zoomOut(final float factor) {
		x *= factor;
		y *= factor;
		z *= factor;
		
		return this;
	}
	
	public Camera left(final float by) {
		x -= by;
		return this;
	}
	
	public Camera right(final float by) {
		x += by;
		return this;
	}
	
	public Camera up(final float by) {
		y += by;
		return this;
	}
	
	public Camera down(final float by) {
		y -= by;
		return this;
	}
	
	public Camera back(final float by) {
		z -= by;
		return this;
	}
	
	public Camera fore(final float by) {
		z += by;
		return this;
	}
	
	public Camera center() {
		x = 0;
		y = Math.abs(z) / 2;
		return this;
	}
	
	public String toString() {
		return String.format(
			"Camera(%.2f %.2f %.2f) f=%.2f", x, y, z, f);
	}
}
