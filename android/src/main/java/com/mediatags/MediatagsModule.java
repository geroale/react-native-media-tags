package com.mediatags;

import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

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
            //Added in API level 10
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                HashMap<String, Object> tags = new HashMap<>();
                retriever.setDataSource(file.getAbsolutePath());

                tags.put("artist", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                tags.put("album", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                tags.put("title", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));

                callback.resolve(tags);
            } catch (Exception e) {
                callback.reject(e);
            }
        } else {
            callback.reject("404", "File doesn't exist");
        }
    }

    @ReactMethod
    public void getArtwork(String stringArgument, final Promise callback) {
        // TODO: Implement some actually useful functionality
        callback.reject("500", "no");
    }
}
