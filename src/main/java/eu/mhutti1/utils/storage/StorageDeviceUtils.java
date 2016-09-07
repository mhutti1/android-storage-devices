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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class StorageDeviceUtils {
  private static ArrayList<String> mStorageDevices;

  @TargetApi(Build.VERSION_CODES.KITKAT)
  public static ArrayList<StorageDevice> getStorageDevices(Activity activity) {
    mStorageDevices = new ArrayList<>();

    for (File file : activity.getExternalFilesDirs("")){
      mStorageDevices.add(file.getPath());
    }


    // Check all devices exist and we can write to them
    return checkStorageValid();
  }

  private static ArrayList<StorageDevice> checkStorageValid() {
    ArrayList<StorageDevice> activeDevices = new ArrayList<>();
    for (String device : mStorageDevices) {
      File devicePath = new File(device);
      if (devicePath.exists() && devicePath.isDirectory() && devicePath.canWrite())
        activeDevices.add(new StorageDevice(device));
    }
    return activeDevices;
  }
}
