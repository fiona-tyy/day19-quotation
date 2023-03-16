package tfip.ssf.day19quotation.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import tfip.ssf.day19quotation.model.Item;
import tfip.ssf.day19quotation.service.QuotationService;

@RestController
@RequestMapping("/quotation")
public class QuotationRestController {
    
    @Autowired
    private QuotationService qSvc;

    @PostMapping
    public ResponseEntity<String> post(@RequestParam(required = true) String item,
                                        @RequestParam(required = true) float price,
                                        @RequestParam(required = true) String username
    )throws IOException {

        JsonObject o = Json.createObjectBuilder()
                        .add("name", item)
                        .add("price", price)
                        .add("username", username)
                        .build();

        RequestEntity<String> req = RequestEntity.post("https://quotation-production.up.railway.app/quotation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .body(o.toString(), String.class);

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);

        Item respItem = Item.createJson(resp.getBody());
        qSvc.saveToRedis(respItem);

        return resp;
        
    }   
    
}
