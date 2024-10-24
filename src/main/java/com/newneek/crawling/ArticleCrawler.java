package com.newneek.crawling;

import com.newneek.series.Article;
import com.newneek.series.ArticleService;
import com.newneek.series.ArticleRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ArticleCrawler implements CommandLineRunner {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private RelativeTimeParser relativeTimeParser; // RelativeTimeParser 주입

    private static final int MAX_ARTICLES = 50; // 크롤링할 아티클의 최대 개수

    @Override
    public void run(String... args) throws Exception {
        System.out.println("크롤링 시작: " + LocalDateTime.now());
        crawlArticles();
    }

    public void crawlArticles() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ilkwe\\Desktop\\work\\project\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        int crawledCount = 0;

        try {
            driver.get("https://newneek.co/series");

            List<WebElement> categoryLinks = driver.findElements(By.cssSelector("article a"));
            System.out.println("카테고리 링크 수: " + categoryLinks.size());

            for (int i = 0; i < categoryLinks.size(); i++) {
                if (crawledCount >= MAX_ARTICLES) break;

                WebElement categoryLink = categoryLinks.get(i);
                String categoryUrl = categoryLink.getAttribute("href");
                System.out.println("카테고리 URL: " + categoryUrl);
                
                int slashCount = categoryUrl.length() - categoryUrl.replace("/", "").length();
                int seriesCode = -1;

                if (slashCount >= 4) {
                    int lastSlash = categoryUrl.lastIndexOf("/") + 1;
                    String seriesCodeString = categoryUrl.substring(lastSlash);

                    try {
                        seriesCode = Integer.parseInt(seriesCodeString);
                        System.out.println("추출된 시리즈 코드: " + seriesCode);
                    } catch (NumberFormatException e) {
                        System.out.println("유효하지 않은 숫자 형식: " + seriesCodeString);
                        continue;
                    }
                } else {
                    System.out.println("시리즈 코드를 추출하지 않습니다.");
                    continue;
                }

                driver.get(categoryUrl);
                List<WebElement> articleLinks = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("a h2")));
                System.out.println("아티클 링크 수: " + articleLinks.size());

                for (int j = 0; j < articleLinks.size(); j++) {
                    if (crawledCount >= MAX_ARTICLES) break;

                    WebElement articleLink = articleLinks.get(j).findElement(By.xpath("ancestor::a"));
                    String articleUrl = articleLink.getAttribute("href");

                    if (articleUrl != null && !articleUrl.isEmpty()) {
                        String fullArticleUrl = articleUrl.startsWith("/") ? "https://newneek.co" + articleUrl : articleUrl;

                        int startIndex = fullArticleUrl.indexOf("@") + 1;
                        int endIndex = fullArticleUrl.indexOf("/", startIndex);
                        String writerId = endIndex != -1 ? fullArticleUrl.substring(startIndex, endIndex) : fullArticleUrl.substring(startIndex);
                        
                        driver.get(fullArticleUrl);
                        String title = driver.findElement(By.tagName("h1")).getText();
                        String content = driver.findElement(By.cssSelector("div.content")).getText();

                        List<WebElement> images = driver.findElements(By.tagName("img"));
                        StringBuilder imageUrls = new StringBuilder();
                        for (WebElement img : images) {
                            String imgUrl = img.getAttribute("src");
                            imageUrls.append(imgUrl).append(", ");
                        }

                        // 작성일 처리
                        String relativeDate = driver.findElement(By.cssSelector("time")).getText(); // 게시물 작성일 추출
                        LocalDateTime postDate = relativeTimeParser.parse(relativeDate); // 상대 시간 문자열을 LocalDateTime으로 변환
                        
                        // 중복 확인
                        if (!articleRepository.existsByTitle(title)) {
                            Article article = new Article();
                            article.setSeriesId(seriesCode);
                            article.setUserId(writerId);
                            article.setTitle(title);
                            article.setContent(content);
                            article.setImage(imageUrls.toString());
                            article.setDate(postDate); // 변환된 작성일 사용

                            try {
                                articleService.createArticle(article);
                                System.out.println("저장된 아티클 제목: " + title);
                                crawledCount++;
                            } catch (Exception e) {
                                System.out.println("DB에 아티클 저장 실패: " + e.getMessage());
                            }
                        } else {
                            System.out.println("중복된 아티클 건너뜀: " + title);
                        }
                    }

                    driver.navigate().back();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a h2")));
                    articleLinks = driver.findElements(By.cssSelector("a h2"));
                }

                driver.navigate().back();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("article a")));
                categoryLinks = driver.findElements(By.cssSelector("article a"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
            System.out.println("크롤링 종료: " + LocalDateTime.now());
        }
    }
}
