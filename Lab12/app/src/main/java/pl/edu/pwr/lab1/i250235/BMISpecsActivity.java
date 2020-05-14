package pl.edu.pwr.lab1.i250235;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BMISpecsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_m_i_specs);
        TextView valueBMI = findViewById(R.id.calculatedBMIVal);
        String value = getIntent().getStringExtra("BMIResult");
        valueBMI.setText(value);
    }

    // Back button method
    public void back(View view)
    {
        Intent next = new Intent(this, MainActivity.class);
        startActivity(next);
    }
}
