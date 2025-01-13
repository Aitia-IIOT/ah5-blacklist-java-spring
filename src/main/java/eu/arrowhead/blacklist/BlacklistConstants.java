package eu.arrowhead.blacklist;

import eu.arrowhead.common.jpa.ArrowheadEntity;

public class BlacklistConstants {

	//=================================================================================================
	// members
	
	// System info related
	public static final String SYSTEM_NAME = "blacklist";
	public static final String VERSION_DISCOVERY = "1.0.0";
	public static final String VERSION_MANAGEMENT = "1.0.0";
	public static final String METADATA_KEY_UNRESTRICTED_DISCOVERY = "unrestricted-discovery";
	
	// JPA related
	public static final String DATABASE_ENTITY_PACKAGE = "eu.arrowhead.blacklist.jpa.entity";
	public static final String DATABASE_REPOSITORY_PACKAGE = "eu.arrowhead.blacklist.jpa.repository";
	
	// HTTP API paths
	public static final String HTTP_API_BASE_PATH = "/blacklist";
	public static final String HTTP_API_MANAGEMENT_PATH = HTTP_API_BASE_PATH + "/mgmt";
	public static final String HTTP_API_CHECK_PATH = "/check/{systemName}";
	public static final String HTTP_API_LOOKUP_PATH = "/lookup";
	public static final String HTTP_API_QUERY_PATH = "/query"; 
	public static final String HTTP_API_CREATE_PATH = "/create"; 
	public static final String HTTP_API_REMOVE_PATH = "/remove/{systemNameList}"; 
	
	// DTO field length limitations
	public static final int SYSTEM_NAME_LENGTH = ArrowheadEntity.VARCHAR_SMALL;
	public static final int REASON_LENGTH = ArrowheadEntity.VARCHAR_LARGE;
	
}
