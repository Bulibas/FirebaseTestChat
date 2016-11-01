package com.example.pc.firebasetestchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnReg;
    private EditText mailText;
    private EditText passwordText;
    private TextView logTextView;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();

        logTextView = (TextView)findViewById(R.id.logTextView);
        mailText=(EditText)findViewById(R.id.mailEditText);
        passwordText=(EditText)findViewById(R.id.passEditText);
        btnReg=(Button)findViewById(R.id.btnReg);
        logTextView = (TextView)findViewById(R.id.logTextView);

        progressDialog=new ProgressDialog(this);
        btnReg.setOnClickListener(this);
        logTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view==btnReg) {
            registerUser();
        }
        if(view==logTextView) {
            //open login activity
        }
    }

    private void registerUser() {
        String mail = mailText.getText().toString().trim();
        String pass = passwordText.getText().toString().trim();

        if(TextUtils.isEmpty(mail)) {
            Toast.makeText(this,"Please enter E-Mail!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)) {
            Toast.makeText(this,"Please enter password !", Toast.LENGTH_SHORT).show();
            return;
        }
        //if validations are ok
        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //registered
                    //TODO start profile activity
                    finish();
                    Toast.makeText(RegistrationActivity.this, "Registed successfully!", Toast.LENGTH_SHORT);
                    openMain();
                }
                else {
                    Toast.makeText(RegistrationActivity.this, "Registration error!", Toast.LENGTH_SHORT);
                }
                progressDialog.dismiss();

            }
        });
    }

    public void openMain() {
        Intent i = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(i);
    }
}
