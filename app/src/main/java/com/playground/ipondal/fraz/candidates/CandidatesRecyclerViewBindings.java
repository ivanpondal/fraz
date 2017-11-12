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

    @BindingAdapter("app:categories")
    public static void setCategories(RecyclerView recyclerView, List<String> categories) {
        CandidatesAdapter adapter = (CandidatesAdapter) recyclerView.getAdapter();
        adapter.setCategoryVotes(categories);
    }

    @BindingAdapter("app:selected_category")
    public static void setCategories(RecyclerView recyclerView, String selectedCategory) {
        CandidatesAdapter adapter = (CandidatesAdapter) recyclerView.getAdapter();
        adapter.switchCategory(selectedCategory);
    }
}
