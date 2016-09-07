/*
 * Copyright 2016 Isaac Hutt <mhutti1@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU  General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package eu.mhutti1.utils.storage;


import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StorageSelectDialog extends DialogFragment implements ListView.OnItemClickListener{

  private StorageSelectArrayAdapter mAdapter;

  private OnSelectListener mOnSelectListener;

  private String mTitle;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.storage_select_dialog, container, false);
    TextView title = (TextView) rootView.findViewById(R.id.title);
    title.setText(mTitle);
    ListView listView = (ListView) rootView.findViewById(R.id.device_list);
    mAdapter = new StorageSelectArrayAdapter(getActivity(),0,StorageDeviceUtils.getStorageDevices(getActivity()));
    listView.setAdapter(mAdapter);
    listView.setOnItemClickListener(this);
    return rootView;
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (mOnSelectListener != null)
      mOnSelectListener.selectionCallback(mAdapter.getItem(position));
    dismiss();
  }

  public void setOnSelectListener(OnSelectListener selectListener){
    mOnSelectListener = selectListener;
  }

  public interface OnSelectListener {
    // you can define any parameter as per your requirement
    public void selectionCallback(StorageDevice s);
  }

  @Override
  public void show (FragmentManager fm, String text){
    mTitle = text;
    super.show(fm,text);
  }
}
