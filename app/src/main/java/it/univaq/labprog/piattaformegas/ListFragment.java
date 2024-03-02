package it.univaq.labprog.piattaformegas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import it.univaq.labprog.piattaformegas.databinding.FragmentListBinding;
import it.univaq.labprog.piattaformegas.model.Platform;
import it.univaq.labprog.piattaformegas.service.MainViewModel;

public class ListFragment extends Fragment {
    private FragmentListBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =  FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainViewModel mainViewModel = new ViewModelProvider(requireActivity())
                .get(MainViewModel.class);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mainViewModel.getPlatforms().observe(getViewLifecycleOwner(), new Observer<List<Platform>>() {
            @Override
            public void onChanged(List<Platform> platforms) {
                binding.recyclerView.setAdapter(new PlatformAdapter(platforms));
            }
        });
    }
}
