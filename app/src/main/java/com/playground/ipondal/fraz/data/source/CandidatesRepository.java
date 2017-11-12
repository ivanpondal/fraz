package com.playground.ipondal.fraz.data.source;

import android.app.DownloadManager;
import com.google.firebase.database.*;
import com.playground.ipondal.fraz.data.Candidate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CandidatesRepository {
	private final FirebaseDatabase mDatabase;
	private final DatabaseReference mCandidates;

	public CandidatesRepository(FirebaseDatabase mDatabase) {
		this.mDatabase = mDatabase;
		this.mCandidates = mDatabase.getReference("candidates");
	}

	public void getCandidates(final LoadCandidatesCallback callback) {
		mCandidates.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				List<Candidate> candidates = new ArrayList<>();
				for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
					CandidateDto candidateDto = postSnapshot.getValue(CandidateDto.class);
					candidates.add(Candidate
							.withIdWithCharacterNameAndImageRef(candidateDto.uid, candidateDto.characterName, candidateDto.imageRef));
				}
				callback.onCandidatesLoaded(candidates);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});
	}

	public void addCandidate(Candidate candidate) {
		mCandidates.child(candidate.id()).setValue(candidate.toMap());
	}
}
