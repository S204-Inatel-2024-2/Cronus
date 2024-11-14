package com.example.teste;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RecoverPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button sendRecoveryButton;
    private TextView backToLoginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password); // Altere para o layout correto

        // Inicializando as views
        emailEditText = findViewById(R.id.email_edit_text);
        sendRecoveryButton = findViewById(R.id.send_recovery_button);
        backToLoginTextView = findViewById(R.id.back_to_login);

        // Lógica para o botão de enviar recuperação
        sendRecoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(RecoverPasswordActivity.this, "Por favor, insira um e-mail válido.", Toast.LENGTH_SHORT).show();
                } else {
                    // Aqui você pode implementar a lógica de envio do e-mail de recuperação.
                    // Exemplo: enviar e-mail de recuperação ao servidor

                    Toast.makeText(RecoverPasswordActivity.this, "E-mail de recuperação enviado.", Toast.LENGTH_SHORT).show();

                    // Voltar automaticamente para a tela de login após o envio do e-mail
                    Intent intent = new Intent(RecoverPasswordActivity.this, LoginCronus.class);
                    startActivity(intent);
                    finish(); // Fechar a tela de recuperação de conta
                }
            }
        });

        // Lógica para voltar à tela de login ao clicar no texto "Voltar para o login"
        backToLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Voltar para a tela de login
                Intent intent = new Intent(RecoverPasswordActivity.this, LoginCronus.class);
                startActivity(intent);
                finish(); // Fechar a tela de recuperação de conta
            }
        });
    }
}
