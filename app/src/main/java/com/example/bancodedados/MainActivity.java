package com.example.bancodedados;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapters.ProdutoAdapter;
import database.DatabaseHelper;
import models.Produto;

public class MainActivity extends AppCompatActivity {
    private List<Produto> listProdutos;
    private ProdutoAdapter produtoAdapter;
    private RecyclerView recycleViewProdutos;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        listProdutos = listarProdutos();

        //listProdutos = new ArrayList<>();
        //listProdutos.add(new Produto("Chocolate", 123.4));

        produtoAdapter = new ProdutoAdapter(listProdutos);

        recycleViewProdutos = findViewById(R.id.recycleViewProdutos);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, RecyclerView.HORIZONTAL);
        recycleViewProdutos.addItemDecoration(dividerItemDecoration);

        recycleViewProdutos.setLayoutManager(layoutManager);
        recycleViewProdutos.setAdapter(produtoAdapter);

        Button adicionarBtn = findViewById(R.id.adicionarBtn);
        Button removerBtn = findViewById(R.id.removerBtn);

        adicionarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editTextNome = findViewById(R.id.editTextNome);

                EditText editTextPreco = findViewById(R.id.editTextPreco);

                String nome = editTextNome.getText().toString();
                String precoString = editTextPreco.getText().toString();
                Double preco = Double.parseDouble(precoString);

                if(!nome.isEmpty() && !precoString.isEmpty()) {
                    Produto produto = new Produto(nome, preco);
                    salvarProduto(produto);
                    produtoAdapter.adicionarItem(produto);
                    Toast.makeText(MainActivity.this, "Produto salvo com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Nome e preço devem ser preenchidos", Toast.LENGTH_SHORT).show();
                }
                editTextNome.setText("");
                editTextPreco.setText("");
            }
        });

        removerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                produtoAdapter.removerItem();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void salvarProduto(Produto produto) {
        try {
            Log.i("DB", "salvando usuário");
            String sql = "INSERT INTO produtos VALUES (null, '" + produto.getNome()
                    + "', '" + produto.getPreco() + "');";
            databaseHelper.getWritableDatabase().execSQL(sql);
            Toast.makeText(this, "Produto criado com sucesso", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao criar produto", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Produto> listarProdutos() {
        String sql = "SELECT * FROM produtos;";
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(sql, null);
        List<Produto> produtos = new ArrayList<>();
        while (cursor.moveToNext()) {
            String nome = cursor.getString(1);
            Double preco = cursor.getDouble(2);

            Produto produto = new Produto(nome, preco);
            produtos.add(produto);
        }

        return produtos;
    }
}