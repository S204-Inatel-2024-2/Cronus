package com.example.teste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginCronus extends AppCompatActivity {

    // Declarações das variáveis
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordTextView;
    private Button createAccountButton; // Adicionar o botão de criar conta
    private ImageView progressBar;
    private ImageView passWordHideAndSeek;

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser(); //pega o usuário que já está logado ao iniciar a aplicativo

        if(usuarioAtual != null)
        {
            // Lógica para login bem-sucedido, como iniciar outra Activity
            Intent intent = new Intent(LoginCronus.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Opcional: para fechar a activity de login
        }
    }

    @SuppressLint({"CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cronus); // Altere para o nome do seu layout

        // Inicialização das variáveis
        emailEditText = findViewById(R.id.email_login);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        forgotPasswordTextView = findViewById(R.id.esqueci_minha_senha_button);
        createAccountButton = findViewById(R.id.criar_conta_button2); // Referência para o botão de criar conta
        progressBar = findViewById(R.id.image_pattern_2);
        passWordHideAndSeek = findViewById(R.id.hide_and_seek);

        //botão de visualizar senha
        passWordHideAndSeek.setImageResource(R.drawable.ic_hide_pwd);
        passWordHideAndSeek.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(passwordEditText.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
                {
                    //se a senha for visible, então olho fechado
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //muda o ícone
                    passWordHideAndSeek.setImageResource(R.drawable.ic_hide_pwd);
                }
                else
                {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passWordHideAndSeek.setImageResource(R.drawable.ic_show_pwd);
                }

                // Move o cursor para o final após mudar a visibilidade
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });


        // Configurando o clique no botão de login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Aqui você pode adicionar a lógica de autenticação
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Exemplo: apenas para ilustrar, você pode querer validar as credenciais
                if (email.isEmpty() || password.isEmpty()) {
                    // Mostrar uma mensagem de erro
                    Toast.makeText(LoginCronus.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AutenticarUsuario(v);
                }
            }

            private void AutenticarUsuario(View v)
            {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            progressBar.setVisibility(View.VISIBLE);

                            new Handler().postDelayed(new Runnable()
                            {
                                @Override
                                public void run() //depois de 3 segundos, vai para a tela principal
                                {
                                    Toast.makeText(LoginCronus.this, "Login bem sucedido!", Toast.LENGTH_SHORT).show();
                                    // Lógica para login bem-sucedido, como iniciar outra Activity
                                    Intent intent = new Intent(LoginCronus.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish(); // Opcional: para fechar a activity de login
                                }
                            },3000);
                        }
                        else
                        {
                            String erro;

                            try
                            {
                                throw task.getException();
                            }
                            catch (FirebaseAuthInvalidUserException e)
                            {
                                erro = "Usuário não encontrado. Verifique o e-mail ou cadastre-se.";
                            }
                            catch (FirebaseAuthInvalidCredentialsException e)
                            {
                                erro = "Senha incorreta. Por favor, tente novamente.";
                            }
                            catch (Exception e)
                            {
                                erro = "Erro ao fazer login: " + e.getMessage();
                            }

                            Toast.makeText(LoginCronus.this, erro, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
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
                Intent intent = new Intent(LoginCronus.this, CreateActivity.class);
                startActivity(intent);
            }
        });

    }
}