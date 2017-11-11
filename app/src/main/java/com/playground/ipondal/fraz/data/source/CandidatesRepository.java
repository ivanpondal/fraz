package com.playground.ipondal.fraz.data.source;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.playground.ipondal.fraz.data.Candidate;

import java.util.ArrayList;
import java.util.List;

public class CandidatesRepository {
    private final FirebaseDatabase mDatabase;
    private final DatabaseReference mCandidates;

    public CandidatesRepository(FirebaseDatabase mDatabase) {
        this.mDatabase = mDatabase;
        this.mCandidates = mDatabase.getReference("candidates");
    }

    public List<Candidate> getCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        candidates.add(Candidate.withIdWithCharacterNameAndImageRef("asr32qkjeasf", "Vaquero", "https://scontent.faep8-1.fna.fbcdn.net/v/t31.0-8/12265964_10208171275148065_2979917388158722343_o.jpg?oh=8417115e32e7cbf5f62db6266fa6f94f&oe=5A6CE629"));
        return candidates;
    }

    public void addCandidate(Candidate candidate) {
        mCandidates.child(candidate.id()).setValue(candidate.toMap());
    }
}
