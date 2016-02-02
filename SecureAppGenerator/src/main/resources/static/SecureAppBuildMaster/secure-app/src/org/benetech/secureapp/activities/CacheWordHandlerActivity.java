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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import info.guardianproject.cacheword.CacheWordHandler;
import info.guardianproject.cacheword.ICacheWordSubscriber;

/**
 * Created by animal@martus.org on 8/27/14.
 */
public class CacheWordHandlerActivity extends Activity implements ICacheWordSubscriber {

    private CacheWordHandler cacheWordActivityHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cacheWordActivityHandler = new CacheWordHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getCacheWordActivityHandler().connectToService();
    }

    @Override
    protected void onPause() {
        super.onPause();

        getCacheWordActivityHandler().disconnectFromService();
    }

    @Override
    public void onCacheWordUninitialized() {
        Intent intent = new Intent(this, CreatePassphraseActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCacheWordLocked() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCacheWordOpened() {
        //NOTE: After we logout and lock, a CacheWord callback calls opened, which we dont want
        //Calling lock here to generate the onCachWordLocked event.  This might get fixed with a cachword update
        getCacheWordActivityHandler().lock();
    }

    protected CacheWordHandler getCacheWordActivityHandler() {
        return cacheWordActivityHandler;
    }
}