package bluetoothphonemouse.buildtest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import static bluetoothphonemouse.buildtest.R.id.XPos;
import static bluetoothphonemouse.buildtest.R.id.YPos;
import static bluetoothphonemouse.buildtest.R.id.ZPos;

public class BluetoothTest extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    static final float small_num = 1.0f / 1000000000.0f;
    float[] offsets = null;
    float[] last_values = null;
    float[] velocity = null;
    float[] position = null;
    long last_timestamp = 0;

    TextView XDisp = (TextView) findViewById(R.id.XPos);
    TextView YDisp = (TextView) findViewById(R.id.YPos);
    TextView ZDisp = (TextView) findViewById(R.id.ZPos);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_test);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        this.printPosition();
    }

    public void onSensorChanged(SensorEvent event) {
        if (offsets != null) {
            float dt = (event.timestamp - last_timestamp) * small_num;
            for(int i = 0; i < 3; ++i) {
                velocity[i] += (event.values[i] + last_values[i]) / 2 * dt;
                position[i] += velocity[i] * dt;
            }
        }
        else {
            offsets = new float[3];
            velocity = new float[3];
            position = new float[3];
            velocity[0] = velocity[1] = velocity[2] = 0f;
            position[0] = position[1] = position[2] = 0f;
            this.calibrate(event);
        }
        System.arraycopy(event.values, 0, last_values, 0, 3);
        last_timestamp = event.timestamp;
    }

    protected void calibrate(SensorEvent event) {
        offsets[0] = event.values[0];
        offsets[1] = event.values[1];
        offsets[2] = event.values[2];
    }

    protected void printPosition()
    {
        XDisp.setText(String.valueOf(position[0]));
        YDisp.setText(String.valueOf(position[1]));
        ZDisp.setText(String.valueOf(position[2]));
    }
}
