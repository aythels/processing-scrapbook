public class ImageScreen {
 boolean locked = false;  //page will not be impacted by events if it is locked
 int yDragStart = 0;  //used with yOffset to handle scrolling events
 int yOffset = 0;  //how much the page has scrolled

 void drawPage() {
  this.offsetStatus();  //ensures that the page cannot be scrolled out of bounds
  drawImages(yOffset);  //draws the elements that make up this page
 }

  void offsetStatus() {
  if (yOffset > 0)
   yOffset = 0;
  else if (yOffset < -getLowestY() + height)
   yOffset = -getLowestY() + height;
 }

 void onPress() {
  if (!locked)
   yDragStart = mouseY - yOffset;
 }

 void onDrag() {
  if (!locked)
   yOffset = mouseY - yDragStart;
 }

 void onRelease() {
   onImageClick(yOffset);
 }

}
