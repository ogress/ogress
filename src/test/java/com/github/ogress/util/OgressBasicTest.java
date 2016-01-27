package com.github.ogress.util;

import com.github.ogress.OgressDb;
import com.github.ogress.model.graph1.A1;
import org.junit.Test;

import java.io.IOException;

public class OgressBasicTest extends AbstractOgressTest {

    @Test(expected = IllegalArgumentException.class)
    public void ifTypeAIsNotRegisteredExceptionIsThrownOnSetRoot() throws IOException {
        OgressDb db1 = createEmptyDbA1();
        A1 a1 = A1.createGraph();
        db1.setRoot(a1);
    }

    @Test
    public void ifTypeBIsNotRegisteredNoExceptionIsThrownOnSetRoot() throws IOException {
        OgressDb db1 = createEmptyDbA1();
        db1.registerType(A1.class);

        A1 a1 = A1.createGraph();
        db1.setRoot(a1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ifTypeBIsNotRegisteredExceptionIsThrownOnFlush() throws IOException {
        OgressDb db1 = createEmptyDbA1();
        db1.registerType(A1.class);

        A1 a1 = A1.createGraph();
        db1.setRoot(a1);
        db1.flush();
    }

    @Test
    public void typeRegistrationNotNeededForNulls() throws IOException {
        OgressDb db1 = createEmptyDbA1();
        db1.registerType(A1.class);

        A1 a1 = A1.createGraph();
        a1.b1 = null;
        db1.setRoot(a1);
        Object root1 = db1.getRoot();
        assertEquals(a1, root1);
    }

}
