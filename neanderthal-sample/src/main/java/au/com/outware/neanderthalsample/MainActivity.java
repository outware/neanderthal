package au.com.outware.neanderthalsample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import au.com.outware.neanderthal.Neanderthal;

/**
 * @author timmutton
 */
public class MainActivity extends AppCompatActivity {
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Configuration configuration = Neanderthal.getConfiguration();

        TextView baseUrlText = (TextView)findViewById(R.id.base_url);
        baseUrlText.setText (String.format("Base URL: %s", configuration.baseUrl));

        TextView logsEnabledText = (TextView) findViewById(R.id.logs_enabled);
        logsEnabledText.setText (String.format("Logging Enabled: %s", String.valueOf(configuration.enableLogging)));

        TextView timeoutText = (TextView) findViewById(R.id.timeout);
        timeoutText.setText (String.format("Timeout: %s", String.valueOf(configuration.timeout)));
    }
}
