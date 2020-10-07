package hrms.Recruitment.models;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.envers.RevisionEntity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@RevisionEntity
public abstract class Auditable<U> {

    @CreatedBy
    @Column(updatable = false)
    protected U createdBy;

    @CreatedDate
 //   @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    protected LocalDateTime createdDate;

    @LastModifiedBy
    protected U lastModifiedBy;

    @LastModifiedDate
//    @Temporal(TemporalType.TIMESTAMP)
    protected LocalDateTime lastModifiedDate;
    

    protected String remoteIp;
    
    public String getRemoteIp() {
    	String ipAddress = "";
    	if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
    		ipAddress = ((WebAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getRemoteAddress();
    	}
		return ipAddress;
	}

	public void setRemoteIp(String remoteIp) {
		remoteIp = "";
    	if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
    		remoteIp = ((WebAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getRemoteAddress();
    	}
		this.remoteIp = remoteIp;
	}

	public U getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(U createdBy) {
        this.createdBy = createdBy;
    }

 

    public U getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(U lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

 
}