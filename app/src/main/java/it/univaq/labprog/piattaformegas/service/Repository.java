package it.univaq.labprog.piattaformegas.service;

import android.content.Context;

public class Repository {
    public void downloadData(Context context, Request.RequestCallBack callBack) {
        Request.getInstance(context).requestDownload(callBack);
    }
}
