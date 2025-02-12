package com.partner.service;

import com.partner.api.BenefitService;
import com.partner.api.exception.CompanyNotFound;
import com.partner.constants.AssociateConstantType;
import com.partner.model.Benefit;
import com.partner.persistence.BenefitPersistence;
import com.partner.service.util.CompanyDynamicQuery;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Albert Gomes Cabral
 */
@Service
public class BenefitServiceImpl implements BenefitService {

    @Autowired
    public BenefitServiceImpl(
            BenefitPersistence benefitPersistence, CompanyDynamicQuery companyDynamicQuery) {

        this._benefitPersistence = benefitPersistence;
        this._companyDynamicQuery = companyDynamicQuery;
    }

    @Override
    public Benefit addBenefit(String benefitName, String benefitStatus, String BenefitResources) {
        return null;
    }

    @Override
    public List<Benefit> fetchAllBenefitsByType(
            long companyId, String type) throws CompanyNotFound {

        if (_companyDynamicQuery.hasCompany(companyId)) {
            return _readBenefitFromJson(type);
        }

        return new ArrayList<>();
    }

    private List<Benefit> _readBenefitFromJson(String type) {
        if (!AssociateConstantType.getAssociateConstantsTypeList().contains(type)) {
            return new ArrayList<>(0);
        }

        ClassLoader classLoader = getClass().getClassLoader();

        String content;

        try (InputStream inputStream =
                     classLoader.getResourceAsStream("benefit.json")) {

            if (inputStream == null) {
                return new ArrayList<>(0);
            }

            content = new String(inputStream.readAllBytes());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return _getBenefits(type, content);
    }

    private List<Benefit> _getBenefits(String type, String content) {
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

                Benefit benefit = new Benefit();
                benefit.setBenefitName(benefitName);
                benefit.setBenefitStatus(benefitStatus);

                JSONObject resourcesObject = benefitObject.optJSONObject("resources");

                if (resourcesObject != null) {
                    benefit.setBenefitResources(resourcesObject.toString());
                }

                Benefit benefitCreated = _benefitPersistence.save(benefit);

                benefits.add(benefitCreated);
            }
        }
        else {
            throw new RuntimeException(
                    "Unable to parse benefit json content");
        }

        return benefits;
    }

    private final BenefitPersistence _benefitPersistence;

    private final CompanyDynamicQuery _companyDynamicQuery;

}
