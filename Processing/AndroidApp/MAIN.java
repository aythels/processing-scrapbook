import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class MAIN extends PApplet {

//array used to hold the current touch coordinates
int coords[] = new int[2];

//create page objects
TitleScreen titleScreen = new TitleScreen();
ImageScreen imgScreen = new ImageScreen();

//types of pages
enum Screen {
 TITLE,
 IMAGES,
}

//the current page
Screen currentPage = Screen.TITLE;

public void setup() {
 orientation(PORTRAIT);    
 //fullScreen();
 
 background(255);
 noStroke();
 setupImages();
}

//continuously draws the current page
public void draw() {
 switch (currentPage) {
  case TITLE:
   titleScreen.drawPage();
   break;
  case IMAGES:
   imgScreen.drawPage();
   break;
 }
}

/* mousePressed() is called at the start of the touch.
 * mouseDragged() is called when the touch moves on the screen.
 * mouseReleased() is called when the touch leaves the screen.
 * 
 * mouseReleased() is used with mousePressed() and treated as tap (touch and release) events.
 * Events are sent to the current page, that page chooses what to do with the event.
 * Libraries can be used for unique gestures such as pinching, rotating and multi-touch.
 */
public void mousePressed() {
 coords[0] = mouseX;
 coords[1] = mouseY;
 switch (currentPage) {
  case TITLE:
   titleScreen.onPress();
   break;
  case IMAGES:
   imgScreen.onPress();
   break;
 }
}

public void mouseDragged() {
 switch (currentPage) {
  case TITLE:
   titleScreen.onDrag();
   break;
  case IMAGES:
   imgScreen.onDrag();
   break;
 }
}

public void mouseReleased() {
 if (mouseX == coords[0] && mouseY == coords[1])
  switch (currentPage) {
   case TITLE:
    titleScreen.onRelease();
    break;
   case IMAGES:
    imgScreen.onRelease();
    break;
  }
}
//the filenames of all images to load
String[] imageNames = {
 "1.jpg",
 "2.jpg",
 "3.jpg",
 "4.jpg",
 "5.jpg",
 "6.jpg",
 "7.jpg",
 "8.jpg",
 "9.jpg",
 "10.jpg",
 "11.jpg",
 "12.jpg"
};

//an array collection of all thumbnail and activated images (thumbnail images that have been clicked on).
//Images differ in size but are identified using the same index in each array
PImage[] imageSmall = new PImage[imageNames.length];
PImage[] imageBig = new PImage[imageNames.length];

//the coordinates of all thumbnail images
int[][] imageCoords = new int[imageNames.length][6];

//index of activated image
int activatedIndex = -1;

//load all images into the two arrays and resize them appropriately. Determine the position of thumbnail images on the screen 
public void setupImages() {
 for (int i = 0; i < imageNames.length; i++) {
  String imageName = imageNames[i];
  imageSmall[i] = loadImage(imageName);
  imageBig[i] = loadImage(imageName);
  resizeImage(i);
  recordCoords(i);
 }
}

/* Display the images.
 * If there are no activated images, the method displays a thumbnail list of all images that can be navigated by scrolling
 * in the ImageScreen class. Otherwise, if there is an activated image, the specified image is enlarged and centered.
 */
public void drawImages(int scroll) {
 if (activatedIndex == -1) {
  background(0);
  pushMatrix();
  translate(0, scroll);
  for (int i = 0; i < imageSmall.length; i++)
   image(imageSmall[i], imageCoords[i][0], imageCoords[i][1], imageSmall[i].width, imageSmall[i].height);
  popMatrix();
  imgScreen.locked = false;
 } else {
  fill(0, 20);
  rect(0, 0, width, height);
  image(imageBig[activatedIndex], 0, height / 2 - imageBig[activatedIndex].height / 2, imageBig[activatedIndex].width, imageBig[activatedIndex].height);
  imgScreen.locked = true;
 }
}

//resize the images to thumbnail and activated size
public void resizeImage(int index) {
 imageSmall[index].resize(width / 2, (imageSmall[index].height * width) / (2 * imageSmall[index].width));
 imageBig[index].resize(width, (imageBig[index].height * width) / (imageBig[index].width));
}

/* recordCoords() is a basic image sorting algorithm. It obtains and records the specified image's thumbnail screen 
 * coordinates and stores the information in the imageCoords array which is used to obtain coordinates for drawing.
 */
public void recordCoords(int index) {
 imageCoords[index][0] = (index % 2 == 0) ? (0) : (width / 2); //x1
 try {
  imageCoords[index][1] = imageCoords[index - 2][3]; //y1
 } catch (Exception e) {
  imageCoords[index][1] = 0;
 }
 imageCoords[index][2] = (index % 2 == 0) ? (width / 2) : (width); //x2
 imageCoords[index][3] = imageCoords[index][1] + imageSmall[index].height; //y2
}

//obtains the lowest y position in the list of images
public int getLowestY() {
 return max(imageCoords[imageSmall.length - 1][3], imageCoords[imageSmall.length - 2][3]);
}

/* onImageClick() checks if a tap event has occured within a specified area, and if so, executes corresponding code
 * 
 * When the page is in its thumbnail list state, the method checks if a tap has occured on any of the thumbnail images
 * If such a tap has occured, activatedIndex records the tapped image's index
 *
 * If an image is currently activated, the function checks if a tap has occured outside of the image. If such a tap has
 * occured, the image is deactivated and the page returns to its list state of thumbnail images
 */
public void onImageClick(int scroll) {
 if (activatedIndex == -1)
  for (int i = 0; i < imageCoords.length; i++) {
   int topLeftX = imageCoords[i][0];
   int topLeftY = imageCoords[i][1];
   int bottomRightX = imageCoords[i][2];
   int bottomRightY = imageCoords[i][3];
   if (mouseX > topLeftX && mouseX < bottomRightX && mouseY > topLeftY + scroll && mouseY < bottomRightY + scroll)
    activatedIndex = i;
  }
 else {
  int upperBorder = height / 2 - imageBig[activatedIndex].height / 2;
  int lowerBorder = height / 2 - imageBig[activatedIndex].height / 2 + imageBig[activatedIndex].height;
  if (mouseY < upperBorder || mouseY > lowerBorder)
   activatedIndex = -1;
 }
}
public class ImageScreen {
 boolean locked = false;  //page will not be impacted by events if it is locked
 int yDragStart = 0;  //used with yOffset to handle scrolling events
 int yOffset = 0;  //how much the page has scrolled

 public void drawPage() {
  this.offsetStatus();  //ensures that the page cannot be scrolled out of bounds
  drawImages(yOffset);  //draws the elements that make up this page
 }

  public void offsetStatus() {
  if (yOffset > 0)
   yOffset = 0;
  else if (yOffset < -getLowestY() + height)
   yOffset = -getLowestY() + height;
 }

 public void onPress() {
  if (!locked)
   yDragStart = mouseY - yOffset;
 }

 public void onDrag() {
  if (!locked)
   yOffset = mouseY - yDragStart;
 }

 public void onRelease() {
   onImageClick(yOffset);
 }

}
public class TitleScreen {
 boolean locked = false;

 boolean transitionOut = false;
 float pageOffset = 0;
 boolean transitionText = false;
 float textOffset = 0;

 public void drawPage() {
  if (transitionOut) {
   imgScreen.drawPage();
   fill(0, 255 - (255 * -pageOffset / width));
   rect(0, 0, width, height);
   pageOffset = lerp(pageOffset, -width - 50, 0.1f);
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


 public void drawText() {
  textSize(width / 11);
  textAlign(CENTER, CENTER);
  fill(153, 51, 255);

  pushMatrix();
  translate(0, textOffset);
  text("APPLICATION DEMO", width / 2, height / 2);
  popMatrix();

  if (transitionText) {
   textOffset = lerp(textOffset, -width/2, 0.2f);
   fill(0, -textOffset);
   textSize(width / 15);
   text("Made using the Processing for Android library. Tested using an android emulator.", 0, 0, width, height);
  }
 }

 public void onPress() {
  //
 }


 public void onDrag() {
  //
 }

 public void onRelease() {
  if (mouseX > width / 2 && mouseX < width && mouseY > height - width / 5 && mouseY < height) {
   locked = true;
   transitionOut = true;
  } else if (mouseX > 0 && mouseX < width / 2 && mouseY > height - width / 5 && mouseY < height)
   transitionText = true;
 }

}
  public void settings() {  size(370, 650); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "MAIN" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
