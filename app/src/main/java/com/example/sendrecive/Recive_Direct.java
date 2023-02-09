package com.example.sendrecive;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.sendrecive.Models.DSD_VendorItems;
import com.example.sendrecive.Models.DSD_VendourInfo;
import com.example.sendrecive.Models.ReciveDetail;
import com.example.sendrecive.Models.ReciveMaster;
import com.example.sendrecive.Models.Setting;
import com.example.sendrecive.Models.VendorInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Recive_Direct extends AppCompatActivity {
    ListView listview_area;
    EditText supplier_No, transaction_no, item_no,supplierVoucherNo,qty,free_qty;
    TextView supplier_name, item_name, total_category_qty,reciver_category_qty,price,date,total;
    private static DataBaseHandler dataBaseHandler;
    int codeState=-1;
    int index=0;
    double totalValue=0;

    ArrayAdapter<String> adapter;
    SweetAlertDialog pdValidation;
    String selectedCustomerId="",fd_text="";
    String description="";
    public static List<ReciveMaster> reciveListMaster_DSD = new ArrayList<>();
    public static List<DSD_VendorItems> itemInfoList = new ArrayList<>();
    double pricevalue=0.0;
    Context context;
    String suplier_text, itemNo_text, reciverQty_text, dateExpire_text, reciverCategory_text, voucherNo_text,qtyText;
    FloatingActionButton addQty;
    private TableLayout tableCheckData;
    double qty_total = 0, bonusQty = 0,qtyValue=0;
    Calendar myCalendar;
    String today;
    int counter=0,currentYear=0;
    public  static  int position=1;
    TextView GrnTextView;

    Button save, cancel;
    public  static  List<ReciveDetail> reciveDetailList_DSD;
    SQLiteDatabase database;
    JSONObject jsonObjectMASTER=null;
    JSONObject jsonObjectDetail = null;
    JSONObject finalObject=null;
    public  static  String ipAddres="";
    JSONArray j;
    Animation animation;
     AlphaAnimation buttonClick;
    ImageView calenderdialog_image,search_image,edit_image;
    Dialog dialog;
    String day="",month="",year="";
    int intday=0,intmonth=0,intyeay=0;
    boolean dayCorrect=false,monthCorrect=false,yearCorrect=false,leepYear=false,monthTow=false;
    String fullDate="";
    LinearLayout linearDate;
    EditText day_editText,month_editText,year_editText;
    Button saveButton;
    private List<String> listAreaName=new ArrayList<>();
    private List<String> listAreaName_filtered=new ArrayList<>();
    private  List<VendorInfo> listAllVendor=new ArrayList<>();
    private  List<VendorInfo> listAllVendor_filtered=new ArrayList<>();
//    Spinner unitSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recive__direct);
        context = getApplicationContext();
        dataBaseHandler = new DataBaseHandler(this);
        database = dataBaseHandler.getWritableDatabase();
        animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in);
        buttonClick = new AlphaAnimation(1F, 0.1F);
        Setting mySetting=new Setting();
        ipAddres=dataBaseHandler.getIP();
        initView();
        getMaxSerial("DSD");

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        today = df.format(currentTimeAndDate);
        date.setText(convertToEnglish(today));
        supplier_No.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            getData(supplier_No.getText().toString());
