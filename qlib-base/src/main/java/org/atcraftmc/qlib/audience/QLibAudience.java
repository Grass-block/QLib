package org.atcraftmc.qlib.audience;

import net.kyori.adventure.audience.ForwardingAudience;

public interface QLibAudience extends ForwardingAudience {
    default PointedAudience pointed() {
        return ((PointedAudience) this);
    }

    default PointedForwardingAudience forwarding() {
        return ((PointedForwardingAudience) this);
    }

    default PipelineForwardedAudience pipe() {
        return ((PipelineForwardedAudience) this);
    }
}
