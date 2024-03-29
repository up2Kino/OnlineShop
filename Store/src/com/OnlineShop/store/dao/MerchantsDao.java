package com.OnlineShop.store.dao;

import com.OnlineShop.store.domain.Merchants;

import java.util.List;

public interface MerchantsDao {

    Merchants findByPk(String pk);

    List<Merchants> findAll();

    void create(Merchants customer);

    void modify(Merchants customer);

    void remove(String pk);

}
