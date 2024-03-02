package it.univaq.labprog.piattaformegas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.univaq.labprog.piattaformegas.databinding.AdapterBinding;
import it.univaq.labprog.piattaformegas.model.Platform;

public class PlatformAdapter extends RecyclerView.Adapter<PlatformAdapter.ViewHolder>{
    private List<Platform> data;

    public PlatformAdapter(List<Platform> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterBinding binding = AdapterBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatformAdapter.ViewHolder holder, int position) {
        Platform platform = data.get(position);
        holder.onBind(platform);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private AdapterBinding binding;
        public ViewHolder(AdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.layoutLinear.setOnClickListener(this);
        }

        public void onBind(Platform platform) {
            binding.setPlatform(platform);
        }

        @Override
        public void onClick(View v) {
            Platform platform = data.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putSerializable(DetailActivity.EXTRA_PLATFORM, platform);

            Navigation.findNavController(v)
                    .navigate(R.id.action_menu_list_to_detailActivity, bundle);
        }
    }
}
