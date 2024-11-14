package com.example.teste;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;


public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading); // Altere para o layout de carregamento correto

        // Referência para o ImageView
        ImageView loadingIcon = findViewById(R.id.image_loading);

        // Carregar a animação de rotação
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);

        // Iniciar a animação
        loadingIcon.startAnimation(rotateAnimation);

        // Atrasar a transição para a LoginActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Iniciar a LoginActivity
                Intent intent = new Intent(LoadingActivity.this, LoginCronus.class);
                startActivity(intent);
                finish(); // Fecha a LoadingActivity para que não volte com o botão voltar
            }
        }, 5000); // Tempo de espera em milissegundos (5 segundos)


    }
}
