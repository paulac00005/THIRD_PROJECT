package com.example.paulac.third_project;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.paulac.third_project.Classes.Config;
import com.example.paulac.third_project.Classes.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    //Defining views
    private EditText etUsername, etPassword, etReenter, etAnswer;
    private Spinner security;
    private Button signup, cam, gallery;
    private ImageView iv;
    private static final int PICTURE_REQUEST = 1;
    private Uri filePath;
    public static final String uploadedfile = "image";
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Spinner security = (Spinner)findViewById(R.id.spinner);
        String[] questions = new String[]{"--","What is the name of your first pet?",
                "What is your favorite movie?",
                "What is your favorite food?",
                "What is your favorite drink?",
                "What is your favorite color?"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, questions);
        security.setAdapter(adapter);

        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etReenter = (EditText)findViewById(R.id.etReenter);
        etAnswer = (EditText)findViewById(R.id.etAnswer);
        gallery = (Button)findViewById(R.id.gallery);
        signup = (Button)findViewById(R.id.signup);
        iv = (ImageView)findViewById(R.id.imageView);

        cam.setOnClickListener(this);
        gallery.setOnClickListener(this);
        signup.setOnClickListener(this);

        if(iv != null){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
            iv.setLayoutParams(layoutParams);
            cam.setVisibility(View.GONE);
            gallery.setVisibility(View.GONE);
        }else{
            iv.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }}

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //Adding an employee
    private void addEmployee(){

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String key = "70930f27";
        String uploadedfile = getStringImage(bitmap);
        String security_question = security.getSelectedItem().toString().trim();
        String answer = etAnswer.getText().toString().trim();

        uploadImage(username, password, key, uploadedfile, security_question, answer);
    }

    private void uploadImage(final String username, final String password, final String key, final String uploadedfile ,final String security_question, final String answer){
        class AddEmployee extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SignUp.this,"Processing","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(SignUp.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {


                HashMap<String,String> params = new HashMap<>();

                params.put(Config.USERNAME,username);
                params.put(Config.PASSWORD,password);
                params.put(Config.KEY_AUTHENTICATION,key);
                params.put(uploadedfile, uploadedfile);
                params.put(Config.SECURITY_QUESTION, security_question);
                params.put(Config.ANSWER, answer);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_SIGNUP, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
    }

    @Override
    public void onClick(View v) {
        if(v == signup){
            addEmployee();
        }else if(v==cam){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICTURE_REQUEST);
        }
    }
}