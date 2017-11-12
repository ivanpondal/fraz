package com.playground.ipondal.fraz.candidates;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.playground.ipondal.fraz.data.Candidate;
import com.playground.ipondal.fraz.databinding.CandidateItemBinding;

import java.util.ArrayList;
import java.util.List;

public class CandidatesAdapter extends RecyclerView.Adapter<CandidatesAdapter.CandidatesViewHolder> {

	private final Activity mActivity;
	private final FirebaseStorage mFirebaseStorage;
	private List<Candidate> mCandidates;

    class CandidatesViewHolder extends RecyclerView.ViewHolder{

		private final CandidateItemBinding binding;

		CandidatesViewHolder(CandidateItemBinding binding) {
			super(binding.getRoot());
			this.binding = binding;
		}
	}

	public CandidatesAdapter(Activity activity, FirebaseStorage firebaseStorage){
		mCandidates = new ArrayList<>();
		mFirebaseStorage = firebaseStorage;
		mActivity = activity;
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
	public void onBindViewHolder(CandidatesViewHolder holder, int position) {
		final CandidateViewModel viewmodel = new CandidateViewModel();

		holder.binding.setViewmodel(viewmodel);
		viewmodel.setCandidate(mCandidates.get(position));
		Glide.with(mActivity)
				.load(mFirebaseStorage.getReference(mCandidates.get(position).imageRef()))
                .apply(RequestOptions.centerCropTransform())
				.into(holder.binding.imageView);

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
}
