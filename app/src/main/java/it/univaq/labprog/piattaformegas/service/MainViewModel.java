package it.univaq.labprog.piattaformegas.service;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.univaq.labprog.piattaformegas.PiattaformeGas;
import it.univaq.labprog.piattaformegas.database.DB;
import it.univaq.labprog.piattaformegas.model.Platform;

public class MainViewModel extends AndroidViewModel {
    private Repository repository;
    private MutableLiveData<List<Platform>> platforms = new MutableLiveData<>();
    public MainViewModel(@NonNull Application application) {
        super(application);

        repository = ((PiattaformeGas) application).getRepository();
        new Thread(() -> {
            List<Platform> list = DB.getInstance(application).getPlatformDao().findAll();

            if (list.isEmpty()) {
                repository.downloadData(application, new Request.RequestCallBack() {
                    @Override
                    public void onCompleted(UrlRequest request, UrlResponseInfo info, byte[] data, CronetException error) {

                        List<Platform> tempPlatforms = new ArrayList<>();

                        if (data != null) {
                            String response = new String(data);
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject item = array.optJSONObject(i);
                                    Platform platform = Platform.parseJSon(item);
                                    if (platform != null) tempPlatforms.add(platform);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (error != null) {
                                error.printStackTrace();
                            }
                        }
                        platforms.postValue(tempPlatforms);
                        DB.getInstance(getApplication()).getPlatformDao().insert(tempPlatforms);
                    }
                });
            } else {
                platforms.postValue(list);
            }
        }).start();

    }

    public LiveData<List<Platform>> getPlatforms() {
        return platforms;
    }
}
