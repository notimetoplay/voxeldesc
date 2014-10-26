// Courtesy of Mark Burger, <http://zesago.net/>

function drawFence(x1, y1, x2, y2, startHeight, endHeight ) {
    var startX = (x1 < x2)? x1 : x2;
    var startY = (y1 < y2)? y1 : y2;

    var endX = (x1 > x2)? x1 : x2;
    var endY = (y1 > y2)? y1 : y2;

    var x = startX;
    var y = startY;

    var dist = Math.sqrt( Math.pow(startX - endX, 2) + 
        Math.pow(startY - endY, 2) );
    var i = 0;
    var j;

    while ( i <= dist ) {
        x = startX + (i/dist)*(endX - startX);
        y = startY + (i/dist)*(endY - startY);
        for ( j = startHeight; j <= endHeight; j++ ) {
            scene.dot(x, j, y);
        }
        i++;
    }
}


for ( i = -100; i < 100; i+=15) {
    for ( j = -100; j < 100; j+=15) {
        var q = Math.floor( Math.random() * 2 );
        if ( q == 0 ) {
            drawFence(i,j, i, j+15, 0, 10);
        } else if ( q == 1 ) {
            drawFence(i, j, i, j + 3, 0, 10);
            drawFence(i, j + 3, i, j + 8, 8, 10);
            drawFence(i, j + 8, i, j + 15, 0, 10);
        }
        var q = Math.floor( Math.random() * 2 );
        if ( q == 0 ) {
            drawFence(i,j, i+15, j, 0, 10);
        } else if ( q == 1 ) {
            drawFence(i, j, i + 3, j, 0, 10);
            drawFence(i + 3, j, i + 8, j, 8, 10);
            drawFence(i + 8, j, i + 15, j, 0, 10);
        }
    }
}
