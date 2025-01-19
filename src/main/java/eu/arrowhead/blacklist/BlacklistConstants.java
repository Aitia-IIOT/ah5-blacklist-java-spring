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
	
	public static final String HTTP_API_OP_CHECK = "/check";
	public static final String HTTP_API_PARAM_NAME = "{systemName}";
	public static final String HTTP_API_OP_CHECK_PATH = HTTP_API_OP_CHECK + "/" + HTTP_API_PARAM_NAME;
	
	public static final String HTTP_API_OP_LOOKUP = "/lookup";
	
	public static final String HTTP_API_OP_QUERY = "/query"; 
	
	public static final String HTTP_API_OP_CREATE = "/create"; 
	
	public static final String HTTP_API_OP_REMOVE = "/remove"; 
	public static final String HTTP_API_PARAM_NAME_LIST = "{systemNameList}";
	public static final String HTTP_API_OP_REMOVE_PATH = HTTP_API_OP_REMOVE + "/" + HTTP_API_PARAM_NAME_LIST; 
	
	// DTO field length limitations
	public static final int SYSTEM_NAME_LENGTH = ArrowheadEntity.VARCHAR_SMALL;
	public static final int REASON_LENGTH = ArrowheadEntity.VARCHAR_LARGE;
	
	// Blacklist parameters
	public static final String WHITELIST = "whitelist";
	public static final String $WHITELIST_WD = "#{'${" + WHITELIST + "}'.split(',')}";
	
}
