package it.univaq.labprog.piattaformegas;

import android.app.Application;

import it.univaq.labprog.piattaformegas.service.Repository;

public class PiattaformeGas extends Application{
    private Repository repository;
    @Override
    public void onCreate() {
        super.onCreate();

        repository = new Repository();
    }

    public Repository getRepository() {
        return repository;
    }
}
