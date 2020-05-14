package pl.edu.pwr.lab1.lab3app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.objects.FirebaseVisionObject;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int default_tag_button = 0;
    public static final int text_button = 1;
    public static final int object_button = 2;
    public static final int pickPhotoRequestCode = 200;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private int activeButton = default_tag_button;
    private ImageView image;
    private TextView itemsFoundText;
    private TextView tagsFound;
    private TextView textFound;
    private TextView objectsFound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.importImageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        Button cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

            }
        });

        this.image = findViewById(R.id.imageView);
        this.itemsFoundText = findViewById(R.id.textViewItemsFound);
        this.tagsFound = findViewById(R.id.textViewTagIntro);
        this.textFound = findViewById(R.id.textViewTextFound);
        this.objectsFound = findViewById(R.id.textViewObjectsIntro);

        this.objectsFound.setVisibility(View.INVISIBLE);
        this.textFound.setVisibility(View.INVISIBLE);


    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setAction(Intent.ACTION_GET_CONTENT); Declared on the constructor
        intent.setType("image/*");
        startActivityForResult(intent, pickPhotoRequestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            if(data == null)
                throw new IllegalStateException("The Intent is null");

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);

            switch (activeButton)
            {
                case default_tag_button:
                    processImageTagging(bitmap);
                    break;

                case text_button:
                    processImageText(bitmap);
                    break;

                case object_button:
                    processImageObject(bitmap);
                    break;
            }

        }

        if (requestCode == pickPhotoRequestCode && resultCode == Activity.RESULT_OK)
        {
            if(data == null)
                throw new IllegalStateException("The Intent is null");
            Bitmap bitmap = null;
            try {
                bitmap = getImageFromData(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.setImageBitmap(bitmap);

            // Does the method selected
            switch (activeButton)
            {
                case default_tag_button:
                    processImageTagging(bitmap);
                    break;

                case text_button:
                    processImageText(bitmap);
                    break;

                case object_button:
                    processImageObject(bitmap);
                    break;
            }

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    // Gets a bitmap from the intent given
    private Bitmap getImageFromData(Intent data) throws IOException {
        Uri selectedImage = data.getData();
        return  MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
    }

    // Gets the tags from an image
    private void processImageTagging(Bitmap bitmap) {
        FirebaseVisionImage visionImage = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                .getOnDeviceImageLabeler();

        labeler.processImage(visionImage)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
                        StringBuilder sb = new StringBuilder("");
                        for (FirebaseVisionImageLabel each : firebaseVisionImageLabels)
                        {
                            sb.append(each.getText()).append(", ");
                        }
                        itemsFoundText.setText(sb.toString());

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.toString());
            }
        });
    }

    // Gets the text from an image
    private void processImageText(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer textRecognizer =
                FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        textRecognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>()
        {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText)
            {
                itemsFoundText.setText(firebaseVisionText.getText());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.toString());
            }
        });
    }

    // Gets the objects from an image
    private void processImageObject(Bitmap bitmap) {


        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionObjectDetectorOptions options =
                new FirebaseVisionObjectDetectorOptions.Builder()
                        .setDetectorMode(FirebaseVisionObjectDetectorOptions.SINGLE_IMAGE_MODE)
                        .enableMultipleObjects()
                        .enableClassification()  // Optional
                        .build();

        FirebaseVisionObjectDetector objectDetector =
                FirebaseVision.getInstance().getOnDeviceObjectDetector(options);

        objectDetector.processImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<List<FirebaseVisionObject>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionObject> detectedObjects) {
                                StringBuilder sb = new StringBuilder("");
                                int i = 1;
                                for (FirebaseVisionObject obj : detectedObjects) {
                                    sb.append("Object ").append(i).append(": id=");
                                    Integer id = obj.getTrackingId();
                                    sb.append(id).append(",category = ");
                                    Rect bounds = obj.getBoundingBox();

                                    int category = obj.getClassificationCategory();
                                    sb.append(category).append(", confidence = ");

                                    Float confidence = obj.getClassificationConfidence();
                                    sb.append(confidence).append("\n");
                                    i++;

                                }

                                itemsFoundText.setText(sb.toString());
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println(e.toString());
                            }
                        });

    }

    // Result of the requested permissions of the result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Logic for the radio buttons work
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        if (checked) {
            // Hide the text before
            switch(activeButton){
                case default_tag_button:
                    tagsFound.setVisibility(View.INVISIBLE);
                    itemsFoundText.setText("");
                    break;
                case text_button:
                    textFound.setVisibility(View.INVISIBLE);
                    itemsFoundText.setText("");
                    break;
                case object_button:
                    objectsFound.setVisibility(View.INVISIBLE);
                    itemsFoundText.setText("");
                    break;
            }

            // Check which radio button was clicked
            switch (view.getId()) {
                case R.id.radioButtonTags:
                    activeButton = default_tag_button;
                    tagsFound.setVisibility(View.VISIBLE);
                    break;
                case R.id.radioButtonText:
                    activeButton = text_button;
                    textFound.setVisibility(View.VISIBLE);
                    break;
                case R.id.radioButtonObject:
                    activeButton = object_button;
                    objectsFound.setVisibility(View.VISIBLE);
                    break;
            }

        }
    }

}
