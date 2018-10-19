
import com.phidget22.*;
import processing.core.PApplet;
import processing.core.PFont;

public class FidgIT extends PApplet {

    public static void main(String[] args) {
        PApplet.main("FidgIT");
    }

    // Creation of Output objects
    Game game = new Game(1260, 720, 51,51,51);
    LeaderBoard lB = new LeaderBoard(this, game);
    Ministick myBall = new Ministick(this, game,0,0);
    Rotation myArc = new Rotation(this, game);
    Click myClick = new Click(this, game);
    Sound mySound = new Sound(this, game);
    Light myLight = new Light(this, game);

    // Font initialisation
    private PFont myFont;
    private String goText = "Cover Sensor to Start";
    private String instructions = "";


    public void settings(){

        // Define the window size
        size(game.getWidth(),game.getHeight());

        // Makes things look nicer with some Anti-Aliasing
        smooth();
    }

    public void setup(){

        // Initialisation of Inputs and Outputs
        VoltageRatioInput chRota;
        VoltageRatioInput chLin1;
        VoltageRatioInput chLin2;
        DigitalInput click;
        VoltageInput sound;
        VoltageRatioInput light;
        //RCServo servo;

        //Import lobster font
        myFont = createFont("Lobster_1.3.otf", 40);

        // Set serial numbers
        int phidgetNo = 274077;
        //int servoNo = 306007;

        //Initialise all the inputs and outputs
        try {
            chRota = new VoltageRatioInput();
            chLin1 = new VoltageRatioInput();
            chLin2 = new VoltageRatioInput();

            //servo = new RCServo();
            click = new DigitalInput();
            sound = new VoltageInput();
            light = new VoltageRatioInput();

            chRota.setDeviceSerialNumber(phidgetNo);
            chLin1.setDeviceSerialNumber(phidgetNo);
            chLin2.setDeviceSerialNumber(phidgetNo);

            sound.setDeviceSerialNumber(phidgetNo);
            //servo.setDeviceSerialNumber(servoNo);
            click.setDeviceSerialNumber(phidgetNo);
            light.setDeviceSerialNumber(phidgetNo);

            chRota.setChannel(2);
            chLin1.setChannel(3);
            chLin2.setChannel(4);
            sound.setChannel(1);

            //servo.setChannel(0);
            click.setChannel(7);
            light.setChannel(0);

            chRota.open();
            chLin1.open();
            chLin2.open();

            sound.open();
            //servo.open(5000);
            click.open();
            light.open();

            chRota.addVoltageRatioChangeListener(myArc);
            chLin1.addVoltageRatioChangeListener(myBall);
            chLin2.addVoltageRatioChangeListener(myBall);
            click.addStateChangeListener(myClick);
            sound.addVoltageChangeListener(mySound);
            light.addVoltageRatioChangeListener(myLight);



            //servo.setTargetPosition(0);
            //System.out.println(servo.getEngaged());
            //servo.setEngaged(true);

            //servo.setTargetPosition(45);
            //servo.setTargetPosition(90);

        } catch (PhidgetException e) {
            System.out.println(e);
        }

        lB.leaderBoardSetup();
    }

