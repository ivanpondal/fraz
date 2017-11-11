package com.playground.ipondal.fraz.candidates;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import com.playground.ipondal.fraz.data.Candidate;

import java.util.List;

public class CandidatesRecyclerViewBindings {
    @SuppressWarnings("unchecked")
    @BindingAdapter("app:items")
    public static void setItems(RecyclerView recyclerView, List<Candidate> items) {
        CandidatesAdapter adapter = (CandidatesAdapter) recyclerView.getAdapter();
        adapter.replaceData(items);
    }
}
