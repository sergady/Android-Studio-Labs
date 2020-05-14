package pl.edu.pwr.lab1.i250235;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_menu);
    }

    // Back button method
    public void back(View view)
    {
        Intent next = new Intent(this, MainActivity.class);
        startActivity(next);
    }
}
