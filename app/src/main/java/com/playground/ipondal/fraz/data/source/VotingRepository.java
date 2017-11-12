package com.playground.ipondal.fraz.data.source;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playground.ipondal.fraz.data.Candidate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingRepository {
	private final FirebaseDatabase mDatabase;

	public VotingRepository(FirebaseDatabase mDatabase) {
		this.mDatabase = mDatabase;
	}

	public void isVotingEnabled(final LoadVotingEnabledCallback callback) {
		mDatabase.getReference("voting-enabled").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				callback.onVotingEnabledLoaded((Boolean) dataSnapshot.getValue());
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

	public void hasUserAlreadyVoted(final String uid, final LoadUserAlreadyVotedCallback loadUserAlreadyVotedCallback) {
		mDatabase.getReference("voter-turnout").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				loadUserAlreadyVotedCallback.onUserAlreadyVotedLoaded(dataSnapshot.hasChild(uid));
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

	public void getCategories(final LoadCategoriesCallback callback) {
		mDatabase.getReference("categories").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				List<String> categories = (List<String>) dataSnapshot.getValue();
				callback.onCategoriesLoaded(categories);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

	public void vote(String mUserId, Map<String, Candidate> categoryVotes, final SaveVotesCallback callback) {
	    mDatabase.getReference("voter-turnout").child(mUserId).setValue(true);
	    Map<String, String> categoryVotesDto = new HashMap<>();

	    for (String category: categoryVotes.keySet()) {
	        categoryVotesDto.put(category, categoryVotes.get(category).id());
		}
		mDatabase.getReference("votes").child(mUserId).setValue(categoryVotesDto).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				callback.onSaveVotes();
			}
		});
	}
}
