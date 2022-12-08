package course.examples.Services.KeyService;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MusicServiceActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_key_generator);

        // Intent used for starting the MusicService
        final Intent musicServiceIntent =
                new Intent(getApplicationContext(), MusicService.class);

        final Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(
                (view) -> startForegroundService(musicServiceIntent)
        ) ;

        final Button stopButton = (Button) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View src) {

                // Stop the MusicService using the Intent
                stopService(musicServiceIntent);

            }
        });

    }

    // Stoooopid API 33 now wants a dangerous level permission granted in order for
    // the notification to be displayed @!*!..*.@%^.@*!
    public void onStart() {
        super.onStart() ;
        if (ActivityCompat.checkSelfPermission(
                this,"android.permission.POST_NOTIFICATIONS")
                == PackageManager.PERMISSION_DENIED)
        {
            requestPermissions(new String[] {"android.permission.POST_NOTIFICATIONS"}, 0) ;
        }
    }

    public void onRequestPermissionsResult(int code, String[] permissions, int[] result) {
        super.onRequestPermissionsResult(code, permissions, result) ;
        if (result.length >0) {
            if (result[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "App will not show notifications", Toast.LENGTH_SHORT).show() ;
            }
        }
    }
}