package com.github.ogress.util;

import com.github.ogress.OgressObjectSchema;
import com.github.ogress.model.SimpleBean;
import com.github.ogress.serializer.ValueSerializers;
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
        assertFalse(schema.fields[0].isReference);
        assertEquals(SimpleBean.class.getField("name"), schema.fields[0].field);
        assertEquals("name", schema.fields[0].ogressFieldName);

        assertNotNull(schema.fields[0].valueSerializer);
        assertSame(ValueSerializers.FIELD_SERIALIZERS.get(String.class), schema.fields[0].valueSerializer);

        assertNotNull(schema.fields[0].valueDeserializer);
        assertSame(ValueSerializers.FIELD_DESERIALIZERS.get(String.class), schema.fields[0].valueDeserializer);

        assertNotNull(schema.referenceFields);
        assertEquals(0, schema.referenceFields.length);
    }
}
