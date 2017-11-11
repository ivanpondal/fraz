package com.playground.ipondal.fraz.addcandidate;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import id.zelory.compressor.Compressor;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Service to handle uploading files to Firebase Storage.
 */
public class CompressAndUploadPhotoService extends Service {

	private static final String TAG = "MyUploadService";

	/** Intent Actions **/
	public static final String ACTION_UPLOAD = "action_upload";
	public static final String UPLOAD_COMPLETED = "upload_completed";
	public static final String UPLOAD_ERROR = "upload_error";

	/** Intent Extras **/
	public static final String EXTRA_FILE_URI = "extra_file_uri";
	public static final String EXTRA_IMAGE_REF = "extra_image_ref";

	private static final int EOF = -1;
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	private StorageReference mStorageRef;
	private String mUserUID;

	@Override
	public void onCreate() {
		super.onCreate();

		mStorageRef = FirebaseStorage.getInstance().getReference();
		mUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (ACTION_UPLOAD.equals(intent.getAction())) {
			Uri fileUri = intent.getParcelableExtra(EXTRA_FILE_URI);
			uploadFromBitmap(compressFromUri(getApplicationContext(), fileUri));
		}

		return START_REDELIVER_INTENT;
	}

	private Uri compressFromUri(Context context, Uri fileUri) {
		File compressedFile = null;
		try {
			compressedFile = new Compressor(context).compressToFile(from(context, fileUri));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Uri.fromFile(compressedFile);
	}

	private void uploadFromBitmap(final Uri imageURI) {

		final String imageRef = "images/" + mUserUID + ".jpg";
		final StorageReference photoRef = mStorageRef.child(imageRef);

		// Upload file to Firebase Storage
		photoRef.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
				broadcastUploadFinished(imageRef);
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception exception) {
				broadcastUploadFinished(null);
			}
		});
	}

	/**
	 * Broadcast finished upload (success or failure).
	 * 
	 * @return true if a running receiver received the broadcast.
	 */
	private boolean broadcastUploadFinished(@Nullable String imageRef) {
		boolean success = imageRef != null;

		String action = success ? UPLOAD_COMPLETED : UPLOAD_ERROR;
		Intent broadcast = new Intent(action).putExtra(EXTRA_IMAGE_REF, imageRef);

		return LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcast);
	}

	public static IntentFilter getIntentFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPLOAD_COMPLETED);
		filter.addAction(UPLOAD_ERROR);

		return filter;
	}

	private File from(Context context, Uri uri) throws IOException {
		InputStream inputStream = context.getContentResolver().openInputStream(uri);

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "COMPRESSED_JPEG_" + timeStamp + "_";

		File tempFile = File.createTempFile(imageFileName, ".jpg");
		tempFile.deleteOnExit();

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(tempFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (inputStream != null) {
			copy(inputStream, out);
			inputStream.close();
		}

		if (out != null) {
			out.close();
		}
		return tempFile;
	}

	private long copy(InputStream input, OutputStream output) throws IOException {
		long count = 0;
		int n;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
}
