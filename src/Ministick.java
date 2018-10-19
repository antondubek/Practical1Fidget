import com.phidget22.PhidgetException;
import com.phidget22.VoltageRatioInputVoltageRatioChangeEvent;
import com.phidget22.VoltageRatioInputVoltageRatioChangeListener;
import processing.core.PApplet;

class Ministick extends PApplet implements VoltageRatioInputVoltageRatioChangeListener {

    // Initialisation of class variables
    private PApplet p;
    private Game game;
    private boolean solved;

    private float posX;
    private float posY;
    private float addX;
    private float addY;
    private float size;

    private int ballR;
    private int ballG;
    private int ballB;

    private int topLineR;
    private int topLineG;
    private int topLineB;

    private int botLineR;
    private int botLineG;
    private int botLineB;

    private int textR;
    private int textG;
    private int textB;

    private boolean top;
    private boolean bottom;

    private int height;
    private int width;

    private int display;



    Ministick(PApplet p, Game game, float x, float y) {
        this.p = p;
        this.game = game;
        this.posX = x;
        this.posY = y;
        this.height = game.getHeight();
        this.width = game.getWidth();
        this.newGame();
    }

    public void draw() {

        printText();

        //Draw ball where the joystick is
        p.strokeWeight(2);
        p.stroke(0,0,0);
        p.fill(ballR, ballG, ballB);
        float x = posX+addX;
        float y = posY+addY;
        p.ellipse(x, y, size, size);

        // Draw top line and update to green if over
        p.strokeWeight(4);
        p.stroke(topLineR, topLineG, topLineB);
        p.line((width/5) - 100, (height/2) - 100, (width/5)+100, (height/2) -100);
        if(y < ((height/2)-100)){
            setTopLineColor(0,255,0);
            top = true;
        }

        // Draw bottom line and update to green if below
        p.stroke(botLineR, botLineG, botLineB);
        p.line((width/5) - 100, (height/2) + 100, (width/5)+100, (height/2) +100);
        if(y > ((height/2)+100)){
            setBotLineColor(0,255,0);
            bottom = true;
        }

        // If the top and the bottom is completed lower the amount of times needed to compelete
        if(top & bottom){
            display--;
            reset();
        }

        // If it has been completed the number of times needed
        if(display <= 0){
            setBallColor(0,255,0);
            setTextColor(0,255,0);
            setBotLineColor(0,255,0);
            setTopLineColor(0,255,0);
            solved = true;
        }


    }

    /**
     * Listener for the voltage changes when moving the joystick
     * @param rce
     */
    public void onVoltageRatioChange(VoltageRatioInputVoltageRatioChangeEvent rce) {
        try {
            if (rce.getSource().getChannel()==4) {
                addX = (float) (rce.getVoltageRatio()*width/(5));
                addX += (width/5) - addX;
                //System.out.println(addX);
            }
            if (rce.getSource().getChannel()==3) {
                addY = (float) rce.getVoltageRatio()*height/(2) + 158;
                //System.out.println(addY);
            }
        } catch (PhidgetException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Change the ball color
     * @param r
     * @param g
     * @param b
     */
    private void setBallColor(int r, int g, int b) {
        this.ballR = r;
        this.ballG = g;
        this.ballB = b;
    }

    /**
     * Change the top line color
     * @param r
     * @param g
     * @param b
     */
    private void setTopLineColor(int r, int g, int b){
        this.topLineR = r;
        this.topLineG = g;
        this.topLineB = b;
    }

    /**
     * Change the bottom line color
     * @param r
     * @param g
     * @param b
     */
    private void setBotLineColor(int r, int g, int b){
        this.botLineR = r;
        this.botLineG = g;
        this.botLineB = b;
    }

    /**
     * Change the text color
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
     * Reset the game variables
     */
    private void reset(){
        setTopLineColor(0,191,255);
        setBotLineColor(0,191,255);
        top = false;
        bottom = false;
    }

    /**
     * Returns if the part of the game is solved
     * @return
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     * Prints the instruction text
     */
    private void printText(){
        String toDisplay = "";
        p.fill(textR,textG,textB);
        p.stroke(textR, textG,textB);
        if(display > 0) {
            toDisplay = "Flick IT (" + display + ")";
        } else {
            toDisplay = "Flick IT";
        }
        p.text(toDisplay, width/5, (height/10) * 8);
    }

    /**
     * Resets the whole class
     */
    public void newGame(){
        solved = false;

        posX = 0;
        posY = 0;
        addX = 0;
        addY = 0;
        size = 10;

        ballR = 0;
        ballG = 191;
        ballB = 255;

        topLineR = 0;
        topLineG = 191;
        topLineB = 255;

        botLineR = 0;
        botLineG = 191;
        botLineB = 255;

        textR = 255;
        textG = 255;
        textB = 255;

        top = false;
        bottom = false;

        // Set the amount of times needed to compelte part of game
        display = 3;
    }
}
