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
import java.text.DecimalFormat;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class ResultActivity extends Activity {
    TextView resultadoInvestimento, resultadoData, resultadoPorcentagem, resultadoRendimentoBruto, resutadoRendimentoLiquido, resultadoImpostoDeRenda, resultadoLucro;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        resultadoData = findViewById(R.id.resultadoData);
        resultadoInvestimento = findViewById(R.id.resultadoInvestimento);
        resultadoPorcentagem = findViewById(R.id.resultadoPorcentagem);
        resultadoRendimentoBruto = findViewById(R.id.resultadoRendimentoBruto);
        resultadoImpostoDeRenda = findViewById(R.id.resultadoImpostoDeRenda);
        resutadoRendimentoLiquido = findViewById(R.id.resultadoRendimentoLiquido);
        resultadoLucro = findViewById(R.id.resultadoLucro);

        Intent intent = getIntent();

        String investimentoStr = intent.getStringExtra("investimento");
        String dataStr = intent.getStringExtra("data");
        String porcentagemStr = intent.getStringExtra("porcentagem");
        Button button = findViewById(R.id.voltar);
        DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(dataStr, formatterEntrada);

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(java.math.RoundingMode.HALF_UP);

        try {
            double investimento = Double.parseDouble(investimentoStr);
            double porcentagem = Double.parseDouble(porcentagemStr);
            long diasCorridos = calcularDiasCorridos(date);
            double rendimento = calcularRendimento(investimento, porcentagem, diasCorridos);
            double imposto = calcularImposto(rendimento, diasCorridos);
            double valorLiquido = rendimento - imposto;
            double lucro = rendimento - (investimento + imposto);

            resultadoInvestimento.setText(String.format("R$ %.2f", investimento));
            resultadoData.setText(String.format("%d dias", diasCorridos));
            resultadoPorcentagem.setText(String.format("%.2f%% de taxa anual", porcentagem));
            resultadoRendimentoBruto.setText(String.format("R$ %.2f", rendimento));
            resultadoImpostoDeRenda.setText(String.format("R$ %.2f", imposto));
            resutadoRendimentoLiquido.setText(String.format("R$ %.2f", valorLiquido));
            resultadoLucro.setText(String.format("R$ %.2f", lucro));
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
        return ChronoUnit.DAYS.between(dataAtual, dataSelecionada);
    }

    private static double calcularRendimento(double investimento, double porcentagem, long diasCorridos) {
        double taxaJurosAnual = porcentagem / 100;
        double meses = diasCorridos / 30;
        double taxaJurosMensal = taxaJurosAnual / 12;
        int periodosPorAno = 12;

        return investimento * Math.pow(1 + taxaJurosMensal, periodosPorAno * (diasCorridos / 365.0));
    }

    private static double calcularImposto(double rendimento, long diasCorridos) {
        double taxaImposto;

        if (diasCorridos <= 100) {
            taxaImposto = 0.20;
        } else if (diasCorridos <= 150) {
            taxaImposto = 0.18;
        } else if (diasCorridos <= 300) {
            taxaImposto = 0.15;
        } else {
            taxaImposto = 0.10;
        }

        return rendimento * taxaImposto;
    }
}
