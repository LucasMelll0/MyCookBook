package com.example.mycookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.mycookbook.R;
import com.example.mycookbook.repository.FireBaseAuthRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActiivty extends AppCompatActivity {
    private AppCompatEditText campoEmail;
    private AppCompatEditText campoSenha;
    private AppCompatButton botaoEntrar;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configuraCampos();
        configuraBotaoEntrar();
        configuraBotaoCadastrar();
    }

    private void configuraBotaoEntrar() {
        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logaUsuario();
            }
        });
    }

    private void logaUsuario() {
        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();
        FireBaseAuthRepository.autentica(this, firebaseAuth, email, senha);

    }

    private void configuraCampos() {
        campoEmail = findViewById(R.id.edittext_email_login);
        campoSenha = findViewById(R.id.edititext_senha_login);
        botaoEntrar = findViewById(R.id.button_entrar_login);

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
