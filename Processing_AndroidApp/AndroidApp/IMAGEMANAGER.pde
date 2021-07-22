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
void setupImages() {
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
void drawImages(int scroll) {
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
void resizeImage(int index) {
 imageSmall[index].resize(width / 2, (imageSmall[index].height * width) / (2 * imageSmall[index].width));
 imageBig[index].resize(width, (imageBig[index].height * width) / (imageBig[index].width));
}

/* recordCoords() is a basic image sorting algorithm. It obtains and records the specified image's thumbnail screen 
 * coordinates and stores the information in the imageCoords array which is used to obtain coordinates for drawing.
 */
void recordCoords(int index) {
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
int getLowestY() {
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
void onImageClick(int scroll) {
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
