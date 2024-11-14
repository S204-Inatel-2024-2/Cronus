package com.example.teste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginCronus extends AppCompatActivity {

    // Declarações das variáveis
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordTextView;
    private Button createAccountButton; // Adicionar o botão de criar conta
    private Button recoveryAccountButton; // Corrigido o nome da variável

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cronus); // Altere para o nome do seu layout

        // Inicialização das variáveis
        usernameEditText = findViewById(R.id.email_login);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        forgotPasswordTextView = findViewById(R.id.esqueci_minha_senha_button);
        createAccountButton = findViewById(R.id.criar_conta_button2); // Referência para o botão de criar conta
        recoveryAccountButton = findViewById(R.id.esqueci_minha_senha_button); // Corrigido o ID

        // Configurando o clique no botão de login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aqui você pode adicionar a lógica de autenticação
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Exemplo: apenas para ilustrar, você pode querer validar as credenciais
                if (username.isEmpty() || password.isEmpty()) {
                    // Mostrar uma mensagem de erro
                    // Ex: Toast.makeText(LoginActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Lógica para login bem-sucedido, como iniciar outra Activity
                    Intent intent = new Intent(LoginCronus.this, HomeActivity.class);
                    startActivity(intent);
                    finish(); // Opcional: para fechar a activity de login
                }
            }
        });

        // Configurando o clique na opção "Esqueci minha senha"
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para redirecionar para a tela de recuperação de senha
                Intent intent = new Intent(LoginCronus.this, RecoverPasswordActivity.class);
                startActivity(intent);
            }
        });

        // Configurando o clique no botão "Criar Conta"
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redireciona para a CreateActivity (tela de criação de conta)
                Intent intent = new Intent(LoginCronus.this, com.example.teste.CreateActivity.class);
                startActivity(intent);
            }
        });

        // Configurando o clique no botão "Recuperar Senha"
        recoveryAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redireciona para a RecoverPasswordActivity (tela de recuperação de conta)
                Intent intent = new Intent(LoginCronus.this, RecoverPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
