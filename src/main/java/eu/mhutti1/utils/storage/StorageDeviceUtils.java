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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class StorageDeviceUtils {
  private static ArrayList<String> mStorageDevices;

  public static ArrayList<StorageDevice> getStorageDevices() {
    mStorageDevices = new ArrayList<>();

    // Add default sd-card location
    mStorageDevices.add("/mnt/sdcard");

    // Check for other devices
    readVoldFile();

    // Check all devices exist and we can write to them
    return checkStorageValid();
  }

  private static void readVoldFile() {
    try {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/system/etc/vold.fstab")));

      for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
        if (line.startsWith("dev_mount")) {
          String word = line.split(" ")[2];

          if (word.contains(":"))
            word = word.substring(0, word.indexOf(":"));

          if (word.contains("usb"))
            continue;

          if (!mStorageDevices.contains(word))
            mStorageDevices.add(line);
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
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
