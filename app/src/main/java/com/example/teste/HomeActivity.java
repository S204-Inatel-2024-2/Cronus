package com.example.teste;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private ImageView menuIcon;
    private ImageView appLogo;
    private EditText noteInput;
    private EditText checkListInput;
    private CheckBox checkBox;
    private CalendarView calendarView;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Certifique-se de que seu arquivo XML está com o nome correto

        // Inicializando os elementos de UI
        menuIcon = findViewById(R.id.menuIcon);
        appLogo = findViewById(R.id.appLogo);
        noteInput = findViewById(R.id.noteInput);   
        checkListInput = findViewById(R.id.checkListInput);
        checkBox = findViewById(R.id.checkBox); // Para um exemplo simples
        calendarView = findViewById(R.id.calendarView);

        // Exemplo de clique no ícone de menu
        menuIcon.setOnClickListener(v -> Toast.makeText(this, "Menu clicado!", Toast.LENGTH_SHORT).show());

        // Exemplo de clique na logo
        appLogo.setOnClickListener(v -> Toast.makeText(this, "Logo clicada!", Toast.LENGTH_SHORT).show());

        // Exemplo de interação com as notas
        findViewById(R.id.notesContainer).setOnClickListener(v -> {
            String note = noteInput.getText().toString().trim();
            if (!note.isEmpty()) {
                Toast.makeText(this, "Nota adicionada: " + note, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Adicione uma nota!", Toast.LENGTH_SHORT).show();
            }
        });

        // Exemplo de interação com o check-list
        checkListInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String item = checkListInput.getText().toString().trim();
                if (!item.isEmpty()) {
                    // Aqui você pode adicionar o item a uma lista ou fazer outras operações
                    Toast.makeText(this, "Item do Check-list adicionado: " + item, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Listener de data no calendário
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            Toast.makeText(this, "Data selecionada: " + selectedDate, Toast.LENGTH_SHORT).show();
        });
    }
}
