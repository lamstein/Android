package avcaliani.recyclelist;

import android.graphics.drawable.Drawable;

/**
 * Created by anthony on 9/26/16.
 */

public class OperatingSystem {

    protected int mId = -1;
    protected String mName = "Empty";
    protected Drawable mImageIcon;

    public OperatingSystem(int id, String name, Drawable image){
        if (id >= 0)
            mId = id;

        if (!(name == null || name.trim().matches("")))
            mName = name;

        mImageIcon = image;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public Drawable getImage() {
        return mImageIcon;
    }
}
