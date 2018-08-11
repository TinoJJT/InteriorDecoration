package tino.example.tino.interiordecoration;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DecorationRequest implements Serializable{

    private String mName;
    private String mRooms;
    private String mBudget;
    private String mArea;
    private String mDescription;
    private List<String> mPhotoUrls = new ArrayList<>();
    public String mKey;
    private String mTime;

    public DecorationRequest() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public DecorationRequest(String name, String rooms, String budget, String area, String description, List<String> photoUrls) {
        mName = name;
        mRooms = rooms;
        mBudget = budget;
        mArea = area;
        mDescription = description;
        mPhotoUrls = photoUrls;
    }

    public DecorationRequest(List<Uri> photoUrls, String name, String rooms, String budget, String area, String description) {
        mName = name;
        mRooms = rooms;
        mBudget = budget;
        mArea = area;
        mDescription = description;
        for(int i = 0; i < photoUrls.size(); i++){
            mPhotoUrls.add(photoUrls.get(i).toString());
        }
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getRooms() {
        return mRooms;
    }

    public void setRooms(String rooms) {
        mRooms = rooms;
    }

    public String getBudget() {
        return mBudget;
    }

    public void setBudget(String budget) {
        mBudget = budget;
    }

    public String getArea() {
        return mArea;
    }

    public void setArea(String area) {
        mArea = area;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public List<String> getPhotoUrls() {
        return mPhotoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        mPhotoUrls = photoUrls;
    }

    public List<Uri> inUriFormat(){
        List<Uri> uriFormatted = new ArrayList<>();
        for(int i=0; i<mPhotoUrls.size(); i++){
            if(mPhotoUrls.get(i) != null) {
               uriFormatted.add(Uri.parse(mPhotoUrls.get(i)));
            }
        }
        return uriFormatted;
    }


    @Override
    public String toString() {
        return "DecorationRequest{" +
                "mName='" + mName + '\'' +
                ", mRooms='" + mRooms + '\'' +
                ", mBudget='" + mBudget + '\'' +
                ", mArea='" + mArea + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mPhotoUrls=" + mPhotoUrls +
                ", mKey='" + mKey + '\'' +
                ", mTime='" + mTime + '\'' +
                '}';
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }
}
