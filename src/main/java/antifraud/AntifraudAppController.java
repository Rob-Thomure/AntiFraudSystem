package antifraud;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AntifraudAppController {

    @PostMapping("/api/antifraud/transaction")
    public ResponseEntity<Map<String, String>> postTransaction(@RequestBody Map<String, Long> transaction) {
        long amount;
        try {
            amount = transaction.get("amount");
        } catch (NullPointerException e) {
            return badRequest();
        }
        ResponseEntity<Map<String, String>> responseMap;
        if (amount > 0 && amount <= 200) {
            responseMap = allowed(transaction);
        } else if (amount > 200 && amount<= 1500) {
            responseMap = manualProcessing(transaction);
        } else if (amount > 1500) {
            responseMap = prohibited(transaction);
        } else {
            responseMap = badRequest();
        }
        return responseMap;
    }

    private ResponseEntity<Map<String, String>> allowed(Map<String, Long> transaction) {
        Map<String, String> responseMap = Map.of("result", "ALLOWED");
        return ResponseEntity.ok(responseMap);
    }

    private ResponseEntity<Map<String, String>> manualProcessing(Map<String, Long> transaction) {
        Map<String, String> responseMap = Map.of("result", "MANUAL_PROCESSING");
        return ResponseEntity.ok(responseMap);
    }

    private ResponseEntity<Map<String, String>> prohibited(Map<String, Long> transaction) {
        Map<String, String> responseMap = Map.of("result", "PROHIBITED");
        return ResponseEntity.ok(responseMap);
    }

    private ResponseEntity<Map<String, String>> badRequest() {
        return ResponseEntity.badRequest().build();
    }

}