//                            getData("2010101010305");


                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        qty.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if(!free_qty.getText().equals(""))
                            {
                                free_qty.setSelectAllOnFocus(true);
                            }
                            free_qty.requestFocus();

                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        free_qty.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                        date.requestFocus();
                        linearDate.setBackground(getResources().getDrawable(R.drawable.edite_fucasable));

                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        date.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            linearDate.setBackground(getResources().getDrawable(R.drawable.edittext_white_border));

                            addItemTotable();

                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        item_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                suplier_text = supplier_No.getText().toString().trim();
                itemNo_text = item_no.getText().toString().trim();
                if ((!TextUtils.isEmpty(suplier_text)) && (!TextUtils.isEmpty(itemNo_text))) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        switch (i) {
                            case KeyEvent.KEYCODE_DPAD_CENTER:
                            case KeyEvent.KEYCODE_ENTER:
                                if ((!item_no.getText().equals("")) && (!supplier_No.getText().equals(""))) {
                                    suplier_text = supplier_No.getText().toString();
                                    itemNo_text = item_no.getText().toString();
                                    getItemInfo(suplier_text, itemNo_text);
                                    fillUnitspinner();
//                                    getItemInfo("402001011","6281073260841");
//                                    ITEMCODE=6281073260841&VENDOR=402001011


//
                                }
                                return true;
                            default:
                                break;
                        }
                    }
                } else {
                    if (TextUtils.isEmpty(suplier_text))
                        supplier_No.setError("Required");

                    if (TextUtils.isEmpty(itemNo_text))
                    {
//                        item_no.setError("Required");
//                        item_no.requestFocus();
                    }

                }

                return false;
            }
        });

    }
    private void displayErrorDialog(String mesage) {


        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("عذرا ...")
                .setContentText(mesage+"!")
                .show();

    }

    private void getItemInfo(String suplier_text, String itemNo_text) {
        itemInfoList.clear();
//        final String url = "http://10.0.0.22:8081/GetVendorInfo?VENDOR=2010101010305";
//        final String url = "http://"+ipAddres+"/GetVendorItems?ITEMCODE="+itemNo_text+"&VENDOR="+suplier_text;
        final String url = "http://"+ipAddres+"/GetItems?ITEMCODE="+itemNo_text;
        Log.e("url",url);
        //[{"AccCode":"402001011","ItemOCode":"6281073260841","ItemNameA":"عسل السنبلة اكاسيا 12 * 250 غم","PRICE":"5.375","TAXPERC":"16"}]
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
//                            Log.e("onResponse: ", "" + jsonArray.getString(0));
                            JSONObject transactioRecive = jsonArray.getJSONObject(0);
                            DSD_VendorItems itemInfo = new DSD_VendorItems();
//                            itemInfo.setAccCode(transactioRecive.getString("AccCode"));
                            itemInfo.setItemOCode(transactioRecive.getString("ItemOCode"));
                            itemInfo.setItemNameA(transactioRecive.getString("ItemNameA"));
//                            itemInfo.setPRICE(transactioRecive.getString("PRICE"));
                            itemInfo.setPRICE(transactioRecive.getString("LASTPRICE"));
                            itemInfo.setTAXPERC(transactioRecive.getString("TAXPERC"));
                            try {
                                itemInfo.setF_D(transactioRecive.getString("F_D"));
                            }catch ( Exception e){
                                itemInfo.setF_D(itemInfo.getPRICE().toString());
                            }

//                            Log.e("itemInfo",""+itemInfo.getF_D());
                            setData(itemInfo);
                            itemInfoList.add(itemInfo);
//                                addToDB();

                        } catch (Exception e) {
                            Log.e("Exception", "" + e.getMessage());
                        }
                    }


                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.getCause() instanceof JSONException)
                {item_no.setSelection(item_no.getText().length() , 0);
                    displayErrorDialog("رقم المادة غير موجود");

                }
                else
                if ((error instanceof TimeoutError) || (error instanceof NoConnectionError)) {
                    Toast.makeText(context,
                            "تأكد من اتصال الانترنت",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    Toast.makeText(context,
                            "تأكد من اتصال الانترنت",
                            Toast.LENGTH_SHORT).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context,
                            "تأكد من اتصال الانترنت",
                            Toast.LENGTH_SHORT).show();
                    //TODO
                } else if (error instanceof ParseError) {
                    displayErrorDialog("رقم المادة غير موجود");
                    //TODO
                }
                Log.e("onErrorResponse: ", "" + error);

            }

        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        MySingeltone.getmInstance(Recive_Direct.this).addToRequestQueue(stringRequest);
    }

    private void setData(DSD_VendorItems itemInfo) {
        item_name.setText(itemInfo.getItemNameA().toString());
        price.setText(itemInfo.getPRICE().toString());
        item_name.setText(itemInfo.getItemNameA().toString());
        fd_text=itemInfo.getF_D();
        qty.setEnabled(true);
        qty.requestFocus();
    }

    private void getData(String suplierNo) {
//        final String url = "http://10.0.0.22:8081/GetVendorInfo?VENDOR=" + suplierNo;
       final String url = "http://"+ipAddres+"/GetVendorInfo?VENDOR="+suplierNo;


        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                                JSONObject transactioRecive = jsonArray.getJSONObject(0);
                                DSD_VendourInfo vendourInfo = new DSD_VendourInfo();
                                vendourInfo.setAccNameA(transactioRecive.getString("AccNameA"));
                                supplier_name.setText(vendourInfo.getAccNameA());
                                item_no.setEnabled(true);
                                item_no.requestFocus();

                        } catch (Exception e) {
                            Log.e("Exception", "" + e.getMessage());
                        }
                    }


                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.getCause() instanceof JSONException)
                {
                    displayErrorDialog("رقم المورد  خاطئ");
                }
                else
                if ((error instanceof TimeoutError) || (error instanceof NoConnectionError)) {
                    Toast.makeText(context,
                            "تأكد من اتصال الانترنت",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    Toast.makeText(context,
                            "تأكد من اتصال الانترنت",
                            Toast.LENGTH_SHORT).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context,
                            "تأكد من اتصال الانترنت",
                            Toast.LENGTH_SHORT).show();
                    //TODO
                } else if (error instanceof ParseError) {
                    displayErrorDialog("رقم المورد  خاطئ");
                    //TODO
                }
                Log.e("onErrorResponse: ", "" + error);

            }

        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        MySingeltone.getmInstance(Recive_Direct.this).addToRequestQueue(stringRequest);
    }
    private void getData_Vendor() {
//        final String url = "http://10.0.0.22:8081/GetVendorInfo?VENDOR=" + suplierNo;
        String Cono="";
        if(dataBaseHandler.getSetting()!=null)
        {
          Setting  mySetting=dataBaseHandler.getSetting();
            Cono=mySetting.getCOMPANEY_NAME();}

        final String url = "http://"+ipAddres+"/GetVendorAll"+"?CONO="+Cono;
        Log.e("url",url);
        listAllVendor.clear();
        listAreaName.clear();
        pdValidation = new SweetAlertDialog(Recive_Direct.this, SweetAlertDialog.PROGRESS_TYPE);
        pdValidation.setTitleText(Recive_Direct.this.getResources().getString(R.string.load));
        pdValidation.setCancelable(false);
        pdValidation.show();

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject transactioRecive = jsonArray.getJSONObject(i);
                                VendorInfo vendourInfo = new VendorInfo();
                                vendourInfo.setAccName(transactioRecive.getString("AccNameA"));
                                vendourInfo.setAccNumber(transactioRecive.getString("AccCode"));
                                vendourInfo.setSelect(0);
                                listAllVendor.add(vendourInfo);
                                listAreaName.add(vendourInfo.getAccName().trim());

                            }
                            Log.e("listAllVendor",""+listAllVendor.size());

                            pdValidation.dismissWithAnimation();
                        } catch (Exception e) {
                            Log.e("Exception", "" + e.getMessage());
                            pdValidation.dismissWithAnimation();
                        }
                    }


                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdValidation.dismissWithAnimation();
                if(error.getCause() instanceof JSONException)
                {
                    Log.e("displayErrorDialog",""+error.getCause().getMessage());
                    displayErrorDialog("رقم المورد  خاطئ");
                }
                else
                if ((error instanceof TimeoutError) || (error instanceof NoConnectionError)) {
                    Toast.makeText(context,
                            error.getCause().getMessage()+"تأكد من اتصال الانترنت 1",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    Toast.makeText(context,
                            "تأكد من اتصال الانترنت 2"+error.getCause().getMessage(),
                            Toast.LENGTH_SHORT).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context,
                            error.getCause().getMessage()+"تأكد من اتصال الانترنت 3",
                            Toast.LENGTH_SHORT).show();
                    //TODO
                } else if (error instanceof ParseError) {
                    displayErrorDialog("رقم المورد  خاطئ"+error.getCause().getMessage());
                    //TODO
                }
                Log.e("onErrorResponse: ", "" + error);

            }

        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        MySingeltone.getmInstance(Recive_Direct.this).addToRequestQueue(stringRequest);
    }
    private void printLastVoucher() {
        Intent i = new Intent(Recive_Direct.this, BluetoothConnectMenu.class);
        i.putExtra("printKey", "2");
        startActivity(i);

    }
    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        GrnTextView=findViewById(R.id.GrnTextView);
