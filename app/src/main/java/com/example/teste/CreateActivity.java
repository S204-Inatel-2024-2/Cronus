package com.example.teste;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateActivity extends AppCompatActivity {

    private static final String TAG = "CreateActivity";
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private Button createAccountButton;
    String usuarioID;
    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create); // Use o layout correto

        FirebaseApp.initializeApp(this);

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
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty())
                {
                    // Exibir uma mensagem de erro
                    Toast.makeText(CreateActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
                else if (!password.equals(repeatPassword))
                {
                    // Exibir erro se as senhas não coincidirem
                    Toast.makeText(CreateActivity.this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CadastrarUsuario(v, name, email, password);
                }
            }
        });
    }

    private void CadastrarUsuario(View v, String name, String email, String password)
    {
        // Cadastro do usuário no Firebase
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            salvarDadosUsuario(name, email); // Salvar nome, email e telefone
                            Log.d(TAG, "Cadastro realizado com sucesso.");
                            Toast.makeText(CreateActivity.this, "Conta criada!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreateActivity.this, LoginCronus.class);
                            startActivity(intent);
                            finish();
                        } else
                        {
                            // Tratamento de exceções para exibir mensagens específicas
                            String mensagemErro;
                            try
                            {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e)
                            {
                                mensagemErro = "A senha deve ter pelo menos 6 caracteres.";
                            } catch (FirebaseAuthInvalidCredentialsException e)
                            {
                                mensagemErro = "O formato do e-mail é inválido.";
                            } catch (FirebaseAuthUserCollisionException e)
                            {
                                mensagemErro = "Este e-mail já está cadastrado.";
                            } catch (Exception e)
                            {
                                mensagemErro = "Erro ao criar conta: " + e.getMessage();
                            }
                            // Exibir a mensagem de erro
                            Toast.makeText(CreateActivity.this, mensagemErro, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Erro ao criar conta", task.getException());
                        }
                    }
                });
    }

    private void salvarDadosUsuario(String name, String email)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Criar um mapa com os dados do usuário
        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome", name);
        usuarios.put("email", email);

        // Obter o ID do usuário autenticado
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Salvar os dados no Firestore
        DocumentReference documentoReferencia = db.collection("Usuários").document(usuarioID);
        documentoReferencia.set(usuarios)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("db", "Sucesso ao salvar os dados!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db", "Falha ao salvar os dados: " + e.toString());
                    }
                });
    }
}
