package tfip.ssf.day19quotation.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import tfip.ssf.day19quotation.model.Item;

@Service
public class QuotationService {

    @Autowired @Qualifier("quote")
    RedisTemplate<String, String> redisTemplate;

    @Value("${quotation.url}")
    private String quotationUrl;
    


    public JsonObject jsonStringtoJsonObject(String json) throws IOException{
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            return o;
        }
    }

    
    // post item to  API
    public String postItem(Item item) throws IOException{

        JsonObject o = Json.createObjectBuilder()
                        .add("name", item.getName())
                        .add("price", item.getPrice())
                        .add("username", item.getUsername())
                        .build();

        RequestEntity<String> req = RequestEntity.post(quotationUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .body(o.toString(), String.class);

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);

        String respJson = resp.getBody();

        //returns Jsonstring of Item
        return respJson;
    }

    // Get quotation from API
    public String getQuotation(){

        RequestEntity<Void> req = RequestEntity.get(quotationUrl)
                                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);
        String respJson = resp.getBody();

        // returns Jsonstring of Quotation
        return respJson;
    }

    public void saveItemToRedis(String json) throws IOException{
        Item item = Item.createJson(json);
        redisTemplate.opsForValue().set(item.getId(), item.toJSON().toString());
    }

   
}
