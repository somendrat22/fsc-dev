package com.fsc.supply_chain_common_operations.services;

import com.fsc.supply_chain_common_operations.models.Role;

public interface EntityService<T> {

    public T saveOrUpdate(T object);

}
