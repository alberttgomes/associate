package com.associate.associate.web.dto;

/**
 * @author Albert Gomes Cabral
 * @param email
 * @param companyId
 * @param name
 * @param status
 * @param type
 */
public record AssociateDto(
        String email, long companyId, String name, String status, String type) {
}
