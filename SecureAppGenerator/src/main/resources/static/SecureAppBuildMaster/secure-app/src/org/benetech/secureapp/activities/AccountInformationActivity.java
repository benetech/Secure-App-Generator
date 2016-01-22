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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import org.benetech.secureapp.R;

/**
 * Created by animal@martus.org on 1/20/16.
 */
public class AccountInformationActivity extends Activity {

    private TextView authorTextView;
    private TextView organizationTextView;

    private static final String PRFERENCES_FILE_NAME = "account_information_pref_file";
    private static final String ORGANIZATION_PREFRENCES_KEY = "organization_key";
    private static final String AUTHOR_PREFERENCES_KEY = "author_key";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.account_information_layout);

        authorTextView = (TextView) findViewById(R.id.author_textfield);
        organizationTextView = (TextView) findViewById(R.id.organization_textfield);
        restoreFields();

        authorTextView.addTextChangedListener(new TextChangeHandler());
    }

    public void onSave(View view) {
        savePreferences();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void savePreferences() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(PRFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(AUTHOR_PREFERENCES_KEY, authorTextView.getText().toString());
        editor.putString(ORGANIZATION_PREFRENCES_KEY, organizationTextView.getText().toString());
        editor.commit();
    }

    private void restoreFields() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(PRFERENCES_FILE_NAME, Context.MODE_PRIVATE);

        authorTextView.setText(sharedPref.getString(AUTHOR_PREFERENCES_KEY, ""));
        organizationTextView.setText(sharedPref.getString(ORGANIZATION_PREFRENCES_KEY, ""));
    }

    private class TextChangeHandler implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savePreferences();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
