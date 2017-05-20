# android-storage-devices
Get a list of writable storage locations in android.

To use this. First create a StorageSelectDialog and bind a listener to it:

```Java
StorageSelectDialog storageSelectDialog = new StorageSelectDialog();
storageSelectDialog.setOnSelectListener(this);
```

When it is time to show the dialog simply call:
```Java
FragmentManager fm = getFragmentManager();
storageSelectDialog.show(fm, "title");
```

You should implement StorageSelectDialog.OnSelectListener and receive the selected device with:

```Java
@Override
public void selectionCallback(StorageDevice storageDevice) {
	Do something here
}

```
To get a list of storage devices available use:

```Java
// Writable
ArrayList<StorageDevice> storageDevices = getStorageDevices(context, true);
// Readable
ArrayList<StorageDevice> storageDevices = getStorageDevices(context, false);
```
Duplicates may be returned (we do our best to filter them but due to system limitations its not always possible).


To add with jcenter add this to gradle:

```Gradle
compile 'eu.mhutti1.utils.storage:android-storage-devices:x.x.x'
```
