package com.example.sendrecive.Models;

import static com.example.sendrecive.Models.GeneralMethod.showSweetDialog;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.example.sendrecive.Dao.ApiService;
import com.example.sendrecive.DataBaseHandler;
import com.example.sendrecive.R;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class ImportData {
    private Context context;
    public String ipAddress = "", CONO = "", headerDll = "", link = "", portIp = "";
    public static SweetAlertDialog pdRepla;
    private DataBaseHandler dataBaseHandler;
    public ApiService myAPI;
    Setting mySetting;

    public  void getIpAddress(){
        mySetting=  dataBaseHandler.getSetting();
        ipAddress=mySetting.getIP();
        CONO=mySetting.getCOMPANEY_NAME();
    }
    public static ArrayList<ItemsUnit> listAllItemsUnit = new ArrayList<>();
    public ImportData(Context context) {
        this.context = context;
        dataBaseHandler= new DataBaseHandler(context);
        //headerDll = "/Falcons/VAN.Dll/";
        getIpAddress();
     //   link = "http://" + ipAddress.trim() + headerDll.trim();
        link = "http://" +ipAddress + headerDll.trim()+"/";
//        Log.e("Link====",""+link.toString());
//        Retrofit retrofit = RetrofitInstance.getInstance(link);
//        Log.e("retrofit====",""+retrofit.toString());
//        myAPI = retrofit.create(ApiService.class);



    }
    public void getUnitData(String from,String to) {
        Log.e("importgetUnitData", "" + "getUnitData");
        pdRepla = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pdRepla.getProgressHelper().setBarColor(Color.parseColor("#7A7A7A"));
        pdRepla.setTitleText("Get Items Unit");
        pdRepla.setCancelable(false);
        pdRepla.show();
        fetchItemsUnitData(from,to);



    }

    public void fetchItemsUnitData(String from,String to){
        Call<List<ItemsUnit>> myData    = myAPI. GetJrdItemUnit(CONO,from, to);
        myData.enqueue(new Callback<List<ItemsUnit>>() {
            @Override
            public void onResponse(Call<List<ItemsUnit>> call, retrofit2.Response<List<ItemsUnit>> response) {
                if (!response.isSuccessful()) {

                    Log.e("fetchItemsUnitData", "not=" + response.message());

                    pdRepla.dismiss();


                } else {
                    Log.e("fetchItemsUnitData", "onResponse=" + response.message());
                    listAllItemsUnit.clear();
                    listAllItemsUnit.addAll(response.body());
                    showSweetDialog(context, 1, context.getResources().getString(R.string.savedSuccsesfule), "");
                    dataBaseHandler.deleteItemUnit();

                for(int i=0;i<listAllItemsUnit.size();i++)
                    dataBaseHandler.add_ItemUnit(listAllItemsUnit.get(i));
                    pdRepla.dismiss();

                }
            }

            @Override
            public void onFailure(Call<List<ItemsUnit>> call, Throwable t) {
                Log.e("fetchItemsUnitDataFailure", "=" + t.getMessage());
                showSweetDialog(context, 0,"netWorkError", "");
                pdRepla.dismiss();
            }
        });
    }
}
