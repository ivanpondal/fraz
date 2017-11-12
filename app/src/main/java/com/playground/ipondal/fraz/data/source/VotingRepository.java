package com.playground.ipondal.fraz.data.source;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
}
