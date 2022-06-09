package com.example.mycookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.mycookbook.R;

public class LoginActiivty extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configuraBotaoCadastrar();
    }

    private void configuraBotaoCadastrar() {
        AppCompatButton botaoCadastrar = findViewById(R.id.button_cadastrar_login);
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vaiParaCadastro = new Intent(LoginActiivty.this, CadastroActivity.class);
                startActivity(vaiParaCadastro);
            }
        });
    }
}
