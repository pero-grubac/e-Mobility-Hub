package org.unibl.etf.emobility_hub.controllers;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Item;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/rss")
public class RSSController {
    @Autowired
    private IAnnouncementService announcementService;

    @Autowired
    private IPromotionService promotionService;

    @GetMapping("/announcements")
    public Channel rssAnnouncements(HttpServletRequest request) {
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
        Channel channel = new Channel();
        channel.setFeedType("rss_2.0");
        channel.setTitle("RSS Announcements");
        channel.setDescription("This is the announcements feed");
        channel.setLink(baseUrl + "/announcements");
        channel.setUri(baseUrl + "/announcements");
        channel.setPubDate(new Date());

        Pageable pageable = PageRequest.of(0, 10);
        Page<AnnouncementResponse> announcements = announcementService.findAll(pageable);
        List<Item> items = new ArrayList<>();
        for (AnnouncementResponse announcement : announcements.getContent()) {
            Item item = new Item();
            item.setTitle(announcement.getTitle());
            item.setLink(baseUrl + "/announcements/" + announcement.getId());
            item.setUri(baseUrl + "/announcements/" + announcement.getId());

            item.setPubDate(Timestamp.valueOf(announcement.getCreationDate()));
            String dateRange = "Creation date: " + announcement.getCreationDate() + ", Update date: " + announcement.getUpdateDate();

            Description description = new Description();
            description.setValue(dateRange);
            item.setDescription(description);
            items.add(item);
        }

        channel.setItems(items);
        return channel;
    }

    @GetMapping("/promotions")
    public Channel rssPromotions(HttpServletRequest request) {
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        Channel channel = new Channel();
        channel.setFeedType("rss_2.0");
        channel.setTitle("RSS Promotions");
        channel.setDescription("This is the promotions feed");
        channel.setLink(baseUrl + "/promotions");
        channel.setUri(baseUrl + "/promotions");
        channel.setPubDate(new Date());

        Pageable pageable = PageRequest.of(0, 10);
        Page<PromotionResponse> promotions = promotionService.findAll(pageable);
        List<Item> items = new ArrayList<>();

        for (PromotionResponse promotion : promotions.getContent()) {
            Item item = new Item();
            item.setTitle(promotion.getTitle());
            item.setLink(baseUrl + "/promotions/" + promotion.getId());
            item.setUri(baseUrl + "/promotions/" + promotion.getId());

            String dateRange = "Start: " + promotion.getStartDate() + ", End: " + promotion.getEndDate();

            item.setPubDate(Timestamp.valueOf(promotion.getStartDate()));

            Description description = new Description();
            description.setValue(dateRange);
            item.setDescription(description);

            items.add(item);
        }

        channel.setItems(items);
        return channel;
    }

}
