package com.fsc.supply_chain_common_operations.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * First validation will be to check these sysId exist in our system or not.
 * Check roleSysIds belong to this companySysId,
 * Check empSysId exist or not
 * Check this employee belongs to this company or not.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonValidationDto {
    private List<UUID> roleSysIds;
    private UUID empSysId;
    private UUID companySysId;
}
