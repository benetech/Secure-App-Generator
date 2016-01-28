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

package org.benetech.secureapp.activities;

import android.os.Handler;
import android.os.Message;

import org.benetech.secureapp.application.AppConfig;
import org.benetech.secureapp.application.MainApplication;
import org.martus.common.crypto.MartusSecurity;

import java.util.concurrent.TimeUnit;

/**
 * Created by animal@martus.org on 11/18/15.
 */
public class AppTimeoutManager {

    private MainApplication mainApplication;
    private static Handler inactivityHandler;
    private LogOutProcess inactivityCallback;
    private LogoutActivityHandler logoutActivityHandler;
    private static final int MINUTES = 7;
    private static final long INACTIVITY_TIMEOUT_MILLIS = TimeUnit.MINUTES.toMillis(MINUTES);

    public AppTimeoutManager(MainApplication mainApplicationToUse) {
        mainApplication = mainApplicationToUse;
        initInactivityHandler();
    }

    private void initInactivityHandler() {
        if (inactivityHandler == null)
            inactivityHandler = new EmptyHandler();

        if (inactivityCallback == null) {
            inactivityCallback = new LogOutProcess();
        }
    }

    public void registerLogoutHandler(LogoutActivityHandler logoutActivityHandlerToUse) {
        logoutActivityHandler = logoutActivityHandlerToUse;
        resetInactivityTimer();
    }

    public void resetInactivityTimer(){
        inactivityHandler.postDelayed(inactivityCallback, INACTIVITY_TIMEOUT_MILLIS);
    }

    public void disableInactivityTimer(){
        inactivityHandler.removeCallbacksAndMessages(null);
    }

    private MainApplication getMainApplication() {
        return mainApplication;
    }

    private class EmptyHandler extends Handler {
        public void handleMessage(Message msg) {}
    }

    private class LogOutProcess implements Runnable {

        @Override
        public void run() {
            MartusSecurity martusCrypto = AppConfig.getInstance(getMainApplication()).getCrypto();
            if (martusCrypto != null) {
                martusCrypto.clearKeyPair();
            }

            logoutActivityHandler.logout();
            logoutActivityHandler.finish();

            disableInactivityTimer();
        }
    }
}
