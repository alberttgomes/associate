package com.associate.associate.web.dto;

/**
 * @author Albert Gomes Cabral
 * @param companyId
 * @param name
 * @param status
 * @param type
 */
public record AssociateDto(
        long companyId, String name, String status, String type) {
}
