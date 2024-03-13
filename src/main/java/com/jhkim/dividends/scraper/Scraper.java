package com.jhkim.dividends.scraper;

import com.jhkim.dividends.model.Company;
import com.jhkim.dividends.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
