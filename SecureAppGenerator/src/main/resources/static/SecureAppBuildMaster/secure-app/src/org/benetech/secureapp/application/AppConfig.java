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

package org.benetech.secureapp.application;

import android.app.Application;
import android.util.Log;

import org.benetech.secureapp.R;
import org.benetech.secureapp.clientside.SecureMobileClientBulletinStore;
import org.martus.clientside.MobileClientSideNetworkGateway;
import org.martus.common.crypto.MartusSecurity;
import org.martus.common.crypto.MobileMartusSecurity;
import org.martus.common.fieldspec.StandardFieldSpecs;
import org.martus.common.network.ClientSideNetworkInterface;
import org.martus.common.network.PassThroughTransportWrapper;

/**
 * @author roms, animal@martus.org Date: 09/11/14
 */

//FIXME this class was duplicated from martus mobile code, then stripped down of any settings saving code.
//Refactor martus mobile's AppConfig and strip it to only settings related code.
public class AppConfig {

    private static final String TAG = "AppConfig";
    private static AppConfig instance;
    private MartusSecurity martusCrypto;
	private PassThroughTransportWrapper transport;
	private ClientSideNetworkInterface currentNetworkInterfaceHandler;
	private MobileClientSideNetworkGateway currentNetworkInterfaceGateway;
    private SecureMobileClientBulletinStore store;
    private MainApplication mMainApplication;

    public static AppConfig getInstance(Application mainApplication) {
        if (instance == null) {
            instance = new AppConfig((MainApplication) mainApplication);
        }

        return instance;
    }

    private AppConfig(MainApplication context) {
        mMainApplication = context;

        transport = new PassThroughTransportWrapper();
        try {
            martusCrypto = new MobileMartusSecurity();
        } catch (Exception e) {
            Log.e(TAG, context.getString(R.string.error_message_unable_to_initialize_crypto), e);
        }

        store = new SecureMobileClientBulletinStore(martusCrypto);
        try {
            store.doAfterSigninInitialization(mMainApplication.getSecureStorageDir());
        } catch (Exception e) {
            Log.e(TAG, context.getString(R.string.error_message_untable_to_initialize_store), e);
        }

        store.setTopSectionFieldSpecs(StandardFieldSpecs.getDefaultTopSectionFieldSpecs());
        store.setBottomSectionFieldSpecs(StandardFieldSpecs.getDefaultBottomSectionFieldSpecs());
    }

    public MartusSecurity getCrypto() {
        return martusCrypto;
    }

	public PassThroughTransportWrapper getTransport() {
		return transport;
	}

	public MobileClientSideNetworkGateway getCurrentNetworkInterfaceGateway(String serverIp, String serverPublicKey)
	{
		if(currentNetworkInterfaceGateway == null) {
			currentNetworkInterfaceGateway = new MobileClientSideNetworkGateway(getCurrentNetworkInterfaceHandler(serverIp, serverPublicKey));
		}

		return currentNetworkInterfaceGateway;
	}

    public void invalidateCurrentHandlerAndGateway()
    {
        currentNetworkInterfaceHandler = null;
        currentNetworkInterfaceGateway = null;
    }

    private ClientSideNetworkInterface getCurrentNetworkInterfaceHandler(String serverIp, String serverPublicKey)
	{
		if(currentNetworkInterfaceHandler == null) {
			currentNetworkInterfaceHandler = createXmlRpcNetworkInterfaceHandler(serverIp, serverPublicKey);
		}

		return currentNetworkInterfaceHandler;
	}

	private ClientSideNetworkInterface createXmlRpcNetworkInterfaceHandler(String serverIp, String serverPublicKey)
	{
		return MobileClientSideNetworkGateway.buildNetworkInterface(serverIp, serverPublicKey, transport);
	}

    public SecureMobileClientBulletinStore getStore() {
        return store;
    }
}
