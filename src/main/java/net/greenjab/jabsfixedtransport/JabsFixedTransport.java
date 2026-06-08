package net.greenjab.jabsfixedtransport;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JabsFixedTransport implements ModInitializer {
	public static final String MOD_ID = "jabsfixedtransport";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}
}