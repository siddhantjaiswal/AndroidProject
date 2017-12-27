package com.example.siddhantjaiswal.sendit;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Settings extends AppCompatActivity {

    ImageView b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://sendit-16aa6.appspot.com").child("mountains.jpg");
        try {

    final File localFile = File.createTempFile("mountain", "jpg");
    storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
            ImageView sid = (ImageView) findViewById(R.id.sid);
            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
           //bitmap.setWidth(200);
            sid.setImageBitmap(bitmap);
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {

        }
    });
        } catch (IOException e ) {}



        b=(ImageView)findViewById(R.id.sid);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });

        TextView name = (TextView)findViewById(R.id.textView);
        name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }


    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 50);
                    intent.putExtra("aspectY", 50);
                    intent.putExtra("outputX", 500);
                    intent.putExtra("outputY", 500);
                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView viewImage = (ImageView) findViewById(R.id.sid);

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    viewImage.setImageBitmap(bitmap);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Bundle extras2 = data.getExtras();
                if (extras2 != null) {
                    Bitmap photo = extras2.getParcelable("data");
                    viewImage.setImageBitmap(photo);
                    viewImage.setDrawingCacheEnabled(true);
                    viewImage.buildDrawingCache();
                    Bitmap bitmap = viewImage.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] data1 = baos.toByteArray();
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference mountainsRef = storageRef.child("mountains.jpg");
                    StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");
                    mountainsRef.getName().equals(mountainImagesRef.getName());
                    UploadTask uploadTask = mountainsRef.putBytes(data1);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        }
                    });
                }
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
    public void save(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void help(View view){
        try {
            final CharSequence[] options = {"Email Us", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
           builder.setTitle("NEED HELP");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Email Us")) {
                        Log.i("Send email", "");
                        String[] TO = {"siddhantjai9098@gmail.com"};
                        String[] CC = {""};
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                        emailIntent.putExtra(Intent.EXTRA_CC, CC);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "SenDiT");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                        try {
                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                            finish();
                            Log.i("Finished sending email.", "");
                        }
                        catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(Settings.this,
                                    "There is no email client installed.", Toast.LENGTH_SHORT).show();}

                    }
                    else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }

                }
            });
            builder.show();
        }catch (Exception e){}
    }

    public void status_user(View view){
        Intent Status = new Intent(this, Status_user.class);
        startActivity(Status);
    }
}
