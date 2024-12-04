package com.example.teste;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TaskPage extends AppCompatActivity {

    private EditText taskTimeEditText;
    private EditText taskTitle;
    private EditText taskDescription;
    private Button saveButton;
    private RadioGroup priorityGroup;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_page);

        taskTitle = findViewById(R.id.task_title);
        taskDescription = findViewById(R.id.task_description);
        taskTimeEditText = findViewById(R.id.task_time);
        priorityGroup = findViewById(R.id.priority_group);
        saveButton = findViewById(R.id.save_button);

        createNotificationChannel();
        taskTimeEditText.setOnClickListener(v -> showTimePickerDialog());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String selectedDate = intent.getStringExtra("selectedDate");
        String userName = intent.getStringExtra("userName");

        saveButton.setOnClickListener(v -> {
            String titulo = taskTitle.getText().toString().trim();
            String descricao = taskDescription.getText().toString().trim();
            String time = taskTimeEditText.getText().toString().trim();

            int selectedPriorityId = priorityGroup.getCheckedRadioButtonId();
            String priority = "";

            if (selectedPriorityId == R.id.priority_low) {
                priority = "Baixa";
            } else if (selectedPriorityId == R.id.priority_medium) {
                priority = "Média";
            } else if (selectedPriorityId == R.id.priority_high) {
                priority = "Alta";
            }

            if (titulo.isEmpty() || descricao.isEmpty() || time.isEmpty() || priority.isEmpty() || selectedDate == null) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }


            // Parse a data e hora para um objeto Calendar
            String[] timeParts = time.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            String[] dateParts = selectedDate.split("/"); // Exemplo de formato: "4/12/2024"
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1; // Meses começam em 0
            int year = Integer.parseInt(dateParts[2]);


            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, hour, minute, 0); // Configura a data e hora
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            System.out.println("Hora configurada: " + calendar.getTime());
            System.out.println("Hora atual: " + Calendar.getInstance().getTime());


            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                Toast.makeText(this, "Data e hora devem ser no futuro", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent notify_intent = new Intent(TaskPage.this, NotificationReceiver.class);
            notify_intent.putExtra("taskTitle", titulo);
            notify_intent.putExtra("taskDescription", descricao);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    TaskPage.this,
                    0,
                    notify_intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            if (alarmManager != null) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );

                Toast.makeText(this, "Notificação programada para " + selectedDate + " às " + time, Toast.LENGTH_SHORT).show();
            }

            saveTaskToFireStore(titulo, descricao, time, priority, selectedDate, userName);

        });
    }


    private void saveTaskToFireStore(String title, String description, String time, String priority, String selectedDate, String userName) {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = currentUser.getEmail();
        int prioridadeInteiro;
        Map<String, Object> task = new HashMap<>();
        task.put("title", title);
        task.put("description", description);
        task.put("time", time);

        if(priority.equals("Alta"))
        {
            prioridadeInteiro = 3;
            task.put("priority", prioridadeInteiro);
        }
        else if(priority.equals("Média"))
        {
            prioridadeInteiro = 2;
            task.put("priority", prioridadeInteiro);
        }
        else if(priority.equals("Baixa"))
        {
            prioridadeInteiro = 1;
            task.put("priority", prioridadeInteiro);
        }
        task.put("priority_string", priority);
        task.put("date", selectedDate);
        task.put("email", email);
        task.put("userName", userName);
        task.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());

        db.collection("tasks")
                .add(task)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TaskPage.this, HomeActivity.class);
                    intent.putExtra("taskTitle", title);
                    intent.putExtra("taskDescription", description);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Erro ao salvar tarefa.", Toast.LENGTH_SHORT).show());
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int selectedHour, int selectedMinute) -> {
                    taskTimeEditText.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                },
                hour, minute, true);
        timePickerDialog.show();
    }

    private void createNotificationChannel()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            CharSequence name = "Lembrete";
            String description = "Lembrete de atividade";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel =  new NotificationChannel("notify", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
