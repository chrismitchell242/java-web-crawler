package com.jenkov.crawler.st.synch;

import com.jenkov.crawler.util.IUrlFilter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 */
public class Crawler {

    protected IUrlFilter urlFilter     = null;

    protected List<String> urlsToCrawl = new ArrayList<String>();
    protected Set<String>  crawledUrls = new HashSet<String>();


    public Crawler(IUrlFilter urlFilter) {
        this.urlFilter = urlFilter;
    }


    public void addUrl(String url) {
        this.urlsToCrawl.add(url);
    }

    public void crawl() {

        long startTime = System.currentTimeMillis();

        while(this.urlsToCrawl.size() > 0) {

            String nextUrl = this.urlsToCrawl.remove(0);

            if (!shouldCrawlUrl(nextUrl)) continue; // skip this URL.


            this.crawledUrls.add(nextUrl);

            try {
                System.out.println(nextUrl);
                CrawlJob crawlJob = new CrawlJob(nextUrl, this);

                crawlJob.crawl();
            } catch (Exception e) {
                System.out.println("Error crawling URL: " + nextUrl);
                e.printStackTrace();
            }

        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        System.out.println("URL's crawled: " + this.crawledUrls.size() + " in " + totalTime + " ms (avg: " + totalTime / this.crawledUrls.size() + ")");

    }

    private boolean shouldCrawlUrl(String nextUrl) {
        if(this.urlFilter != null && !this.urlFilter.include(nextUrl)){
            return false;
        }
        if(this.crawledUrls.contains(nextUrl)) { return false; }
        if(nextUrl.startsWith("javascript:"))  { return false; }
        if(nextUrl.startsWith("#"))            { return false; }
        if(nextUrl.endsWith(".swf"))           { return false; }
        if(nextUrl.endsWith(".pdf"))           { return false; }
        if(nextUrl.endsWith(".png"))           { return false; }
        if(nextUrl.endsWith(".gif"))           { return false; }
        if(nextUrl.endsWith(".jpg"))           { return false; }
        if(nextUrl.endsWith(".jpeg"))          { return false; }

        return true;
    }


}
