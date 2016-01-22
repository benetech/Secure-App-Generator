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

import java.io.FileNotFoundException;

import org.benetech.secureapp.R;
import org.benetech.secureapp.collect.io.SecureFileStorageManager;
import org.martus.android.library.io.SecureFile;
import org.odk.collect.android.provider.FormsProvider;
import org.odk.collect.android.utilities.FileUtils;
import org.odk.collect.android.utilities.MediaUtils;

import android.util.Log;

/**
 * A FormsProvider that channels all File I/O through IOCipher
 * 
 * @author David Brodsky (dbro@dbro.pro)
 *
 */
public class SecureFormsProvider extends FormsProvider {
	private static final String t = "SecureFormsProvider";
	
	protected String getNameForFormAtPath(String path) {
		return new SecureFile(path).getName();
	}
	
	protected String normalizePath(String path) {
		SecureFile form = new SecureFile(path);
		return form.getAbsolutePath();
	}
	
	protected String getMd5HashForFormAtPath(String path) {
		try {
			return FileUtils.getMd5Hash(SecureFileStorageManager.openFile(path));
		} catch (FileNotFoundException e) {
			Log.e(t, getContext().getString(R.string.error_message_could_not_find_data_for_path, path), e);
			return null;
		}
	}

	protected void deleteFileOrDir(String fileName) {
		SecureFile file = new SecureFile(fileName);
		if (file.exists()) {
			if (file.isDirectory()) {
				// delete any media entries for files in this directory...
				int images = MediaUtils
						.deleteImagesInFolderFromMediaProvider(file);
				int audio = MediaUtils
						.deleteAudioInFolderFromMediaProvider(file);
				int video = MediaUtils
						.deleteVideoInFolderFromMediaProvider(file);

				Log.i(t, "removed from content providers: " + images
						+ " image files, " + audio + " audio files," + " and "
						+ video + " video files.");

				// delete all the containing files
				SecureFile[] files = file.listFiles();
				for (SecureFile f : files) {
					// should make this recursive if we get worried about
					// the media directory containing directories
					Log.i(t,
							"attempting to delete file: " + f.getAbsolutePath());
					f.delete();
				}
			}
			file.delete();
			Log.i(t, "attempting to delete file: " + file.getAbsolutePath());
		}
	}

}
