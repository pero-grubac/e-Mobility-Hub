package org.unibl.etf.emobility_hub_promotions.models.dto.request;

import java.util.Objects;

public class PromotionRequest {
	private Long id;
	private String title;
	private String content;
	private String startDate;
	private String endDate;
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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	@Override
	public int hashCode() {
		return Objects.hash(content, endDate, id, startDate, title);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PromotionRequest other = (PromotionRequest) obj;
		return Objects.equals(content, other.content) && Objects.equals(endDate, other.endDate)
				&& Objects.equals(id, other.id) && Objects.equals(startDate, other.startDate)
				&& Objects.equals(title, other.title);
	}
	@Override
	public String toString() {
		return "PromotionRequest [id=" + id + ", title=" + title + ", content=" + content + ", startDate=" + startDate
				+ ", endDate=" + endDate + "]";
	}

}
