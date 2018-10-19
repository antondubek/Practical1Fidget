import com.phidget22.VoltageInputVoltageChangeEvent;
import com.phidget22.VoltageInputVoltageChangeListener;
import processing.core.PApplet;

import java.util.concurrent.TimeUnit;

public class Sound extends PApplet implements VoltageInputVoltageChangeListener {

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
        p.fill(textR,textG,textB);
        p.stroke(textR, textG,textB);
        String toDisplay = "Clap IT (" + display+")";
        p.text(toDisplay, (width/5) * 4, (height/10) * 8);

        p.noStroke();
        p.rectMode(CENTER);

        if(counter <= maxStacks && !isSolved()) {
            setTextColor(255,255,255);
            for (float i = 1; i < counter; i++) {

                p.fill(p.lerpColor(p.color(240,255,0), p.color(255,0,0), ((i)/counter)));
                p.rect(((width)/5) * 4, ((height/2)+140) - (i * 15), 140, 10);

            }
        } else {
            setTextColor(0,255,0);
            display--;
            try{TimeUnit.MILLISECONDS.sleep(300);} catch(Exception e){ System.out.println(e);};
        }

        if(display == 0) {
            for (int i = 1; i < maxStacks; i++) {
                p.fill(0, 255, 0);
                p.rect(((width)/5) * 4, ((height/2)+140) - (i * 15), 140, 10);
                setTextColor(0,255,0);
                solved = true;
            }
        }
    }

    public boolean isSolved() {
        return solved;
    }

    @Override
    public void onVoltageChange(VoltageInputVoltageChangeEvent event) {
        double input = event.getVoltage() * 1000;

        // USE FOR REAL THING
        //System.out.println(input);
        counter =  (int) (1 + (input - 60)*(20-3)/(300-60));

        // USE FOR LATE NIGHT TESTING
        //counter =  (int) (1 + (input - 60)*(20-3)/(200-60));
    }

    private void setTextColor(int r, int g, int b){
        this.textR = r;
        this.textG = g;
        this.textB = b;
    }

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
