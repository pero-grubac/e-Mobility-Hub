package org.unibl.etf.emobility_hub_user.models.dto;

import java.io.Serializable;
import java.util.List;


public class PaginatedResponse<T> implements Serializable{
    private static final long serialVersionUID = 1L;

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