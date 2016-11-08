package avcaliani.jsonapp;

/**
 * Created by avcaliani on 17/09/16.
 */
public class User {
    protected String mName = "";
    protected int mAge = 0;

    public User (String name, int age){
        setName(name);
        setAge(age);
    }

    public void setName(String name) {
        if (!(name == null || name.trim().matches("")))
            mName = name;
    }

    public void setAge(int age) {

        if (age < 0){
            mAge = age * (-1);
        }

        mAge = age;
    }

    public String getName() {
        return mName;
    }

    public int getAge() {
        return mAge;
    }

    @Override
    public String toString() {
        return "User{" +
                "mName='" + mName + '\'' +
                ", mAge=" + mAge +
                '}';
    }
}
