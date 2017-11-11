package com.playground.ipondal.fraz.candidates;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import com.playground.ipondal.fraz.data.source.CandidatesRepository;
import com.playground.ipondal.fraz.data.Candidate;

public class CandidatesViewModel extends BaseObservable {
	public final ObservableList<Candidate> items = new ObservableArrayList<>();

	private final CandidatesRepository mCandidatesRepository;
	private final CandidatesNavigator mCandidatesNavigator;

	public CandidatesViewModel(CandidatesRepository mCandidatesRepository, CandidatesNavigator mCandidatesNavigator) {
		this.mCandidatesRepository = mCandidatesRepository;
		this.mCandidatesNavigator = mCandidatesNavigator;
	}

	public void start() {
		items.addAll(mCandidatesRepository.getCandidates());
	}

    public void addCandidate() {
	    mCandidatesNavigator.addCandidate();
    }
}
