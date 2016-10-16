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

import android.os.Build;
import android.os.StatFs;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.sql.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class StorageDevice {

  private File mFile;

  public StorageDevice(String path){
    mFile = new File(path);
  }

  public StorageDevice(File file){
    mFile = file;
  }

  public String getName(){
    return mFile.getPath();
  }

  public String getSize(){
    StatFs statFs = new StatFs(mFile.getPath());
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      return String.valueOf(bytesToHuman(statFs.getBlockSizeLong() *  statFs.getAvailableBlocksLong()));
    } else {
      return String.valueOf(bytesToHuman((long) statFs.getBlockSize() *  (long) statFs.getAvailableBlocks()));
    }
  }

  public Long getBytes() {
    StatFs statFs = new StatFs(mFile.getPath());
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      return statFs.getBlockSizeLong() *  statFs.getAvailableBlocksLong();
    } else {
      return Long.valueOf((long) statFs.getBlockSize() *  (long) statFs.getAvailableBlocks());
    }
  }

  public Long getTotalBytes() {
    StatFs statFs = new StatFs((mFile.getPath()));
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      return statFs.getBlockSizeLong() *  statFs.getBlockCountLong();
    } else {
      return Long.valueOf((long) statFs.getBlockSize() * (long) statFs.getBlockCount());
    }
  }

  public static String bytesToHuman (long size) {
    long Kb = 1  * 1024;
    long Mb = Kb * 1024;
    long Gb = Mb * 1024;
    long Tb = Gb * 1024;
    long Pb = Tb * 1024;
    long Eb = Pb * 1024;

    if (size <  Kb)                 return floatForm(        size     ) + " byte";
    if (size >= Kb && size < Mb)    return floatForm((double)size / Kb) + " KB";
    if (size >= Mb && size < Gb)    return floatForm((double)size / Mb) + " MB";
    if (size >= Gb && size < Tb)    return floatForm((double)size / Gb) + " GB";
    if (size >= Tb && size < Pb)    return floatForm((double)size / Tb) + " TB";
    if (size >= Pb && size < Eb)    return floatForm((double)size / Pb) + " PB";
    if (size >= Eb)                 return floatForm((double)size / Eb) + " EB";

    return "???";
  }

  /*public ArrayList<File> getDirectories() {
    ArrayList<File> folders = new ArrayList<File>(Arrays.asList(mFile.listFiles(new FileFilter() {
      @Override
      public boolean accept(File file) {
        return file.isDirectory();
      }
    })));
    ArrayList<File> relativeFolders = new ArrayList<>();
    for (File file : folders){
      relativeFolders.add(new File(mFile.toURI().relativize(file.toURI()).getPath()));
    }

    return relativeFolders;
  }*/

  /*@Override
  public boolean equals(Object obj) {
    if (obj instanceof StorageDevice){
      StorageDevice device = (StorageDevice) obj;
      return device.getDirectories().containsAll(getDirectories());
    }
    return false;
  }*/

  public static String floatForm (double d)
  {
    return new DecimalFormat("#.##").format(d);
  }

}
