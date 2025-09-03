package com.example.appimc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String nome, sexo, condicao = "";
    private double altura, imc= 0;
    private int peso;
    private EditText etNome, etCondicao;
    private TextView tvPeso, tvAltura;
    private SeekBar skPeso, skAltura;
    private RadioButton rbMasculino, rbFeminino;
    private Button btCalcular, btArmazenar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etNome = findViewById(R.id.etNome);
        tvPeso = findViewById(R.id.tvPeso);
        tvAltura = findViewById(R.id.tvAltura);
        skPeso = findViewById(R.id.skPeso);
        skAltura = findViewById(R.id.skAltura);
        rbMasculino = findViewById(R.id.rbMasculino);
        rbFeminino = findViewById(R.id.rbFeminino);
        etCondicao = findViewById(R.id.etCondicao);
        btCalcular = findViewById(R.id.btCalcular);
        btArmazenar = findViewById(R.id.btArmazenar);
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);

        nome = sharedPreferences.getString("nome", "Matheus");
        etNome.setText(nome);

        peso = sharedPreferences.getInt("peso", 90);
        tvPeso.setText(""+peso+" kg");
        skPeso.setProgress(peso);

        altura = sharedPreferences.getFloat("altura", 1.75F);
        tvAltura.setText(""+String.format("%.2f", altura)+" m");
        skAltura.setProgress((int)(altura*100));

        sexo = sharedPreferences.getString("sexo", "Masculino");
        if (sexo.equals("Masculino"))
            rbMasculino.setChecked(true);
        else
            rbFeminino.setChecked(true);

        atualizarCondicao();
        carregarIMCModels();
        // gerar eventos
        skPeso.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                tvPeso.setText(""+i+" kg");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        skAltura.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                tvAltura.setText(""+(i/100.)+" m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        rbFeminino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexo = "Feminino";
            }
        });
        rbMasculino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexo = "Masculino";
            }
        });
        btCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarCondicao();
            }
        });
        btArmazenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                armazenarIMC();

            }
        });
    }

    private void calcIMC()
    {
        nome = etNome.getText().toString();
        peso = skPeso.getProgress();
        altura = skAltura.getProgress()/100.;
        imc = IMC.calcularIMC(peso, altura);
        if (sexo.equals("Feminino"))
        {
            if (imc < 19.1)
                condicao = "Abaixo do Peso";
            else
            if(imc > 19.1 && imc < 25.8)
                condicao = "No Peso Ideal";
            else
            if(imc > 25.8 && imc < 27.3)
                condicao = "Marginalmente Acima do Peso";
            else
            if(imc > 27.3 && imc < 32.3)
                condicao = "Acima do Peso Ideal";
            else
            if(imc > 32.3)
                condicao = "Obeso";
        }
        else
        {
            if (imc < 20.7)
                condicao = "Abaixo do Peso";
            else
            if(imc > 20.7 && imc < 26.4)
                condicao = "No Peso Ideal";
            else
            if(imc > 26.4 && imc < 27.8)
                condicao = "Marginalmente Acima do Peso";
            else
            if(imc > 27.8 && imc < 31.1)
                condicao = "Acima do Peso Ideal";
            else
            if(imc > 31.1)
                condicao = "Obeso";
        }
    }
    private void atualizarCondicao()
    {
        if (!etNome.getText().toString().isEmpty() && (rbFeminino.isChecked() || rbMasculino.isChecked()))
        {
            calcIMC();
            if (sexo.equals("Masculino"))
                etCondicao.setText(nome+","+" Você Possui "+peso+" Kg"+" e "+altura+" m\n"+"Seu IMC é de "+String.format("%.2f", imc)+"."+"\n"+condicao+", "+"Para Homens.");
            else
                etCondicao.setText(nome+","+" Você Possui "+peso+" Kg"+" e "+altura+" m\n"+"Seu IMC é de "+String.format("%.2f", imc)+"."+"\n"+condicao+", "+"Para Mulheres.");
        }
    }
    private void fechar()
    {
        Snackbar snackbar;
        snackbar = Snackbar.make(findViewById(android.R.id.content), "Fechar o App?", Snackbar.LENGTH_LONG);
        snackbar.setAction("Confirme", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        snackbar.show();
    }
    private void limpar()
    {
        etNome.setText("");
        rbMasculino.setChecked(false);
        rbFeminino.setChecked(false);
        skAltura.setProgress(175);
        tvAltura.setText(""+1.75+" m");
        skPeso.setProgress(90);
        tvPeso.setText(""+90+" kg");
        etCondicao.setText("");
        imc = 0;
        condicao = "";
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_opcao,menu);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()== R.id.it_limpar)
        {
            limpar();
        }
        if (item.getItemId()== R.id.it_sair){

            fechar();
        }
        if (item.getItemId()== R.id.it_consultar)
        {
            verIMCModels();
        }
        return super.onOptionsItemSelected(item);
    }
    private void carregarIMCModels()
    {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        try{
            fileInputStream=openFileInput("dados.dat");
            objectInputStream=new ObjectInputStream(fileInputStream);
            Singleton.imcModelList=(List<IMCModel>)objectInputStream.readObject();
            objectInputStream.close();
        }
        catch (Exception e){
            Log.e("erroxxx",e.getMessage());
        }
    }
    private void verIMCModels()
    {
        Intent intent = new Intent(this, ListaActivity.class);
        startActivity(intent);
    }
    private void armazenarIMC()
    {
        nome = etNome.getText().toString();
        peso = skPeso.getProgress();
        altura = skAltura.getProgress()/100.;
        if (rbFeminino.isChecked())
            sexo = "F";
        else
        if (rbMasculino.isChecked())
            sexo = "M";
        else
            sexo = "";
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        String dataFormatada = agora.format(formatter);
        System.out.println(dataFormatada);
        if (!nome.isEmpty() && peso > 0 && altura > 0 && imc > 0 && !condicao.isEmpty() && !sexo.isEmpty())
        {
            Singleton.imcModelList.add(new IMCModel(nome, sexo, dataFormatada, condicao, altura, imc, peso));
            Toast.makeText(this,"Armazenado Com Sucesso",Toast.LENGTH_LONG).show();
            etNome.setText("");
            rbMasculino.setChecked(false);
            rbFeminino.setChecked(false);
            skAltura.setProgress(175);
            tvAltura.setText(""+1.75+" m");
            skPeso.setProgress(90);
            tvPeso.setText(""+90+" kg");
            etCondicao.setText("");
            imc = 0;
            condicao = "";
        }
        else
        {
            Toast.makeText(this,"Erro ao Armazenar",Toast.LENGTH_LONG).show();
        }


    }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putFloat("altura", (float)altura);
        editor.putInt("peso",peso);
        editor.putString("sexo",sexo);
        editor.putString("nome", nome);
        editor.commit();

        try {
            FileOutputStream fileOutputStream;
            ObjectOutputStream objectOutputStream;
            fileOutputStream = openFileOutput("dados.dat", MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(Singleton.imcModelList);
            objectOutputStream.close();
        }catch (Exception  e){
            Log.e("erroxxx",e.getMessage());
        }

    }

}