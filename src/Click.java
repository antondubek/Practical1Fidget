import com.phidget22.DigitalInputStateChangeEvent;
import com.phidget22.DigitalInputStateChangeListener;
import processing.core.PApplet;

/**
 * Class that looks after the click it part
 */
public class Click extends PApplet implements DigitalInputStateChangeListener {

    // Initialisation of variables
    private PApplet p;
    private Game game;
    private boolean solved;

    private int maxStacks;
    private int counter;
    private int helper;

    private int r;
    private int g;
    private int b;

    private int textR;
    private int textG;
    private int textB;

    private int width;
    private int height;


    public Click(PApplet p, Game game) {
        this.p = p;
        this.game = game;
        this.width = game.getWidth();
        this.height = game.getHeight();
        this.newGame();
    }

    public void draw(){
        // Draw the text
        p.fill(textR,textG,textB);
        p.stroke(textR, textG,textB);
        p.text("Click IT", (width/5) * 3, (height/10) * 8);
        p.noStroke();
        p.rectMode(CENTER);

        // Draw the bars going up to the max amount when a click is registered
        if(counter <= maxStacks) {
            for (int i = 1; i < counter; i++) {
                p.fill(r, g, b);
                p.rect(((width)/5) * 3, ((height/2)+140) - (i * 15), 140, 10);

            }
        } else { // Show all the bars green if its been completed and set solved true
            for (int i = 1; i < maxStacks; i++) {
                p.fill(0, 255, 0);
                p.rect(((width)/5) * 3, ((height/2)+140) - (i * 15), 140, 10);
                setTextColor(0,255,0);
                solved = true;
            }
        }
    }

    /**
     * Monitor the state change of the joystick click
     * Increment counter when it is registered
     * @param event
     */
    @Override
    public void onStateChange(DigitalInputStateChangeEvent event) {
        helper++;
        if(helper % 2 == 0){
            counter++;
        }
    }

    /**
     * Change the text color, used to change to green
     * @param r R color value
     * @param g G color value
     * @param b B color value
     */
    private void setTextColor(int r, int g, int b){
        this.textR = r;
        this.textG = g;
        this.textB = b;
    }

    /**
     * Getter method to check if the part of the game has been solved
     * @return
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     * Method to reset the class
     */
    public void newGame(){
        solved = false;

        maxStacks = 20;
        counter = 1;
        helper = 0;

        r = 138;
        g = 20;
        b = 246;

        textR = 255;
        textG = 255;
        textB = 255;
    }
}
