package com.example.sendrecive;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.example.sendrecive.Models.ImportData;
import com.example.sendrecive.Models.Setting;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public   TextView fromdate,todate;
    SliderLayout sliderLayout;
    FloatingActionButton addSetting,fab_add_po,fab_print_po,fab_add_DSD,fab_print_DSD;
    LinearLayout first_linear,second_linear;
    public static DataBaseHandler dataBaseHandler;
    SQLiteDatabase database;
    public  static  String last_ipAddres="";
    String ip_str="",tel_str="",name_str="";
    int sameqty_value=0;
    Bitmap itemBitmapPic = null;
    public static final int PICK_IMAGE = 1;
     ImageView companeyLogo;
    Animation animation,anim_move_to_right,anim_move_to_left;
     AlphaAnimation buttonClick;
    ImportData importData;
Dialog ItemUnitsdialog;
    Calendar myCalendar;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBaseHandler = new DataBaseHandler(this);
        database = dataBaseHandler.getWritableDatabase();
        importData =new ImportData(MainActivity.this);
//        importData.getUnitData("01/12/2021","16/08/2022");
        initView();
        last_ipAddres = dataBaseHandler.getIP();
        buttonClick = new AlphaAnimation(1F, 0.2F);
        animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in);
        anim_move_to_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_to_right);
        anim_move_to_left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_to_left);
        first_linear.startAnimation(anim_move_to_right);
        second_linear.startAnimation(anim_move_to_left);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :
        setSliderViews();
        setSliderViews();

        addSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                showSettingDialog();
            }
        });
    }

    private void initView() {
        second_linear = findViewById(R.id.second_linear);
        sliderLayout = findViewById(R.id.imageSlider_2);
        first_linear = findViewById(R.id.first_linear);
        addSetting=(FloatingActionButton) findViewById(R.id.fab_add_setting);

        fab_add_po=(FloatingActionButton) findViewById(R.id.fab_add_po);
        fab_add_po.setOnClickListener(onClickListener);
        fab_print_po=(FloatingActionButton) findViewById(R.id.fab_print_po);
        fab_print_po.setOnClickListener(onClickListener);
        fab_add_DSD=(FloatingActionButton) findViewById(R.id.fab_add_DSD);
        fab_add_DSD.setOnClickListener(onClickListener);
        fab_print_DSD=(FloatingActionButton) findViewById(R.id.fab_print_DSD);
        fab_print_DSD.setOnClickListener(onClickListener);

    }

    private void printLastVoucher() {
        Intent i = new Intent(getApplicationContext(), BluetoothConnectMenu2.class);
        i.putExtra("printKey", "1");
        startActivity(i);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                itemBitmapPic = bitmap;
                companeyLogo.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private void showSettingDialog() {

        last_ipAddres=dataBaseHandler.getIP();
        Setting mySetting=new Setting();
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.setting_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);
        final Button saveButton = dialog.findViewById(R.id.save_setting);
        final Button cancelButton = dialog.findViewById(R.id.cancel_btn);
        final EditText companeyName = dialog.findViewById(R.id.companeyName);
        final EditText companeyTEL = dialog.findViewById(R.id.companeyTEL);
       companeyLogo = dialog.findViewById(R.id.companeyLogo);
        final EditText ipText = dialog.findViewById(R.id.ip_textview);
        final CheckBox sameQTY=dialog.findViewById(R.id.sameQTY);
        dialog.findViewById(R.id.copydata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                  verifyStoragePermissions(MainActivity.this);
                    copyFile();
                }
                catch (Exception e)
                {
                    verifyStoragePermissions(MainActivity.this);


                   Toast.makeText(MainActivity.this, "backup_failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        companeyLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);

            }
        });


        if(dataBaseHandler.getSetting()!=null)
        {
            mySetting=dataBaseHandler.getSetting();
            companeyName.setText(mySetting.getCOMPANEY_NAME());
            companeyTEL.setText(mySetting.getTEL());
            if(mySetting.getLOGO()!= null)
            {
                companeyLogo.setImageBitmap(mySetting.getLOGO());
            }
            else
            {companeyLogo.setImageDrawable(getResources().getDrawable(R.drawable.icon));}
            try{
                if( mySetting.getSAME_QUANTITY().equals("1"))
                {
                    sameQTY.setChecked(true);
                }
            }
            catch (Exception e)
            {
                sameQTY.setChecked(false);

            }

        }




        ip_str =ipText.getText().toString();
        if(!last_ipAddres.equals(""))
        {
            ipText.setText(last_ipAddres);

        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkqtyvalu=0;
                view.startAnimation(buttonClick);
                ip_str =ipText.getText().toString();
                name_str=companeyName.getText().toString();
                tel_str=companeyTEL.getText().toString();
                if(sameQTY.isChecked())
                {
                    sameqty_value=1;

                }
                else {
                    sameqty_value=0;
                }


                if(!ip_str.equals("")&& !name_str.equals(""))
                {
                    Setting newSetting=new Setting();
                    newSetting.setIP(ip_str);
                    newSetting.setCOMPANEY_NAME(name_str);
                    newSetting.setTEL(tel_str);
                    newSetting.setSAME_QUANTITY(sameqty_value+"");
                    newSetting.setIP(ip_str);
                    newSetting.setLOGO(itemBitmapPic);

                    dataBaseHandler.deleteSetting();
                    dataBaseHandler.addSetting(newSetting);
              //      getitemUnit();
                    dialog.dismiss();

                }
                else
                {   if(ip_str.equals(""))
                    ipText.setError("Required");

                    if(name_str.equals(""))
                    {companeyName.setError("Required");}
                }


            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setSliderViews() {
        for (int i = 0; i < 3; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(this);
//            sliderView.setImageScaleType(ImageView.ScaleType.FIT_END);
            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.first);


                    break;
                case 1:   sliderView.setImageDrawable(R.drawable.second);

                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.thered);
                    break;
                case 3:

                    sliderView.setImageDrawable(R.drawable.four);
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setDescription("Recive System  by Falcons Soft " ) ;
            final int finalI = i;
//
//            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
//                @Override
//                public void onSliderClick(SliderView sliderView) {
//                    Toast.makeText(MainActivity.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
//
//                }
//            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }
    private View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.fab_add_po:
                    view.startAnimation(animation);
                    Intent i=new Intent(MainActivity.this,Recive_PO.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);

                    break;
                case R.id.fab_print_po:
                    view.startAnimation(animation);
                    Intent print=new Intent(MainActivity.this,PrintPO.class);
                    print.putExtra("Key", "PO");
                    startActivity(print);
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                    break;
                case R.id.fab_add_DSD:
                    view.startAnimation(animation);
                    Intent iDSD=new Intent(MainActivity.this,Recive_Direct.class);
                    startActivity(iDSD);
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);

                    break;
                case R.id.fab_print_DSD:
                    view.startAnimation(animation);
                    Intent print_DSD=new Intent(MainActivity.this,PrintPO.class);
                    print_DSD.putExtra("Key", "DSD");
                    startActivity(print_DSD);
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                    break;

            }

        }
    };
    private void getitemUnit(){
        Log.e("getitemUnit","getitemUnit");
       importData=new ImportData(MainActivity.this);

        ItemUnitsdialog = new Dialog(MainActivity.this);
        ItemUnitsdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ItemUnitsdialog.setCancelable(true);
        ItemUnitsdialog.setContentView(R.layout.datefor_itemunit);

        fromdate= ItemUnitsdialog.findViewById(R.id.fromdate);
        todate= ItemUnitsdialog.findViewById(R.id.todate);

        myCalendar = Calendar.getInstance();
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String today = df.format(currentTimeAndDate);
        fromdate.setText(today);
        todate.setText(today);
        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, openDatePickerDialog(0), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, openDatePickerDialog(1), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ItemUnitsdialog.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemUnitsdialog.dismiss();
                dataBaseHandler.deleteItemUnit();
                importData.getUnitData(fromdate.getText().toString().trim(), todate.getText().toString().trim());

            }
        });

        ItemUnitsdialog.show();
    }
    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final int flag) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(flag);
            }

        };
        return date;
    }
    private void updateLabel(int flag) {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (flag == 0)
            fromdate.setText(sdf.format(myCalendar.getTime()));
        else

            todate.setText(sdf.format(myCalendar.getTime()));
    }
    public void copyFile()
    {
        try
        {
            File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File data = Environment.getDataDirectory();
            boolean isPresent = true;
            if (!sd.canWrite())
            {
                isPresent= sd.mkdir();

            }



            String backupDBPath = "ReciveDatabase";

            File currentDB= getApplicationContext().getDatabasePath("ReciveDatabase");
            File backupDB = new File(sd, backupDBPath);

            if (currentDB.exists()&&isPresent) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(MainActivity.this, "Backup Succesfulley", Toast.LENGTH_SHORT).show();
            }else {

                Toast.makeText(MainActivity.this, "Backup Failed", Toast.LENGTH_SHORT).show();
            }
            isPresent=false;


            
        }
        catch (Exception e) {
            Log.e("Settings Backup", e.getMessage());
        }
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        Log.e("permission",""+permission);

        ActivityCompat.requestPermissions( activity,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 1
        );
    }
}
