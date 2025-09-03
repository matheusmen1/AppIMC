package com.example.appimc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity {

    private ListView listView;
    private AlertDialog alerta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Singleton.imcModelList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                excluir(position);
            }


        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_lista,menu);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()== R.id.it_apagarTodos)
        {
            excluirTodos();
        }

        return super.onOptionsItemSelected(item);
    }
    private void removerLista(int pos)
    {
        IMCModel aux;
        aux = Singleton.imcModelList.get(pos);
        if (aux != null)
        {
            Singleton.imcModelList.remove(pos);
            listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Singleton.imcModelList));
        }

    }
    private void removerTodos()
    {

        while(Singleton.imcModelList.size() > 0)
        {
           Singleton.imcModelList.remove(0);
        }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Singleton.imcModelList));
    }
    private void excluir(int pos)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirme"); //define o titulo
        builder.setMessage("Deseja Apagar?"); //define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                removerLista(pos);
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // código a ser executado
            }
        });
        alerta = builder.create(); //cria o AlertDialog
        alerta.show(); //Exibe
    }
    private void excluirTodos()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirme"); //define o titulo
        builder.setMessage("Deseja Apagar Todos?"); //define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                removerTodos();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // código a ser executado
            }
        });
        alerta = builder.create(); //cria o AlertDialog
        alerta.show(); //Exibe
    }

}