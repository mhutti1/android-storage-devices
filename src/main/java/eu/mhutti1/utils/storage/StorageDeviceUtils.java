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
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;

import static java.lang.Math.abs;


public class StorageDeviceUtils {
  private static ArrayList<StorageDevice> mStorageDevices;

  public static ArrayList<StorageDevice> getStorageDevices(Activity activity, boolean writable) {
    mStorageDevices = new ArrayList<>();

    // Add as many possible mount points as we know about

    // Only add this device if its very likely that we have missed a users sd card
    if (Environment.isExternalStorageEmulated()) {
      // This is our internal storage directory
      mStorageDevices.add(new StorageDevice(generalisePath(Environment.getExternalStorageDirectory().getPath(), writable), "Internal"));
    } else {
      // This is the internal directory of our app that only we can write to
      mStorageDevices.add(new StorageDevice(activity.getFilesDir().getPath(), "Internal"));
      // This is an external storage directory
      mStorageDevices.add(new StorageDevice(generalisePath(Environment.getExternalStorageDirectory().getPath(), writable), "External"));
    }

    // These are possible manufacturer sdcard mount points

    String[] paths = ExternalPaths.getPossiblePaths();

    for (String path : paths){
      mStorageDevices.add(new StorageDevice(path, "External"));
    }



    // Iterate through any sdcards manufacturers may have specified in Kitkat+ and add them
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      for (File file : activity.getExternalFilesDirs("")) {
        if (file != null) {
          mStorageDevices.add(new StorageDevice(generalisePath(file.getPath(), writable), "External"));
        }
      }
    }


    // Check all devices exist, we can write to them if required and they are not duplicates
    return checkStorageValid(writable);
  }

  // Remove app specific path from directories so that we can search them from the top
  public static String generalisePath(String path, boolean writable){
    if (writable) {
      return path;
    }
    int endIndex = path.lastIndexOf("/Android/data/");
    if (endIndex != -1)
    {
      return path.substring(0, endIndex);
    }
    return path;
  }

  private static ArrayList<StorageDevice> checkStorageValid(boolean writable) {
    ArrayList<StorageDevice> activeDevices = new ArrayList<>();
    ArrayList<StorageDevice> devicePaths = new ArrayList<>();
    ArrayList<Long> sizes = new ArrayList<>();
    for (StorageDevice device : mStorageDevices) {
      File devicePath = device.getPath();
      // Only return paths that exist, are directories, are writable (if required) and are not duplicates
      if (devicePath.exists() && devicePath.isDirectory() && (devicePath.canWrite() || !writable) && !device.isDuplicate() && !devicePaths.contains(devicePath)) {
        activeDevices.add(device);
        devicePaths.add(device);
        sizes.add(device.getTotalBytes());
      }
    }
    return activeDevices;
  }
}
