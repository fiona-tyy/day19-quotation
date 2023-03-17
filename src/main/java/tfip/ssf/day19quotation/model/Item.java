package tfip.ssf.day19quotation.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Item implements Serializable{
    
    private String name;
    private float price;
    private String username;
    private String id;
    private String dt;

    public Item() {
    }

    public Item(String name, float price, String username) {
        this.name = name;
        this.price = price;
        this.username = username;
    }

    public Item(String name, float price, String username, String id, String dt) {
        this.name = name;
        this.price = price;
        this.username = username;
        this.id = id;
        this.dt = dt;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    public String getDt() {
        return dt;
    }
    public void setDt(String dt) {
        this.dt = dt;
    }

    @Override
    public String toString() {
        return "Quotation [name=" + name + ", price=" + price + ", username=" + username + ", id=" + id + ", dt=" + dt
                + "]";
    }

    // create JSON from POJO
    public JsonObject toJSON(){
        return Json.createObjectBuilder()
                    .add("name", this.getName())
                    .add("price", this.getPrice())
                    .add("username", this.getUsername())
                    .add("id", this.getId())
                    .add("dt", this.getDt())
                    .build();
    }

    public static Item createJson(String json) throws IOException{
        Item q = new Item();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())){
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            q.setName(o.getString("name"));
            q.setPrice((float)o.getJsonNumber("price").doubleValue());
            q.setUsername(o.getString("username"));
            q.setId(o.getString("id"));
            q.setDt(o.getString("dt"));
        }

        return q;
    }

    public static Item createJsonFromObj(JsonObject o){
        Item q = new Item();
        q.setName(o.getString("name"));
        q.setPrice((float)o.getJsonNumber("price").doubleValue());
        q.setUsername(o.getString("username"));
        q.setId(o.getString("id"));
        q.setDt(o.getString("dt"));
        return q;
    }
    
}
