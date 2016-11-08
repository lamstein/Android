package avcaliani.listviewapp;

import java.util.Comparator;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * Created by avcaliani on 16/08/16.
 */
public class Item implements Comparable<Item>{

    protected String mItemName = "Default";
    protected String mCurrentTime = "0000/00/00 00:00:00";


    public Item (String itemName){

        setItemName(itemName);
        updateTime();

    }

    public Item (String itemName, String currentTime){

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
            mItemName = "Default";
            return;
        }
        mItemName = itemName;
    }

    public void setCurrentTime(String currentTime){
        if (currentTime == null){
            updateTime();
            return;
        }
        mCurrentTime = currentTime;
    }

    public String getTime(){
        return mCurrentTime;
    }

    public String getItemName(){
        return mItemName;
    }

    @Override
    public String toString() {
        return mItemName + " " + mCurrentTime;
    }

    public int hashCode(){
        int hash = super.hashCode();

        hash = hash * 7 + mItemName.hashCode();
        hash = hash * 7 + mItemName.hashCode();

        return hash;
    }
    public boolean equals (Object obj){
        if (obj == null || !(obj instanceof Item))
            return false;
        if (obj == this)
            return true;

        Item myItem = (Item) obj;

        if (!(mItemName.equals(myItem.mItemName)) || !(mCurrentTime.equals(myItem.mCurrentTime)))
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
