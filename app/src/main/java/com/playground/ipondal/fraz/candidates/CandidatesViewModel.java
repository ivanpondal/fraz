package com.playground.ipondal.fraz.candidates;

import android.databinding.*;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import com.playground.ipondal.fraz.R;
import com.playground.ipondal.fraz.data.source.*;
import com.playground.ipondal.fraz.data.Candidate;

import java.util.List;

public class CandidatesViewModel extends BaseObservable {
	public final ObservableList<Candidate> items = new ObservableArrayList<>();
	public final ObservableList<String> categories = new ObservableArrayList<>();
	public final ObservableBoolean isVotingEnabled = new ObservableBoolean(false);
	public final ObservableBoolean userAlreadyVoted = new ObservableBoolean(false);

	private final CandidatesRepository mCandidatesRepository;
	private final VotingRepository mVotingRepository;
	private final CandidatesNavigator mCandidatesNavigator;
	private final String mUserId;

	public CandidatesViewModel(CandidatesRepository mCandidatesRepository, VotingRepository mVotingRepository,
			CandidatesNavigator mCandidatesNavigator, String userId) {
		this.mCandidatesRepository = mCandidatesRepository;
		this.mVotingRepository = mVotingRepository;
		this.mCandidatesNavigator = mCandidatesNavigator;
		this.mUserId = userId;
	}

	public void start() {
		mCandidatesRepository.getCandidates(new LoadCandidatesCallback() {
			@Override
			public void onCandidatesLoaded(List<Candidate> candidates) {
				items.clear();
				items.addAll(candidates);
			}
		});
		mVotingRepository.isVotingEnabled(new LoadVotingEnabledCallback() {
			@Override
			public void onVotingEnabledLoaded(boolean votingEnabled) {
				isVotingEnabled.set(votingEnabled);
			}
		});
		mVotingRepository.hasUserAlreadyVoted(mUserId, new LoadUserAlreadyVotedCallback() {
			@Override
			public void onUserAlreadyVotedLoaded(boolean hasUserAlreadyVoted) {
				userAlreadyVoted.set(hasUserAlreadyVoted);
			}
		});
		mVotingRepository.getCategories(new LoadCategoriesCallback() {
			@Override
			public void onCategoriesLoaded(List<String> loadedCategories) {
				categories.clear();
				categories.addAll(loadedCategories);
			}
		});
	}

	public void addCandidate() {
		mCandidatesNavigator.addCandidate();
	}

}
