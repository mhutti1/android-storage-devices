# android-storage-devices
Get a list of writable storage locations in android.

To use this. First create a StorageSelectDialog and bind a listener to it:

```
StorageSelectDialog storageSelectDialog = new StorageSelectDialog();
storageSelectDialog.setOnSelectListener(this);
```

When it is time to show the dialog simply call:
```
FragmentManager fm = getFragmentManager();
storageSelectDialog.show(fm, "title");
```

You should implement StorageSelectDialog.OnSelectListener and recieve the selected device with:

```
@Override
public void selectionCallback(StorageDevice storageDevice) {
	Do something here
}

```

To add with jcenter add this to gradle:

```
compile 'eu.mhutti1.utils.storage:android-storage-devices:0.4.1'
```