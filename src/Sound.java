import com.phidget22.VoltageInputVoltageChangeEvent;
import com.phidget22.VoltageInputVoltageChangeListener;
import processing.core.PApplet;

import java.util.concurrent.TimeUnit;

public class Sound extends PApplet implements VoltageInputVoltageChangeListener {

    // Initialise class variables
    private PApplet p;
    private Game game;
    private boolean solved;

    private int maxStacks;
    private int counter;
    private int display;

    private int textR;
    private int textG;
    private int textB;

    private int height;
    private int width;

    public Sound(PApplet p, Game game) {
        this.p = p;
        this.game = game;
        this.height = game.getHeight();
        this.width = game.getWidth();
        this.newGame();
    }

    public void draw(){
        // Display text
        p.fill(textR,textG,textB);
        p.stroke(textR, textG,textB);
        String toDisplay = "Clap IT (" + display+")";
        p.text(toDisplay, (width/5) * 4, (height/10) * 8);

        p.noStroke();
        p.rectMode(CENTER);

        // Show the volume level as stacks going from yello to red
        if(counter <= maxStacks && !isSolved()) {
            setTextColor(255,255,255);
            for (float i = 1; i < counter; i++) {

                p.fill(p.lerpColor(p.color(240,255,0), p.color(255,0,0), ((i)/counter)));
                p.rect(((width)/5) * 4, ((height/2)+140) - (i * 15), 140, 10);

            }
        } else { // Set them green when its completed
            setTextColor(0,255,0);
            display--;
            // Sleep for a bit of debounce
            try{TimeUnit.MILLISECONDS.sleep(300);} catch(Exception e){ System.out.println(e);};
        }

        // When the game is completed
        if(display == 0) {
            for (int i = 1; i < maxStacks; i++) {
                p.fill(0, 255, 0);
                p.rect(((width)/5) * 4, ((height/2)+140) - (i * 15), 140, 10);
                setTextColor(0,255,0);
                solved = true;
            }
        }
    }

    /**
     * Returns if the game is solved
     * @return
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     * Maps the voltage input of the sound sensor
     * @param event
     */
    @Override
    public void onVoltageChange(VoltageInputVoltageChangeEvent event) {
        double input = event.getVoltage() * 1000;

        // USE FOR REAL THING
        //System.out.println(input);
        counter =  (int) (1 + (input - 60)*(20-3)/(300-60));

        // USE FOR LATE NIGHT TESTING
        //counter =  (int) (1 + (input - 60)*(20-3)/(200-60));
    }

    /**
     * Changes the color of the text
     * @param r
     * @param g
     * @param b
     */
    private void setTextColor(int r, int g, int b){
        this.textR = r;
        this.textG = g;
        this.textB = b;
    }

    /**
     * Resets the class
     */
    public void newGame(){
        solved = false;

        maxStacks = 20;
        counter = 1;
        display = 10;

        textR = 255;
        textG = 255;
        textB = 255;
    }
}
