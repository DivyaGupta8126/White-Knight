package adapter;

public class Modal {
    String ExpiryDate,Key,Image,ProductName;

    Modal()
    {

    }

    public Modal(String d,String i,String k,String n)
    {
        this.ExpiryDate=d;
        this.Image=i;
        this.Key=k;
        this.ProductName=n;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getKey() {
        return Key;
    }

    public String getImage() {
        return Image;
    }




}
