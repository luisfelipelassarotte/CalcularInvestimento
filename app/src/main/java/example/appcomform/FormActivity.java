package example.appcomform;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import java.util.Locale;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class FormActivity extends Activity {
    EditText investimento, data, porcentagem;
    Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        investimento = (EditText) findViewById(R.id.investimento);
        data = (EditText) findViewById(R.id.data);
        porcentagem = (EditText) findViewById(R.id.porcentagem);
        Button button = (Button) findViewById(R.id.calculo);

        data.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    FormActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        // Format the date as yyyy-MM-dd
                        String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
                        data.setText(selectedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormActivity.this, ResultActivity.class);

                intent.putExtra("investimento", investimento.getText().toString());
                intent.putExtra("data", data.getText().toString());
                intent.putExtra("porcentagem", porcentagem.getText().toString());

                startActivity(intent);
            }
        });
    }
}

