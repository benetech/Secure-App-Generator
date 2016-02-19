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
import android.util.Log;

import org.benetech.secureapp.application.AppConfig;
import org.benetech.secureapp.application.MainApplication;
import org.martus.common.crypto.MartusSecurity;

import java.util.concurrent.TimeUnit;

/**
 * Created by animal@martus.org on 11/18/15.
 */
public class AppTimeoutManager {

    private static final String LOG_TAG = "AppTimeoutManager";
    private MainApplication mainApplication;
    private static Handler inactivityHandler;
    private LogOutProcess inactivityCallback;
    private LogoutActivityHandler logoutActivityHandler;
    private static final int MINUTES = 7;
    private static final long INACTIVITY_TIMEOUT_MILLIS = TimeUnit.MINUTES.toMillis(MINUTES);
    private boolean isTimerEnabled;

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
        if (isTimerEnabled())
            postDelayed();
    }

    public void enableInactivityTimer() {
        if (isTimerEnabled())
            Log.e(LOG_TAG, "Timer is already enabled, calling this method twice is unnecessary!");

        enableTimer();
        postDelayed();
    }

    public void disableInactivityTimer() {
        if (!isTimerEnabled())
            Log.e(LOG_TAG, "Timer is already disabled, calling this method twice is unnecessary!");

        disableTimer();
        inactivityHandler.removeCallbacksAndMessages(null);
    }

    private void postDelayed() {
        inactivityHandler.postDelayed(inactivityCallback, INACTIVITY_TIMEOUT_MILLIS);
    }

    private MainApplication getMainApplication() {
        return mainApplication;
    }

    private boolean isTimerEnabled() {
        return isTimerEnabled;
    }

    private void enableTimer() {
        isTimerEnabled =  true;
    }

    private void disableTimer() {
        isTimerEnabled = false;
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
            Log.i(LOG_TAG, "SecureApp is now logging out due to inactivity");
        }
    }
}
