package eu.arrowhead.blacklist.api.mqtt;

import java.security.InvalidParameterException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import eu.arrowhead.blacklist.BlacklistConstants;
import eu.arrowhead.blacklist.service.DiscoveryService;
import eu.arrowhead.common.Constants;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.mqtt.MqttStatus;
import eu.arrowhead.common.mqtt.handler.MqttTopicHandler;
import eu.arrowhead.common.mqtt.model.MqttRequestModel;
import eu.arrowhead.dto.BlacklistEntryListResponseDTO;

@Service
@ConditionalOnProperty(name = Constants.MQTT_API_ENABLED, matchIfMissing = false)
public class DiscoveryMqttHandler extends MqttTopicHandler {

	//=================================================================================================
	// members

	@Autowired
	private DiscoveryService discoveryService;

	private final Logger logger = LogManager.getLogger(getClass());

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@Override
	public String topic() {
		return BlacklistConstants.MQTT_API_DISCOVERY_TOPIC;
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public void handle(final MqttRequestModel request) throws ArrowheadException {
		logger.debug("DiscoveryMqttHandler.handle started");
		Assert.isTrue(request.getRequestTopic().equals(topic()), "MQTT topic-handler mismatch");

		final String origin = topic() + request.getOperation();
		MqttStatus responseStatus = MqttStatus.OK;
		Object responsePayload = null;

		switch (request.getOperation()) {
		case Constants.SERVICE_OP_CHECK:
			final String systemName = readPayload(request.getPayload(), String.class);
			responsePayload = check(systemName, origin);
			break;

		case Constants.SERVICE_OP_LOOKUP:
			responsePayload = lookup(request.getRequester(), origin);
			break;

		default:
			throw new InvalidParameterException("Unknown operation: " + request.getOperation());
		}

		successResponse(request, responseStatus, responsePayload);
	}

	//=================================================================================================
	// assistant methods

	// check
	//-------------------------------------------------------------------------------------------------
	private boolean check(final String systemName, final String origin) {
		logger.debug("DiscoveryMqttHandler.check started");

		return discoveryService.check(systemName, origin);
	}

	// lookup
	//-------------------------------------------------------------------------------------------------
	private BlacklistEntryListResponseDTO lookup(final String identifiedRequester, final String origin) {
		logger.debug("DiscoveryMqttHandler.lookup started");

		return discoveryService.lookup(identifiedRequester, origin);
	}

}
