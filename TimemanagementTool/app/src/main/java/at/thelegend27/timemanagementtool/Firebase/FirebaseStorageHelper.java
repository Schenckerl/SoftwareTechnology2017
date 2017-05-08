package at.thelegend27.timemanagementtool.Firebase;


import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseStorageHelper {

    private static final String TAG = FirebaseStorageHelper.class.getCanonicalName();

    private FirebaseStorage firebaseStorage;

    private StorageReference rootRef;

    private Context context;

    public FirebaseStorageHelper(Context context){
        this.context = context;
        init();
    }

    private void init(){
        this.firebaseStorage = FirebaseStorage.getInstance();
        rootRef = firebaseStorage.getReferenceFromUrl("gs://fir-analyticexample.appspot.com");
    }

    public void saveProfileImageToCloud(String userId, Uri selectedImageUri, final ImageView imageView) {

    }
}
