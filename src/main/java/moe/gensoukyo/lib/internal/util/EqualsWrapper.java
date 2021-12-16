package moe.gensoukyo.lib.internal.util;

import net.minecraftforge.fml.common.discovery.ModCandidate;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Compare by {@link ModCandidate#getModContainer()}'s actual file.
 * @author ChloePrime
 */
public class EqualsWrapper {
    public EqualsWrapper(ModCandidate wrapped) {
        this.wrapped = wrapped;
    }

    public final ModCandidate wrapped;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EqualsWrapper that = (EqualsWrapper) o;

        try {
            return Files.isSameFile(
                    wrapped.getModContainer().toPath(),
                    that.wrapped.getModContainer().toPath()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        try {
            return wrapped.getModContainer().getCanonicalFile().hashCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
