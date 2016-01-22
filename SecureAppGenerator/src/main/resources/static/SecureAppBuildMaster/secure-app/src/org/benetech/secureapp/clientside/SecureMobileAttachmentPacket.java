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

package org.benetech.secureapp.clientside;

import org.martus.android.library.io.SecureFile;
import org.martus.android.library.io.SecureFileInputStream;
import org.martus.common.crypto.MartusCrypto;
import org.martus.common.crypto.SessionKey;
import org.martus.common.packet.AttachmentPacket;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by animal@martus.org on 4/23/15.
 */
public class SecureMobileAttachmentPacket extends AttachmentPacket{
    public SecureMobileAttachmentPacket(String account, SessionKey sessionKeyToUse, SecureFile secureFileToAttach, MartusCrypto crypto) {
        super(account, sessionKeyToUse, secureFileToAttach, crypto);

        mSecureFile = secureFileToAttach;
    }

    @Override
    protected InputStream createFileInputStream() throws FileNotFoundException {
        return new SecureFileInputStream(getSecureRawFile());
    }

    private SecureFile getSecureRawFile() {
        return mSecureFile;
    }

    private SecureFile mSecureFile;
}
