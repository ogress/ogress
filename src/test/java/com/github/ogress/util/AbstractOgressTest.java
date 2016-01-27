package com.github.ogress.util;

import com.github.ogress.OgressDb;
import com.github.ogress.io.FileSystemIOAdapter;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractOgressTest extends Assert {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @NotNull
    public OgressDb createEmptyDbA1() throws IOException {
        Map<String, Object> props = new HashMap<>();
        OgressDb db1 = OgressDb.newInstance(props);
        assertNotNull(db1);

        Object root1 = db1.getRoot();
        assertNotNull(root1);
        assertTrue(root1 instanceof EmptyOgressObject);
        return db1;
    }
}
