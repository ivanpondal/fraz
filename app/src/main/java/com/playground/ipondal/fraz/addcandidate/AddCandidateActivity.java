package com.playground.ipondal.fraz.addcandidate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.playground.ipondal.fraz.R;
import com.playground.ipondal.fraz.data.source.CandidatesRepository;
import com.playground.ipondal.fraz.databinding.AddCandidateActivityBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCandidateActivity extends AppCompatActivity implements AddCantidateActions {
	private static final int TAKE_PHOTO = 0;
	private static final int CHOOSE_PHOTO_FROM_GALLERY = 1;

	private static final int REQUEST_PHOTO_FROM_GALLERY = 0;
	private static final int REQUEST_PHOTO_FROM_CAMERA = 1;
	public static final String MIME_IMAGE = "image/*";

	private AddCandidateViewModel mAddCandidateViewModel;
	private AddCandidateActivityBinding mAddCandidateActivityBinding;
	private AlertDialog mPhotoPromptDialog;
	private Uri cameraPhotoURI;
	private BroadcastReceiver mBroadcastReceiver;
	private CandidatesRepository mCandidatesRepository;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCandidatesRepository = new CandidatesRepository(FirebaseDatabase.getInstance());
		mAddCandidateViewModel = new AddCandidateViewModel(mCandidatesRepository, this, this);

		mAddCandidateActivityBinding = DataBindingUtil.setContentView(this, R.layout.add_candidate_activity);
		mAddCandidateActivityBinding.setViewmodel(mAddCandidateViewModel);

		mPhotoPromptDialog = buildCameraOrGalleryPhotoPickerDialog();
		mBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				switch (intent.getAction()) {
					case CompressAndUploadPhotoService.UPLOAD_COMPLETED:
						String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
						String imageRef = intent.getExtras().getString(CompressAndUploadPhotoService.EXTRA_IMAGE_REF);
					    mAddCandidateViewModel.uploadFinished(uid, imageRef);
					    finish();
						break;
				}
			}
		};
	}

	@Override
	protected void onStart() {
		super.onStart();

		LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
		manager.registerReceiver(mBroadcastReceiver, CompressAndUploadPhotoService.getIntentFilter());
	}

	private AlertDialog buildCameraOrGalleryPhotoPickerDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(R.string.character_photo_dialog_title);
		builder.setItems(R.array.candidate_photo_sources, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int selectedOptionIndex) {
				switch (selectedOptionIndex) {
					case TAKE_PHOTO:
						requestPhotoFromCamera();
						break;
					case CHOOSE_PHOTO_FROM_GALLERY:
						requestPhotoFromGallery();
						break;
				}
			}
		});

		return builder.create();
	}

	private void requestPhotoFromGallery() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType(MIME_IMAGE);
		startActivityForResult(intent, REQUEST_PHOTO_FROM_GALLERY);
	}

	private void requestPhotoFromCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File photoFile = null;
		try {
			photoFile = createImageFile();
		} catch (IOException exception) {
			// TODO: Show/log error message
		}
		if (photoFile != null) {
			cameraPhotoURI = FileProvider.getUriForFile(this, "com.playground.ipondal.fraz.fileprovider", photoFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoURI);
			startActivityForResult(intent, REQUEST_PHOTO_FROM_CAMERA);
		}
	}

	@Override
	public void promptPhoto() {
		mPhotoPromptDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent result) {
		super.onActivityResult(requestCode, resultCode, result);

		if (resultCode == RESULT_OK) switch (requestCode) {
			case REQUEST_PHOTO_FROM_GALLERY:
				mAddCandidateViewModel.loadCharacterPhoto(result.getData());
				break;
			case REQUEST_PHOTO_FROM_CAMERA:
				mAddCandidateViewModel.loadCharacterPhoto(cameraPhotoURI);
				break;
		}
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		return File.createTempFile(
				imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
				);
	}
}
