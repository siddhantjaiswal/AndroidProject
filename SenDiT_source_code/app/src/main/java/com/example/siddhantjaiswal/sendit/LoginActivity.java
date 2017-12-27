package com.example.siddhantjaiswal.sendit;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.provider.*;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class LoginActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    RelativeLayout activity_login1;
    FloatingActionButton fab;
    static int adg = 10;
    private FirebaseListAdapter<ChatMessage> adapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {

            final CharSequence[] options = {"Yes", "No"};
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Click Yes To SignOut");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    dialog.dismiss();
                    if (options[item].equals("Yes")) {
                        adg = 1;

                    } else if (options[item].equals("No")) {
                        adg = 0;
                        dialog.dismiss();
                    }

                }
            });builder.show();}
            if (adg == 1) {

                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Snackbar.make(activity_login1, "You have Signed Out", Snackbar.LENGTH_SHORT).show();
                        setContentView(R.layout.activity_login1);
                    }
                });
            }


        if (item.getItemId() == R.id.aboutus) {
            Intent intent1 = new Intent(this, AboutUs.class);
            startActivity(intent1);
            this.finish();
        }
        if (item.getItemId() == R.id.theme) {
            selectImage();

        }
        if (item.getItemId() == R.id.Settings) {
            Intent intent2 = new Intent(this, Settings.class);
            startActivity(intent2);
            this.finish();
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login1);

            activity_login1 = (RelativeLayout) findViewById(R.id.activity_login1);
            fab = (FloatingActionButton) findViewById(R.id.fab);
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setTheme(R.style.AppTheme).build(), SIGN_IN_REQUEST_CODE);
            }
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Snackbar.make(activity_login1, "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();

                displayChatMessage();
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.logo)
                                .setContentTitle(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                                .setContentText("Team SenDiT Welcomess You!!");
                Intent resultIntent = new Intent(this, LoginActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addParentStack(LoginActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int qwer = resultIntent.toString().length();
                mNotificationManager.notify(qwer, mBuilder.build());

            }
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText input = (EditText) findViewById(R.id.input);
                    if(input.getText().toString().trim().length()==0){

                    }
                    else{
                    FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(input.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                    input.setText("");}
                }
            });

           }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit",
                Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    private void displayChatMessage() {
        try {
            ListView listOfMessage = (ListView) findViewById(R.id.list_of_message);

            adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference()) {
                @Override
                protected void populateView(View v, ChatMessage model, int position) {
                    TextView messageText, messageUser, messageTime;
                    messageText = (TextView) v.findViewById(R.id.message_text);
                    messageUser = (TextView) v.findViewById(R.id.message_user);
                    messageTime = (TextView) v.findViewById(R.id.message_time);
                    messageText.setText(model.getMessageText());
                    messageUser.setText(model.getMessageUser());
                     String[] separated = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");
                    String qwerty = separated[0];

                    if(messageUser.getText().toString().trim().equals(qwerty.toString().trim())){
                        messageText.setBackgroundColor(Color.parseColor("#ADD8E6"));
                    }
                    else {
                        messageText.setBackgroundColor(Color.WHITE);
                    }
                    messageText.setPadding(20,10,50,20);
                    messageTime.setText(DateFormat.format("dd-MM-yy (hh:mm)", model.getMessageTime()));
                }
            };
            listOfMessage.setAdapter(adapter);
        } catch (Exception e) {
        }
    }

    private void selectImage() {
try{
        final CharSequence[] options = {"Kiss", "nature", "Love", "Default", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Choose Theme!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Kiss")) {
                    RelativeLayout siddhant = (RelativeLayout) findViewById(R.id.activity_login1);
                    siddhant.setBackgroundResource(R.drawable.kiss);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 0);
                    intent.putExtra("aspectY", 0);
                    intent.putExtra("outputX", 200);
                    intent.putExtra("outputY", 300);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (options[item].equals("nature")) {
                    RelativeLayout siddhant = (RelativeLayout) findViewById(R.id.activity_login1);
                    siddhant.setBackgroundResource(R.drawable.nature1);
                } else if (options[item].equals("Default")) {
                    RelativeLayout siddhant = (RelativeLayout) findViewById(R.id.activity_login1);
                    siddhant.setBackgroundResource(R.drawable.lite);
                } else if (options[item].equals("Love")) {
                    RelativeLayout siddhant = (RelativeLayout) findViewById(R.id.activity_login1);
                    siddhant.setBackgroundResource(R.drawable.love);
                }
            }
        });
        builder.show();}catch(Exception e){}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == SIGN_IN_REQUEST_CODE) {
                if (requestCode == RESULT_OK) {
                    Snackbar.make(activity_login1, "Succssfully Signed In", Snackbar.LENGTH_SHORT).show();
                    displayChatMessage();
                } else {
                    Snackbar.make(activity_login1, "We couldn't sign You in Please try Again Later", Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            }

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
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("pathofimagefromgallery", picturePath + "");
                }
            }
        }
        catch (Exception e) {
        }
    }
}
