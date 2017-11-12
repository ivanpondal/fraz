package com.playground.ipondal.fraz.candidates;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.playground.ipondal.fraz.data.Candidate;
import com.playground.ipondal.fraz.databinding.CandidateItemBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CandidatesAdapter extends RecyclerView.Adapter<CandidatesAdapter.CandidatesViewHolder> {

	private final Activity mActivity;
	private final FirebaseStorage mFirebaseStorage;
	private final CandidatesViewModel mCandidatesViewModel;
	private List<Candidate> mCandidates;
	private Candidate selectedCandidate;
	private String selectedCategory;


	class CandidatesViewHolder extends RecyclerView.ViewHolder{

		private final CandidateItemBinding binding;

		CandidatesViewHolder(CandidateItemBinding binding) {
			super(binding.getRoot());
			this.binding = binding;
		}
	}

	public CandidatesAdapter(Activity activity, FirebaseStorage firebaseStorage, CandidatesViewModel candidatesViewModel) {
		mCandidates = new ArrayList<>();
		mFirebaseStorage = firebaseStorage;
		mActivity = activity;
		mCandidatesViewModel = candidatesViewModel;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public CandidatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		CandidateItemBinding binding = CandidateItemBinding.inflate(inflater, parent, false);
		return new CandidatesViewHolder(binding);
	}

	@Override
	public void onBindViewHolder(CandidatesViewHolder holder, final int position) {
		final CandidateViewModel viewmodel = new CandidateViewModel();

		holder.binding.setViewmodel(viewmodel);
		viewmodel.setCandidate(mCandidates.get(position));
		Glide.with(mActivity)
				.load(mFirebaseStorage.getReference(mCandidates.get(position).imageRef()))
                .apply(RequestOptions.centerCropTransform())
				.into(holder.binding.imageView);
		holder.binding.radioButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedCandidate = viewmodel.candidate.get();
				mCandidatesViewModel.categoryVotes.put(selectedCategory, selectedCandidate);
				notifyDataSetChanged();
			}
		});
		if (selectedCandidate != null){
			holder.binding.radioButton.setChecked(selectedCandidate.id().equals(viewmodel.candidate.get().id()));
		} else {
			holder.binding.radioButton.setChecked(false);
		}
	}

	@Override
	public int getItemCount() {
		return mCandidates.size();
	}

	private void setList(List<Candidate> candidates) {
		mCandidates = candidates;
		notifyDataSetChanged();
	}

	public void replaceData(List<Candidate> candidates) {
	    setList(candidates);
	}

	public void setCategoryVotes(List<String> categories) {
		notifyDataSetChanged();
	}

	public void switchCategory(String category) {
		selectedCategory = category;
		selectedCandidate = mCandidatesViewModel.categoryVotes.get(selectedCategory);
		notifyDataSetChanged();
	}
}
