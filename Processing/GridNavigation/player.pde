public class Player {
  PVector pos = new PVector();
  PVector target = new PVector();
  PVector movement = new PVector();
 
  public Player(float a, float b) { 
    this.pos.set(a, b);
  }
  
  void setTarget(float a, float b) {
    this.target.set(a, b);
    movement = target.copy().sub(pos).setMag(10);
  }
  
  void update() {
    if (pos.dist(target) > 5)
      this.pos.add(movement);
  }

}
