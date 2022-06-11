package com.example.mycookbook.repository;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mycookbook.activity.CadastroActivity;
import com.example.mycookbook.activity.ListaDeReceitas;
import com.example.mycookbook.activity.LoginActiivty;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBaseAuthRepository {

    public static void desloga(Context context, FirebaseAuth firebaseAuth){
        firebaseAuth.signOut();
        Toast.makeText(context, "Usuário Deslogado Com Sucesso!!", Toast.LENGTH_SHORT).show();
    }
    public static void cadastra(Context context, FirebaseAuth firebaseAuth, String email, String senha) {
        Task<AuthResult> novoUsuario = firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(context, "Usuário Cadastrado com Sucesso!!", Toast.LENGTH_SHORT).show();
                Intent voltaParaLogin = new Intent(context, LoginActiivty.class);
                context.startActivity(voltaParaLogin);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Ocorreu Um Erro ao Cadastrar.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void autentica(Context context, FirebaseAuth firebaseAuth, String email, String senha){
        firebaseAuth.signInWithEmailAndPassword(email, senha).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(context, "Usuário Logado com Sucesso!!", Toast.LENGTH_SHORT).show();
                Intent vaiParaListaDeReceitas = new Intent(context, ListaDeReceitas.class);
                context.startActivity(vaiParaListaDeReceitas);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Autentica", "onFailure: " + e );
                Toast.makeText(context, "Ocorreu Um Erro ao Logar.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}