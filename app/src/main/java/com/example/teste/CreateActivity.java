package com.example.teste;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateActivity extends AppCompatActivity {

    // Declaração dos campos
    private static final String TAG = "CreateActivity"; // Para log de depuração
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create); // Use o layout correto

        // Verificação de inicialização do Firebase
        /*if (FirebaseAuth.getInstance() == null) {
            Toast.makeText(this, "Erro ao inicializar o Firebase", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Firebase não inicializado.");
            return;
        }*/

        // Inicializar os campos
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        repeatPasswordEditText = findViewById(R.id.repeat_password);
        createAccountButton = findViewById(R.id.create_account_button);

        // Configurar o clique do botão "Criar Conta"
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Obter os valores dos campos
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String repeatPassword = repeatPasswordEditText.getText().toString();

                // Validação básica dos campos
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                    // Exibir uma mensagem de erro
                    Toast.makeText(CreateActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(repeatPassword)) {
                    // Exibir erro se as senhas não coincidirem
                    Toast.makeText(CreateActivity.this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
                } else {

                    CadastrarUsuario(v);
                }
            }
            private void CadastrarUsuario(View v)
            {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                //cadastro do usuário
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        //task é o resultado da ação
                        if(task.isSuccessful())
                        {
                            Snackbar snackbar = Snackbar.make(v, "Conta criada!", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();

                            // Redirecionar de volta para a tela de login
                            Intent intent = new Intent(CreateActivity.this, LoginCronus.class);
                            startActivity(intent);
                            finish(); // Fecha a tela de criação de conta
                        }
                        else
                        {
                            String errorMessage = "Erro ao criar conta: " + task.getException().getMessage();
                            Toast.makeText(CreateActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            Log.e(TAG, errorMessage); // Log do erro
                        }

                    }
                });
            }
        });
    }

}

