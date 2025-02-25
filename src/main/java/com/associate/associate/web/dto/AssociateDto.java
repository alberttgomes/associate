package com.associate.associate.web.dto;

/**
 * @author Albert Gomes Cabral
 * @param category
 * @param email
 * @param companyId
 * @param name
 * @param phoneNumber
 * @param status
 */
public record AssociateDto(
        String category, String email, long companyId, String name,
        String phoneNumber, String status) {
}
