ArrayList<Wall> obstacles = new ArrayList<Wall>();
Player sad = new Player(300, 200);

void setup() {
  size(1800, 700); 
  obstacles.add(new Wall(500, 100, 100));
  obstacles.add(new Wall(100, 500, 200));
}

void draw() { 
  sad.update();
  
  noStroke();  
  background(51);
  drawGrid();
  drawPlayer();
  drawObstacles();
  
  stroke(10);
  fill(255);
  line(sad.pos.x, sad.pos.y, sad.target.x, sad.target.y);
  
}

/*
void calculateS() {
  x = lerp(x, v1.x, .05);
  y = lerp(y, v1.y, .05);
}
*/

void mousePressed() {
  sad.setTarget(mouseX, mouseY);
}

void drawObstacles() {
  fill(255, 0, 0);
  for (int i = 0; i < obstacles.size(); i++)
    ellipse(obstacles.get(i).xpos, obstacles.get(i).ypos, obstacles.get(i).radius, obstacles.get(i).radius);
}

void drawPlayer() {
  fill (255);
  ellipse(sad.pos.x, sad.pos.y, 100, 100);
  
  fill(100);
  ellipse(sad.target.x, sad.target.y, 10, 10);
  
}

void drawGrid() {
  for (int x = 0; x < 19; x++) {
    for (int y = 0; y < 8; y++) {
      int roundedX = (int) ((sad.target.x + 99) / 100 ) * 100;
      int roundedY = (int) ((sad.target.y + 99) / 100 ) * 100;
      /*
      if (dist(x * 100, y * 100, sad.target.x, sad.target.y) < 50)
        fill(0, 255, 0);
      else
      */
      fill(255, 255, 255, 100);
      ellipse(x * 100 + sad.target.x - roundedX, y * 100 + sad.target.y - roundedY, 100, 100);
    }
  }
}
