package avcaliani.bluegearstest.View;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by avcaliani on 16/08/16.
 */
public class Item implements Comparable<Item>, Cloneable{

    protected String mItemName = "default";
    protected String mItemDesc = "default";
    protected String mCurrentTime = "0000/00/00 00:00:00";
    protected String mImagePath = "default";


    public Item (String itemName, String itemDesc){

        setItemDesc(itemDesc);
        setItemName(itemName);
        updateTime();

    }

    public Item (){
        updateTime();
    }

    public Item (String itemName, String itemDesc ,String currentTime){
        setItemDesc(itemDesc);
        setItemName(itemName);
        setCurrentTime(currentTime);

    }

    public void updateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        mCurrentTime = dateFormat.format(date);
    }

    public void setItemName(String itemName){
        if (itemName == null){
            return;
        }
        mItemName = itemName;
    }

    public void setCurrentTime(String currentTime){
        if (currentTime == null){
            return;
        }
        mCurrentTime = currentTime;
    }

    public void setItemDesc(String itemDesc){
        if (itemDesc == null){
            mItemDesc = "default";
            return;
        }
        mItemDesc = itemDesc;
    }

    public void setImage (String imagePathName){
        if (imagePathName == null || imagePathName.trim().matches(""))
            return;
        mImagePath = imagePathName;
    }

    public String getTime(){
        return mCurrentTime;
    }

    public String getItemName(){
        return mItemName;
    }

    public String getItemDesc(){
        return mItemDesc;
    }

    public String getImagePath(){
        return mImagePath;
    }


    // Canonicos
    @Override
    public String toString() {
        return mItemName + " " + mItemDesc + " " + mCurrentTime;
    }

    public int hashCode(){
        int hash = super.hashCode();

        hash = hash * 7 + mItemName.hashCode();
        hash = hash * 7 + mItemName.hashCode();
        hash = hash * 7 + mItemDesc.hashCode();

        return hash;
    }
    public boolean equals (Object obj){
        if (obj == null || !(obj instanceof Item))
            return false;
        if (obj == this)
            return true;

        Item myItem = (Item) obj;

        if (!(mItemName.equals(myItem.mItemName)) || !(mCurrentTime.equals(myItem.mCurrentTime))
                || !(mItemDesc.equals(myItem.mItemDesc)))
            return false;

        return true;
    }

    public int compareTo(Item it){
        if (mItemName.toLowerCase().compareTo(it.mItemName.toLowerCase()) > 0)
            return 1;
        if (mItemName.toLowerCase().compareTo(it.mItemName.toLowerCase()) < 0)
            return -1;
        return 0;
    }

    public Item (Item newItem) throws Exception{
        if (newItem == null)
            throw new Exception("Item null!");

        mItemName = newItem.mItemName;
        mCurrentTime = newItem.mCurrentTime;
        mItemDesc = newItem.mItemDesc;
        mImagePath = newItem.mImagePath;
    }

    public Object clone (){
        Item newItem = null;

        try {
            newItem = new Item(this);
        }catch (Exception e){

        }
        return newItem;
    }
}
