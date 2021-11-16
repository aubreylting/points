package com.points.demo.api;

import com.points.demo.dto.TransactionDTO;
import com.points.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionAPI {

    @Autowired
    private TransactionService transactionService;

    @PostMapping(value = "/addTransaction")
    public ResponseEntity<String> addTransaction(@RequestBody String stringData)throws ParseException  {
        String[] data = stringData.split(",");
        String payer = data[0].split(":")[1];
        Long point =Long.valueOf(data[1].split(": ")[1]);
        String timeStampString = data[2].split(": ")[1];
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        Date timeStamp = df.parse(timeStampString);

        TransactionDTO transactionDTO = new TransactionDTO(payer, point, timeStamp);
        Integer transactionId= transactionService.addTransaction(transactionDTO);
        return new ResponseEntity<String>("add successfully: "  + transactionId, HttpStatus.CREATED);
    }

    @PostMapping(value = "/spendPoints")
    public ResponseEntity<String> spendPoints(@RequestBody Long point) throws Exception{
        Map<String, Long> spentMap = transactionService.spendPoints(point);
        String result = new String();
        for(String payer : spentMap.keySet()) {
            result += "payer:" + payer + ", points: -" + String.valueOf(spentMap.get(payer));
            result += "\n";
        }
        return new ResponseEntity<String>(result, HttpStatus.CREATED);
    }

    @GetMapping(value = "/allPayerBalances")
    public ResponseEntity<String> allPayerBalances(){
        Map<String, Long> payerAccountsInfo = transactionService.allPayerBalances();
        String result = new String();
        for(String payer : payerAccountsInfo.keySet()) {
            result += "payer:" + payer + ", points: " + String.valueOf(payerAccountsInfo.get(payer));
            result += "\n";
        }
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

}
