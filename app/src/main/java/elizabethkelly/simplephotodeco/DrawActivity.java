package elizabethkelly.simplephotodeco;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.util.UUID;

public class DrawActivity extends Activity {
    private Uri fileUri; // file URI to store image/video
    private DrawView imgView; //the custom ImageView of the DrawView class

    Button savePhoto;
    Button clearCanvas;

    Spinner colorSpinner;
    Spinner brushSpinner;
    Spinner photoSpinner;
    Spinner bkgdSpinner;
    private static final String[] colorOptions = {"Black","Red", "Blue", "Green", "Yellow", "White", "Cyan", "Grey", "Magenta"};
    private static final String[] brushOptions = {"Small", "Medium", "Big", "Bigger"};
    private static final String[] photoOptions = {"New Page","Take Photo", "Choose Photo"};
    private static final String[] bkgdOptions = {"White","Black", "Blue"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        imgView = (DrawView) findViewById(R.id.drawView);

        //store photos in a file and set the name
        String outputFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myimage.jpg";
        fileUri = Uri.fromFile(new File(outputFilePath));

        /* ************************************************ */
        /* ************* SPINNER DEFINITIONS ************** */
        /* ************************************************ */
        colorSpinner = (Spinner) findViewById(R.id.brushColorSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DrawActivity.this, android.R.layout.simple_spinner_item, colorOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(adapter);
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imgView.chooseColor((String) parent.getItemAtPosition(position)); //call the chooseColor() method to set the color
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });//end colorOptions Spinner

        brushSpinner = (Spinner) findViewById(R.id.brushSizeSpinner);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(DrawActivity.this, android.R.layout.simple_spinner_item, brushOptions);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brushSpinner.setAdapter(adapter1);
        brushSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imgView.brushSize((String) parent.getItemAtPosition(position));//call the brushSize() method to set the brush size
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });//end brushOptions Spinner

        photoSpinner = (Spinner) findViewById(R.id.takePic);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(DrawActivity.this, android.R.layout.simple_spinner_item, photoOptions);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        photoSpinner.setAdapter(adapter2);
        photoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String option = (String) parent.getItemAtPosition(position); //get the item at the position in the arrayAdapter
                if (option.equals("Take Photo")) {
                    imgView.clearDrawing();//clears the canvas in case there was a pre-existing drawing
                    takePicture();
                } else if (option.equals("Choose Photo")) {
                    imgView.clearDrawing();//clears the canvas in case there was a pre-existing drawing
                    choosePicture();
                } else if (option.equals("New Page")) {
                    imgView.clearDrawing();
                    imgView.setImageBitmap(null);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });//end photoOption Spinner

        bkgdSpinner = (Spinner) findViewById(R.id.bkgdColor);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(DrawActivity.this, android.R.layout.simple_spinner_item, bkgdOptions);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bkgdSpinner.setAdapter(adapter4);
        bkgdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imgView.bkgdColor((String) parent.getItemAtPosition(position));//call the bkgdColor() method to set the brush size
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });//end bkgdOption Spinner
        /* END SPINNER DEFINITIONS */


        /* ************************************************ */
        /* ************* BUTTON DEFINITIONS *************** */
        /* ************************************************ */
        savePhoto = (Button) findViewById(R.id.savePic); //define the savePhoto button
        savePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgView.setDrawingCacheEnabled(true);
                String savedImg = MediaStore.Images.Media.insertImage(getContentResolver(), imgView.getDrawingCache(),
                        UUID.randomUUID().toString(), "PhotoDeco");
                if (savedImg != null) {
                    Toast savedMsg = Toast.makeText(getApplicationContext(), "Drawing saved!", Toast.LENGTH_SHORT);
                    savedMsg.show();//show the Toast to notify the user that the drawing was saved
                }
                imgView.destroyDrawingCache();
            }
        });//end savePhoto onClick()

        clearCanvas = (Button) findViewById(R.id.clear); //define the clearCanvas button
        clearCanvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgView.clearDrawing(); //clears the canvas in case there was a pre-existing drawing
                Toast savedMsg = Toast.makeText(getApplicationContext(), "Drawing cleared!", Toast.LENGTH_SHORT);
                savedMsg.show();//show the Toast to notify the user that the drawing was saved
            }
        });//end clearCanvas onClick()
        /* END BUTTON DEFINITIONS */
    }//end onCreate()


    /* choosePicture() method
    * Starts the device camera to take a photo
    */
    private void takePicture() {
        //boolean to check if the device has a camera
        boolean deviceHasCamera = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        //Check if the device has a camera
        if (deviceHasCamera) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //create a new Intent to take the photo
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); //store the photo into the intent
            startActivityForResult(intent, 1); //start the activity with the intent created above
        }
        else {
            Log.i("CAMERA_APP", "No camera found");
        }
    }//end takePicture()


    /* choosePicture() method
    * Starts a new intent for the Gallery application so the user can choose a pre-existing photo on their device
    */
    private void choosePicture() {
        //create a new external Intent to choose the photo
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);//store the photo into the intent
        startActivityForResult(galleryIntent, 2);//start the activity with the intent created above, with a unique resultCode of 2
    }//end choosePicture()


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            try {
                Matrix matrix;// = new Matrix();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;

                if (requestCode == 2) { //if the photo was chosen from the gallery
                    Uri selectedImage = intent.getData(); //get the uri passed as an extra from the intent
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null); //cursor object to find photo
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex); //the file path of the selected photo
                    cursor.close();

                    ExifInterface exifInterface = new ExifInterface(picturePath); //get info about the photo's orientation
                    matrix = rotateImage(exifInterface); //call the rotate image function

                    Bitmap bitmap1 = BitmapFactory.decodeFile(picturePath, options);
                    Bitmap rotatedBitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
                    imgView.setImageBitmap(rotatedBitmap1); //display the image on the UI
                }

                else if (requestCode == 1) { //if the photo was taken by the camera
                    ExifInterface exifInterface = new ExifInterface(fileUri.getPath());  //get info about the photo's orientation
                    matrix = rotateImage(exifInterface); //call the rotate image function

                    Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    imgView.setImageBitmap(rotatedBitmap); //display the image on the UI
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }//close if statement
    }//end onActivityResult()


    /*
    * rotateImage() method
    * @param ExifInterface exifInterface
    * @return matrix with the correct dimension and roation applied
    */
    protected Matrix rotateImage (ExifInterface exifInterface) {
        Matrix matrix = new Matrix();
        //Get orientation of the photograph
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                matrix.postRotate(0);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
        }
        return matrix; //return the rotated matrix
    }//end rotateImage()
}//close class