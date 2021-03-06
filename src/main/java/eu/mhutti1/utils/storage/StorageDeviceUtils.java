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
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.regex.Matcher;

import static android.R.attr.path;
import static java.lang.Math.abs;


public class StorageDeviceUtils {
  private static ArrayList<StorageDevice> mStorageDevices;

  public static ArrayList<StorageDevice> getStorageDevices(Context context, boolean writable) {
    mStorageDevices = new ArrayList<>();

    // Add as many possible mount points as we know about

    // Only add this device if its very likely that we have missed a users sd card
    if (Environment.isExternalStorageEmulated()) {
      // This is our internal storage directory
      mStorageDevices.add(new StorageDevice(generalisePath(Environment.getExternalStorageDirectory().getPath(), writable), true));
    } else {
      // This is the internal directory of our app that only we can write to
      mStorageDevices.add(new StorageDevice(context.getFilesDir().getPath(), true));
      // This is an external storage directory
      mStorageDevices.add(new StorageDevice(generalisePath(Environment.getExternalStorageDirectory().getPath(), writable), false));
    }

    // These are possible manufacturer sdcard mount points

    String[] paths = ExternalPaths.getPossiblePaths();

    for (String path : paths) {
      if (path.endsWith("*")) {
        File root = new File(path.substring(0, path.length() - 1));
        File[] directories = root.listFiles(new FileFilter() {
          @Override
          public boolean accept(File file) {
            return file.isDirectory();
          }
        });
        if (directories != null) {
          for (File dir : directories) {
            mStorageDevices.add(new StorageDevice(dir, false));
          }
        }
      } else {
        mStorageDevices.add(new StorageDevice(path, false));
      }
    }

    // Iterate through any sdcards manufacturers may have specified in Kitkat+ and add them
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      for (File file : context.getExternalFilesDirs("")) {
        if (file != null) {
          mStorageDevices.add(new StorageDevice(generalisePath(file.getPath(), writable), false));
        }
      }
    }


    // Check all devices exist, we can write to them if required and they are not duplicates
    return checkStorageValid(writable);
  }

  // Remove app specific path from directories so that we can search them from the top
  public static String generalisePath(String path, boolean writable) {
    if (writable) {
      return path;
    }
    int endIndex = path.lastIndexOf("/Android/data/");
    if (endIndex != -1) {
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
      if (devicePath.exists() && devicePath.isDirectory() && (canWrite(devicePath) || !writable) && !device.isDuplicate() && !devicePaths.contains(devicePath)) {
        activeDevices.add(device);
        devicePaths.add(device);
        sizes.add(device.getTotalBytes());
      }
    }
    return activeDevices;
  }

  // Amazingly file.canWrite() does not always return the correct value
  private static boolean canWrite(File file) {
    try {
      RandomAccessFile randomAccessFile = new RandomAccessFile(file + "/test.txt", "rw");
      FileChannel fileChannel = randomAccessFile.getChannel();
      FileLock fileLock = fileChannel.lock();
      fileLock.release();
      fileChannel.close();
      randomAccessFile.close();
      return true;
    } catch (Exception ex) {
      return false;
    }
  }
}

