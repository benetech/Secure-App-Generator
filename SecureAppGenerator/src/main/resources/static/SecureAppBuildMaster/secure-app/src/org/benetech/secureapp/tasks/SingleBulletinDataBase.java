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

import android.os.Environment;

import org.martus.common.crypto.MartusCrypto;
import org.martus.common.database.DatabaseKey;
import org.martus.util.UnicodeReader;
import org.martus.util.inputstreamwithseek.FileInputStreamWithSeek;
import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by animal@martus.org on 9/22/14.
 */

public class SingleBulletinDataBase extends AbstractSingleBulletinDataBase {

    public SingleBulletinDataBase() throws Exception {

        deleteAllData();
    }

    @Override
    public void deleteAllData() throws Exception {
        databaseKeyToStringMap = new HashMap<DatabaseKey, String>();
    }

    @Override
    public InputStreamWithSeek openInputStream(DatabaseKey rawKey, MartusCrypto decryptor) throws IOException, MartusCrypto.CryptoException {
        DatabaseKey legacyDatabaseKey = createLegacyDatabaseKey(rawKey);
        String packetAsString = getDatabaseKeyToStringMap().get(legacyDatabaseKey);

        return new StringInputStreamWithSeek(packetAsString);
    }

    @Override
    public void writeRecord(DatabaseKey rawKey, String record) throws IOException, RecordHiddenException {
        DatabaseKey legacyDatabaseKey = createLegacyDatabaseKey(rawKey);
        getDatabaseKeyToStringMap().put(legacyDatabaseKey, record);
    }

    @Override
    public void visitAllRecords(PacketVisitor visitor) {
        Set keys = getDatabaseKeyToStringMap().keySet();
        Iterator iterator = keys.iterator();
        while(iterator.hasNext())
        {
            DatabaseKey key = (DatabaseKey)iterator.next();
            try
            {
                visitor.visit(key);
            }
            catch (RuntimeException nothingWeCanDoAboutIt)
            {
                // nothing we can do, so ignore it
            }
        }
    }

    @Override
    public boolean doesRecordExist(DatabaseKey rawKey) {
        DatabaseKey legacyDatabaseKey = createLegacyDatabaseKey(rawKey);

        return getDatabaseKeyToStringMap().containsKey(legacyDatabaseKey);
    }

    private DatabaseKey createLegacyDatabaseKey(DatabaseKey rawKey) {
        return DatabaseKey.createLegacyKey(rawKey.getUniversalId());
    }

    private HashMap<DatabaseKey, String> getDatabaseKeyToStringMap() {
        return databaseKeyToStringMap;
    }

    @Override
    public void importFiles(HashMap fileMapping) throws IOException, RecordHiddenException {
        Iterator keys = fileMapping.keySet().iterator();
        while(keys.hasNext()) {
            DatabaseKey key = (DatabaseKey) keys.next();
            File fromFile = (File) fileMapping.get(key);
            String asString = loadFileContentAsUtf8(fromFile);
            getDatabaseKeyToStringMap().put(key, asString);
        }
    }

    private String loadFileContentAsUtf8(File fileToReadIn) throws IOException, RecordHiddenException {
        FileInputStreamWithSeek inputFileStream = new FileInputStreamWithSeek(fileToReadIn);
        try {
            UnicodeReader reader = new UnicodeReader(inputFileStream);

            return reader.readAll();
        } finally {
            inputFileStream.close();
        }
    }

    //FIXME urgent - this needs to be inspected:
    // - is this the correct location we want
    // - are unecryped files being written to this location
    // - should this be dir in IOCipher
    @Override
    public File getInterimDirectory(String accountId) throws IOException {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }



    private HashMap<DatabaseKey, String> databaseKeyToStringMap;
}
