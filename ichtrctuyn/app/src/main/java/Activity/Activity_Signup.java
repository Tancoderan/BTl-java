package Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.Activity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import user.User;


public class Activity_Signup extends AppCompatActivity {
    private EditText emailuser,hoten,pass,comfirmpass;
    private Button sigup;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        init();
    }
    public void init(){
        hoten=findViewById(R.id.edthoten);
        emailuser=findViewById(R.id.edtemail);
        pass=findViewById(R.id.signuppass);
        comfirmpass=findViewById(R.id.edtcofirmpass);
        sigup=findViewById(R.id.btnsignup);
        progressDialog = new ProgressDialog(this);
        firebaseAuth= FirebaseAuth.getInstance();
        sigup.setOnClickListener(view -> signup());
    }
    public void signup() {
        String name = hoten.getText().toString().trim();
        String email = emailuser.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String passwordcomfirm = comfirmpass.getText().toString().trim();
        String address="",phone="",sex="";
        if (email.isEmpty()) {
            emailuser.setError("B???n ch??a nh???p email!");
            emailuser.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailuser.setError("Email kh??ng ????ng!");
            emailuser.requestFocus();
            return;
        }
        if (name.isEmpty()) {
            hoten.setError("B???n ch??a nh???p h??? t??n!");
            hoten.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            pass.setError("B???n c???n nh???p m???t kh???u!");
            pass.requestFocus();
            return;
        }
        if (password.length() < 8 || password.length() > 16) {
            pass.setError("M???t kh???u n??n d??i t??? 8 ????n 16 k?? t???");
            pass.requestFocus();
            return;
        }
        if (passwordcomfirm.isEmpty()) {
            comfirmpass.setError("B???n c???n x??c nh???n m???t kh???u!");
            comfirmpass.requestFocus();
            return;
        }
        if (!password.equals(passwordcomfirm)) {
            comfirmpass.setError("M???t kh???u x??c nh???n kh??ng ????ng");
            comfirmpass.requestFocus();
            return;
        }
        progressDialog.setMessage("??ang ????ng k??...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User(email, name, password,sex,phone,address);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        progressDialog.cancel();
                                        Toast.makeText(Activity_Signup.this, "Ch??c m???ng b???n ???? ????ng k?? th??nh c??ng!", Toast.LENGTH_LONG).show();

                                    } else {
                                        progressDialog.cancel();
                                        Toast.makeText(Activity_Signup.this, "????ng k?? th???t b???i, vui l??ng th??? l???i!", Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        progressDialog.cancel();
                        Toast.makeText(Activity_Signup.this, "????ng k?? th???t b???i, vui l??ng th??? l???i!", Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void loginn(View view){
        startActivity(new Intent(Activity_Signup.this, Login.class));
    }

}