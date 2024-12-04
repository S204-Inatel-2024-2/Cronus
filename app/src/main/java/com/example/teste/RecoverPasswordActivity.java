package com.example.teste;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class RecoverPasswordActivity extends AppCompatActivity
{

    private EditText emailEditText;
    private Button sendRecoveryButton;
    private FirebaseAuth mAuth;
    private final String TAG = "RecoverPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        emailEditText = findViewById(R.id.email_edit_text);
        sendRecoveryButton = findViewById(R.id.send_recovery_button);
        mAuth = FirebaseAuth.getInstance();

        sendRecoveryButton.setOnClickListener(v ->
        {
            String email = emailEditText.getText().toString();

            if (TextUtils.isEmpty(email))
            {
                Toast.makeText(RecoverPasswordActivity.this, "Por favor, insira um email válido.", Toast.LENGTH_SHORT).show();
                return; // Early exit to prevent unnecessary processing
            }

            // Verificar se o formato do e-mail é válido
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                Toast.makeText(RecoverPasswordActivity.this, "Formato de email inválido.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Enviar o e-mail de recuperação
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task ->
                    {
                        if (task.isSuccessful())
                        {
                            // E-mail enviado com sucesso
                            Toast.makeText(RecoverPasswordActivity.this, "E-mail de recuperação enviado.", Toast.LENGTH_SHORT).show();

                            // Lógica para login bem-sucedido, como iniciar outra Activity
                            Intent intent = new Intent(RecoverPasswordActivity.this, LoginCronus.class);
                            startActivity(intent);
                            finish();

                        }
                        else
                        {
                            // Erro ao tentar enviar o e-mail de recuperação
                            try
                            {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e)
                            {
                                // E-mail não registrado
                                Toast.makeText(RecoverPasswordActivity.this, "E-mail não registrado.", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e)
                            {
                                // Credenciais inválidas (geralmente não esperado nesse fluxo)
                                Toast.makeText(RecoverPasswordActivity.this, "Erro nas credenciais do usuário.", Toast.LENGTH_SHORT).show();
                            }
                            catch (FirebaseAuthException e)
                            {
                                // Erro de autenticação genérico
                                Log.e(TAG, "Erro de autenticação:", e);
                                Toast.makeText(RecoverPasswordActivity.this, "Erro ao enviar e-mail. Tente novamente.", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e)
                            {
                                // Erro inesperado
                                Log.e(TAG, "Erro inesperado:", e);
                                Toast.makeText(RecoverPasswordActivity.this, "Erro inesperado. Tente novamente.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }
}
