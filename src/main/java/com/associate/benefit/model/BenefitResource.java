package com.associate.benefit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

/**
 * @author Albert Gomes Cabral
 */
@Entity
public class BenefitResource {

    public Long getBenefitResourcesId() {
        return benefitResourcesId;
    }

    public long getBenefitId() {
        return benefitId;
    }

    public void setBenefitId(long benefitId) {
        this.benefitId = benefitId;
    }

    public String getBenefitName() {
        return benefitName;
    }

    public void setBenefitName(String benefitName) {
        this.benefitName = benefitName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public Map<String, String> getMetaDataMap() {
        Map<String, String> metaDataMap = new HashMap<>();

        JSONObject jsonObject = new JSONObject(metaData);

        for (String key : jsonObject.keySet()) {
            JSONObject object = jsonObject.getJSONObject(key);

            metaDataMap.put(key, object.toString());
        }

        return metaDataMap;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long benefitResourcesId;

    @Column(nullable = false)
    private long benefitId;

    @Column(nullable = false)
    private String benefitName;

    @Column(nullable = false)
    private Date createDate;

    @Column(nullable = false, length = 50000)
    private String metaData;

}