    public void draw() {
        background(game.getBackgroundR(), game.getBackgroundG(), game.getBackgroundB());

        int currentState = game.getGameState();
        game.LEDUpdate();

        fill(255,255,255);
        stroke(255,255,255);

        textSize(32);
        textAlign(CENTER, CENTER);
        textFont(myFont);

        int width = game.getWidth();
        int height = game.getHeight();

        text(goText, width/2, (height/10) * 9);


        if(currentState == 1){
            printTopText();
            printInstructions();
            game.setRed(true);
            goText = "Cover Sensor to Start";
            if(myLight.getSensorReading() < 10 && !game.getTimeOn()){
                clearInstructions();
                goText = "GO!!!";
                game.timerStart();
                game.setRed(false);
                game.randomiseState();
            }
        } else if(currentState == 2){

            printTopText();
            myBall.draw();
            goText = game.getTime();
            if(myBall.isSolved()){
                myBall.newGame();
                game.randomiseState();
            }
        } else if(currentState == 3){

            printTopText();
            myArc.draw();
            goText = game.getTime();
            if(myArc.isSolved()){
                myArc.newGame();
                game.randomiseState();
            }
        } else if(currentState == 4){

            printTopText();
            myClick.draw();
            goText = game.getTime();
            if(myClick.isSolved()){
                myClick.newGame();
                game.randomiseState();
            }
        } else if(currentState == 5){
            printTopText();
            mySound.draw();
            goText = game.getTime();
            if(mySound.isSolved()){
                mySound.newGame();
                game.randomiseState();
            }
        } else if(currentState == 6){
            game.timerStop();
            goText = "";
            game.setRed(true);

            fill(255,255,255);
            stroke(255,255,255);
            textSize(50);
            String newText = ("COMPLETED in " + game.getTime() + " seconds.");
            text(newText, width/2, (height/8));

            lB.printHighscores();

            textAlign(CENTER, CENTER);
            textSize(32);
            fill(255,255,255);

            if(lB.highEnoughForHighScore(game.getTimeInt())){
                text("Press space to enter name, N for new game or Q to Quit", width/2, (height/10) * 8);
            } else {
                text("Score not high enough for top 5, press N for new game or Q to Quit", width/2, (height/10) * 8);
            }

        }
        else if(currentState == 8){
            fill(255, 255, 255);
            text("Please enter your name", width/2, height/4);
            textSize(32);
            textAlign(CENTER, CENTER);
            text("Use cursor left right and up and down, return to finish input", width/2, height/10 * 8);
            textSize(80);

            int i=0;
            for (char c : lB.letters) {
                fill(255, 255, 255);
                if (i== lB.index)
                    fill(255);
                text((lB.letters[i])+"", ((width/2) - 100)+i*65, height/2);
                i++;
            }
        } else if (currentState == 9){

            lB.result=""+ lB.letters[0]+ lB.letters[1]+ lB.letters[2]+ lB.letters[3];
            println(lB.result);

            lB.addNewScore(game.getTimeInt(), lB.result);
            lB.saveScores();
        } else if(currentState == 10) {
            fill(255, 255, 255);
            textSize(50);
            text("Highscores", width / 2, (height / 8));

            lB.printHighscores();

            textSize(32);
            textAlign(CENTER, CENTER);
            fill(255, 255, 255);
            text("Press SPACE for new game or Q to quit", width / 2, (height / 10) * 9);
        }

        else if(currentState == 11){
            newGame();
            game.resetGame();
        }
    }

    public void printTopText(){
        textSize(50);
        text("Welcome to FidgIT", width/2, height/10);
        textAlign(CENTER, TOP);
    }

    public void printInstructions(){
        textSize(32);
        instructions = ("FidgIT is a reaction game, aimed to test your, speed, agility and " +
                "\n most importantly, clapping ability!! " +
                "\n\n Follow the instructions and complete the tasks as fast as possible!! " +
                "\n\n Good Luck");
        text(instructions, width/2, height/3);
        textAlign(CENTER, TOP);
    }

    public void clearInstructions(){
        instructions = "";
    }

    public void newGame(){
        // Leaderboard Initialisation
        lB.letters = new char[4]; // 4 letters
        lB.index=0;                  // which letter is active
        lB.result="";
    }

    public void keyPressed() {

        int currentState = game.getGameState();
        // state tells how the program works:
        if (currentState==6) {

            if (key == ' ') { // Press space to add score
                lB.letters = new char[4];
                for (int i = 0; i< lB.letters.length; i++) {
                    lB.letters[i]='A';
                }
                // add one element to high scores
                if (lB.highEnoughForHighScore(game.getTimeInt())) {
                    game.setGameState(8);
                }
            } else if (key == 'n'){
                game.setGameState(11);
            } else if (key == 'q' || key == 'Q'){
                exit();
            }
        }

        else  if (currentState==8) {

            if (keyCode == UP) {
                if (lB.letters[lB.index] > 64) {
                    lB.letters[lB.index]--;
                    if (lB.letters[lB.index] == 64) {
                        lB.letters[lB.index] = 90;
                    }
                }
            } else if (keyCode == DOWN) {
                if (lB.letters[lB.index] < 91) {
                    lB.letters[lB.index]++;
                    if (lB.letters[lB.index] == 91) {
                        lB.letters[lB.index] = 65;
                    }
                }
            } else if (keyCode == LEFT) {
                lB.index--;
                if (lB.index<0)
                    lB.index=0;
            } else if (keyCode == RIGHT) {
                lB.index++;
                if (lB.index>3)
                    lB.index=3;
            } else if (key == RETURN||key==ENTER) {
                game.setGameState(9);
            }
        } else if (currentState==10){
            if(key == ' '){
                game.setGameState(11);
            }
            if (key == 'q' || key == 'Q'){
                exit();
            }
        }
    }


}




