public class TitleScreen {
 boolean locked = false;

 boolean transitionOut = false;
 float pageOffset = 0;
 boolean transitionText = false;
 float textOffset = 0;

 void drawPage() {
  if (transitionOut) {
   imgScreen.drawPage();
   fill(0, 255 - (255 * -pageOffset / width));
   rect(0, 0, width, height);
   pageOffset = lerp(pageOffset, -width - 50, 0.1);
   if (pageOffset < -width)
    currentPage = Screen.IMAGES;
  }

  pushMatrix();
  translate(pageOffset, 0);

  //background
  fill(255);
  rect(0, 0, width, height);

  //text
  drawText();

  //button 1
  textSize(width / 11);
  textAlign(CENTER, CENTER);
  fill(255, 87, 51);
  rect(0, height, width / 2, -width / 5);
  fill(255);
  text("INFO", width / 4, height - width / 10);

  //button 2
  fill(118, 215, 196);
  rect(width / 2, height, width / 2, -width / 5);
  fill(255);
  text("IMAGES", width / 4 * 3, height - width / 10);

  popMatrix();

 }


 void drawText() {
  textSize(width / 11);
  textAlign(CENTER, CENTER);
  fill(153, 51, 255);

  pushMatrix();
  translate(0, textOffset);
  text("APPLICATION DEMO", width / 2, height / 2);
  popMatrix();

  if (transitionText) {
   textOffset = lerp(textOffset, -width/2, 0.2);
   fill(0, -textOffset);
   textSize(width / 15);
   text("Made using the Processing for Android library. Tested using an android emulator.", 0, 0, width, height);
  }
 }

 void onPress() {
  //
 }


 void onDrag() {
  //
 }

 void onRelease() {
  if (mouseX > width / 2 && mouseX < width && mouseY > height - width / 5 && mouseY < height) {
   locked = true;
   transitionOut = true;
  } else if (mouseX > 0 && mouseX < width / 2 && mouseY > height - width / 5 && mouseY < height)
   transitionText = true;
 }

}
