/* ********* S I M P L E  P H O T O  D E C O ********* */
/* This app is a simple paint/photo deco program that lets the user
 * take a photo or choose a photo from the device gallery.
 * The user can also use this app as a painting application.
 * The ability to save the drawing is available as well.
 */
package elizabethkelly.simplephotodeco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button) findViewById(R.id.startButton);
    }
    public void startClick(View view) {
        startActivity(new Intent(this, DrawActivity.class)); //start the DrawActivity
    }

}
