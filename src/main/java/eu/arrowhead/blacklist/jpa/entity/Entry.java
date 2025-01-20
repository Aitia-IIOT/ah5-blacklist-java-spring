package eu.arrowhead.blacklist.jpa.entity;

import java.time.ZonedDateTime;
import java.util.List;

import eu.arrowhead.common.jpa.ArrowheadEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Entry extends ArrowheadEntity {

	//=================================================================================================
	// members

	public static final List<String> SORTABLE_FIELDS_BY = List.of("id", "systemName", "createdAt");
	public static final String DEFAULT_SORT_FIELD = "systemName";

	// system name
	@Column(nullable = false, length = VARCHAR_SMALL)
	private String systemName;

	// expires at
	@Column(nullable = true)
	private ZonedDateTime expiresAt;

	// active
	@Column(nullable = false)
	private boolean active = true;

	// created by
	@Column(nullable = false, length = VARCHAR_SMALL)
	private String createdBy;

	// revoked by
	@Column(nullable = true, length = VARCHAR_SMALL)
	private String revokedBy = null;

	// reason
	@Column(nullable = false, length = VARCHAR_LARGE)
	private String reason;

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public Entry() {
	}

	//-------------------------------------------------------------------------------------------------
	public Entry(final String systemName, final boolean active, final ZonedDateTime expiresAt, final String createdBy, final String revokedBy, final String reason) {
		this.systemName = systemName;
		this.expiresAt = expiresAt;
		this.active = active;
		this.createdBy = createdBy;
		this.revokedBy = revokedBy;
		this.reason = reason;
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public String toString() {
		return "Entry [id = " + id + ", systemName = " + systemName + ", expiresAt = " + expiresAt + ", "
				+ "active = " + active + ", createdBy = " + createdBy + ", revokedBy = "
				+ revokedBy + ", reason = " + reason + "]";
	}

	//=================================================================================================
	// boilerplate

	//-------------------------------------------------------------------------------------------------
	public String getSystemName() {
		return systemName;
	}
	//-------------------------------------------------------------------------------------------------
	public void setSystemName(final String systemName) {
		this.systemName = systemName;
	}

	//-------------------------------------------------------------------------------------------------
	public ZonedDateTime getExpiresAt() {
		return expiresAt;
	}

	//-------------------------------------------------------------------------------------------------
	public void setExpiresAt(final ZonedDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	//-------------------------------------------------------------------------------------------------
	public boolean getActive() {
		return active;
	}

	//-------------------------------------------------------------------------------------------------
	public void setActive(final boolean active) {
		this.active = active;
	}
	//-------------------------------------------------------------------------------------------------
	public String getCreatedBy() {
		return createdBy;
	}

	//-------------------------------------------------------------------------------------------------
	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
	}

	//-------------------------------------------------------------------------------------------------
	public String getRevokedBy() {
		return revokedBy;
	}

	//-------------------------------------------------------------------------------------------------
	public void setRevokedBy(final String revokedBy) {
		this.revokedBy = revokedBy;
	}

	//-------------------------------------------------------------------------------------------------
	public String getReason() {
		return reason;
	}

	//-------------------------------------------------------------------------------------------------
	public void setReason(final String reason) {
		this.reason = reason;
	}

}
