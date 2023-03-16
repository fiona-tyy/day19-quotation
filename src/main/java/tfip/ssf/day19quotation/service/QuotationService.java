package tfip.ssf.day19quotation.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import tfip.ssf.day19quotation.model.Item;

@Service
public class QuotationService {

    @Autowired @Qualifier("quote")
    RedisTemplate<String, String> redisTemplate;
    

    public void saveToRedis(Item item){
        redisTemplate.opsForValue().set(item.getId(), item.toJSON().toString());
    }


    public List<Item> getAllQuotations(String json) throws IOException{
        List<Item> listOfQuotations = new LinkedList<>();
        
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            JsonArray items = o.getJsonArray("items");
            for(int i=0; i < items.size(); i++){
                JsonObject x = items.getJsonObject(i);
                listOfQuotations.add(Item.createJsonFromObj(x));
            }
        }

        return listOfQuotations;
    }
}
