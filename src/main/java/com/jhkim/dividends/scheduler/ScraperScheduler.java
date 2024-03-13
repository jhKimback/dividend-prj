package com.jhkim.dividends.scheduler;

import com.jhkim.dividends.model.Company;
import com.jhkim.dividends.model.ScrapedResult;
import com.jhkim.dividends.persist.entity.CompanyEntity;
import com.jhkim.dividends.persist.entity.DividendEntity;
import com.jhkim.dividends.persist.repository.CompanyRepository;
import com.jhkim.dividends.persist.repository.DividendRepository;
import com.jhkim.dividends.scraper.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

import static com.jhkim.dividends.model.constants.CacheKey.KEY_FINANCE;

@Slf4j
@Component
@AllArgsConstructor
public class ScraperScheduler {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper yahooFinanceScraper;

    // 일정 주기마다 수행
    @CacheEvict(value= KEY_FINANCE, allEntries = true)
    @Scheduled(cron="${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {
        // 저장되어 있는 회사 목록 조회
        List<CompanyEntity> companies =  this.companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for (var company: companies){
            log.info("Scraping Scheduler is Started for " +  company.getName());
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(new Company(company.getName(),company.getTicker()));

            // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
            scrapedResult.getDividends().stream()
                    .map(e -> new DividendEntity(company.getId(), e))
                    .forEach(e -> {
                        boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if (!exists){
                            this.dividendRepository.save(e);
                            log.info("Insert new Dividend");
                        }
                    });

            // 연속적으로 서버에 요청을 날리지 않도록 일시정지 (3초) 추가
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
            }

        }
    }

}

