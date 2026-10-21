package org.unibl.etf.emobility_hub.controllers;

import lombok.RequiredArgsConstructor;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Item;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.unibl.etf.emobility_hub.models.dto.response.AnnouncementResponse;
import org.unibl.etf.emobility_hub.models.dto.response.PromotionResponse;
import org.unibl.etf.emobility_hub.services.IAnnouncementService;
import org.unibl.etf.emobility_hub.services.IPromotionService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/rss")
@RequiredArgsConstructor
public class RSSController {
    // Always RSS XML, also for clients that send no Accept header.
    private static final String RSS = "application/rss+xml";
    private static final int FEED_SIZE = 10;

    private final IAnnouncementService announcementService;

    private final IPromotionService promotionService;

    // Promotions and announcements together, newest first.
    @GetMapping(produces = RSS)
    public Channel rss(HttpServletRequest request) {
        String baseUrl = baseUrl(request);
        List<Item> items = Stream.concat(promotionItems(baseUrl).stream(), announcementItems(baseUrl).stream())
                .sorted(Comparator.comparing(Item::getPubDate).reversed())
                .limit(FEED_SIZE)
                .toList();
        return channel("eMobilityHub news", "Promotions and announcements", baseUrl, items);
    }

    @GetMapping(value = "/announcements", produces = RSS)
    public Channel rssAnnouncements(HttpServletRequest request) {
        String baseUrl = baseUrl(request);
        return channel("RSS Announcements", "This is the announcements feed", baseUrl + "/announcements",
                announcementItems(baseUrl));
    }

    @GetMapping(value = "/promotions", produces = RSS)
    public Channel rssPromotions(HttpServletRequest request) {
        String baseUrl = baseUrl(request);
        return channel("RSS Promotions", "This is the promotions feed", baseUrl + "/promotions",
                promotionItems(baseUrl));
    }

    private List<Item> announcementItems(String baseUrl) {
        List<Item> items = new ArrayList<>();
        for (AnnouncementResponse announcement : announcementService.findAll(newest("creationDate")).getContent()) {
            items.add(item(announcement.getTitle(), baseUrl + "/announcements/" + announcement.getId(),
                    Timestamp.valueOf(announcement.getCreationDate()), announcement.getContent()));
        }
        return items;
    }

    private List<Item> promotionItems(String baseUrl) {
        List<Item> items = new ArrayList<>();
        for (PromotionResponse promotion : promotionService.findAll(newest("startDate")).getContent()) {
            String text = promotion.getContent()
                    + " (valid " + promotion.getStartDate() + " to " + promotion.getEndDate() + ")";
            items.add(item(promotion.getTitle(), baseUrl + "/promotions/" + promotion.getId(),
                    Timestamp.valueOf(promotion.getStartDate()), text));
        }
        return items;
    }

    private static Pageable newest(String dateField) {
        return PageRequest.of(0, FEED_SIZE, Sort.by(Sort.Direction.DESC, dateField));
    }

    private static Item item(String title, String link, Date pubDate, String text) {
        Item item = new Item();
        item.setTitle(title);
        item.setLink(link);
        item.setUri(link);
        item.setPubDate(pubDate);
        Description description = new Description();
        description.setValue(text);
        item.setDescription(description);
        return item;
    }

    private static Channel channel(String title, String description, String link, List<Item> items) {
        Channel channel = new Channel();
        channel.setFeedType("rss_2.0");
        channel.setTitle(title);
        channel.setDescription(description);
        channel.setLink(link);
        channel.setUri(link);
        channel.setPubDate(new Date());
        channel.setItems(items);
        return channel;
    }

    private static String baseUrl(HttpServletRequest request) {
        return ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();
    }
}
