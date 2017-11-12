package com.playground.ipondal.fraz.data.source;

import com.playground.ipondal.fraz.data.Candidate;

import java.util.List;

public interface LoadCandidatesCallback {
	void onCandidatesLoaded(List<Candidate> candidates);
}
