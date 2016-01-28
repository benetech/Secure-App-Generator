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

package org.benetech.secureapp.collect.provider;

import org.martus.android.library.io.SecureFile;
import org.odk.collect.android.application.Collect;
import org.odk.collect.android.provider.InstanceProvider;
import org.odk.collect.android.utilities.MediaUtils;

import android.util.Log;

/**
 * An InstanceProvider that channels all File I/O through IOCipher
 * 
 * @author David Brodsky (dbro@dbro.pro)
 *
 */
public class SecureInstanceProvider extends InstanceProvider {
	private static final String t = "SecureInstanceProvider";
	
	protected void deleteAllFilesInDirectory(String directoryPath) {
    	SecureFile directory = new SecureFile(directoryPath);
        if (directory.exists()) {
        	// do not delete the directory if it might be an
        	// ODK Tables instance data directory. Let ODK Tables
        	// manage the lifetimes of its filled-in form data
        	// media attachments.
            if (directory.isDirectory() && !Collect.isODKTablesInstanceDataDirectory(directory)) {
            	// delete any media entries for files in this directory...
                int images = MediaUtils.deleteImagesInFolderFromMediaProvider(directory);
                int audio = MediaUtils.deleteAudioInFolderFromMediaProvider(directory);
                int video = MediaUtils.deleteVideoInFolderFromMediaProvider(directory);

                Log.i(t, "removed from content providers: " + images
                        + " image files, " + audio + " audio files,"
                        + " and " + video + " video files.");

                // delete all the files in the directory
                SecureFile[] files = directory.listFiles();
                for (SecureFile f : files) {
                    // should make this recursive if we get worried about
                    // the media directory containing directories
                    f.delete();
                }
            }
            directory.delete();
        }
    }
	
	protected java.io.File getParent(String childPath) {
    	return new SecureFile(childPath).getParentFile();
    }

}