//        unitSpinner=findViewById(R.id.item_unit);
        transaction_no = (EditText) findViewById(R.id.transaction_no);
        calenderdialog_image=findViewById(R.id.calenderdialog_image);
        search_image=findViewById(R.id.search_image);
        edit_image=findViewById(R.id.edit_image);
        edit_image.setOnClickListener(onClickListener);
        calenderdialog_image.setOnClickListener(onClickListener);
        search_image.setOnClickListener(onClickListener);
        item_no = (EditText) findViewById(R.id.item_no);

        reciver_category_qty = (TextView) findViewById(R.id.reciver_category_qty);
        supplier_name = (TextView) findViewById(R.id.supplier_name);
        item_name = (TextView) findViewById(R.id.item_name);
        qty = (EditText) findViewById(R.id.qty);
        free_qty = (EditText) findViewById(R.id.free_qty);
        supplierVoucherNo=(EditText) findViewById(R.id.supplierVoucherNo);
        total_category_qty = (TextView) findViewById(R.id.total_category_qty);
        supplier_No=(EditText) findViewById(R.id.supplier_No);
//
        addQty = (FloatingActionButton) findViewById(R.id.add_qty);
        addQty.setOnClickListener(onClickListener);
        linearDate = (LinearLayout) findViewById(R.id.linearDate);
        tableCheckData = (TableLayout) findViewById(R.id.tableData);
        tableCheckData.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        price=(TextView) findViewById(R.id.price);
        total=(TextView) findViewById(R.id.total);
        myCalendar = Calendar.getInstance();
//        row=new TableRow(Recive_PO.this);
        save=findViewById(R.id.save);
        save.setOnClickListener(onClickListener);
        cancel=findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(onClickListener);
        reciveDetailList_DSD=new ArrayList<>();
        date=(TextView) findViewById(R.id.date);
        date.setOnClickListener(onClickListener);
        getData_Vendor();
        item_no.setEnabled(false);
        qty.setEnabled(false);

    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.add_qty:
//                    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.1F);
//                    view.startAnimation(buttonClick);
                    view.startAnimation(animation);
                    addItemTotable();


                    break;
                case R.id.date:
                    new DatePickerDialog(Recive_Direct.this, openDatePickerDialog(date), myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    break;
                case R.id.save:

                    view.startAnimation(buttonClick);
                    try {
                        if (tableCheckData.getChildCount() > 0) {
                            voucherNo_text=supplierVoucherNo.getText().toString().trim();
                            if (TextUtils.isEmpty(voucherNo_text)) {
                                supplierVoucherNo.setError("Required");
                                supplierVoucherNo.requestFocus();
                            }
                            else{
                                suplier_text = supplier_No.getText().toString().trim();
                                setVoucherNoToDetailList(voucherNo_text);
                                ReciveMaster reciveMaster=new ReciveMaster();
                                reciveMaster.setORDERNO("xxxxxxxxxx");
                                reciveMaster.setVHFNO(maxSerial+"");
                                reciveMaster.setIS_POSTED("0");
                                reciveMaster.setVHFDATE(convertToEnglish(today));
                                reciveMaster.setVENDOR_VHFDATE(convertToEnglish(today));
                                reciveMaster.setTAXKIND("0");
                                reciveMaster.setAccCode(suplier_text);
                                reciveMaster.setAccName(supplier_name.getText().toString());
                                reciveMaster.setAccName(supplier_name.getText().toString());
                                reciveMaster.setCOUNTX(reciver_category_qty.getText().toString());

                                reciveListMaster_DSD.add(reciveMaster);
                                reciveListMaster_DSD.get(0).setVENDOR_VHFNO(voucherNo_text);
                                saveDataSendtoURL();
//                        addMasterToDB();
//                        addDetailToDB();
                            }


                        } else {
                            Toast.makeText(context, "املى  بيانات الجدول ", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(context, "تأكد من  بيانات الجدول ", Toast.LENGTH_SHORT).show();

                    }



                    break;
                case R.id.cancel_btn:
                    view.startAnimation(buttonClick);
                    new SweetAlertDialog(Recive_Direct.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("تأكيد")
                            .setContentText("هل تريد الخروج  ؟")
                            .setConfirmText("نعم")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    finish();
                                    Intent intent = new Intent(Recive_Direct.this, MainActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .setCancelButton("لا", null)
                            .show();            //
                    break;
                case R.id.calenderdialog_image:
                    dialogEditDate();

                    break;
                case R.id.search_image:
                    if(listAllVendor.size()!=0)
                    openSelectAreaDialog();
                    else Toast.makeText(context, "No data available", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.edit_image:
                    if(supplier_No.getText().toString().length()!=0)
                    {
                        supplier_No.setError(null);
                        showEditDialog();
                    }
                    else supplier_No.setError("*Required");
                    break;
                

            }
        }
    };

    private void showEditDialog() {

            final EditText editText = new EditText(this);
//            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            SweetAlertDialog sweetMessage= new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);

            sweetMessage.setTitleText("رقم المادة");
            sweetMessage .setConfirmText("Ok");
            sweetMessage.setCanceledOnTouchOutside(true);
            sweetMessage.setCustomView(editText);
            sweetMessage.setConfirmButton(getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    if(editText.getText().toString().length()!=0)
                    {
                        item_no.setText(editText.getText().toString().trim());
                        suplier_text = supplier_No.getText().toString();
                        itemNo_text = item_no.getText().toString();
                        getItemInfo(suplier_text, itemNo_text);

                        sweetAlertDialog.dismissWithAnimation();

                    }
                    else {
                        editText.setError("Incorrect");
                    }
                }
            })

                    .show();

    }

    private void showSearchDialog() {




    }
    private void openSelectAreaDialog() {
        final Dialog dialog = new Dialog(this,R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.search_vendor);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());


         listview_area = dialog.findViewById(R.id.listview_area);
        EditText  searchText=dialog.findViewById(R.id.searchText);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length()!=0){
                    listAreaName_filtered.clear();
                    listAllVendor_filtered.clear();
                    for(int i=0; i<listAreaName.size();i++){
                        if(listAreaName.get(i).contains(s.toString().trim())){
                            listAreaName_filtered.add(listAreaName.get(i));
                        }
                    }
                }
                fillAdapterName(2);
