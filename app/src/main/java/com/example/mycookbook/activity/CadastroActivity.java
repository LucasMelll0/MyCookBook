package com.example.mycookbook.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CadastroActivity extends AppCompatActivity {
    private AppCompatEditText campoNomeUsuario;
    private AppCompatEditText campoEmailUsuario;
    private AppCompatEditText campoSenhaUsuario;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private AppCompatButton botaoConfirmar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        configuraCampos();
        configuraBotaoConfirmar();
    }

    private void configuraBotaoConfirmar() {
        botaoConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastraUsuario();
            }
        });

    }

    private void cadastraUsuario() {
        String email = campoEmailUsuario.getText().toString();
        String senha = campoSenhaUsuario.getText().toString();
        FireBaseAuthRepository.cadastra(this, firebaseAuth, email, senha);
    }

    private void configuraCampos() {
        campoNomeUsuario = findViewById(R.id.edittext_nome_usuario_cadastro);
        campoEmailUsuario = findViewById(R.id.edittext_email_cadastro);
        campoSenhaUsuario = findViewById(R.id.edittext_senha_cadastro);
        botaoConfirmar = findViewById(R.id.button_confirmar_cadastro);
    }
}
