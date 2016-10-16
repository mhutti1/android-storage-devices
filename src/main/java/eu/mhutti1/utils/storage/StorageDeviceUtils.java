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

import static java.lang.Math.abs;


public class StorageDeviceUtils {
  private static ArrayList<String> mStorageDevices;

  public static ArrayList<StorageDevice> getStorageDevices(Activity activity, boolean writable) {
    mStorageDevices = new ArrayList<>();

    // Add as many possible mount points as we know about
    mStorageDevices.add(generalisePath(Environment.getExternalStorageDirectory().getPath(), writable));


    mStorageDevices.add(activity.getFilesDir().getPath());
    mStorageDevices.add("/storage/sdcard1");
    mStorageDevices.add("/storage/extsdcard");
    mStorageDevices.add("/storage/sdcard0/external_sdcard");
    mStorageDevices.add("/mnt/sdcard/external_sd");
    mStorageDevices.add("/mnt/external_sd");
    mStorageDevices.add("/mnt/media_rw/sdcard1");
    mStorageDevices.add("/removable/microsd");
    mStorageDevices.add("/mnt/emmc");
    mStorageDevices.add("/storage/external_SD");
    mStorageDevices.add("/storage/ext_sd");
    mStorageDevices.add("/storage/removable/sdcard1");
    mStorageDevices.add("/data/sdext");
    mStorageDevices.add("/data/sdext2");
    mStorageDevices.add("/data/sdext3");
    mStorageDevices.add("/data/sdext2");
    mStorageDevices.add("/data/sdext3");
    mStorageDevices.add("/data/sdext4");
    mStorageDevices.add("/sdcard");
    mStorageDevices.add("/sdcard1");
    mStorageDevices.add("/sdcard2");
    mStorageDevices.add("/storage/microsd");



    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      for (File file : activity.getExternalFilesDirs("")) {
        if (file != null) {
          mStorageDevices.add(generalisePath(file.getPath(), writable));
        }
      }
    }


    // Check all devices exist and we can write to them
    return checkStorageValid(writable);
  }

  public static String generalisePath(String path, boolean writable){
    if (writable)
      return path;
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
    for (String device : mStorageDevices) {
      File devicePath = new File(device);
      StorageDevice storageDevice = new StorageDevice(device);
      if (devicePath.exists() && devicePath.isDirectory() && (devicePath.canWrite() || !writable) && !contains(sizes, storageDevice.getTotalBytes())) {
        activeDevices.add(storageDevice);
        devicePaths.add(storageDevice);
        sizes.add(storageDevice.getTotalBytes());
      }
    }
    return activeDevices;
  }

  private static boolean contains (ArrayList<Long> sizes, Long size){
    for (Long l : sizes){
      if (abs(l - size) < 10000) {
        return true;
      }
    }
    return false;
  }


}
