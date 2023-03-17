package tfip.ssf.day19quotation.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tfip.ssf.day19quotation.model.Item;
import tfip.ssf.day19quotation.service.QuotationService;

@RestController
@RequestMapping("/quotation")
public class QuotationRestController {
    
    @Autowired
    private QuotationService qSvc;

    @Value("${quotation.url}")
    private String quotationUrl;


    @PostMapping
    public ResponseEntity<String> post(@RequestParam(required = true) String name,
                                        @RequestParam(required = true) float price,
                                        @RequestParam(required = true) String username
    )throws IOException {

        String resp = qSvc.postItem(new Item(name, price, username));

        if(resp == null || resp.trim().length() <= 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("");
        }

        qSvc.saveItemToRedis(resp);
        return ResponseEntity.status(HttpStatus.OK)
                            .body(resp);
        
    }   

    @GetMapping
    public ResponseEntity<String> getQuotation() throws IOException{

        String resp = qSvc.getQuotation();

        if(resp == null || resp.trim().length() <= 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("");
        }

        return ResponseEntity.status(HttpStatus.OK)
                            .body(resp);
    }
    
}
