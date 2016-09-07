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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class StorageSelectArrayAdapter extends ArrayAdapter<StorageDevice>{

  private ArrayList<StorageDevice> mStorageDevices;

  public StorageSelectArrayAdapter(Context context, int resource, ArrayList<StorageDevice> devices) {
    super(context, resource, devices);
    mStorageDevices = devices;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    ViewHolder holder;
    if (convertView == null) {
      convertView = View.inflate(getContext(), R.layout.device_item, null);
      holder = new ViewHolder();
      holder.fileName = (TextView) convertView.findViewById(R.id.file_name);
      holder.fileSize = (TextView) convertView.findViewById(R.id.file_size);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.fileName.setText(String.valueOf(position + 1));
    holder.fileSize.setText(getItem(position).getSize());

    return convertView;

  }

  private class ViewHolder {
    public TextView fileName;
    public TextView fileSize;
  }
}
