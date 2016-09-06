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
import java.text.DecimalFormat;

public class StorageDevice {

  private File mFile;

  public StorageDevice(String path){
    mFile = new File(path);
  }

  public String getName(){
    return mFile.getPath();
  }

  public String getSize(){
    StatFs statFs = new StatFs(mFile.getPath());
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      return String.valueOf(bytesToHuman(statFs.getBlockSizeLong() *  statFs.getAvailableBlocksLong()));
    } else {
      return String.valueOf(bytesToHuman(statFs.getBlockSize() *  statFs.getAvailableBlocks()));
    }
  }

  public static String bytesToHuman (long size)
  {
    long Kb = 1  * 1024;
    long Mb = Kb * 1024;
    long Gb = Mb * 1024;
    long Tb = Gb * 1024;
    long Pb = Tb * 1024;
    long Eb = Pb * 1024;

    if (size <  Kb)                 return floatForm(        size     ) + " byte";
    if (size >= Kb && size < Mb)    return floatForm((double)size / Kb) + " Kb";
    if (size >= Mb && size < Gb)    return floatForm((double)size / Mb) + " Mb";
    if (size >= Gb && size < Tb)    return floatForm((double)size / Gb) + " Gb";
    if (size >= Tb && size < Pb)    return floatForm((double)size / Tb) + " Tb";
    if (size >= Pb && size < Eb)    return floatForm((double)size / Pb) + " Pb";
    if (size >= Eb)                 return floatForm((double)size / Eb) + " Eb";

    return "???";
  }


  public static String floatForm (double d)
  {
    return new DecimalFormat("#.##").format(d);
  }

}
