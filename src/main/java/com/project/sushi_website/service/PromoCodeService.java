package com.project.sushi_website.service;

import com.project.sushi_website.model.PromoCode;
import com.project.sushi_website.repository.PromoCodeRepository;
import org.springframework.stereotype.Service;

@Service
public class PromoCodeService {
   private final PromoCodeRepository promoCodeRepository;

    public PromoCodeService(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    public PromoCode getPromoCodeByCode(String name){
        return promoCodeRepository.findPromoCodeByCode(name);
    }
}
