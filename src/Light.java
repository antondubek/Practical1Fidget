import com.phidget22.VoltageRatioInputVoltageRatioChangeEvent;
import com.phidget22.VoltageRatioInputVoltageRatioChangeListener;
import processing.core.PApplet;

public class Light extends PApplet implements VoltageRatioInputVoltageRatioChangeListener {

    private PApplet p;
    private Game game;

    private float sensorReading;

    public Light(PApplet p, Game game) {
        this.p = p;
        this.game = game;
    }

    @Override
    public void onVoltageRatioChange(VoltageRatioInputVoltageRatioChangeEvent event) {
        //Add to progress
        sensorReading = (float) (event.getVoltageRatio()) * 1000;
        //System.out.println(sensorReading);
    }

    public float getSensorReading() {
        return sensorReading;
    }

}
