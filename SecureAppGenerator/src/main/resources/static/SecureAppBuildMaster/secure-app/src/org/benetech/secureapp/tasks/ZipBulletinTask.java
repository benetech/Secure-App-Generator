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

import android.os.AsyncTask;
import android.util.Log;

import org.benetech.secureapp.R;
import org.benetech.secureapp.application.MainApplication;
import org.martus.android.library.utilities.AsyncTaskResult;
import org.martus.android.library.utilities.BulletinZipper;
import org.martus.clientside.MobileClientBulletinStore;
import org.martus.common.bulletin.Bulletin;
import org.martus.common.bulletin.BulletinZipUtilities;
import org.martus.common.bulletin.DebugClass;
import org.martus.common.crypto.MartusCrypto;
import org.martus.common.database.DatabaseKey;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author roms Date: 10/3/12
 *         animal@martus.org 09/21/2014
 */

public class ZipBulletinTask extends AsyncTask<Object, Integer, AsyncTaskResult<File>> {

    private static final SimpleDateFormat FILE_NAME_DATE_FORMAT = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SS_");
    private Bulletin mBulletin;
    private BulletinZipper mBulletinZipper;

    public  ZipBulletinTask(Bulletin bulletin, BulletinZipper sender) {
        mBulletin = bulletin;
        mBulletinZipper = sender;
    }

    @Override
    protected AsyncTaskResult<File> doInBackground(Object... params) {

        final File currentBulletinDir = (File)params[0];
        final MobileClientBulletinStore store = (MobileClientBulletinStore)params[1];
        final String bulletinFileExtension = (String) params[2];

        try {
            SingleBulletinDataBase database = new SingleBulletinDataBase();
            store.setDatabase(database);
            store.saveBulletin(mBulletin);
            File file = File.createTempFile("tmp_send_" + getCurrentTimeStamp(), bulletinFileExtension, currentBulletinDir);
            DebugClass.setDebugToTrue();

            DatabaseKey databaseKey = mBulletin.getDatabaseKey();
            MartusCrypto signatureGenerator = mBulletin.getSignatureGenerator();
            BulletinZipUtilities.exportBulletinPacketsFromDatabaseToZipFile(database, databaseKey, file, signatureGenerator);
            if (!file.exists())
                throw new FileNotFoundException(MainApplication.getInstance().getString(R.string.error_message_could_not_find_record_zip_file_after_it_was_created));

            store.clearCache();
            return new AsyncTaskResult<>(file);
        } catch (Exception e) {
            Log.e("martus", MainApplication.getInstance().getString(R.string.error_message_problem_serializing_record_to_zip), e);

            return new AsyncTaskResult<>(e);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<File> result) {
        if (null != mBulletinZipper) {
            mBulletinZipper.onZipped(mBulletin, result);
        }

        super.onPostExecute(result);
    }

    private String getCurrentTimeStamp() {
        Date now = new Date();
        return FILE_NAME_DATE_FORMAT.format(now);
    }
}
