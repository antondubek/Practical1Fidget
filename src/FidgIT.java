
import com.phidget22.*;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * Main Game Class containing settings and setup
 */
public class FidgIT extends PApplet {

    /**
     * Needed to get Processing working with intellij
     * @param args
     */
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


    /**
     * Settings method, creates the window and does some smoothing
     */
    public void settings(){

        // Define the window size
        size(game.getWidth(),game.getHeight());

        // Makes things look nicer with some Anti-Aliasing
        smooth();
    }

    /**
     * Main setup class, contains setup of all hardware inputs and outputs
     */
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

        //Initialise all the inputs and outputs
        try {
            chRota = new VoltageRatioInput();
            chLin1 = new VoltageRatioInput();
            chLin2 = new VoltageRatioInput();

            click = new DigitalInput();
            sound = new VoltageInput();
            light = new VoltageRatioInput();

            chRota.setDeviceSerialNumber(phidgetNo);
            chLin1.setDeviceSerialNumber(phidgetNo);
            chLin2.setDeviceSerialNumber(phidgetNo);

            sound.setDeviceSerialNumber(phidgetNo);
            click.setDeviceSerialNumber(phidgetNo);
            light.setDeviceSerialNumber(phidgetNo);

            chRota.setChannel(2);
            chLin1.setChannel(3);
            chLin2.setChannel(4);
            sound.setChannel(1);

            click.setChannel(7);
            light.setChannel(0);

            chRota.open();
            chLin1.open();
            chLin2.open();

            sound.open();
            click.open();
            light.open();

            chRota.addVoltageRatioChangeListener(myArc);
            chLin1.addVoltageRatioChangeListener(myBall);
            chLin2.addVoltageRatioChangeListener(myBall);
            click.addStateChangeListener(myClick);
            sound.addVoltageChangeListener(mySound);
            light.addVoltageRatioChangeListener(myLight);

        } catch (PhidgetException e) {
            System.out.println(e);
        }

        lB.leaderBoardSetup();
    }

    /**
     * Main draw method, sets background and contains all various game states
     */
    public void draw() {
        //Set background color
        background(game.getBackgroundR(), game.getBackgroundG(), game.getBackgroundB());

        // Set the initial LED state
        int currentState = game.getGameState();
        game.LEDUpdate();

        fill(255,255,255);
        stroke(255,255,255);

        textSize(32);
        textAlign(CENTER, CENTER);
        textFont(myFont);

        int width = game.getWidth();
        int height = game.getHeight();

        // Display text at the top
        text(goText, width/2, (height/10) * 9);


        if(currentState == 1){ // Welcome screen
            // Print instructions and intro text
            printTopText();
            printInstructions();
            game.setRed(true);
            goText = "Cover Sensor to Start";

            // If the light sensor is covered
            if(myLight.getSensorReading() < 10 && !game.getTimeOn()){
                clearInstructions();
                goText = "GO!!!";
                game.timerStart();
                game.setRed(false);
                game.randomiseState();
            }
        } else if(currentState == 2){ // Flick it
            // Reprint top text
            printTopText();
            // Run the class draw method
            myBall.draw();
            // Update the timer at the bottom
            goText = game.getTime();
            // When the part is solved
            if(myBall.isSolved()){
                //reset the game
                myBall.newGame();
                // Call another random state
                game.randomiseState();
            }
        } else if(currentState == 3){ // Twist it
            printTopText();
            myArc.draw();
            goText = game.getTime();
            if(myArc.isSolved()){
                myArc.newGame();
                game.randomiseState();
            }
        } else if(currentState == 4){ // Click it
            printTopText();
            myClick.draw();
            goText = game.getTime();
            if(myClick.isSolved()){
                myClick.newGame();
                game.randomiseState();
            }
        } else if(currentState == 5){ // Clap it
            printTopText();
            mySound.draw();
            goText = game.getTime();
            if(mySound.isSolved()){
                mySound.newGame();
                game.randomiseState();
            }
        } else if(currentState == 6){ // Completed screen
            // Stop timer
            game.timerStop();
            // Reset text
            goText = "";
            // Set led red
            game.setRed(true);

            // Display the score
            fill(255,255,255);
            stroke(255,255,255);
            textSize(50);
            String newText = ("COMPLETED in " + game.getTime() + " seconds.");
            text(newText, width/2, (height/8));

            // Print the highscores
            lB.printHighscores();

            textAlign(CENTER, CENTER);
            textSize(32);
            fill(255,255,255);

            // Check if the score is good enough to get into the top 5
            if(lB.highEnoughForHighScore(game.getTimeInt())){
                text("Press space to enter name, N for new game or Q to Quit", width/2, (height/10) * 8);
            } else {
                text("Score not high enough for top 5, press N for new game or Q to Quit", width/2, (height/10) * 8);
            }

        }
        else if(currentState == 8){ // Enter the name Screen
            fill(255, 255, 255);
            text("Please enter your name", width/2, height/4);
            textSize(32);
            textAlign(CENTER, CENTER);
            text("Use cursor left right and up and down, return to finish input", width/2, height/10 * 8);
            textSize(80);

            // Set leaderboard name
            int i=0;
            for (char c : lB.letters) {
                fill(255, 255, 255);
                if (i== lB.index)
                    fill(255);
                text((lB.letters[i])+"", ((width/2) - 100)+i*65, height/2);
                i++;
            }
        } else if (currentState == 9){ // SHow the updated leaderboard with username

            // Formats the inputted name
            lB.result=""+ lB.letters[0]+ lB.letters[1]+ lB.letters[2]+ lB.letters[3];
            println(lB.result);

            // Adds the name and score to the leaderboard
            lB.addNewScore(game.getTimeInt(), lB.result);
            // Saves the new table to the file
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

        else if(currentState == 11){ // Resets the game for a new game
            newGame();
            game.resetGame();
        }
    }

    /**
     * Prints the top text
     */
    public void printTopText(){
        textSize(50);
        text("Welcome to FidgIT", width/2, height/10);
        textAlign(CENTER, TOP);
    }

    /**
     * Prints the instructions for the game
     */
    public void printInstructions(){
        textSize(32);
        instructions = ("FidgIT is a reaction game, aimed to test your, speed, agility and " +
                "\n most importantly, clapping ability!! " +
                "\n\n Follow the instructions and complete the tasks as fast as possible!! " +
                "\n\n Good Luck");
        text(instructions, width/2, height/3);
        textAlign(CENTER, TOP);
    }

    /**
     * Removes the instructions when the game starts
     */
    public void clearInstructions(){
        instructions = "";
    }

    /**
     * Resets any variables for a new game
     */
    public void newGame(){
        // Leaderboard Initialisation
        lB.letters = new char[4]; // 4 letters
        lB.index=0;                  // which letter is active
        lB.result="";
    }

    /**
     * Registers keystrokes and processes them based on game state
     */
    public void keyPressed() {

        // get the current gamestate
        int currentState = game.getGameState();


        if (currentState==6) { // Completed screen
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
                game.setGameState(11); // Reset game
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




