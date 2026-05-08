package org.atcraftmc.qlib.audience;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.kyori.adventure.audience.Audience;
import org.atcraftmc.qlib.text.TextEngine;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

public abstract class AudienceService<A> implements QLibAudienceProvider {
    protected final TextEngine textEngine;
    protected final Cache<Audience, PipelineForwardedAudience> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(5))
            .build();

    protected AudienceService(TextEngine textEngine) {
        this.textEngine = textEngine;
    }

    protected AudienceService() {
        this(new TextEngine());
    }

    public abstract Audience wrap(A pointer);

    public abstract AudienceSource source(A pointer);

    public final TextEngine getTextEngine() {
        return textEngine;
    }

    public final PointedAudience get(A pointer) {
        var wrapped = wrap(pointer);

        try {
            return this.cache.get(wrapped, () -> new PipelineForwardedAudience(this, wrapped, pointer, source(pointer)));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
    }
}
