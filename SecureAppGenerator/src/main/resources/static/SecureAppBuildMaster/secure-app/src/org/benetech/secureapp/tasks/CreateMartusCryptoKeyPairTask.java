/*
 * The Martus(tm) free, social justice documentation and
 * monitoring software. Copyright (C) 2016, Beneficent
 * Technology, Inc. (Benetech).
 *
 * Martus is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later
 * version with the additions and exceptions described in the
 * accompanying Martus license file entitled "license.txt".
 *
 * It is distributed WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, including warranties of fitness of purpose or
 * merchantability.  See the accompanying Martus License and
 * GPL license for more details on the required license terms
 * for this software.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package org.benetech.secureapp.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.benetech.secureapp.R;
import org.benetech.secureapp.activities.AbstractLoginActivity;
import org.benetech.secureapp.application.MainApplication;
import org.martus.common.crypto.MartusCrypto;

import java.io.ByteArrayOutputStream;

/**
 * Created by animal@martus.org on 10/3/14.
 */
public class CreateMartusCryptoKeyPairTask extends AsyncTask<Object, Void, Boolean> {

    private static final String TAG = "CreateMartusCryptoKeyPairTask";
    private MartusCrypto mMartusCrypto;
    private CreateMartusCryptoKeyPairCallback mCallback;
    private SharedPreferences mSettings;

    /** Callback used by clients of this class */
    public interface CreateMartusCryptoKeyPairCallback {
        public void onCreateKeyPairError();
        public void onCreateKeyPairSuccess();
    }

    public CreateMartusCryptoKeyPairTask(MartusCrypto martusCrypto, CreateMartusCryptoKeyPairCallback callback, SharedPreferences settings) {
        mMartusCrypto = martusCrypto;
        mCallback = callback;
        mSettings = settings;
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            getMartusCrypto().createKeyPair();
            char[] passwordArray = (char[]) params[0];

            getMartusCrypto().writeKeyPair(out, passwordArray);
            out.close();

            byte[] keyPairData = out.toByteArray();
            String encodedKeyPair = Base64.encodeToString(keyPairData, Base64.NO_WRAP);

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(AbstractLoginActivity.KEY_KEY_PAIR, encodedKeyPair);
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG, MainApplication.getInstance().getString(R.string.error_message_problem_creating_account), e);
            mCallback.onCreateKeyPairError();
            return false;
        }

        return true;
    }

    private MartusCrypto getMartusCrypto() {
        return mMartusCrypto;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);

        if (success)
            mCallback.onCreateKeyPairSuccess();
    }
}
