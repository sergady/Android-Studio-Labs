package pl.edu.pwr.lab1.lab4sensorlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Adapter and lists for the sensor data in raw form and formatted into a string
    static RecyclerViewAdapter adapter;
    private static SensorManager sensorManager;
    static List<Sensor> sensorList;
    static List<String> sensorDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList =  sensorManager.getSensorList(Sensor.TYPE_ALL);

        for (Sensor each : sensorList)
        {
            if (each != null)
                sensorDataList.add(each.getName());
        }

        // Put the recyclerView into the recyclerView object
        RecyclerView recyclerView = findViewById(R.id.main_list);

        // Put teh layout manager in the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        //Create the adapter and connect it to the list
        adapter = new RecyclerViewAdapter(this, sensorDataList);
        recyclerView.setAdapter(adapter);

    }


    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
        private List<String> sensorsData;
        private LayoutInflater layoutInflater;
        private RecyclerViewAdapter recyclerAdapter = this;

        //Data is passed into the constructor
        RecyclerViewAdapter(Context context, List<String> data) {
            this.layoutInflater = LayoutInflater.from(context);
            this.sensorsData = data;
        }

        //Inflates the row layout from xml when needed
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.recyclerview_row, parent, false);
            return new ViewHolder(view);
        }

        //Binds the data to the TextView in each row
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String sensorDataText = sensorsData.get(position).toString();
            holder.textView.setText(sensorDataText);
        }

        //Total number of rows
        @Override
        public int getItemCount() {
            return sensorsData.size();
        }

        //Stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                //Get each TextView for displaying the data in it
                textView = itemView.findViewById(R.id.sensor_text);
            }

        }


    }


}
