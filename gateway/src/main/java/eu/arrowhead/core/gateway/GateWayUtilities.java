package eu.arrowhead.core.gateway;

import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.dto.internal.RelayType;

public class GateWayUtilities {
    private GateWayUtilities() { super(); }
    //-------------------------------------------------------------------------------------------------
    public static RelayType convertStringToRelayType(final String str) {
        if (Utilities.isEmpty(str)) {
            return RelayType.GENERAL_RELAY;
        }

        try {
            return RelayType.valueOf(str.toUpperCase().trim());
        } catch (final IllegalArgumentException ex) {
            return null;
        }
    }


}
