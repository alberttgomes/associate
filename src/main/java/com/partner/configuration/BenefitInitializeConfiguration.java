package com.partner.configuration;

import com.partner.api.BenefitService;
import com.partner.constants.AssociateConstantType;
import com.partner.model.Benefit;
import com.partner.model.Company;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albert Gomes Cabral
 */
@Configuration
public class BenefitInitializeConfiguration {

    @Autowired
    public BenefitInitializeConfiguration(
            BenefitService benefitService, @Qualifier("initCompany") Company company) {

        this._benefitService = benefitService;
        this._company = company;
    }

    @Bean
    public Map<String, List<Benefit>> createDefaultBenefitsFromJSON() {
        Map<String, List<Benefit>> benefitsMap = new HashMap<>();

        for (String type :
                AssociateConstantType.getAssociateConstantsTypeList()) {

            List<Benefit> benefits =
                    _readBenefitFromJson(_company.getCompanyId(), type);

            benefitsMap.put(type, benefits);
        }

        return benefitsMap;
    }

    private List<Benefit> _getBenefits(long companyId, String content, String type) {
        List<Benefit> benefits = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(content);

        jsonObject = (JSONObject) jsonObject.get(type);

        JSONArray jsonArray;

        if (jsonObject.has("benefit")) {
            jsonArray = jsonObject.getJSONArray("benefit");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject benefitObject = jsonArray.getJSONObject(i);

                String benefitName = benefitObject.optString("name", "N/A");
                String benefitStatus = benefitObject.optString("status", "N/A");

                JSONObject resourcesObject = benefitObject.optJSONObject("resources");

                Benefit benefit = null;

                if (_benefitService.fetchBenefitByName(benefitName) == null) {
                    benefit = _benefitService.addBenefit(
                            benefitName, benefitStatus,
                            resourcesObject.optString("resources", "N/A"),
                            type, companyId);
                }

                benefits.add(benefit);
            }
        }
        else {
            throw new RuntimeException(
                    "Unable to parse benefit json content");
        }

        return benefits;
    }

    private List<Benefit> _readBenefitFromJson(long companyId, String type) {
        if (!AssociateConstantType.getAssociateConstantsTypeList().contains(type)) {
            return new ArrayList<>(0);
        }

        if (_getContentJSONCache() != null) {
            _getBenefits(companyId, _getContentJSONCache(), type);
        }

        ClassLoader classLoader = getClass().getClassLoader();

        String content;

        try (InputStream inputStream =
                     classLoader.getResourceAsStream("benefit.json")) {

            if (inputStream == null) {
                return new ArrayList<>(0);
            }

            content = new String(inputStream.readAllBytes());

            _putContentJSONCache(content);

            return _getBenefits(companyId, content, type);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void _putContentJSONCache(String value) {
        if (_cache.containsKey("benefits")) return;

        CacheEntry entry = new CacheEntry(value);

        _cache.put("benefits", entry);
    }

    private String _getContentJSONCache() {
        CacheEntry entry = _cache.get("benefits");

        return (entry != null) ? entry.getValue() : null;
    }

    private final BenefitService _benefitService;

    private final Company _company;

    private static final Map<String, CacheEntry> _cache = new HashMap<>();

    private static class CacheEntry {

        public CacheEntry(String value) {
            this._value = value;
            this._timestamp = System.currentTimeMillis();
        }

        public String getValue() {
            return _value;
        }

        public long getTimestamp() {
            return _timestamp;
        }

        private final String _value;

        private final long _timestamp;

    }


}
