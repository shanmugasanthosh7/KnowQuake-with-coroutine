package com.sample.knowquake.ui.earthquakes;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sample.knowquake.databinding.LoadMoreProgressBinding;

public class LoadMoreViewHolder extends RecyclerView.ViewHolder {

    private LoadMoreProgressBinding binding;

    public LoadMoreViewHolder(@NonNull LoadMoreProgressBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindProgress() {
        binding.progressBar.setIndeterminate(true);
    }
}