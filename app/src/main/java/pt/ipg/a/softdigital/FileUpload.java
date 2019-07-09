package pt.ipg.a.softdigital;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FileUpload extends AppCompatActivity implements View.OnClickListener{

    private TextView notification;

    Uri pdfUri;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(); // retorna um objeto do Firebase DataBase
    StorageReference storageReference =  FirebaseStorage.getInstance().getReference("Files"); // retorna um objeto  do Firebase Storage

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);

        // Botões
        findViewById(R.id.select_file_button).setOnClickListener(this);
        findViewById(R.id.upload_button).setOnClickListener(this);
        notification = findViewById(R.id.notification);
    }

    @Override
    public void onClick(View view) {

        int i = view.getId();

        if (i == R.id.select_file_button){
            if(ContextCompat.checkSelfPermission(FileUpload.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                selectPdf();
            }
            else{
                ActivityCompat.requestPermissions(FileUpload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
            }
        }
        if (i == R.id.upload_button){
            if(pdfUri != null ){ // o utilizador selecionou o ficheiro
                uploadFile(pdfUri);
            }else {
                Toast.makeText(FileUpload.this, R.string.select_file, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectPdf();
        }else{
            Toast.makeText(FileUpload.this, R.string.select_file_permissiondenied, Toast.LENGTH_SHORT).show();
        }
    }

    private void selectPdf(){

        Intent intent = new Intent();
        intent.setType("aplication/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            pdfUri = data.getData(); // retorna o uri do ficheiro selecionado
            notification.setText(R.string.selected_file+ data.getData().getLastPathSegment());
        }
        else{
            Toast.makeText(FileUpload.this, R.string.select_file, Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFile(Uri pdfUri){

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle(R.string.uploading_file);
        progressDialog.setProgress(0);
        progressDialog.show();


        StorageReference reference = storageReference.child("Files/"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete());
                        Uri url = uri.getResult();

                        UploadPdf uploadPdf = new UploadPdf(url.toString());

                        databaseReference.child(databaseReference.push().getKey()).setValue(uploadPdf);
                        Toast.makeText(FileUpload.this,R.string.upload_file_success, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();

                progressDialog.setMessage(R.string.selected_file+(int)progress+"%");

            }
        });

//        final String fileName = System.currentTimeMillis()+"";
//
//        StorageReference storageReference = storage.getReference(); // retorna root path
//
//        storageReference.child("Files").child(fileName).putFile(pdfUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        String url = StorageReference.getDownloadUrl(); // retorna o url do ficheiro enviado
//                        DatabaseReference reference = database.getReference(); // retorna o root path
//
//                        reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//
//                                if(task.isSuccessful()){
//                                    Toast.makeText(FileUpload.this, R.string.upload_file_success, Toast.LENGTH_SHORT).show();
//                                }else {
//                                    Toast.makeText(FileUpload.this, R.string.upload_file_failed, Toast.LENGTH_SHORT).show();
//                                }
//
//                            }
//                        });
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                Toast.makeText(FileUpload.this, R.string.upload_file_failed, Toast.LENGTH_SHORT).show();
//
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//
//                int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
//                progressDialog.setProgress(currentProgress);
//            }
//        });
    }
}
