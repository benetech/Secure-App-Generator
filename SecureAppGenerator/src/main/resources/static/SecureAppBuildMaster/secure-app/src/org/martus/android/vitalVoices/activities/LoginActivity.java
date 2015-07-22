package org.martus.android.vitalVoices.activities;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.iangclifton.android.floatlabel.FloatLabel;

import org.martus.android.vitalVoices.utilities.Utility;
import org.martus.android.vitalVoices.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;

import info.guardianproject.cacheword.ICacheWordSubscriber;
import info.guardianproject.cacheword.Wiper;

/**
 * Created by animal@martus.org on 8/27/14.
 */
public class LoginActivity extends AbstractLoginActivity implements ICacheWordSubscriber {

    private static final String TAG = "LoginActivity";
    private TextView passPhraseEditField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_secure_app);

        FloatLabel floatingLabelEditText = ((FloatLabel) findViewById(R.id.passphrase_edit_field));
        passPhraseEditField = floatingLabelEditText.getEditText();
        Util.setupFloatingLabelEditTextForPassword(floatingLabelEditText.getEditText());

    }

    public void login(View view) {
        if (passPhraseEditField.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.label_empty_pass_phrase), Toast.LENGTH_SHORT).show();
            return;
        }

        char[] passphrase = passPhraseEditField.getText().toString().toCharArray();
        try {
            getCacheWordActivityHandler().setPassphrase(passphrase);
        } catch (GeneralSecurityException e) {
            Log.e(TAG, "Cacheword pass verification failed: " + e.getMessage());
            Toast.makeText(getApplicationContext(), getString(R.string.error_incorrect_passphrase), Toast.LENGTH_SHORT).show();
            passPhraseEditField.getEditableText().clear();
            login(view);
        }
        finally {
            Wiper.wipe(passphrase);
        }
    }

    @Override
    protected void postMountStorageExecute() {
        super.postMountStorageExecute();

        confirmAccount();
    }

    private void confirmAccount()  {
        try {
            byte[] keyPair = Base64.decode(getSettings().getString(AbstractLoginActivity.KEY_KEY_PAIR, ""), Base64.NO_WRAP);
            InputStream byteArrayInputStream = new ByteArrayInputStream(keyPair);

            char[] passphraseAsCharArray = Utility.convertToCharArray(getCacheWordActivityHandler().getEncryptionKey());
            getMartusCrypto().readKeyPair(byteArrayInputStream, passphraseAsCharArray);
        } catch (Exception e) {
            Util.alterUserOfUnexpectedError(this, e, "Problem confirming password");
        }

        getMartusCrypto().setShouldWriteAuthorDecryptableData(false);
    }

    @Override
    public void onCacheWordLocked() {
    }

    @Override
    protected TextView getPassPhraseTextField() {
        return passPhraseEditField;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }
}
