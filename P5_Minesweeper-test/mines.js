
let game = null;

function setup() {
	createCanvas(displayWidth, displayHeight);	
	
	game = new createGame(5);
}

function draw() {
	background(5, 128, 153);
	strokeWeight(0);
	fill(255);
	
	const pos = drawSquares(game.getData());

	let minDist = Infinity;
	let minSquare = null;
	let minSquareIndex = null;
	
	for (const e of pos) {
		const distance = dist(e[0] + (e[2]/2), e[1] + (e[2]/2), mouseX, mouseY);
		if (distance < minDist) {
			minDist =  distance;
			minSquare = e;
			minSquareIndex = pos.indexOf(e);
		}
	}
	
	if (minSquare[3] == -1) {
		fill(150);
		square(minSquare[0], minSquare[1], minSquare[2]);
	}
	
	if (mouseIsPressed) game.pickSquare(minSquareIndex);
}


const createGame = function createGame(mines) {
	const secretView = [
					0, 0, 0, 0, 0,
					0, 0, 0, 0, 0,
					0, 0, 0, 0, 0,
					0, 0, 0, 0, 0,
					0, 0, 0, 0, 0,
					];
				
	const mainView = [
				-1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, 
				];
			
	(function init() {
		const fieldIndex = [
					00, 01, 02, 03, 04,
					05, 06, 07, 08, 09,
					10, 11, 12 ,13, 14,
					15, 16, 17, 18, 19,
					20, 21, 22, 23, 24,]
	
		let arrayLength = 25;
		for (let i = 0; i < mines; i++) {
			const val = Math.floor(Math.random() * arrayLength);
			secretView[fieldIndex[val]] = 1;

			fieldIndex.splice(val, 1); 
			arrayLength -= 1;			
			
		}
	
	})();
	
	this.pickSquare = function (index) {
		if (mainView[index] == -1) mainView[index] = secretView[index];
	}
	
	this.getData = function () {
		return mainView;
	}
	
}


function drawSquares(data) {
	const pos = [];
	const startX = 30;
	const startY = 30;
	const width = 50;
	const gap = 20;
	
	let mine = 0;
	for (let i = 0; i < 5; i++) {
		for (let j = 0; j < 5; j++) {
			
			const x = startX + (i * (width + gap));
			const y = startY + (j * (width + gap));
			
			switch(data[mine]) {
			  case -1:
				fill(255);
				break;
			  case 0:
				fill(0, 255, 0);
				break;
			  case 1:
				fill(255, 0, 0);
				break;
			}
			
			square(x, y, width);
			pos.push([x, y, width, data[mine]]);
			mine += 1;
		}
	}
	
	return pos;
	
}

const createUI = function () {
	textSize(20);
	text('$ 10,000', 10, 30);
	
	const input = createInput('1', 'number');
	input.position(20, 80);
  
	const select = createSelect();
	select.position(100, 100);
	for (let i = 1; i < 25; i++) select.option(i);
	select.selected(1);

	const button = createButton('Bet');
	button.position(input.x + input.width, 65); 
	
}