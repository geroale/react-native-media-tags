package com.mediatags;

import android.media.MediaMetadataRetriever;
import android.util.Base64;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

import java.io.File;
import java.util.HashMap;

public class MediatagsModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public MediatagsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "Mediatags";
    }

    @ReactMethod
    public void getMetaData(String filePath, final Promise callback) {
        File file = new File(filePath);

        if (file.exists()) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                WritableMap resultData = new WritableNativeMap();
                retriever.setDataSource(file.getAbsolutePath());

                resultData.putString("artist", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                resultData.putString("album", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                resultData.putString("title", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));

                callback.resolve(resultData);
            } catch (Exception e) {
                callback.reject(e);
            }
        } else {
            callback.reject("404", "File doesn't exist");
        }
    }

    @ReactMethod
    public void getArtwork(String filePath, Callback callback) {
        File file = new File(filePath);

        if (file.exists()) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(file.getAbsolutePath());

                byte[] audioAlbumArtBytes = retriever.getEmbeddedPicture();
                if (audioAlbumArtBytes == null) {
                    callback.invoke(0);
                    return;
                }

                String base64 = Base64.encodeToString(audioAlbumArtBytes, Base64.DEFAULT);

                callback.invoke(base64);
            } catch (Exception e) {
                callback.invoke(0);
            }
        } else {
            callback.invoke(0);
        }
    }
}
