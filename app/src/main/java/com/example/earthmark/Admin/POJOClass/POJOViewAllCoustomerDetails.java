package com.example.earthmark.Admin.POJOClass;

public class POJOViewAllCoustomerDetails {
    String id,image,name,phoneno,emailid,address,username;

    public POJOViewAllCoustomerDetails(String id, String image, String name, String phoneno, String emailid, String address, String username) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.phoneno = phoneno;
        this.emailid = emailid;
        this.address = address;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
