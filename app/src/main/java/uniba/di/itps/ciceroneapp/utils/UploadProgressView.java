package uniba.di.itps.ciceroneapp.utils;

/**
 * Created by H.T. on 09/12/17.
 */

public interface UploadProgressView {
    void showUploadProgressDialog();

    void hideUploadProgressDialog();

    void showUploadProgress(int progress);

    void showMessage(String errorMessage);
}
