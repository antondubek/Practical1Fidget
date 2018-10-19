import com.phidget22.DigitalInputStateChangeEvent;
import com.phidget22.DigitalInputStateChangeListener;
import processing.core.PApplet;

public class Click extends PApplet implements DigitalInputStateChangeListener {

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
        p.fill(textR,textG,textB);
        p.stroke(textR, textG,textB);
        p.text("Click IT", (width/5) * 3, (height/10) * 8);
        p.noStroke();
        p.rectMode(CENTER);

        if(counter <= maxStacks) {
            for (int i = 1; i < counter; i++) {
                p.fill(r, g, b);
                p.rect(((width)/5) * 3, ((height/2)+140) - (i * 15), 140, 10);

            }
        } else {
            for (int i = 1; i < maxStacks; i++) {
                p.fill(0, 255, 0);
                p.rect(((width)/5) * 3, ((height/2)+140) - (i * 15), 140, 10);
                setTextColor(0,255,0);
                solved = true;
            }
        }
    }

    @Override
    public void onStateChange(DigitalInputStateChangeEvent event) {
        helper++;
        if(helper % 2 == 0){
            counter++;
        }
    }

    private void setTextColor(int r, int g, int b){
        this.textR = r;
        this.textG = g;
        this.textB = b;
    }

    public boolean isSolved() {
        return solved;
    }

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
