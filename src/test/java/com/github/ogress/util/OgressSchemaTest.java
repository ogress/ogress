package com.github.ogress.util;

import com.github.ogress.OgressFieldInfo;
import com.github.ogress.OgressFieldKind;
import com.github.ogress.OgressObjectSchema;
import com.github.ogress.model.ChildBeanWithFields;
import com.github.ogress.model.RefListBean;
import com.github.ogress.model.SimpleBean;
import com.github.ogress.model.StringListBean;
import com.github.ogress.model.graph1.A1;
import com.github.ogress.serializer.DefaultSerializers;
import org.junit.Assert;
import org.junit.Test;

public class OgressSchemaTest extends Assert {
    //todo: + test and fix duplicate naming in ChildBeanWithFields

    @Test
    public void correctSchemaIsCreatedForSimpleBean() throws NoSuchFieldException {
        OgressObjectSchema schema = OgressUtils.prepareObjectSchema(SimpleBean.class);
        assertNotNull(schema);

        assertEquals("simple_bean", schema.typeName);
        assertSame(SimpleBean.class, schema.typeClass);

        assertNotNull(schema.fieldByOgressName);
        assertEquals(1, schema.fieldByOgressName.size());

        assertNotNull(schema.fields);
        assertEquals(1, schema.fields.length);

        assertFalse(schema.fields[0].kind == OgressFieldKind.Reference);

        assertEquals(SimpleBean.class.getField("name"), schema.fields[0].field);
        assertEquals("name", schema.fields[0].ogressFieldName);

        assertNotNull(schema.fields[0].valueSerializer);
        assertSame(DefaultSerializers.DEFAULT_VALUE_SERIALIZERS.get(String.class), schema.fields[0].valueSerializer);

        assertNotNull(schema.fields[0].valueDeserializer);
        assertSame(DefaultSerializers.DEFAULT_VALUE_DESERIALIZERS.get(String.class), schema.fields[0].valueDeserializer);

        assertNotNull(schema.referenceFields);
        assertEquals(0, schema.referenceFields.length);
    }

    @Test
    public void correctSchemaIsCreatedForChildBeanWithFields() throws NoSuchFieldException {
        OgressObjectSchema schema = OgressUtils.prepareObjectSchema(ChildBeanWithFields.class);
        assertNotNull(schema);
        assertEquals("child_bean_with_fields", schema.typeName);
        assertSame(ChildBeanWithFields.class, schema.typeClass);

        assertNotNull(schema.fieldByOgressName);
        assertEquals(6, schema.fieldByOgressName.size());

        for (OgressFieldInfo f : schema.fields) {
            assertNotNull(f.valueSerializer);
            assertNotNull(f.valueDeserializer);
        }
    }

    @Test
    public void referenceFieldIsProcessedCorrectly() {
        OgressObjectSchema schema = OgressUtils.prepareObjectSchema(A1.class);
        assertNotNull(schema);
        assertEquals("a1", schema.typeName);
        assertSame(A1.class, schema.typeClass);

        assertEquals(2, schema.fields.length);
        assertEquals(1, schema.referenceFields.length);

        OgressFieldInfo ref = schema.referenceFields[0];
        assertNotNull(ref);
        assertTrue(ref.kind == OgressFieldKind.Reference);
        assertNotNull(ref.accessor.field);
        assertNull(ref.accessor.getter);
        assertNull(ref.accessor.setter);
        assertNull(ref.valueSerializer);
        assertNull(ref.valueDeserializer);
        assertEquals("b1", ref.ogressFieldName);
    }

    @Test
    public void stringsListFieldIsProcessedCorrectly() {
        OgressObjectSchema schema = OgressUtils.prepareObjectSchema(StringListBean.class);
        assertEquals(1, schema.fields.length);
        assertEquals(0, schema.referenceFields.length);

        OgressFieldInfo f = schema.fields[0];
        assertFalse(f.kind == OgressFieldKind.Reference);
        assertNotNull(f.valueSerializer);
        assertNotNull(f.valueDeserializer);
    }

    @Test
    public void referencesListFieldIsProcessedCorrectly() {
        OgressObjectSchema schema = OgressUtils.prepareObjectSchema(RefListBean.class);
        assertEquals(1, schema.fields.length);
        OgressFieldInfo f = schema.fields[0];
        assertFalse(f.kind == OgressFieldKind.Reference);
        assertNotNull(f.valueSerializer);
        assertNotNull(f.valueDeserializer);
    }
}