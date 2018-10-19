import com.phidget22.VoltageRatioInputVoltageRatioChangeEvent;
import com.phidget22.VoltageRatioInputVoltageRatioChangeListener;
import processing.core.PApplet;

class Rotation extends PApplet implements VoltageRatioInputVoltageRatioChangeListener {

    // Initialisation of class variables
    private PApplet p;
    private Game game;
    private boolean solved = false;

    // Number of times to complete
    private float progress = 9;

    private float x;
    private float y;
    private float d;

    private int r;
    private int g;
    private int b;

    private int innerR;
    private int innerG;
    private int innerB;

    private int outerR;
    private int outerG;
    private int outerB;

    private int textR = 255;
    private int textG = 255;
    private int textB = 255;

    private boolean lower = false;
    private boolean upper = false;


    public Rotation(PApplet p, Game game){
        this.p = p;
        this.game = game;
        this.newGame();
    }

    public void draw(){
        p.fill(textR,textG,textB);
        p.stroke(textR, textG,textB);
        p.text("Twist IT", (game.getWidth()/5) * 2, (game.getHeight()/10) * 8);

        // Outer circle
        p.ellipseMode(CENTER);
        p.stroke(outerR,outerG,outerB);
        p.fill(r, g, b, 50);
        p.ellipse(x, y, d, d);

        showArcs();

        // Inner circle
        p.stroke(innerR,innerG,innerB);
        p.fill(game.getBackgroundR(), game.getBackgroundG(), game.getBackgroundB());
        p.ellipse(x, y, d-30, d-30);

        // Set turn left as compelted
        if(progress < 0.5){
            setInner(0,255,0);
            lower = true;
        }

        // set the turn right as complete
        if(progress > 134){
            setOuter(0,255,0);
            upper = true;
        }

        // If both have been compelted, set game as complete
        if(lower && upper){
            setArc(0,255,0);
            setTextColor(0,255,0);
            solved = true;
        }

    }

    /**
     * Listener which maps the rotation input voltage to 0 -140
     * @param event
     */
    @Override
    public void onVoltageRatioChange(VoltageRatioInputVoltageRatioChangeEvent event) {
        //Add to progress
        float input = (float) (event.getVoltageRatio());
        progress =  (float) ((input-0.0)/(0.999 - 0.0) * (140) + 0);
    }

    /**
     * Builds the arcs
     */
    private void showArcs() {
        p.stroke(r,g,b);
        p.fill(r, g, b, 200);
        p.arc(x, y, d, d, PI+HALF_PI, map(progress, 0, 100, PI+HALF_PI, PI+HALF_PI+PI+HALF_PI));
    }

    /**
     * Changes the outer arc color
     * @param r
     * @param g
     * @param b
     */
    private void setOuter(int r, int g, int b){
        this.outerR = r;
        this.outerG = g;
        this.outerB = b;
    }

    /**
     * Changes the inner arc color
     * @param r
     * @param g
     * @param b
     */
    private void setInner(int r, int g, int b){
        this.innerR = r;
        this.innerG = g;
        this.innerB = b;
    }

    /**
     * Changes the whole arc color
     * @param r
     * @param g
     * @param b
     */
    private void setArc(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Changes the text color
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
     * Returns if the game is solved
     * @return
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     * Resets the whole class
     */
    public void newGame(){
        solved = false;

        progress = 9;

        x = (game.getWidth()/5) * 2;
        y = game.getHeight()/2;
        d = (float) (game.getHeight() *0.4);

        r = 255;
        g = 0;
        b = 128;

        innerR = 255;
        innerG = 0;
        innerB = 128;

        outerR = 255;
        outerG = 0;
        outerB = 128;

        textR = 255;
        textG = 255;
        textB = 255;

        lower = false;
        upper = false;
    }
}