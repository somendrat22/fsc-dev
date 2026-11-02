package com.fsc.supply_chain_common_operations.repositories;

import com.fsc.supply_chain_common_operations.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepo extends JpaRepository<Role, UUID> {
    public Role findByRoleName(String roleName);
}
