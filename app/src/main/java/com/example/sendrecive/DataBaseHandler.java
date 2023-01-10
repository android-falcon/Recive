package com.example.sendrecive;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.sendrecive.Models.ItemsUnit;
import com.example.sendrecive.Models.ReciveDetail;
import com.example.sendrecive.Models.ReciveMaster;
import com.example.sendrecive.Models.Setting;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {
    private static String TAG = "DatabaseHandler";
    // Database Version
    private static final int DATABASE_VERSION = 12;
    public static final String DBLOCATION = "/data/data/com.example.abc/databases/";

    // Database Name
    private static final String DATABASE_NAME = "ReciveDatabase";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public static SQLiteDatabase db;
    // tables from JSON
    //----------------------------------------------------------------------

    private static final String RECEIVE_MASTER = "RECEIVE_MASTER";

    private static final String ORDERNO = "ORDERNO";
    private static final String VHFNO = "VHFNO";
    private static final String VHFDATE = "VHFDATE";
    private static final String VENDOR_VHFNO = "VENDOR_VHFNO";
    private static final String VENDOR_VHFDATE = "VENDOR_VHFDATE";
    private static final String SUBTOTAL = "SUBTOTAL";
    private static final String TAX = "TAX";
    private static final String NETTOTAL = "NETTOTAL";
    private static final String IS_POSTED = "IS_POSTED";
    private static final String TAXKIND = "TAXKIND";
    private static final String DISC = "DISC";
    private static final String AccName = "AccName";
    private static final String COUNTX = "COUNTX";
    //-------------------------------------------------------------------------
    private static final String RECEIVE_DETAILS = "RECEIVE_DETAILS";

    private static final String ORDERNUMBER = "ORDERNUMBER";
    private static final String VHFNO_DETAIL = "VHFNO_DETAIL";
    private static final String VHFDATE_DETAIL = "VHFDATE_DETAIL";
    private static final String VSERIAL = "VSERIAL";
    private static final String ITEMOCODE = "ITEMOCODE";
    private static final String ORDER_QTY = "ORDER_QTY";
    private static final String ORDER_BONUS = "ORDER_BONUS";
    private static final String RECEIVED_QTY = "RECEIVED_QTY";
    private static final String BONUS = "BONUS";
    private static final String PRICE = "PRICE";
    private static final String TOTAL = "TOTAL";
    private static final String TAXDETAIL = "TAXDETAIL";
    private static final String INDATE = "INDATE";
    private static final String DISCL = "DISCL";
    private static final String EXPDATE = "EXPDATE";
    private static final String ITEM_NAME = "ITEM_NAME";
    private static final String V_Serial="V_Serial";
    //-------------------------------------------------------------------------
    //-------------------------------------------------------------------------
    private static final String SETTING = "SETTING";

    private static final String IP = "IP";
    private static final String COMPANEY_NAME = "COMPANEY_NAME";
    private static final String LOGO = "LOGO";
    private static final String TEL = "TEL";
    private static final String SAME_QUANTITY = "SAME_QUANTITY";
/////////////

    private static final String    ItemsUnitTable="ItemsUnitTable";
private static final String UnitSERIAL="UnitSERIAL";
    private static final   String SALEPRICE="SALEPRICE";
    private static final  String  Unit_ITEMOCODE="ITEMOCODE";
    private static final String      ITEMBARCODE="ITEMBARCODE";
    private static final  String  ITEMU="ITEMU";
    private static final  String       UQTY="UQTY";
    private static final   String USERIAL="USERIAL";
    private static final  String     CALCQTY="CALCQTY";
    private static final String  WHOLESALEPRC="WHOLESALEPRC";
    private static final String     PURCHASEPRICE="PURCHASEPRICE";
    private static final  String  UNIT_NAME="UNIT_NAME";
    private static final   String         ORG_SALEPRICE="ORG_SALEPRICE";
    private static final  String  OLD_SALE_PRICE="OLD_SALE_PRICE";


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_RECEIVE_MASTER = "CREATE TABLE " + RECEIVE_MASTER + "("
                + ORDERNO + " TEXT,"
                + VHFNO + " TEXT,"
                + VHFDATE + " TEXT,"
                + VENDOR_VHFNO + " TEXT,"
                + VENDOR_VHFDATE + " TEXT,"
                + SUBTOTAL + " TEXT,"
                + TAX + " TEXT,"
                + NETTOTAL + " TEXT,"
                + IS_POSTED + " TEXT,"
                + TAXKIND + " TEXT,"
                + DISC + " TEXT,"
                + AccName + " TEXT,"
                + COUNTX + " TEXT" +
                ")";
        sqLiteDatabase.execSQL(CREATE_RECEIVE_MASTER);
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        String CREATE_RECEIVE_DETAILS = "CREATE TABLE " + RECEIVE_DETAILS + "("
                + ORDERNUMBER + " TEXT,"
                + VHFNO_DETAIL + " TEXT,"
                + VHFDATE_DETAIL + " TEXT,"
                + VSERIAL + " TEXT,"
                + ITEMOCODE + " TEXT,"
                + ORDER_QTY + " TEXT,"
                + ORDER_BONUS + " TEXT,"
                + RECEIVED_QTY + " TEXT,"
                + BONUS + " TEXT,"
                + PRICE + " TEXT,"
                + TOTAL + " TEXT,"
                + TAXDETAIL + " TEXT,"
                + INDATE + " TEXT,"
                + DISCL + " TEXT,"
                + EXPDATE + " TEXT,"
                + ITEM_NAME + " TEXT,"
                +   V_Serial+ " TEXT DEFAULT ''" +

                ")";
        sqLiteDatabase.execSQL(CREATE_RECEIVE_DETAILS);
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        String CREATE_SETTING = "CREATE TABLE " + SETTING + "("
                + IP + " TEXT,"
                + COMPANEY_NAME + " TEXT,"

                + LOGO + " BLOB,"

                + TEL + " TEXT,"

                + SAME_QUANTITY + " TEXT" +

                ")";
        sqLiteDatabase.execSQL(CREATE_SETTING);
        ///
//        String ItemsUnitTableCreate = "CREATE TABLE " + ItemsUnitTable + "("
//        +UnitSERIAL+" INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + SALEPRICE + " TEXT,"
//                + Unit_ITEMOCODE + " TEXT,"
//
//                + ITEMBARCODE + " TEXT,"
//                + ITEMU + " TEXT,"
//                + UQTY + " INTEGER NOT NULL DEFAULT '0',"
//                + USERIAL + " TEXT,"
//                + CALCQTY + " INTEGER NOT NULL DEFAULT '0',"
//                + WHOLESALEPRC + " INTEGER NOT NULL DEFAULT '0',"
//
//                + PURCHASEPRICE + " INTEGER NOT NULL DEFAULT '0',"
//                + UNIT_NAME + " TEXT,"
//                + ORG_SALEPRICE + " INTEGER NOT NULL DEFAULT '0',"
//                + OLD_SALE_PRICE + " INTEGER NOT NULL DEFAULT '0'"+
//
//
//                ")";
//        sqLiteDatabase.execSQL(ItemsUnitTableCreate);

                String ItemsUnitTableCreate = "CREATE TABLE " + ItemsUnitTable + "("
        +UnitSERIAL+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SALEPRICE + " TEXT,"
                + Unit_ITEMOCODE + " TEXT,"

                + ITEMBARCODE + " TEXT,"
                + ITEMU + " TEXT,"
                + UQTY + " TEXT NOT NULL DEFAULT '0',"
                + USERIAL + " TEXT,"
                + CALCQTY + " TEXT NOT NULL DEFAULT '0',"
                + WHOLESALEPRC + " TEXT NOT NULL DEFAULT '0',"

                + PURCHASEPRICE + " TEXT NOT NULL DEFAULT '0',"
                + UNIT_NAME + " TEXT,"
                + ORG_SALEPRICE + " TEXT NOT NULL DEFAULT '0',"
                + OLD_SALE_PRICE + " TEXT NOT NULL DEFAULT '0'"+


                ")";
        sqLiteDatabase.execSQL(ItemsUnitTableCreate);
Log.e("ItemsUnitTableCreate",ItemsUnitTableCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try{
            sqLiteDatabase.execSQL("ALTER TABLE SETTING ADD COMPANEY_NAME  TEXT NOT NULL DEFAULT ''");
            sqLiteDatabase.execSQL("ALTER TABLE SETTING ADD LOGO  BLOB NOT NULL DEFAULT ''");
            sqLiteDatabase.execSQL("ALTER TABLE SETTING ADD TEL  TEXT NOT NULL DEFAULT ''");
            sqLiteDatabase.execSQL("ALTER TABLE SETTING ADD SAME_QUANTITY  TEXT NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            sqLiteDatabase.execSQL("ALTER TABLE RECEIVE_DETAILS ADD ITEM_NAME  TEXT NOT NULL DEFAULT ''");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
try {
    String ItemsUnitTableCreate = "CREATE TABLE IF NOT EXISTS " + ItemsUnitTable + "("
            +UnitSERIAL+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SALEPRICE + " TEXT,"
            + Unit_ITEMOCODE + " TEXT,"

            + ITEMBARCODE + " TEXT,"
            + ITEMU + " TEXT,"
            + UQTY + " TEXT NOT NULL DEFAULT '0',"
            + USERIAL + " TEXT,"
            + CALCQTY + " TEXT NOT NULL DEFAULT '0',"
            + WHOLESALEPRC + " TEXT NOT NULL DEFAULT '0',"

            + PURCHASEPRICE + " TEXT NOT NULL DEFAULT '0',"
            + UNIT_NAME + " TEXT,"
            + ORG_SALEPRICE + " TEXT NOT NULL DEFAULT '0',"
            + OLD_SALE_PRICE + " TEXT NOT NULL DEFAULT '0'"+


            ")";
    sqLiteDatabase.execSQL(ItemsUnitTableCreate);
    Log.e("ItemsUnitTableCreate",ItemsUnitTableCreate);
}catch (Exception e){
    Log.e(TAG, e.getMessage().toString());
}

        try {
            Log.e("aya","aya");
            sqLiteDatabase.execSQL("ALTER TABLE RECEIVE_DETAILS ADD V_Serial TEXT DEFAULT ''");
        } catch (Exception e) {
            Log.e(TAG, "aya,"+e.getMessage());
        }

    }

    public void add_RECIVE_MASTER(ReciveMaster reciveMaster) {
        try {
            String s = "";
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(ORDERNO, reciveMaster.getORDERNO());
            values.put(VHFNO, reciveMaster.getVHFNO());
            values.put(VHFDATE, reciveMaster.getVHFDATE());
            values.put(VENDOR_VHFNO, reciveMaster.getVENDOR_VHFNO());
            values.put(VENDOR_VHFDATE, reciveMaster.getVENDOR_VHFDATE());
            values.put(SUBTOTAL, reciveMaster.getSUBTOTAL());
            values.put(TAX, reciveMaster.getTAX());
            values.put(NETTOTAL, reciveMaster.getNETTOTAL());
            values.put(IS_POSTED, reciveMaster.getIS_POSTED());
            values.put(TAXKIND, reciveMaster.getTAXKIND());
            values.put(DISC, reciveMaster.getDISC());
            values.put(AccName, reciveMaster.getAccName());
            values.put(COUNTX, reciveMaster.getCOUNTX());

            db.insert(RECEIVE_MASTER, null, values);
            db.close();
        } catch (Exception e) {
            Log.e("add_RECIVE_MASTER", "" + e.getMessage());

        }

    }
    public void add_ItemUnit(ItemsUnit itemsUnit) {
        try {
            String s = "";
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(SALEPRICE, itemsUnit.getSALEPRICE());
            values.put(Unit_ITEMOCODE, itemsUnit.getITEMOCODE());
            values.put(ITEMBARCODE, itemsUnit.getITEMBARCODE());
            values.put(ITEMU, itemsUnit.getITEMU());
            values.put(UQTY, itemsUnit.getUQTY());
            values.put(USERIAL, itemsUnit.getUSERIAL());

            values.put(CALCQTY, itemsUnit.getCALCQTY());
            values.put(WHOLESALEPRC, itemsUnit.getWHOLESALEPRC());
            values.put(PURCHASEPRICE, itemsUnit.getPURCHASEPRICE());
            values.put(UNIT_NAME, itemsUnit.getUNIT_NAME());
            values.put(ORG_SALEPRICE, itemsUnit.getORG_SALEPRICE());
            values.put(OLD_SALE_PRICE, itemsUnit.getOLD_SALE_PRICE());



            db.insert(ItemsUnitTable, null, values);
            db.close();
        } catch (Exception e) {
            Log.e("add_ItemsUnitTable", "" + e.getMessage());

        }

    }
    public void deleteItemUnit() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + ItemsUnitTable);
        db.close();
    }

    public List<String> getItemUnits(String itemno,String flag) {
        List<String> itemsUnitList = new ArrayList<String>();

        String selectQuery ="SELECT DISTINCT ITEMU FROM ItemsUnitTable WHERE ITEMOCODE = '"+itemno+"' AND ITEMU <> ''";



        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("cursor===",""+cursor.getCount()+"");
        if (cursor.moveToFirst()) {
            do {
                ItemsUnit itemsUnit = new ItemsUnit();

//                itemsUnit.setSALEPRICE(cursor.getString(1));
//                itemsUnit.setITEMOCODE(cursor.getString(2));
//                itemsUnit.setITEMBARCODE(cursor.getString(3));
//                itemsUnit.setITEMU(cursor.getString(4));
//                itemsUnit.setUQTY(cursor.getString(5));
//                itemsUnit.setUSERIAL(cursor.getString(6));
//
//                itemsUnit.setCALCQTY(cursor.getString(7));
//                itemsUnit.setWHOLESALEPRC(cursor.getString(8));
//                itemsUnit.setPURCHASEPRICE(cursor.getString(9));
//                itemsUnit.setUNIT_NAME(cursor.getString(10));
//                itemsUnit.setORG_SALEPRICE(cursor.getString(11));
//                itemsUnit.setOLD_SALE_PRICE(cursor.getString(12));

                itemsUnitList.add(cursor.getString(0));
                Log.e("itemsUnitList===",""+itemsUnitList.size());
            } while (cursor.moveToNext());
        }

        return itemsUnitList;
    }

    public double getConvRate(String itemNo, String unitId) {
        double UQTY = 1;
        // Select All Query
        String selectQuery = "SELECT  UQTY FROM " + ItemsUnitTable+" WHERE ITEMOCODE ='"+itemNo +"' AND ITEMU ='"+unitId+"'";
        ItemsUnit itemsUnit=new ItemsUnit();

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                UQTY= Double.parseDouble(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        Log.e("getUQTY",""+UQTY);
        return UQTY;
    }
    public double getunitPrice(String itemNo, String unitId) {
        double price = 1;
        // Select All Query
        String selectQuery = "SELECT  SALEPRICE FROM " + ItemsUnitTable+" WHERE ITEMOCODE ='"+itemNo +"' AND ITEMU ='"+unitId+"'";
        ItemsUnit itemsUnit=new ItemsUnit();

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                price= Double.parseDouble(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        Log.e("price==",""+price);
        return price;
    }

    public List<ReciveMaster> getReciveMaster(String grn,String flag) {
        List<ReciveMaster> reciveList = new ArrayList<ReciveMaster>();

        String selectQuery;

        if(flag.equals("1"))
        {
            selectQuery = "SELECT  * FROM " + RECEIVE_MASTER +" where VHFNO ='"+grn+"' and ORDERNO <> 'xxxxxxxxxx' ";

        }
        else {
            selectQuery = "SELECT  * FROM " + RECEIVE_MASTER +" where VHFNO ='"+grn+"'  and ORDERNO == 'xxxxxxxxxx'";

        }
        Log.e("selectQuery",selectQuery+"");
        // Select All Query


        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ReciveMaster reciveMaster = new ReciveMaster();

                reciveMaster.setORDERNO(cursor.getString(0));
                reciveMaster.setVHFNO(cursor.getString(1));
                reciveMaster.setVHFDATE(cursor.getString(2));
                reciveMaster.setVENDOR_VHFNO(cursor.getString(3));
                reciveMaster.setVENDOR_VHFDATE(cursor.getString(4));
                reciveMaster.setSUBTOTAL(cursor.getString(5));
                reciveMaster.setTAX(Double.parseDouble(cursor.getString(6)));
                reciveMaster.setNETTOTAL(cursor.getString(7));
                reciveMaster.setIS_POSTED((cursor.getString(8)));
                reciveMaster.setTAXKIND(cursor.getString(9));
                reciveMaster.setDISC(Double.parseDouble(cursor.getString(10)));
                reciveMaster.setAccName(cursor.getString(11));
                reciveMaster.setCOUNTX(cursor.getString(12));


                reciveList.add(reciveMaster);
                Log.e("reciveList",""+reciveList.size());
            } while (cursor.moveToNext());
        }

        return reciveList;
    }

    public void deleteREciveMaster() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + RECEIVE_MASTER);
        db.close();
    }

    public void add_RECIVE_DETAIL(ReciveDetail reciveDetail) {
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(ORDERNUMBER, reciveDetail.getORDERNUMBER());
            values.put(VHFNO_DETAIL, reciveDetail.getVHFNO_DETAIL());
            values.put(VHFDATE_DETAIL, reciveDetail.getVHFDATE_DETAIL());
            values.put(VSERIAL, reciveDetail.getVSERIAL());
            values.put(ITEMOCODE, reciveDetail.getITEMOCODE());
            values.put(ORDER_QTY, reciveDetail.getORDER_QTY());
            values.put(ORDER_BONUS, reciveDetail.getBONUS());
            values.put(RECEIVED_QTY, reciveDetail.getRECEIVED_QTY());
            values.put(BONUS, reciveDetail.getBONUS());
            values.put(PRICE, reciveDetail.getPRICE());
            values.put(TOTAL, reciveDetail.getTOTAL());
            values.put(TAXDETAIL, reciveDetail.getTAXDETAIL());
            values.put(INDATE, reciveDetail.getINDATE());
            values.put(DISCL, reciveDetail.getDISCL());
            values.put(EXPDATE, reciveDetail.getEXPDATE());
            values.put(ITEM_NAME, reciveDetail.getITEM_NAME());
            values.put(V_Serial, reciveDetail.getVSerial());
            db.insert(RECEIVE_DETAILS, null, values);
            db.close();
        } catch (Exception e) {
            Log.e("add_RECIVE_DETAIL", "" + e.getMessage());

        }

    }

    public List<ReciveDetail> getReciveDETAIL(String grn,String flag) {
        List<ReciveDetail> reciveList = new ArrayList<ReciveDetail>();
        // Select All Query
        String selectQuery ="";
//        String selectQuery = "SELECT  * FROM " + RECEIVE_DETAILS+" where VHFNO_DETAIL ='"+grn+"' ";
        if(flag.equals("1"))
        {
            selectQuery = "SELECT  * FROM " + RECEIVE_DETAILS +" where VHFNO_DETAIL ='"+grn+"' and ORDERNUMBER <> 'xxxxxxxxxx' ";
        }
        else {
            selectQuery = "SELECT  * FROM " + RECEIVE_DETAILS +" where VHFNO_DETAIL ='"+grn+"'  and ORDERNUMBER == 'xxxxxxxxxx'";

        }

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ReciveDetail reciveDetail = new ReciveDetail();

                reciveDetail.setORDERNUMBER(cursor.getString(0));
                reciveDetail.setVHFNO_DETAIL(cursor.getString(1));
                reciveDetail.setVHFDATE_DETAIL(cursor.getString(2));
                reciveDetail.setVSERIAL(cursor.getString(3));
                reciveDetail.setITEMOCODE(cursor.getString(4));
                reciveDetail.setORDER_QTY(cursor.getString(5));
                reciveDetail.setORDER_BONUS((cursor.getString(6)));
                reciveDetail.setRECEIVED_QTY(cursor.getString(7));
                reciveDetail.setBONUS((cursor.getString(8)));
                reciveDetail.setPRICE(cursor.getString(9));
                reciveDetail.setTOTAL((cursor.getString(10)));
                reciveDetail.setTAXDETAIL(cursor.getString(11));
                reciveDetail.setINDATE(cursor.getString(12));
                reciveDetail.setDISCL(cursor.getString(13));
                reciveDetail.setEXPDATE(cursor.getString(14));
                reciveDetail.setITEM_NAME(cursor.getString(15));

                reciveList.add(reciveDetail);
                Log.e("reciveDetail",""+reciveList.size());
            } while (cursor.moveToNext());
        }

        return reciveList;
    }

    public void deleteReciveDETAIL() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + RECEIVE_DETAILS);
        db.close();
    }

    public void addSetting(Setting setting) {
        try {
            String s = "";

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            byte[] byteImage = {};
            if (setting.getLOGO() != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                setting.getLOGO().compress(Bitmap.CompressFormat.PNG, 0, stream);
                byteImage = stream.toByteArray();
            }

            values.put(IP, setting.getIP());
            values.put(COMPANEY_NAME, setting.getCOMPANEY_NAME());
            values.put(LOGO,byteImage);
            values.put(TEL, setting.getTEL());
            values.put(SAME_QUANTITY, setting.getSAME_QUANTITY());
            db.insert(SETTING, null, values);
            Log.e("addIp", "" + setting.getIP());
            db.close();
        } catch (Exception e) {
            Log.e("addIp", "" + e.getMessage());

        }
    }

    public void deleteIp() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + SETTING);
        db.close();
    }

    public String getIP() {
        String ip = "";
        // Select All Query
        String selectQuery = "SELECT  IP FROM " + SETTING;
        Setting mySetting=new Setting();

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
              ip= cursor.getString(0);
            }
            while (cursor.moveToNext());
        }
        Log.e("getIP",""+ip);
        return ip;
    }
    public Setting getSetting() {
        String ip = "";
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SETTING;
        Setting mySetting=new Setting();

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                mySetting.setIP( cursor.getString(0));
                mySetting.setCOMPANEY_NAME(cursor.getString(1));
//                mySetting.setLOGO(cursor.getBlob(2));

                if (cursor.getBlob(2).length == 0)
                    mySetting.setLOGO(null);
                else
                    mySetting.setLOGO(BitmapFactory.decodeByteArray(cursor.getBlob(2), 0, cursor.getBlob(2).length));
                mySetting.setTEL(cursor.getString(3));
                mySetting.setSAME_QUANTITY(cursor.getString(4));
            }
            while (cursor.moveToNext());
        }
        Log.e("getIP",""+mySetting.getIP());
        return mySetting;
    }

    public void deleteSetting() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + SETTING);
        db.close();
    }
}
/*
*
*   public void updateTransactions() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_POSTED2, 1);
        db.update(TABLE_TRANSACTIONS, values, IS_POSTED + "=" + 0, null);
    }
    * */

