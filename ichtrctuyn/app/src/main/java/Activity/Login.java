package Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Activity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import Database.DatabaseHelper;

public class Login extends AppCompatActivity {
    private Button buttonlog,buttonsig;
    private EditText emailuser,passuser;
    private TextView forgotpass;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        init();
    }
    public void init() {
        emailuser = findViewById(R.id.useremail);
        passuser = findViewById(R.id.pass);
        forgotpass = findViewById(R.id.edtforgotpass);
        buttonlog = (Button) findViewById(R.id.btlogin);
        progressDialog= new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
        forgotpass.setOnClickListener(view -> {
            Intent i =new Intent(Login.this, ResetPass.class);
            startActivity(i);
        });
        buttonlog.setOnClickListener(view -> login());
    }
    public void login(){
        String email=emailuser.getText().toString().trim();
        String password=passuser.getText().toString().trim();
        if(email.isEmpty()){
            emailuser.setError("B???n c???n nh???p email!");
            emailuser.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailuser.setError("Email kh??ng ????ng vui l??ng ki???m tra l???i!");
            emailuser.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passuser.setError("B???n ch??a nh???p m???t kh???u!");
            passuser.requestFocus();
            return;
        }
        progressDialog.setMessage("??ang ????ng nh???p...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        progressDialog.cancel();
                        Intent i = new Intent(Login.this, Normal_User.class);
                        startActivity(i);
                    }
                    else {
                        progressDialog.cancel();
                        user.sendEmailVerification();
                        Toast.makeText(Login.this,"Ki???m tra email ????? x??c nh???n",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    progressDialog.cancel();
                    Toast.makeText(Login.this, "????ng nh???p th???t b???i vui l??ng ki???m tra l???i th??ng tin", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}