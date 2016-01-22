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

package org.benetech.secureapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.benetech.secureapp.R;

import java.util.ArrayList;

/**
 * Created by animal@martus.org on 4/28/15.
 */
public class AttachmentAdapter extends BaseAdapter {

    private AttachmentAdapterItemClickListener mListener;
    private ArrayList<String> mAttachmentFileNames;
    private LayoutInflater mLayoutInflator;

    public AttachmentAdapter(LayoutInflater layoutInflater, ArrayList<String> attachmentFileNames, AttachmentAdapterItemClickListener listener) {
        mLayoutInflator = layoutInflater;
        mAttachmentFileNames = attachmentFileNames;
        mListener = listener;
    }
  @Override
    public int getCount() {
        return mAttachmentFileNames.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup viewGroup) {
        View view = mLayoutInflator.inflate(R.layout.attachments_list_item, null);
        TextView attachmentName = (TextView) view.findViewById(R.id.attachmentFileName);
        attachmentName.setText(mAttachmentFileNames.get(index));

        ImageView deleteButton = (ImageView) view.findViewById(R.id.deleteAttachmentButton);
        deleteButton.setOnClickListener(mDeleteButtonListener);

        return view;
    }

    private View.OnClickListener mDeleteButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (mListener == null)
                return;

            View parentView = (View) view.getParent();
            TextView attachmentFileNameTextView = (TextView) parentView.findViewById(R.id.attachmentFileName);
            String attachmentFileName = attachmentFileNameTextView.getText().toString();
            mListener.onDeleteRequested(attachmentFileName);
        }
    };

    public void updateAttachmentFiles(ArrayList<String> attachmentFileNames) {
        mAttachmentFileNames = attachmentFileNames;
        super.notifyDataSetChanged();
    }
}
