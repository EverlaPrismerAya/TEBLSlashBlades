package com.tebl.tcpl;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * T.E.B.L. Slashblades — a purely data-driven mod.
 *
 * <p>All content (named blades and their recipes) is defined in {@code data/tcpl/}
 * and {@code assets/tcpl/}. This entrypoint only exists so the mod is a proper
 * Forge Java mod instead of a lowcodefml pack; it registers nothing.</p>
 */
@Mod(TCPL.MOD_ID)
public final class TCPL {
    public static final String MOD_ID = "tcpl";
    private static final Logger LOGGER = LogManager.getLogger();

    public TCPL() {
        LOGGER.info("{} initialized (data-driven, content in data/ and assets/)", MOD_ID);
    }
}