//                Log.e("listAreaName_filtered",""+listAreaName_filtered.size());
            }
        });
        listAreaName_filtered.clear();
        listAllVendor_filtered.clear();
        fillAdapterName(1);


        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        Button saveButton = (Button) dialog.findViewById(R.id.saveButton);
//        TextView cancelButton = (TextView) dialog.findViewById(R.id.cancel);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                filterListLocation();
                supplier_No.setText(selectedCustomerId);
                getData(selectedCustomerId);

                dialog.dismiss();


            }
        });

//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
        dialog.show();


    }

    private void fillAdapterName(int flag) {
        if(listAreaName_filtered.size()!=0&& flag==2)
        {
            fillFilteredAll();
            adapter = new ArrayAdapter<String>(getBaseContext(),
                    android.R.layout.simple_list_item_multiple_choice, listAreaName_filtered);
        }else {
            adapter = new ArrayAdapter<String>(getBaseContext(),
                    android.R.layout.simple_list_item_multiple_choice, listAreaName);
        }

        listview_area.setAdapter(adapter);
        listview_area.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        for (int i = 0; i < listAllVendor.size(); i++) {
            if (listAllVendor.get(i).getSelect() == 1)
                listview_area.setItemChecked(i, true);
        }

        listview_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(listAreaName_filtered.size()==0){
                    updateListSelectArea(position,1);
                    selectedCustomerId=listAllVendor.get(position).getAccNumber().toString();
                }else {
                    selectedCustomerId=listAllVendor_filtered.get(position).getAccNumber().toString();
                    updateListSelectArea(position,2);
//                   position= getfilteredPosition();
                }

//                Log.e("selectedCustomerId",""+selectedCustomerId);

            }
        });
    }

    private void fillFilteredAll() {
       for(int i=0;i<listAllVendor.size();i++){
           Log.e("listAllVendor",""+listAllVendor.get(i).getAccName()+"\t"+listAllVendor.get(i).getAccNumber());
           for(int j=0;j<listAreaName_filtered.size();j++){
               if(listAllVendor.get(i).getAccName().equals(listAreaName_filtered.get(j)))
               {
                   listAllVendor_filtered.add(listAllVendor.get(i));
               }

           }


       }
       Log.e("listAllVendor_filtered",""+listAllVendor_filtered.size());

    }

