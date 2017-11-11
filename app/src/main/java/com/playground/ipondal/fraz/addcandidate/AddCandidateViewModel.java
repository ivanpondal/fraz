package com.playground.ipondal.fraz.addcandidate;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import com.google.firebase.storage.StorageReference;
import com.playground.ipondal.fraz.data.Candidate;
import com.playground.ipondal.fraz.data.source.CandidatesRepository;
import id.zelory.compressor.Compressor;

import java.io.IOException;

public class AddCandidateViewModel extends BaseObservable {
	private final AddCantidateActions mAddCandidateActions;
	public final ObservableField<Bitmap> characterPhoto = new ObservableField<>();
	public final ObservableBoolean isSubmitting = new ObservableBoolean(false);
	public final ObservableField<String> characterName = new ObservableField<>("");

	private final Context mContext;
	private final CandidatesRepository mCandidatesRepository;
	private Uri mPhotoUri;

	public AddCandidateViewModel(CandidatesRepository candidatesRepository, Context context, AddCantidateActions addCantidateActions) {
		this.mContext = context;
		this.mAddCandidateActions = addCantidateActions;
		this.mCandidatesRepository = candidatesRepository;
	}

	public void promptPhoto() {
		if (!isSubmitting.get()) {
			mAddCandidateActions.promptPhoto();
		}
	}

	public void loadCharacterPhoto(Uri photoUri) {
		mPhotoUri = photoUri;
		characterPhoto.set(imageUriToBitmap(mPhotoUri));
	}

	public void submit() {
		if (!characterName.get().isEmpty() && characterPhoto.get() != null) {
			mContext.startService(new Intent(mContext, CompressAndUploadPhotoService.class)
					.putExtra(CompressAndUploadPhotoService.EXTRA_FILE_URI, mPhotoUri)
					.setAction(CompressAndUploadPhotoService.ACTION_UPLOAD));
			isSubmitting.set(true);
		}
	}

	private Bitmap imageUriToBitmap(Uri imageUri) {
		Bitmap image = null;
		try {
			image = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
		} catch (IOException e) {
			// TODO: Show/log error message
		}
		return image;
	}

	public void uploadFinished(String uid, String imageRef) {
		mCandidatesRepository.addCandidate(Candidate.withIdWithCharacterNameAndImageRef(uid, characterName.get(), imageRef));
	}
}
