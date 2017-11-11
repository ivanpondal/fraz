package com.playground.ipondal.fraz;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.playground.ipondal.fraz.candidates.CandidatesActivity;

import java.util.Arrays;

public class StartActivity extends AppCompatActivity {

	private static final int REQUEST_SIGN_IN = 0;

	@Override
	protected void onStart() {
		super.onStart();
		FirebaseAuth auth = FirebaseAuth.getInstance();
		if (auth.getCurrentUser() != null) {
			startCandidatesActivity();
		} else {
			startActivityForResult(
				// Get an instance of AuthUI based on the default app
				AuthUI.getInstance()
						.createSignInIntentBuilder()
						.setIsSmartLockEnabled(false)
						.setAvailableProviders(
								Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
										new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
						.build(),
				REQUEST_SIGN_IN);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == REQUEST_SIGN_IN) {
			startCandidatesActivity();
		}
	}

	private void startCandidatesActivity() {
		startActivity(new Intent(this, CandidatesActivity.class));
		finish();
	}
}
