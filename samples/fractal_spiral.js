// Courtesy of Mark Burger, <http://zesago.net/>

function spiral(centerX, centerY, depth) {
    var x;
    var y;
    var i;
    if ( depth >= 3 ) { return; }

    for ( i=0.1; i < 30; i+=0.4 ) {
        scene.color(i*8, 255-(i*5), 255/depth);
        x = 20 * (i/(10*depth)) * Math.cos(i);
        y = 20 * (i/(10*depth)) * Math.sin(i);
        scene.from(centerX + x, centerY + y, i*4);
        scene.sphere(3-(i/10));
        spiral(x,y,depth+1);
    }
}

spiral(0,0,1);
