package com.playground.ipondal.fraz.candidates;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.playground.ipondal.fraz.R;
import com.playground.ipondal.fraz.addcandidate.AddCandidateActivity;
import com.playground.ipondal.fraz.data.source.CandidatesRepository;
import com.playground.ipondal.fraz.data.source.VotingRepository;
import com.playground.ipondal.fraz.databinding.CandidatesActivityBinding;

public class CandidatesActivity extends AppCompatActivity implements CandidatesNavigator {
	private CandidatesViewModel mCandidatesViewModel;
	private CandidatesActivityBinding mCandidatesActivityBinding;
	private CandidatesAdapter mCandidatesAdapter;
	private CandidatesRepository mCandidatesRepository;
	private String mUserId;
	private VotingRepository mVotingRepository;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

		mCandidatesRepository = new CandidatesRepository(FirebaseDatabase.getInstance());
		mVotingRepository = new VotingRepository(FirebaseDatabase.getInstance());
		mCandidatesViewModel = new CandidatesViewModel(mCandidatesRepository, mVotingRepository, this, mUserId);

		mCandidatesActivityBinding = DataBindingUtil.setContentView(this, R.layout.candidates_activity);
		mCandidatesActivityBinding.setViewmodel(mCandidatesViewModel);

		mCandidatesAdapter = new CandidatesAdapter(this, FirebaseStorage.getInstance());
		RecyclerView recyclerView = mCandidatesActivityBinding.candidatesList;
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(mCandidatesAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mCandidatesViewModel.start();
	}

	@Override
	public void addCandidate() {
		startActivity(new Intent(this, AddCandidateActivity.class));
	}
}
