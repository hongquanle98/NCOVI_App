package com.example.ncovi_app.Model;

public class Nation {
    private String name, flag;

    public  Nation(){}
    public Nation(String name, String flag) {
        this.name = name;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Nation{" +
                "name='" + name + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
