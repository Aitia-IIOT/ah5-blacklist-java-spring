package eu.arrowhead.blacklist;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import eu.arrowhead.common.Constants;
import eu.arrowhead.common.SystemInfo;
import eu.arrowhead.common.http.filter.authentication.AuthenticationPolicy;
import eu.arrowhead.common.http.model.HttpInterfaceModel;
import eu.arrowhead.common.http.model.HttpOperationModel;
import eu.arrowhead.common.model.InterfaceModel;
import eu.arrowhead.common.model.ServiceModel;
import eu.arrowhead.common.model.SystemModel;
import eu.arrowhead.common.mqtt.model.MqttInterfaceModel;

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

		final String httpTemplateName = getSslProperties().isSslEnabled() ? Constants.GENERIC_HTTPS_INTERFACE_TEMPLATE_NAME : Constants.GENERIC_HTTP_INTERFACE_TEMPLATE_NAME;
		final String mqttTemplateName = getSslProperties().isSslEnabled() ? Constants.GENERIC_MQTTS_INTERFACE_TEMPLATE_NAME : Constants.GENERIC_MQTT_INTERFACE_TEMPLATE_NAME;

		// discovery

		List<InterfaceModel> discoveryInterfaces = new ArrayList<>();

		final HttpOperationModel httpCheckOp = new HttpOperationModel.Builder()
				.method(HttpMethod.GET.name())
				.path(BlacklistConstants.HTTP_API_OP_CHECK)
				.build();

		final HttpOperationModel httpLookupOp = new HttpOperationModel.Builder()
				.method(HttpMethod.GET.name())
				.path(BlacklistConstants.HTTP_API_OP_LOOKUP)
				.build();

		final HttpInterfaceModel httpDiscoveryIntf = new HttpInterfaceModel.Builder(httpTemplateName, getDomainAddress(), getServerPort())
				.basePath(BlacklistConstants.HTTP_API_BASE_PATH)
				.operation(Constants.SERVICE_OP_CHECK, httpCheckOp)
				.operation(Constants.SERVICE_OP_LOOKUP, httpLookupOp)
				.build();

		discoveryInterfaces.add(httpDiscoveryIntf);

		if (isMqttApiEnabled()) {
			final MqttInterfaceModel mqttDiscoveryIntf = new MqttInterfaceModel.Builder(mqttTemplateName, getMqttBrokerAddress(), getMqttBrokerPort())
					.topic(BlacklistConstants.MQTT_API_DISCOVERY_TOPIC)
					.operations(Set.of(Constants.SERVICE_OP_CHECK, Constants.SERVICE_OP_LOOKUP))
					.build();

			discoveryInterfaces.add(mqttDiscoveryIntf);
		}

		final ServiceModel discovery = new ServiceModel.Builder()
				.serviceDefinition(Constants.SERVICE_DEF_BLACKLIST_DISCOVERY)
				.version(BlacklistConstants.VERSION_DISCOVERY)
				.metadata(BlacklistConstants.METADATA_KEY_UNRESTRICTED_DISCOVERY, true)
				.serviceInterfaces(discoveryInterfaces)
				.build();

		// management

		List<InterfaceModel> managementInterfaces = new ArrayList<>();

		final HttpOperationModel httpQueryOp = new HttpOperationModel.Builder()
				.method(HttpMethod.POST.name())
				.path(BlacklistConstants.HTTP_API_OP_QUERY)
				.build();

		final HttpOperationModel httpCreateOp = new HttpOperationModel.Builder()
				.method(HttpMethod.POST.name())
				.path(BlacklistConstants.HTTP_API_OP_CREATE)
				.build();

		final HttpOperationModel httpRemoveOp = new HttpOperationModel.Builder()
				.method(HttpMethod.DELETE.name())
				.path(BlacklistConstants.HTTP_API_OP_REMOVE)
				.build();

		final HttpInterfaceModel httpManagementIntf = new HttpInterfaceModel.Builder(httpTemplateName, getDomainAddress(), getServerPort())
				.basePath(BlacklistConstants.HTTP_API_MANAGEMENT_PATH)
				.operation(Constants.SERVICE_OP_BLACKLIST_QUERY, httpQueryOp)
				.operation(Constants.SERVICE_OP_BLACKLIST_CREATE, httpCreateOp)
				.operation(Constants.SERVICE_OP_BLACKLIST_REMOVE, httpRemoveOp)
				.build();

		managementInterfaces.add(httpManagementIntf);

		if (isMqttApiEnabled()) {
			final MqttInterfaceModel mqttManagementIntf = new MqttInterfaceModel.Builder(mqttTemplateName, getMqttBrokerAddress(), getMqttBrokerPort())
					.topic(BlacklistConstants.MQTT_API_MANAGEMENT_TOPIC)
					.operations(Set.of(Constants.SERVICE_OP_BLACKLIST_CREATE, Constants.SERVICE_OP_BLACKLIST_QUERY,  Constants.SERVICE_OP_BLACKLIST_REMOVE))
					.build();

			managementInterfaces.add(mqttManagementIntf);
		}

		final ServiceModel management = new ServiceModel.Builder()
				.serviceDefinition(Constants.SERVICE_DEF_BLACKLIST_MANAGEMENT)
				.version(BlacklistConstants.VERSION_MANAGEMENT)
				.metadata(BlacklistConstants.METADATA_KEY_UNRESTRICTED_DISCOVERY, false)
				.serviceInterfaces(managementInterfaces)
				.build();

		// general management

		final HttpOperationModel log = new HttpOperationModel.Builder()
				.method(HttpMethod.POST.name())
				.path(Constants.HTTP_API_OP_LOGS_PATH)
				.build();

		final HttpOperationModel config = new HttpOperationModel.Builder()
				.method(HttpMethod.GET.name())
				.path(Constants.HTTP_API_OP_GET_CONFIG_PATH)
				.build();

		final HttpInterfaceModel generalManagementInterface = new HttpInterfaceModel.Builder(httpTemplateName, getDomainAddress(), getServerPort())
				.basePath(BlacklistConstants.HTTP_API_GENERAL_MANAGEMENT_PATH)
				.operation(Constants.SERVICE_OP_GET_LOG, log)
				.operation(Constants.SERVICE_OP_GET_CONFIG, config)
				.build();

		final ServiceModel generalManagement = new ServiceModel.Builder()
				.serviceDefinition(Constants.SERVICE_DEF_GENERAL_MANAGEMENT)
				.version(BlacklistConstants.VERSION_GENERAL_MANAGEMENT)
				.metadata(BlacklistConstants.METADATA_KEY_UNRESTRICTED_DISCOVERY, false)
				.serviceInterface(generalManagementInterface)
				.build();

		// monitor

		final HttpOperationModel echo = new HttpOperationModel.Builder()
				.method(HttpMethod.GET.name())
				.path(BlacklistConstants.HTTP_API_OP_ECHO)
				.build();

		final HttpInterfaceModel monitorInterface = new HttpInterfaceModel.Builder(httpTemplateName, getDomainAddress(), getServerPort())
				.basePath(BlacklistConstants.HTTP_API_MONITOR_PATH)
				.operation(Constants.SERVICE_OP_ECHO, echo)
				.build();

		final ServiceModel monitor = new ServiceModel.Builder()
				.serviceDefinition(Constants.SERVICE_DEF_MONITOR)
				.version(BlacklistConstants.VERSION_MONITOR)
				.metadata(BlacklistConstants.METADATA_KEY_UNRESTRICTED_DISCOVERY, false)
				.serviceInterface(monitorInterface)
				.build();


		return List.of(discovery, management, generalManagement, monitor);
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
