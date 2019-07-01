package c346.rp.edu.mybmicalculator;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText etWeight;
    EditText etHeight;
    Button btnCalculate;
    Button btnReset;
    TextView tvDate;
    TextView tvBMI;

    String datetime;
    Float bmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWeight = findViewById(R.id.editTextWeight);
        etHeight = findViewById(R.id.editTextHeight);
        btnCalculate = findViewById(R.id.buttonCalculate);
        btnReset = findViewById(R.id.buttonReset);
        tvDate = findViewById(R.id.textViewDate);
        tvBMI = findViewById(R.id.textViewBMI);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etWeight.getText().toString().isEmpty() || etHeight.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this,"Enter your Weight and Height",Toast.LENGTH_LONG).show();
                }
                else {
                    Calendar now = Calendar.getInstance();
                    datetime = now.get(Calendar.DAY_OF_MONTH) + "/" +
                            (now.get(Calendar.MONTH) + 1) + "/" +
                            now.get(Calendar.YEAR) + " " +
                            now.get(Calendar.HOUR_OF_DAY) + ":" +
                            now.get(Calendar.MINUTE);

                    String strWeight = etWeight.getText().toString();
                    Float fltWeight = Float.parseFloat(strWeight);
                    String strHeight = etHeight.getText().toString();
                    Float fltHeight = Float.parseFloat(strHeight);

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                    SharedPreferences.Editor prefEdit = prefs.edit();

                    bmi = fltWeight / (fltHeight * fltHeight);

                    String bmiRound = String.format("%.3f", bmi);

                    prefEdit.putString("Date", datetime);
                    prefEdit.putFloat("Weight", fltWeight);
                    prefEdit.putFloat("Height", fltHeight);

                    prefEdit.commit();

                    String type;

                    if (bmi < 18.5) {
                        type = "underweight";
                    } else if (bmi < 25) {
                        type = "normal";
                    } else if (bmi < 30) {
                        type = "overweight";
                    } else {
                        type = "obese";
                    }

                    tvDate.setText("Last Calculated Date: " + datetime);
                    tvBMI.setText("Last Calculated BMI: " + bmiRound + "\nYou are " + type);

                    etHeight.setText("");
                    etWeight.setText("");
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                SharedPreferences.Editor prefEdit = prefs.edit();

                prefEdit.putString("Date", "");
                prefEdit.putFloat("Weight", 0);
                prefEdit.putFloat("Height", 0);

                prefEdit.commit();

                tvDate.setText("Last Calculated Date: ");
                tvBMI.setText("Last Calculated BMI: ");

                etHeight.setText("");
                etWeight.setText("");
            }
        });
    }

    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String strDate = prefs.getString("Date", "");
        Float fltWeight = prefs.getFloat("Weight", 0);
        Float fltHeight = prefs.getFloat("Height", 0);

        bmi = fltWeight / (fltHeight * fltHeight);

        String bmiRound = String.format("%.3f", bmi);

        tvDate.setText("Last Calculated Date: " + strDate);
        tvBMI.setText("Last Calculated BMI: " + bmiRound);
    }

}
