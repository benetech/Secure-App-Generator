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
import org.martus.common.FieldSpecCollection;
import org.martus.common.bulletin.AttachmentProxy;
import org.martus.common.bulletin.Bulletin;
import org.martus.common.crypto.MartusCrypto;
import org.martus.common.crypto.SessionKey;
import org.martus.common.packet.AttachmentPacket;
import org.martus.common.packet.BulletinHeaderPacket;

import java.io.IOException;

/**
 * Created by animal@martus.org on 4/23/15.
 */
public class SecureMobileBulletin extends Bulletin {
    public SecureMobileBulletin(MartusCrypto signatureGenerator, FieldSpecCollection topSectionSpecs, FieldSpecCollection bottomSectionSpecs) throws Exception {
        super(signatureGenerator, topSectionSpecs, bottomSectionSpecs);
    }

    @Override
    public void addPublicAttachment(AttachmentProxy attachmentProxy) throws IOException, MartusCrypto.EncryptionException {
        //TODO this implementation is almost a duplicate of the parent method.  Due to code freeze in desktop code, we
        //postponed refactoring of parent method. The parent should be refactored to expose only parts of this method
        //that can be then overriden.
        BulletinHeaderPacket bhp = getBulletinHeaderPacket();
        SecureMobileAttachmentProxy secureAttachmentProxy = (SecureMobileAttachmentProxy) attachmentProxy;
        SecureFile secureRawFile = secureAttachmentProxy.getFile();
        if(secureRawFile != null)
        {
            SessionKey sessionKey = getSignatureGenerator().createSessionKey();
            AttachmentPacket ap = new SecureMobileAttachmentPacket(getAccount(), sessionKey, secureRawFile, getSignatureGenerator());
            bhp.addPublicAttachmentLocalId(ap.getLocalId());
            attachmentProxy.setPendingPacket(ap, sessionKey);
        }
        else
        {
            bhp.addPublicAttachmentLocalId(attachmentProxy.getUniversalId().getLocalId());
        }

        getFieldDataPacket().addAttachment(attachmentProxy);
    }
}
