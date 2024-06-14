package com.project.sushi_website.repository;

import com.project.sushi_website.model.Item;
import com.project.sushi_website.model.PromoCode;
import org.springframework.data.repository.CrudRepository;

public interface PromoCodeRepository extends CrudRepository<PromoCode, Integer> {
    public PromoCode findPromoCodeByCode(String code);
}
