package com.jhkim.dividends.service;

import com.jhkim.dividends.exception.impl.NoCompanyException;
import com.jhkim.dividends.model.Company;
import com.jhkim.dividends.model.Dividend;
import com.jhkim.dividends.model.ScrapedResult;
import com.jhkim.dividends.persist.repository.CompanyRepository;
import com.jhkim.dividends.persist.repository.DividendRepository;
import com.jhkim.dividends.persist.entity.CompanyEntity;
import com.jhkim.dividends.persist.entity.DividendEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.jhkim.dividends.model.constants.CacheKey.KEY_FINANCE;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Cacheable(key="#companyName", value= KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName){
        log.info("Search Company -> " + companyName);
        // 1. 회사명을 기준으로 회사정보를 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
                                        .orElseThrow(() -> new NoCompanyException());
        // 2. 조회된 회사의 ID로 배당금 정보 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());

        // 3. 결과 조합 후 반환
        List<Dividend> dividends = dividendEntities.stream()
                                                    .map(e -> new Dividend(e.getDate(), e.getDividend()))
                                                .collect(Collectors.toList());

        return new ScrapedResult(new Company(company.getTicker(), company.getName()), dividends);
    }
}
