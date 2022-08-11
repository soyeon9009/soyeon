package com.example.soyeon.service.TextTransfer;

import com.example.soyeon.persistence.Scraping.ScrapingRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//selenium : 동적인 데이터 수집 가능 (직접 브라우저를 통제해서 사람처럼 브라우저 작동을 하여 데이터 수집) : chromeDriver
//jsoup : httpRequest 사용해서 정적인 데이터(HTML, CSS..)를 수집

@Service
public class SeleniumExam {

    @Autowired
    ScrapingRepository scrapingRepository;

    //selenum 드라이버 다운
    private WebDriver driver;

    private static final String WEB_DRIVER_ID =  "webdriver.chrome.driver";
    private static final String WEB_DRIVER_PATH = "C:/Users/woaak/spring/chromedriver_win32/chromedriver.exe" ;
    private String base_url;

    public void scraping(){

        //System.io : 개발한 자바 프로그램(런타임)에서 외부 프로그램을 작동하기 위한 객체
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        driver = new ChromeDriver();
        base_url =  "http://www.fragrantica.com/search/";

        driver.get(base_url);

        try {
            Thread.sleep(5000);
            //스크랩핑 할 페이지의 전체 데이터 출력
            System.out.println(driver.getPageSource());

            WebElement element = driver.findElement(By.tagName("button"));
            List<WebElement> elements_span = driver.findElements(By.tagName("span"));
            int checkNum = 0;
            for(WebElement e : elements_span){
                System.out.println(checkNum);
                System.out.println(e.getTagName());
                checkNum++;

            }



        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //공식문서에서는 close가 아니라 quit권장
            driver.quit();
        }

//        try {
//            Thread.sleep(5000);
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
//
//        WebElement className = driver.findElement(By.className("link-span"));
//        List<WebElement> product
//
    }

}
