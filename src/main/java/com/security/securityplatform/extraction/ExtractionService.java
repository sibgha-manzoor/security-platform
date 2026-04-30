package com.security.securityplatform.extraction;

import com.security.securityplatform.model.IOC;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExtractionService {

    public List<IOC> extractFromAbuseIPDB(String jsonData) {
        List<IOC> iocs = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(jsonData);
            JSONArray data = json.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                String ip = item.getString("ipAddress");

                IOC ioc = new IOC("IP", ip, "AbuseIPDB");
                iocs.add(ioc);
            }
        } catch (Exception e) {
            System.out.println("Extraction error: " + e.getMessage());
        }
        return iocs;
    }

    public List<IOC> extractFromAlienVault(String jsonData) {
        List<IOC> iocs = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(jsonData);
            JSONArray results = json.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject pulse = results.getJSONObject(i);
                JSONArray indicators = pulse.getJSONArray("indicators");

                for (int j = 0; j < indicators.length(); j++) {
                    JSONObject indicator = indicators.getJSONObject(j);
                    String type = indicator.getString("type");
                    String value = indicator.getString("indicator");

                    if (type.equals("IPv4")) {
                        iocs.add(new IOC("IP", value, "AlienVault"));
                    } else if (type.equals("domain") || type.equals("hostname")) {
                        iocs.add(new IOC("Domain", value, "AlienVault"));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Extraction error: " + e.getMessage());
        }
        return iocs;
    }
}
