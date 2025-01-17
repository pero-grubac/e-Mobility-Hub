package org.unibl.etf.emobility_hub_promotions.models.beans;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PromotionResponseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String title;
	private String content;
	private LocalDateTime startDate;
	private LocalDateTime endDate;

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
}