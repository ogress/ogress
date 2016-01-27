package com.github.ogress.util;

import com.github.ogress.OgressDb;
import com.github.ogress.model.graph1.A1;
import com.github.ogress.model.graph1.B1;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;


public class TestFilesystemIOAdapterTest extends AbstractOgressTest {

    @Ignore
    @Test
    public void simpleCreateAndLoadTest() throws IOException {
        OgressDb db1 = createEmptyDbA1();

        A1 a1 = A1.createGraph();
        db1.setRoot(a1);
        Object root1 = db1.getRoot();
        assertEquals(a1, root1);
        db1.flush();

        OgressDb db2 = OgressDb.newInstance(db1.getProperties());
        assertNotNull(db2);

        Object root2 = db2.getRoot();
        assertTrue(root2.getClass() == A1.class);
        A1 a2 = (A1) root2;
        assertEquals(A1.VALUE, a2.value);
        assertNotNull(a2.b1);
        assertEquals(B1.VALUE, a2.b1.value);
    }

}
