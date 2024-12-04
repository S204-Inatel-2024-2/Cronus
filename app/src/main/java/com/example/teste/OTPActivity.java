package com.example.teste;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OTPActivity extends AppCompatActivity {

    private Button OTPAuthButton;
    private TextView backToAccountRecover, reenviarBotao;
    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    private boolean reenviarAtivado = false;
    private int resendTime = 60;
    private int selectedETPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        OTPAuthButton = findViewById(R.id.otp_auth_button);
        backToAccountRecover = findViewById(R.id.back_to_account_recover);
        reenviarBotao = findViewById(R.id.reenviar_otp);
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);

        Intent intent = getIntent();
        String verificationId = intent.getStringExtra("verificationId");

        StarTimeCounter();

        reenviarBotao.setOnClickListener(v -> {
            if (reenviarAtivado) {
                StarTimeCounter();
            }
        });

        OTPAuthButton.setOnClickListener(v -> {
            String generatedOTP = otp1.getText().toString() + otp2.getText().toString() +
                    otp3.getText().toString() + otp4.getText().toString() +
                    otp5.getText().toString() + otp6.getText().toString();

            if (generatedOTP.length() == 6) {
                // Lógica de verificação do OTP aqui
                Toast.makeText(this, "Verificado com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Código inválido!", Toast.LENGTH_SHORT).show();
            }
        });

        backToAccountRecover.setOnClickListener(v -> {
            startActivity(new Intent(this, RecoverPasswordActivity.class));
            finish();
        });
    }

    private void StarTimeCounter() {
        reenviarAtivado = false;
        reenviarBotao.setTextColor(Color.parseColor("#99000000"));

        new CountDownTimer(resendTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                reenviarBotao.setText("Reenviar código (" + (millisUntilFinished / 1000) + ")");
            }

            @Override
            public void onFinish() {
                reenviarAtivado = true;
                reenviarBotao.setText("Reenviar código");
                reenviarBotao.setTextColor(Color.parseColor("#85CEC3"));
            }
        }.start();
    }
}
