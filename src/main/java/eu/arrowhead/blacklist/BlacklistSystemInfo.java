package eu.arrowhead.blacklist;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import eu.arrowhead.common.Constants;
import eu.arrowhead.common.SystemInfo;
import eu.arrowhead.common.http.filter.authentication.AuthenticationPolicy;
import eu.arrowhead.common.http.model.HttpInterfaceModel;
import eu.arrowhead.common.http.model.HttpOperationModel;
import eu.arrowhead.common.model.ServiceModel;
import eu.arrowhead.common.model.SystemModel;

@Component
public class BlacklistSystemInfo extends SystemInfo {

	//=================================================================================================
	// members

	private SystemModel systemModel;

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@Override
	public String getSystemName() {
		return BlacklistConstants.SYSTEM_NAME;
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public List<ServiceModel> getServices() {

		final String templateName = getSslProperties().isSslEnabled() ? Constants.GENERIC_HTTPS_INTERFACE_TEMPLATE_NAME : Constants.GENERIC_HTTP_INTERFACE_TEMPLATE_NAME;

		// discovery

		final HttpOperationModel check = new HttpOperationModel.Builder()
				.method(HttpMethod.GET.name())
				.path(BlacklistConstants.HTTP_API_OP_CHECK)
				.build();

		final HttpOperationModel lookup = new HttpOperationModel.Builder()
				.method(HttpMethod.GET.name())
				.path(BlacklistConstants.HTTP_API_OP_LOOKUP)
				.build();

		final HttpInterfaceModel discovery_interface = new HttpInterfaceModel.Builder(templateName, getDomainAddress(), getServerPort())
				.basePath(BlacklistConstants.HTTP_API_BASE_PATH)
				.operation(Constants.SERVICE_OP_CHECK, check)
				.operation(Constants.SERVICE_OP_LOOKUP, lookup)
				.build();

		final ServiceModel discovery = new ServiceModel.Builder()
				.serviceDefinition(Constants.SERVICE_DEF_BLACKLIST_DISCOVERY)
				.version(BlacklistConstants.VERSION_DISCOVERY)
				.metadata(BlacklistConstants.METADATA_KEY_UNRESTRICTED_DISCOVERY, true)
				.serviceInterface(discovery_interface)
				.build();

		// management

		final HttpOperationModel query = new HttpOperationModel.Builder()
				.method(HttpMethod.POST.name())
				.path(BlacklistConstants.HTTP_API_OP_QUERY)
				.build();

		final HttpOperationModel create = new HttpOperationModel.Builder()
				.method(HttpMethod.POST.name())
				.path(BlacklistConstants.HTTP_API_OP_CREATE)
				.build();

		final HttpOperationModel remove = new HttpOperationModel.Builder()
				.method(HttpMethod.DELETE.name())
				.path(BlacklistConstants.HTTP_API_OP_REMOVE)
				.build();

		final HttpInterfaceModel management_interface = new HttpInterfaceModel.Builder(templateName, getDomainAddress(), getServerPort())
				.basePath(BlacklistConstants.HTTP_API_MANAGEMENT_PATH)
				.operation(Constants.SERVICE_OP_BLACKLIST_QUERY, query)
				.operation(Constants.SERVICE_OP_BLACKLIST_CREATE, create)
				.operation(Constants.SERVICE_OP_BLACKLIST_REMOVE, remove)
				.build();

		final ServiceModel management = new ServiceModel.Builder()
				.serviceDefinition(Constants.SERVICE_DEF_BLACKLIST_MANAGEMENT)
				.version(BlacklistConstants.VERSION_MANAGEMENT)
				.metadata(BlacklistConstants.METADATA_KEY_UNRESTRICTED_DISCOVERY, false)
				.serviceInterface(management_interface)
				.build();

		return List.of(discovery, management);
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public SystemModel getSystemModel() {
		if (systemModel == null) {
			SystemModel.Builder builder = new SystemModel.Builder()
					.address(getAddress())
					.version(Constants.AH_FRAMEWORK_VERSION);

			if (AuthenticationPolicy.CERTIFICATE == this.getAuthenticationPolicy()) {
				builder = builder.metadata(Constants.METADATA_KEY_X509_PUBLIC_KEY, getPublicKey());
			}

			systemModel = builder.build();
		}

		return systemModel;
	}
}
