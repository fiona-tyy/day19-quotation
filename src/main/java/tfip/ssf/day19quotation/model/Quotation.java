package tfip.ssf.day19quotation.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Quotation {
    private String quotation_id;
    private List<Item> items;

    public String getQuotation_id() {
        return quotation_id;
    }

    public void setQuotation_id(String quotation_id) {
        this.quotation_id = quotation_id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Quotation [quotation_id=" + quotation_id + ", items=" + items + "]";
    }

    // to read GET results
    public static Quotation createJson(String json) throws IOException{
        Quotation q = new Quotation();
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            q.setQuotation_id(o.getString("quotation_id"));
            JsonArray itemsArr = o.getJsonArray("items");
            List<Item> list = new LinkedList<>();
            for(int i=0; i < itemsArr.size(); i++){
                JsonObject x = itemsArr.getJsonObject(i);
                list.add(Item.createJsonFromObj(x));
            }
            q.setItems(list);
        }
        return q;
    }
    
    
}
