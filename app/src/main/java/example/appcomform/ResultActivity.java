package example.appcomform;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class ResultActivity extends Activity {
    TextView resultadoInvestimento, resultadoData, resultadoPorcentagem;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        resultadoData = findViewById(R.id.resultadoData);
        resultadoInvestimento = findViewById(R.id.resultadoInvestimento);
        resultadoPorcentagem = findViewById(R.id.resultadoPorcentagem);

        Intent intent = getIntent();

        String investimentoStr = intent.getStringExtra("investimento");
        String dataStr = intent.getStringExtra("data");
        String porcentagemStr = intent.getStringExtra("porcentagem");
        Button button = findViewById(R.id.voltar);

        try {
            double investimento = Double.parseDouble(investimentoStr);
            double porcentagem = Double.parseDouble(porcentagemStr);

            LocalDate date = LocalDate.parse(dataStr);
            long diasCorridos = calcularDiasCorridos(date);

            resultadoInvestimento.setText("Investimento: " + investimento);
            resultadoData.setText("Data: " + diasCorridos + " dias corridos");
            resultadoPorcentagem.setText("Porcentagem: " + porcentagem);
        } catch (NumberFormatException e) {
            resultadoInvestimento.setText("Erro: Formato de número inválido");
            resultadoPorcentagem.setText("Erro: Formato de número inválido");
        } catch (DateTimeParseException e) {
            resultadoData.setText("Erro: Formato de data inválido");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, FormActivity.class);

                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static long calcularDiasCorridos(LocalDate dataSelecionada) {
        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dataAtualFormatada = dataAtual.format(formatter);
        return ChronoUnit.DAYS.between(dataAtualFormatada, dataSelecionada);
    }
}
