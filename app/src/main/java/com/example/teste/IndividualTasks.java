package com.example.teste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;



public class IndividualTasks extends AppCompatActivity {

    private LinearLayout checkListContainer; // Aonde os "bloquinhos" vão ser adicionados
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_tasks);

        checkListContainer = findViewById(R.id.individual_checkListContainer);

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String selectedDate = intent.getStringExtra("selectedDate");

        // Supondo que você tenha uma variável userId para identificar o usuário
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Recuperar as atividades do usuário específico
        db.collection("tasks")
                .whereEqualTo("userId", userId) // Filtrar pelo ID do usuário
                .whereEqualTo("date", selectedDate)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Pega os dados do Firestore
                            String titulo = document.getString("title");
                            String descricao = document.getString("description");
                            String prioridade = document.getString("priority_string");
                            String hora = document.getString("time");
                            String activityId = document.getId(); // Pegar o ID do documento para deletar depois

                            // Criar um objeto Atividade
                            Atividade atividade = new Atividade(titulo, descricao, prioridade, hora, activityId);

                            // Adicionar o "bloquinho" de atividade na tela
                            addAtividadeToUI(atividade);
                        }
                    } else {
                        Log.d("Firestore", "Erro ao obter documentos.", task.getException());
                    }
                });
    }

    private void addAtividadeToUI(Atividade atividade) {
        // Inflar o layout do "bloquinho" de atividade
        LinearLayout atividadeLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_atividade, null);

        TextView tituloTextView = atividadeLayout.findViewById(R.id.activity_title);
        TextView descricaoTextView = atividadeLayout.findViewById(R.id.activity_description);
        TextView horaDataTextView = atividadeLayout.findViewById(R.id.activity_time_and_date);
        ImageButton btnDelete = atividadeLayout.findViewById(R.id.btn_delete);

        // Setando os valores dos campos no layout
        tituloTextView.setText(atividade.getTitulo());
        descricaoTextView.setText(atividade.getDescricao());
        horaDataTextView.setText(atividade.getHora());

        // Ação de excluir a atividade
        btnDelete.setOnClickListener(view -> {
            // Apagar do Firestore
            db.collection("tasks").document(atividade.getActivityId()).delete()
                    .addOnSuccessListener(aVoid -> {
                        // Remover o "bloquinho" da tela
                        checkListContainer.removeView(atividadeLayout);
                        Log.d("Firestore", "Atividade excluída com sucesso");
                    })
                    .addOnFailureListener(e -> Log.d("Firestore", "Erro ao excluir atividade", e));
        });

        // Adicionar o layout à interface
        checkListContainer.addView(atividadeLayout);
    }
}