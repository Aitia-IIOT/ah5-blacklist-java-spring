package eu.arrowhead.blacklist;

import java.util.List;

import eu.arrowhead.common.jpa.ArrowheadEntity;

public final class BlacklistConstants {

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
	public static final String HTTP_API_MONITOR_PATH = HTTP_API_BASE_PATH + "/monitor";

	public static final String HTTP_API_OP_CHECK = "/check";
	public static final String HTTP_API_PARAM_NAME = "{systemName}";
	public static final String HTTP_API_OP_CHECK_PATH = HTTP_API_OP_CHECK + "/" + HTTP_API_PARAM_NAME;

	public static final String HTTP_API_OP_LOOKUP = "/lookup";

	public static final String HTTP_API_OP_QUERY = "/query";

	public static final String HTTP_API_OP_CREATE = "/create";

	public static final String HTTP_API_OP_REMOVE = "/remove";
	public static final String HTTP_API_PARAM_NAME_LIST = "{systemNameList}";
	public static final String HTTP_API_OP_REMOVE_PATH = HTTP_API_OP_REMOVE + "/" + HTTP_API_PARAM_NAME_LIST;

	public static final String HTTP_API_OP_GET_CONFIG_PATH = "/get-config";

	// DTO field length limitations
	public static final int SYSTEM_NAME_LENGTH = ArrowheadEntity.VARCHAR_SMALL;
	public static final int REASON_LENGTH = ArrowheadEntity.VARCHAR_LARGE;

	// Blacklist parameters
	public static final String WHITELIST = "whitelist";
	public static final String $WHITELIST = "#{'${" + WHITELIST + "}'.split(',')}";

	// Forbidden keys (for config service)

	public static final List<String> FORBIDDEN_KEYS = List.of(
			// database related
			"spring.datasource.url",
			"spring.datasource.username",
			"spring.datasource.password",
			"spring.datasource.driver-class-name",
			"spring.jpa.hibernate.ddl-auto",
			"spring.jpa.show-sql",

			// cert related
			"authenticator.secret.key",
			"server.ssl.key-store-type",
			"server.ssl.key-store",
			"server.ssl.key-store-password",
			"server.ssl.key-alias",
			"server.ssl.key-password",
			"server.ssl.client-auth",
			"server.ssl.trust-store-type",
			"server.ssl.trust-store",
			"server.ssl.trust-store-password",
			"disable.hostname.verifier");

}
