package org.unibl.etf.emobility_hub_promotions.models.beans;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AnnouncementResponseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	    private String title;
	    private String content;
	    private LocalDateTime creationDate;
	    private LocalDateTime updateDate;
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
		public LocalDateTime getCreationDate() {
			return creationDate;
		}
		public void setCreationDate(LocalDateTime creationDate) {
			this.creationDate = creationDate;
		}
		public LocalDateTime getUpdateDate() {
			return updateDate;
		}
		public void setUpdateDate(LocalDateTime updateDate) {
			this.updateDate = updateDate;
		}
}
