package pl.edu.pwr.lab1.i250235;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private static final boolean METRIC_SYSTEM = true;

    private EditText editTextMass;
    private EditText editTextHeight;
    private TextView finalResultText;
    private TextView massText;
    private TextView heightText;
    private Task task;
    private boolean systemMeasure = METRIC_SYSTEM;
    private ArrayList<Task> tasks = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Date date = new Date();
        tasks.add(new Task("My first task", "Hello", "mail", "", date, false));

        /**
        finalResultText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent next = new Intent(MainActivity.this, BMISpecsActivity.class );
                next.putExtra("BMIResult", (finalResultText.getText().toString()));
                startActivity(next);
            }

        });*/

        recyclerView = (RecyclerView) findViewById(R.id.main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter(this, (Task[]) tasks.toArray()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_mainactivity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch (item.getItemId())
        {
            case R.id.add_task_menu:
                Intent addTask = new Intent(this, InfoActivity.class);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return true;
    }


}