//    private int getfilteredPosition() {
//    }

    private void updateListSelectArea(int position,int type) {
        if(type==1){
            if (listAllVendor.get(position).getSelect() == 0)
                listAllVendor.get(position).setSelect(1);
            else listAllVendor.get(position).setSelect(0);
        }else {
            if(listAllVendor_filtered.get(position).getSelect()==0)
                listAllVendor_filtered.get(position).setSelect(1);
            else listAllVendor_filtered.get(position).setSelect(0);
        }


    }

    private void dialogEditDate() {

        dialog = new Dialog(Recive_Direct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.edit_date_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);
        saveButton = dialog.findViewById(R.id.save);
        final Button cancelButton = dialog.findViewById(R.id.cancel_btn);

        day_editText = dialog.findViewById(R.id.day_editText);
        month_editText = dialog.findViewById(R.id.month_editText);
        year_editText = dialog.findViewById(R.id.year_editText);
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        today = df.format(currentTimeAndDate);
        day_editText.setText(convertToEnglish(today.substring(0,2)));
        month_editText.setText(convertToEnglish(today.substring(3,5)));
        year_editText.setText(convertToEnglish(today.substring(6,today.length())));

        day_editText.setOnClickListener(onclickListenerDate);
        month_editText.setOnClickListener(onclickListenerDate);
        year_editText.setOnClickListener(onclickListenerDate);
        saveButton.setOnClickListener(onclickListenerDate);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    View.OnClickListener onclickListenerDate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.day_editText:
                    if(!day_editText.getText().equals(""))
                    {
                        day_editText.setSelection(day_editText.getText().length(),0);
                    }
                    break;
                case R.id.month_editText:
                    if(!month_editText.getText().equals(""))
                    {
                        month_editText.setSelection(month_editText.getText().length(),0);
                    }
                    break;
                case R.id.year_editText:
                    if(!year_editText.getText().equals(""))
                    {
                        year_editText.setSelection(year_editText.getText().length(),0);
                    }
                    break;
                case R.id.save:
                    day=day_editText.getText().toString();
                    month=month_editText.getText().toString();
                    year=year_editText.getText().toString();
                    checkdataDateDialog();
                    break;



            }
        }
    };

    private void checkdataDateDialog() {
        currentYear=Integer.parseInt(today.substring(6,today.length()));
        dayCorrect=false;monthCorrect=false;yearCorrect=false;leepYear=false;monthTow=false;

        if ((!TextUtils.isEmpty(day)) && (!TextUtils.isEmpty(month))&& (!TextUtils.isEmpty(year)))
        {
            chechYear();
            checkMonth();
            checkDay();




            if(  dayCorrect && monthCorrect && yearCorrect)
            {
                date.setText(day+"/"+month+"/"+year);
                addItemTotable();
                linearDate.setBackground(getResources().getDrawable(R.drawable.edittext_white_border));
                dialog.dismiss();

            }
            else{
                Log.e("date.setText",""+ dayCorrect +""+ monthCorrect +""+ yearCorrect);

            }

        }
        else{
            if(TextUtils.isEmpty(day))
            {
                day_editText.setError("Required");
            }
            if(TextUtils.isEmpty(month))
            {
                month_editText.setError("Required");
            }
            if(TextUtils.isEmpty(year))
            {
                year_editText.setError("Required");
            }

        }


    }

    private void checkDay() {
        if(day.length()<=2)
        {
            intday=Integer.parseInt(day);

            if(leepYear)
            {
                Log.e("towwwwwwwwwww",""+monthTow);
                if(monthTow)
                {
                    if(intday>0&&intday<=29)
                    {
                        dayCorrect=true;
                        fullDate+=day+"/";
                    }
                    else{
                        day_editText.setError("Invalid Number of day ");
                    }
                }
                else{// all month without 2
                    if(intday>0&&intday<=31) {
                        dayCorrect=true;
                        fullDate+=day+"/";
                    }
                    else{
                        day_editText.setError("Invalid Number of day ");
                    }

                }

            }else{
                Log.e("towwwwwwwwwww",""+monthTow);

                if(monthTow)
                {
                    if(intday>0&&intday<=28)
                    {
                        dayCorrect=true;
                        fullDate+=day+"/";
                    }
                    else{
                        day_editText.setError("Invalid Number of day ");
                    }
                }
                else{// all month without 2
                    if(intday>0&&intday<=31) {
                        dayCorrect=true;
                        fullDate+=day+"/";
                    }
                    else{
                        day_editText.setError("Invalid Number of day ");
                    }

                }

            }

        }


        else {
            day_editText.setError("Only tow Digit");

        }
    }

    private void checkMonth() {
        if(month.length()<=2)
        {
            intmonth=Integer.parseInt(month);
            if(intmonth>0&&intmonth<=12)
            {
                monthCorrect=true;
                fullDate+=month+"/";
                Log.e("towwwwwwwwwww",""+intmonth);
                if(intmonth==2)
                {
                    monthTow=true;
                    Log.e("towwwwwwwwwww",""+monthTow);
                }

            }
            else {
                month_editText.setError("Invalid Number of Month ");
            }


        }

        else {
            month_editText.setError("Only tow Digit");


        }
    }

    private void chechYear() {
        if(year.length()<=4)
        {    intyeay=Integer.parseInt(year);
            if(intyeay>=currentYear)
            {
                GregorianCalendar cal = new GregorianCalendar();

                if(cal.isLeapYear(intyeay))
                {
                    leepYear=true;

                }
                else
                {
                    leepYear=false;

                }


                yearCorrect=true;
                fullDate+=year;
            }
            else {
                year_editText.setError("Invalid Number of year ");
            }




        }

        else {

            year_editText.setError("Only four Digit");
        }
    }

    private void addItemTotable() {

        if (checkFildesRequired()) {
            ReciveDetail reciveDetail=isExist();
            if (reciveDetail!=null) {
            Log.e("isExist","true");
               updateQty(reciveDetail,qty.getText().toString());
               tableCheckData.removeView( tableCheckData.getChildAt(index) );


                final TableRow row = new TableRow(Recive_Direct.this);
                row.setPadding(0, 7, 2, 7);
                row.setTag(position);
                for (int i = 0; i < 4; i++) {
                    double total=Double.parseDouble (reciveDetailList_DSD.get(index).getRECEIVED_QTY())*Double.parseDouble(reciveDetailList_DSD.get(index).getPRICE());
                    String[] record = {item_name.getText().toString(),  reciveDetailList_DSD.get(index).getORDER_QTY()+ "",
                            free_qty.getText().toString(),new DecimalFormat("###.###").format(total)+""};

                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView textView = new TextView(Recive_Direct.this);
                    textView.setHint(record[i]);
                    textView.setTextColor(ContextCompat.getColor(Recive_Direct.this, R.color.darkblue_));
                    textView.setHintTextColor(ContextCompat.getColor(Recive_Direct.this, R.color.darkblue_));
                    if (i == 0) {
                        textView.setGravity(Gravity.START);
                    } else {
                        textView.setGravity(Gravity.RIGHT);
                    }

                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                    textView.setLayoutParams(lp2);
                    row.addView(textView);
                }
                row.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        row.setBackgroundColor(getResources().getColor(R.color.layer4));

                        new SweetAlertDialog(Recive_Direct.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("تحذير")
                                .setContentText("هل تريد الحذف بالتأكيد ؟")
                                .setConfirmText("نعم")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        int tag = Integer.parseInt(row.getTag().toString());
                                        tableCheckData.removeView(row);
                                        reciveDetailList_DSD.remove(tag - 1);
                                        position--;
                                        counter--;
                                        updateTotalqty();
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .setCancelButton("لا", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();

                        return true;
                    }
                });//
                tableCheckData.addView(row,index);
                updateTotalqty();
//                item_no.requestFocus();
                clearData();
            } else {



            final TableRow row = new TableRow(Recive_Direct.this);
            row.setPadding(0, 7, 2, 7);
            row.setTag(position);
            for (int i = 0; i < 4; i++) {
              double total= Double.parseDouble( qty.getText().toString() )*Double.parseDouble(price.getText().toString());

                String[] record = {item_name.getText().toString(), qty.getText().toString() + "",
                        free_qty.getText().toString(),convertToEnglish(new DecimalFormat("###.###").format(total)+"")};

                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                TextView textView = new TextView(Recive_Direct.this);
                textView.setHint(record[i]);
                textView.setTextColor(ContextCompat.getColor(Recive_Direct.this, R.color.darkblue_));
                textView.setHintTextColor(ContextCompat.getColor(Recive_Direct.this, R.color.darkblue_));
                if (i == 0) {
                    textView.setGravity(Gravity.START);
                } else {
                    textView.setGravity(Gravity.RIGHT);
                }

                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                textView.setLayoutParams(lp2);
                row.addView(textView);
            }
            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    row.setBackgroundColor(getResources().getColor(R.color.layer4));

                    new SweetAlertDialog(Recive_Direct.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("تحذير")
                            .setContentText("هل تريد الحذف بالتأكيد ؟")
                            .setConfirmText("نعم")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    int tag = Integer.parseInt(row.getTag().toString());
                                    tableCheckData.removeView(row);
                                    reciveDetailList_DSD.remove(tag - 1);
                                    position--;
                                    counter--;
                                    updateTotalqty();
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .setCancelButton("لا", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                    return true;
                }
            });//
            adddataToList(row.getTag().toString());
            Log.e("rowtag", "" + row.getTag().toString());
            tableCheckData.addView(row);
            position++;
            counter = tableCheckData.getChildCount();
            item_no.requestFocus();
            Log.e("counter", "" + counter);
            updateTotalqty();

            clearData();
        }
    }
        setTotalBalance();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setTotalBalance() {
        totalValue=0;
//        totalValue= reciveDetailList_DSD.stream()
//                .filter(totalV ->totalV!=null&&
//                        totalV.getTOTAL()!=null)
//                .mapToDouble(Double.parseDouble(ReciveDetail::getTOTAL))
//                .sum();
        totalValue= reciveDetailList_DSD.stream().map(ReciveDetail::getTOTAL).mapToDouble(Double::parseDouble).sum();
        Log.e("setTotalBalance",""+totalValue);
        total.setText(convertToEnglish(new DecimalFormat("###.###").format(totalValue)+""));
//        for(int i=0;i<reciveDetailList_DSD.size();i++){
//            totalValue+=reciveDetailList_DSD.get(i).getTOTAL();
//        }
    }

    private void updateQty(  ReciveDetail reciveDetail, String qty) {
//        int index= reciveDetailList_DSD.stream())
//        .map(reciveDetail->reciveDetail.))
//        .coll
         index = reciveDetailList_DSD.stream()
                .map(item -> item.getITEMOCODE())
                .collect(Collectors.toList())
                .indexOf(reciveDetail.getITEMOCODE());
//        Log.e("index==",index+"");

     double oldqty=   Double.parseDouble(reciveDetailList_DSD.get(index).getORDER_QTY());
        double newqty=  oldqty+ Double.parseDouble(qty);
        reciveDetailList_DSD.get(index).setORDER_QTY(newqty+"");
        reciveDetailList_DSD.get(index).setRECEIVED_QTY(newqty+"");
        double taxValue= calcTax(reciveDetailList_DSD.get(index).getPRICE());
        reciveDetailList_DSD.get(index).setTAXDETAIL(taxValue+"");
        double totalValue= calckTotal( taxValue , reciveDetailList_DSD.get(index).getORDER_QTY(), reciveDetailList_DSD.get(index).getPRICE());
        reciveDetailList_DSD.get(index).setTOTAL(totalValue+"");
//        Log.e("ORDER_QTY===", "oldqty"+oldqty +","+ reciveDetailList_DSD.get(index).getORDER_QTY());
    }

    private ReciveDetail isExist() {
        ReciveDetail recive_direct= reciveDetailList_DSD.stream().
                filter(item->item_no.getText().toString().equals(item.getITEMOCODE()))
                .findAny()
                .orElse(null);
        if(recive_direct!=null)

        {
            Log.e("recive_direct",""+recive_direct.getITEMOCODE());

        }
        return  recive_direct;
    }

    private void setVoucherNoToDetailList(String voucherNo_text) {
        for(int i=0;i<reciveDetailList_DSD.size();i++)
        {
            reciveDetailList_DSD.get(i).setVHFNO_DETAIL(maxSerial+"");
//            reciveDetailList_DSD.get(i).setVHFNO_DETAIL(voucherNo_text);
        }
    }
    private void clearData() {


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                item_no.setText("");
                qty.setText("");
                free_qty.setText("0");
                price.setText("");
                item_name.setText("");
                date.setText(convertToEnglish(today));
                Log.e("clearData","hereeeee");
                item_no.requestFocus();

            }
        });


    }
    void clearAllData() {
        clearData();
        supplierVoucherNo.setText("");
        supplier_No.setText("");
        supplier_name.setText("");
        reciver_category_qty.setText("");
        tableCheckData.removeAllViews();

        itemInfoList.clear();
        date.setText(convertToEnglish(today));
        pricevalue=0;
        counter=0;
        position=1;
        supplier_No.requestFocus();
        item_no.setEnabled(false);
        qty.setEnabled(false);

    }
    public  static void clearLists()
    {
        reciveDetailList_DSD.clear();
        reciveListMaster_DSD.clear();
    }
    private void adddataToList(String rowNo) {
        ReciveDetail data=new ReciveDetail();
        data.setORDERNUMBER("xxxxxxxxxx");
        data.setVHFNO_DETAIL(maxSerial+"");
        data.setVSERIAL(rowNo);
        data.setITEMOCODE(item_no.getText().toString());
        data.setORDER_QTY(qty.getText().toString());
        data.setORDER_BONUS( free_qty.getText().toString());
        if(free_qty.getText().toString().equals(""))
        {
            data.setBONUS("0");
        }
        else{
            data.setBONUS(free_qty.getText().toString());
        }


        data.setRECEIVED_QTY(qty.getText().toString());
//        double CountOfItems=dataBaseHandler.getConvRate(item_no.getText().toString().trim(), unitSpinner.getSelectedItem().toString());
//        if(!unitSpinner.getSelectedItem().equals("One Unit"))
//        {  data.setUnitID(unitSpinner.getSelectedItem().toString());
//            data.setCal_Qty(String.valueOf( Double.parseDouble(data.getORDER_QTY())*CountOfItems));
//            double price =dataBaseHandler.getunitPrice(item_no.getText().toString().trim(), unitSpinner.getSelectedItem().toString());
//            data.setPRICE((price/Double.parseDouble(data.getCal_Qty() ))+"");
//        }
//        else {
//            data.setUnitID(unitSpinner.getSelectedItem().toString());
//            data.setCal_Qty(data.getORDER_QTY());
//
//            data.setPRICE(price.getText().toString());
//
//        }
        data.setCal_Qty(data.getORDER_QTY());

        data.setPRICE(price.getText().toString());
        data.setDISCL("0");

        data.setINDATE(convertToEnglish(today));
        data.setEXPDATE(convertToEnglish(date.getText().toString()));
        double taxValue= calcTax(data.getPRICE());
        data.setTAXDETAIL(taxValue+"");
       //double totalValue= calckTotal(taxValue,data.getORDER_QTY(),data.getPRICE());
        double totalValue= calckTotal(taxValue,data.getCal_Qty(),data.getPRICE());
       data.setTOTAL(totalValue+"");
       data.setITEM_NAME(itemInfoList.get(0).getItemNameA());
       data.setF_D(fd_text);
        reciveDetailList_DSD.add(data);
//        Log.e("listSize",""+reciveDetailList_DSD.size()+"\tgetF_D="+   data.getF_D());
        Log.e("dataunit==",""+data.getUnitID()+" Cal_Qty=="+   data.getCal_Qty()+"  price=="+data.getPRICE());
    }

    private double calckTotal(double taxValue, String order_qty,String price) {
        double total=0, qty=0,priceValue=0;
        try {
            qty=Double.parseDouble(order_qty);
            priceValue=Double.parseDouble(price);
        }
        catch ( Exception e)
        {
            qty=0;
            priceValue=0;
            Log.e("totalException",""+total+e.getMessage());
        }

        total=(qty*priceValue)+(taxValue*qty);
        Log.e("total",""+total);
        return  total;
    }


    private double calcTax(String price) {
        Log.e("price",""+price);
        double tax=0, priceValue=0,taxPercent=0;
        try{
             priceValue=Double.parseDouble(price);
             taxPercent= Double.parseDouble(itemInfoList.get(0).getTAXPERC());

        }catch (Exception e){
            tax=0;
        }


        if(taxPercent!=0)
        {
            tax=priceValue*taxPercent/100;
        }
        else{
            tax=0;
        }

        Log.e("calcTax",""+taxPercent+"\t"+priceValue+"\t"+tax);

        return tax;
    }

    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final TextView editText) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editText);
            }

        };
        return date;
    }
    private void updateTotalqty() {
        reciver_category_qty.setText(counter+"");
        Log.e("countItems",""+counter);
    }
    private void updateLabel(TextView editText) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(convertToEnglish(sdf.format(myCalendar.getTime())));

    }
    private Boolean checkFildesRequired() {
        boolean allFull = true;
        itemNo_text = item_no.getText().toString().trim();
        suplier_text = supplier_No.getText().toString().trim();
        reciverQty_text = qty.getText().toString().trim();
        dateExpire_text = date.getText().toString().trim();
        if (TextUtils.isEmpty(suplier_text)) {
            allFull = false;
            supplier_No.setError("Required");
        } else if (TextUtils.isEmpty(itemNo_text)) {
            allFull = false;
            item_no.setError("Required");
            item_no.requestFocus();
        } else if (TextUtils.isEmpty(reciverQty_text)) {
            allFull = false;
            qty.setError("Required");
        }
        else if(reciverQty_text.equals("0")){
            allFull = false;
            qty.setError("Error value Zero");

        }
        else if (TextUtils.isEmpty(dateExpire_text)) {
            allFull = false;
            date.setError("Required");
        }


        if (allFull) {
            return true;
        }


        return false;
    }
    private int calckQty() {
        String freQtyText=free_qty.getText().toString();

        try {
            reciverQty_text = qty.getText().toString();


            qtyValue = Double.parseDouble(reciverQty_text);
            if(!freQtyText.equals(""))
            {
                bonusQty = Double.parseDouble(freQtyText);
            }
            else{
                bonusQty=0;
            }

        }catch (Exception e)
        {
            bonusQty=0;

        }

        if (qtyValue >=0) {
            qty_total = qtyValue +bonusQty;
        } else {
            qty_total = qtyValue;
        }
        int totalQty=(int)qty_total;

        return totalQty;

    }
    int maxSerial = 0;

    int getMaxSerial(String type) {
        if(type.equals("PO")||type.equals("DSD"))
        {
            final String url = "http://"+ipAddres+"/GetMaxSerial?MAXTYPE=" + type;

            JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray jsonArray) {
                            try {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject transactioRecive = jsonArray.getJSONObject(i);

                                    maxSerial = Integer.parseInt(transactioRecive.getString("VSERIAL"));
                                    Log.e("maxSerial", "" + maxSerial);

                                }
                            } catch (Exception e) {
                                Log.e("Exception", "" + e.getMessage());
                            }
                        }


                    }

                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("onErrorResponse: ", "" + error);
                }

            });
            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            MySingeltone.getmInstance(Recive_Direct.this).addToRequestQueue(stringRequest);
        }
        return maxSerial;

    }
    private void addDetailToDB() {
        for (int i = 0; i < reciveDetailList_DSD.size(); i++) {

            dataBaseHandler.add_RECIVE_DETAIL(reciveDetailList_DSD.get(i));

        }
    }
    void addMasterToDB() {

        for (int i = 0; i < reciveListMaster_DSD.size(); i++) {
            reciveListMaster_DSD.get(i).setIS_POSTED("1");
            dataBaseHandler.add_RECIVE_MASTER(reciveListMaster_DSD.get(i));
        }

    }

    private void saveDataSendtoURL() {
        calckTotalValueMaster();
        getDataJSON();
        new JSONTask().execute();

    }

    private void calckTotalValueMaster() {
        double totalTax=0,netTotal=0,subTotal=0;
        for(int i=0;i<reciveDetailList_DSD.size();i++)
        {
            totalTax+=Double.parseDouble(reciveDetailList_DSD.get(i).getTAXDETAIL());
            netTotal+=Double.parseDouble(reciveDetailList_DSD.get(i).getTOTAL());
            subTotal+=(Double.parseDouble(reciveDetailList_DSD.get(i).getORDER_QTY())*(Double.parseDouble(reciveDetailList_DSD.get(i).getPRICE())));
        }
        Log.e("TotalValueMaster",""+totalTax+"\t"+netTotal+"\t"+subTotal);


        reciveListMaster_DSD.get(0).setTAX(totalTax);
        reciveListMaster_DSD.get(0).setNETTOTAL(netTotal+"");
        reciveListMaster_DSD.get(0).setSUBTOTAL(subTotal+"");

    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI("http://" +ipAddres+"/" + "SaveOrder"));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("JSONSTR", finalObject.toString().trim()));

                request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = client.execute(request);

                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();

                JsonResponse = sb.toString();
                Log.e("tag", "" + JsonResponse);

                return JsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("onPostExecute", "" + s);

            if (s != null ) {
                if (s.contains("ErrorCode")) {

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        codeState = Integer.parseInt(jsonObject.getString("ErrorCode"));
                        description = (jsonObject.getString("ErrorDesc"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(codeState==0)
            {


                Log.e("POSTEXECmaxSerial",""+maxSerial);
                updateGRN();
               savedDialog();
                askForPrint();
                addDetailToDB();
                addMasterToDB();
                maxSerial++;


//                clearAllData();

            }
            else
            {
                Toast.makeText(context, ""+s.toString(), Toast.LENGTH_SHORT).show();
            }


//            Log.e("tag", "****Failed to export data Please check internet connection");


        }
    }

//    private void askForPrint() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(Recive_Direct.this);
//        builder.setTitle("طباعة الفاتورة");
//        builder.setCancelable(false);
//        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
////               reciveListMaster_DSD=dataBaseHandler.getReciveMaster(7);
////                reciveDetailList_DSD=dataBaseHandler.getReciveDETAIL(7);
////                try {
////                    Thread.sleep(1000);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////                Log.e("reciveDetailList_DSD",""+reciveDetailList_DSD.size());
//                printLastVoucher();
//                clearAllData();
//
//
//            }
//        });
//
//
//        builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//                clearAllData();
//                clearLists();
//            }
//        });
//        builder.setMessage("هل تريد الطباعة ؟");
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//
//
//    }
private void askForPrint() {

    new SweetAlertDialog(Recive_Direct.this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("طباعة الفاتورة")
            .setContentText("هل تريد الطباعة ؟")
            .setConfirmText("نعم!")
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    printLastVoucher();
                    clearAllData();
                    sDialog.dismissWithAnimation();
                }
            })
            .setCancelButton("لا", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();
                    clearAllData();
                    clearLists();
                }
            })
            .show();


}

    private void updateGRN() {
        Log.e("reciveListMaster_DSD",reciveListMaster_DSD.size()+"");
        reciveListMaster_DSD.get(0).setVHFNO(maxSerial+"");
        for(int i=0;i<reciveDetailList_DSD.size();i++)
        {
            reciveDetailList_DSD.get(i).setVHFNO_DETAIL(maxSerial+"");
        }

    }


    private void getDataJSON() {
        jsonObjectMASTER = new JSONObject();
        try {
            jsonObjectMASTER.put("ORDERNO", "xxxxxxxxxx");
            jsonObjectMASTER.put("VHFNO",maxSerial);
            jsonObjectMASTER.put("VHFDATE",  reciveListMaster_DSD.get(0).getVHFDATE());
            Log.e("English",""+ convertToEnglish(date.getText().toString()));
            jsonObjectMASTER.put("VENDOR_VHFNO", reciveListMaster_DSD.get(0).getVENDOR_VHFNO());
            jsonObjectMASTER.put("VENDOR_VHFDATE" ,reciveListMaster_DSD.get(0).getVENDOR_VHFDATE());
            jsonObjectMASTER.put("SUBTOTAL", reciveListMaster_DSD.get(0).getSUBTOTAL());
            jsonObjectMASTER.put("TAX", reciveListMaster_DSD.get(0).getTAX());
            jsonObjectMASTER.put("NETTOTAL", reciveListMaster_DSD.get(0).getNETTOTAL());
            jsonObjectMASTER.put("IS_POSTED", "0");
            jsonObjectMASTER.put("TAXKIND", "0");
            jsonObjectMASTER.put("DISC", "0");
            jsonObjectMASTER.put("ACCCODE",  reciveListMaster_DSD.get(0).getAccCode());
            Log.e("jsonObjectMASTER","" + jsonObjectMASTER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonObjectDetail = null;
        j = new JSONArray();
        for (int i = 0; i < reciveDetailList_DSD.size(); i++) {
            jsonObjectDetail = new JSONObject();
            try {
                jsonObjectDetail.put("ORDERNO", reciveDetailList_DSD.get(i).getORDERNUMBER());

                jsonObjectDetail.put("VHFNO", reciveDetailList_DSD.get(i).getVHFNO_DETAIL());
                jsonObjectDetail.put("ITEMOCODE", reciveDetailList_DSD.get(i).getITEMOCODE());
                jsonObjectDetail.put("VSERIAL", reciveDetailList_DSD.get(i).getVSERIAL());
                jsonObjectDetail.put("ORDER_QTY", reciveDetailList_DSD.get(i).getORDER_QTY());
                jsonObjectDetail.put("ORDER_BONUS",reciveDetailList_DSD.get(i).getBONUS());
//                jsonObjectDetail.put("ORDER_BONUS", reciveDetailList_DSD.get(i).getBONUS());
                jsonObjectDetail.put("VHFDATE", convertToEnglish(date.getText().toString()));
                jsonObjectDetail.put("RECEIVED_QTY", reciveDetailList_DSD.get(i).getRECEIVED_QTY());
                jsonObjectDetail.put("BONUS", reciveDetailList_DSD.get(i).getBONUS());
                jsonObjectDetail.put("PRICE", reciveDetailList_DSD.get(i).getPRICE());
                jsonObjectDetail.put("TOTAL", reciveDetailList_DSD.get(i).getTOTAL());
                jsonObjectDetail.put("TAX", reciveDetailList_DSD.get(i).getTAXDETAIL());
                jsonObjectDetail.put("INDATE",convertToEnglish(reciveDetailList_DSD.get(i).getINDATE()));
                jsonObjectDetail.put("DISCL", reciveDetailList_DSD.get(i).getDISCL());
                jsonObjectDetail.put("EXPDATE", convertToEnglish(reciveDetailList_DSD.get(i).getEXPDATE()));
                jsonObjectDetail.put("ACCCODE",  reciveListMaster_DSD.get(0).getAccCode());
                jsonObjectDetail.put("F_D",reciveDetailList_DSD.get(i).getF_D());
                j.put(jsonObjectDetail);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        finalObject=new JSONObject();
        try {
            finalObject.put("MASTER", jsonObjectMASTER);
            Log.e("finalObjectMas",""+finalObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            finalObject.put("DETAIL", j);
            Log.e("finalObjectDETAIL",""+finalObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }
    private void savedDialog() {


        new SweetAlertDialog(Recive_Direct.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("تم الحفظ بنجاح")
                .setContentText("  GRN  " +maxSerial)
                .hideConfirmButton()
                .show();

        GrnTextView.setText(maxSerial+"");

    }
    void fillUnitspinner(){

        List<String> itemUnits = new ArrayList<>();

        itemUnits.add("One Unit");
        itemUnits.addAll(dataBaseHandler.getItemUnits(item_no.getText().toString().trim(),"1"));

Log.e("itemUnits",itemUnits.size()+"");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, itemUnits);

//        unitSpinner.setAdapter(arrayAdapter);
//        try {
//            for(int i=0;i<itemUnits.size();i++)
//                if(itemUnits.get(i).equals(list.get(position).getUnitID()))
//                    unitSpinner.setSelection(i);
//
//        }catch (Exception e){
//            Log.e("Exception",e.getMessage());
//        }
    }
}
//10.0.0.22/FalconsWebApp/main.dll/
