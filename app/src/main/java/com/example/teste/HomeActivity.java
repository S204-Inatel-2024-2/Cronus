package com.example.teste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {

    private ImageView menuIcon;
    private ImageView appLogo;
    private CheckBox checkBox;
    private LinearLayout checkListContainer;
    private CalendarView calendarView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private TextView nomeUserTextView;
    private TextView emailUserTextView;
    private static final String TAG = "HomeActivity";

    FirebaseFirestore db;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Certifique-se de que seu arquivo XML está com o nome correto

        // Inicializando os elementos de UI
        appLogo = findViewById(R.id.appLogo);
        checkListContainer = findViewById(R.id.checkListContainer);
        calendarView = findViewById(R.id.calendarView);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.home);

        //menu com credenciais
        View headerView = navigationView.getHeaderView(0);

        nomeUserTextView = headerView.findViewById(R.id.nomeUserContainer);
        emailUserTextView = headerView.findViewById(R.id.emailUserContainer);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        fetchTasksFromFirestore();
        fetchTaskDates();


        if (currentUser != null)
        {
            String userId = currentUser.getUid();
            db.collection("Usuários").document(userId).get()
                    .addOnCompleteListener(task ->
                    {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists())
                            {
                                String displayName = document.getString("nome");
                                nomeUserTextView.setText(displayName != null ? displayName : "Nome indisponível");

                                String email = currentUser.getEmail();
                                emailUserTextView.setText(email != null ? email : "Email indisponível");
                            }
                            else
                            {
                                Log.e(TAG, "Documento do usuário não encontrado.");
                                nomeUserTextView.setText("-");
                                emailUserTextView.setText("--");
                            }
                        }
                        else
                        {
                            Log.e(TAG, "Erro ao acessar Firestore: ", task.getException());
                            nomeUserTextView.setText("Erro");
                            emailUserTextView.setText("Erro");
                        }
                    });
        }
        else
        {
            Log.e(TAG, "Usuário não autenticado");
        }

        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.abrir, R.string.fechar); //controlador do menu lateral
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.atividades:
                    {
                        Intent intent = new Intent(HomeActivity.this, AllTasks.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.deslogar:
                    {
                        FirebaseAuth.getInstance().signOut(); // Encerra a sessão
                        Intent intent = new Intent(HomeActivity.this, LoginCronus.class);
                        startActivity(intent);
                        break;
                    }
                }
                return false;
            }
        });

        //------------------------------------------------------------------------------------------


        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            String userName = nomeUserTextView.getText().toString().trim();

            // Obter o UID do usuário logado
            if (currentUser == null) {
                // Usuário não autenticado
                return;
            }
            String userId = currentUser.getUid();

            // Criar a referência à coleção no Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("tasks")
                    .whereEqualTo("date", selectedDate)  // A consulta que busca pela data
                    .whereEqualTo("userId", userId)  // Adicionando o filtro para o userId
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Se a data já existir no Firestore para o usuário, redireciona para a página exibição de tarefas
                            Intent intent = new Intent(HomeActivity.this, IndividualTasks.class);  // Alterar para a página desejada
                            intent.putExtra("selectedDate", selectedDate);
                            startActivity(intent);

                        } else {
                            // Se não encontrar a data para o usuário, redireciona para a página de registro de tarefas
                            Intent intent = new Intent(HomeActivity.this, TaskPage.class);  // Alterar para a página desejada
                            intent.putExtra("selectedDate", selectedDate);
                            intent.putExtra("userName", userName);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(HomeActivity.this, "Erro ao verificar a data", Toast.LENGTH_SHORT).show();
                    });
        });

    }

    @Override
    public void onBackPressed()
    {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void fetchTaskDates()
    {
        // Referência à coleção "tasks"
        db.collection("tasks")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            // Pega a data armazenada no campo "date"
                            String dateString = document.getString("date");


                            if (dateString != null)
                            {
                                Log.d("Firestore", "Data encontrada: " + dateString);
                            } else {
                                Log.d("Firestore", "Documento sem data ou campo 'date' ausente.");
                            }
                        }
                    } else {
                        Log.d("Firestore", "Nenhum documento encontrado na coleção.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Erro ao recuperar os documentos: ", e);
                });
    }

    private void fetchTasksFromFirestore() {
        // Pega o ID do usuário atual
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Consulta ao Firestore para pegar todas as tarefas do usuário, ordenadas por prioridade
        db.collection("tasks")
                .whereEqualTo("userId", userId) // Garantir que estamos pegando as tarefas do usuário correto
                .orderBy("priority")  // Ordena pelas prioridades
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String taskTitle = document.getString("title"); // Pega o título da tarefa
                                int priority = document.getLong("priority").intValue(); // Pega a prioridade
                                addTaskToUI(taskTitle, priority); // Adiciona na UI com a prioridade
                            }
                        } else {
                            Toast.makeText(HomeActivity.this, "Nenhuma tarefa encontrada", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Log.e(TAG, "Erro ao carregar tarefas: ", task.getException());
                        Toast.makeText(HomeActivity.this, "Erro ao carregar tarefas", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addTaskToUI(String taskTitle, int priority) {
        // Cria um LinearLayout para o retângulo (a tarefa)
        LinearLayout taskContainer = new LinearLayout(this);
        taskContainer.setOrientation(LinearLayout.HORIZONTAL);
        taskContainer.setPadding(16, 16, 16, 16);

        // Cor de fundo dependendo da prioridade
        int backgroundColor = getPriorityColor(priority);
        taskContainer.setBackgroundColor(backgroundColor); // Definindo a cor de fundo

        // Cria a CheckBox
        CheckBox checkBox = new CheckBox(this);
        checkBox.setPadding(8, 8, 8, 8);

        // Cria o TextView para exibir o título da tarefa
        TextView taskView = new TextView(this);
        taskView.setText(taskTitle);  // Define o título da tarefa
        taskView.setTextSize(18);     // Define o tamanho da fonte
        taskView.setPadding(16, 0, 0, 0); // Adiciona padding para não colar o texto na checkbox

        // Adiciona a CheckBox e o TextView ao LinearLayout
        taskContainer.addView(checkBox);
        taskContainer.addView(taskView);

        // Adiciona o LinearLayout com a tarefa ao checkListContainer
        checkListContainer.addView(taskContainer);
    }

    private int getPriorityColor(int priority) {
        // Retorna a cor de fundo dependendo da prioridade
        switch (priority) {
            case 1: // Baixa
                return getResources().getColor(R.color.color_low_priority);
            case 2: // Média
                return getResources().getColor(R.color.color_medium_priority);
            case 3: // Alta
                return getResources().getColor(R.color.color_high_priority);
            default:
                return getResources().getColor(R.color.color_default_priority); // Caso padrão
        }
    }

}