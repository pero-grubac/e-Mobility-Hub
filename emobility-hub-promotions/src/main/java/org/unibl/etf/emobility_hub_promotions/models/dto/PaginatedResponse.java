package org.unibl.etf.emobility_hub_promotions.models.dto;

import java.util.List;

public class PaginatedResponse<T> {
	private List<T> content;
	private PageMetadata page;

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public PageMetadata getPage() {
		return page;
	}

	public void setPage(PageMetadata page) {
		this.page = page;
	}
}
