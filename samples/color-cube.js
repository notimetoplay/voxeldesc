camera.moveTo(10, 30, -40);
canvas.repaint();
for (x = 0; x <= 20; x += 2) {
	for (y = 0; y <= 20; y += 2) {
		for (z = 0; z <= 20; z += 2) {
			scene.color(x * 10, y * 10, z * 10).dot(x, y, z);
		}
	}
	canvas.repaint();
}
