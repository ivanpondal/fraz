package com.playground.ipondal.fraz.candidates;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import com.playground.ipondal.fraz.data.Candidate;

public class CandidateViewModel extends BaseObservable {

    public final ObservableField<Candidate> candidate = new ObservableField<>();

    public CandidateViewModel() {
    }

    public void setCandidate(Candidate candidate) {
        this.candidate.set(candidate);
    }
}
