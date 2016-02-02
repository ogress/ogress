package com.github.ogress.util;

import com.github.ogress.OgressFieldAccessor;
import com.github.ogress.OgressFieldKind;
import com.github.ogress.model.AllFieldTypesBean;
import com.github.ogress.model.ChildBean;
import com.github.ogress.model.ChildBeanWithFields;
import com.github.ogress.model.ComplexChildBean;
import com.github.ogress.model.ParentBeanWithFields;
import com.github.ogress.model.SimpleBean;
import com.github.ogress.model.errors.BeanIncompatibleSetter;
import com.github.ogress.model.errors.BeanMultiArgsGetter;
import com.github.ogress.model.errors.BeanMultiArgsSetter;
import com.github.ogress.model.errors.BeanNoPublicGetter;
import com.github.ogress.model.errors.BeanNoPublicSetter;
import com.github.ogress.model.errors.BeanStaticAccessor;
import com.github.ogress.model.errors.BeanWithDuplicateFieldName;
import com.github.ogress.model.errors.BeanWithDuplicateFieldNameWithParent;
import com.github.ogress.model.errors.BeanWithFinalField;
import com.github.ogress.model.errors.BeanWithIllegalFieldName;
import com.github.ogress.model.errors.BeanWithIllegalTypeName;
import com.github.ogress.model.errors.BeanWithStaticField;
import com.github.ogress.model.errors.ChildOfAbstractParentBean;
import com.github.ogress.model.errors.ChildOfAbstractParentNoBean;
import com.github.ogress.model.errors.ChildOfTypedInterfaceBean;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static com.github.ogress.util.OgressUtils.getOgressFieldKind;

public class OgressUtilsTest extends Assert {

    @Test
    public void getObjectTypeNameSimpleBean() {
        String typeName = OgressUtils.getObjectTypeName(SimpleBean.class);
        assertEquals("simple_bean", typeName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getObjectTypeOfNoBeanThrowsException() {
        OgressUtils.getObjectTypeName(Object.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTypeOfBeanWithIllegalTypeThrowsException() {
        OgressUtils.getObjectTypeName(BeanWithIllegalTypeName.class);
    }

    @Test
    public void getObjectTypeNameChildBean() {
        String typeName = OgressUtils.getObjectTypeName(ChildBean.class);
        assertEquals("child_bean", typeName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getObjectTypeNameNotBeanWithInterfacesThrowsException() {
        OgressUtils.getObjectTypeName(String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getObjectTypeNameChildIsNoBeanThrowsException() {
        OgressUtils.getObjectTypeName(ChildOfAbstractParentNoBean.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getObjectTypeNameChildIsAbstractAndMarkedAsTypeThrowsException() {
        OgressUtils.getObjectTypeName(ChildOfAbstractParentBean.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getObjectTypeNameChildIsInterfacesAndMarkedAsTypeThrowsException() {
        OgressUtils.getObjectTypeName(ChildOfTypedInterfaceBean.class);
    }

    @Test
    public void getOgressFieldsTestSimpleBean() {
        Map<String, Field> fieldMap = OgressUtils.getOgressFields(SimpleBean.class);
        assertEquals(1, fieldMap.size());
        assertTrue(fieldMap.containsKey("name"));
    }

    @Test
    public void getOgressFieldsTestComplexBean() {
        Map<String, Field> fieldMap = OgressUtils.getOgressFields(ComplexChildBean.class);
        assertEquals(5, fieldMap.size());
        assertTrue(fieldMap.containsKey("name"));
        assertTrue(fieldMap.containsKey("public_field"));
        assertTrue(fieldMap.containsKey("default_access_field"));
        assertTrue(fieldMap.containsKey("protected_field"));
        assertTrue(fieldMap.containsKey("private_field"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldWithNoNameThrowsException() {
        OgressUtils.getOgressFields(BeanWithIllegalFieldName.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldChecksStaticsAndThrowsException() {
        OgressUtils.getOgressFields(BeanWithStaticField.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldChecksFinalsAndThrowsException() {
        OgressUtils.getOgressFields(BeanWithFinalField.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldChecksForDuplicatesAndThrowsException() {
        OgressUtils.getOgressFields(BeanWithDuplicateFieldName.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldChecksForDuplicatesInParentsAndThrowsException() {
        OgressUtils.getOgressFields(BeanWithDuplicateFieldNameWithParent.class);
    }

    @Test
    public void getFieldAccessorReturnsCorrectResultForPublicField() throws NoSuchFieldException {
        Field field = SimpleBean.class.getField("name");
        OgressFieldAccessor accessor = OgressUtils.getFieldAccessor(SimpleBean.class, field);
        assertNotNull(accessor);
        assertEquals(field, accessor.field);
        assertNull(accessor.getter);
        assertNull(accessor.setter);
    }

    @Test
    public void getFieldAccessorReturnsCorrectResultForPublicParentField() throws NoSuchFieldException {
        Field field = ChildBeanWithFields.class.getField("publicParentField");
        OgressFieldAccessor accessor = OgressUtils.getFieldAccessor(ChildBeanWithFields.class, field);
        assertNotNull(accessor);
        assertEquals(field, accessor.field);
        assertNull(accessor.getter);
        assertNull(accessor.setter);
    }

    @Test
    public void getFieldAccessorReturnsCorrectResultForGetterAndSetter() throws NoSuchFieldException, NoSuchMethodException {
        Field field = ChildBeanWithFields.class.getDeclaredField("privateChildField");
        OgressFieldAccessor accessor = OgressUtils.getFieldAccessor(ChildBeanWithFields.class, field);
        assertNotNull(accessor);
        assertNull(accessor.field);

        Method expectedGetter = ChildBeanWithFields.class.getMethod("getPrivateChildField");
        assertNotNull(expectedGetter);
        assertEquals(expectedGetter, accessor.getter);

        Method expectedSetter = ChildBeanWithFields.class.getMethod("setPrivateChildField", String.class);
        assertNotNull(expectedSetter);
        assertEquals(expectedSetter, accessor.setter);
    }

    @Test
    public void getFieldAccessorReturnsCorrectResultForGetterAndSetterInParent() throws NoSuchFieldException, NoSuchMethodException {
        Field field = ParentBeanWithFields.class.getDeclaredField("privateParentField");
        OgressFieldAccessor accessor = OgressUtils.getFieldAccessor(ChildBeanWithFields.class, field);
        assertNotNull(accessor);
        assertNull(accessor.field);

        Method expectedGetter = ChildBeanWithFields.class.getMethod("getPrivateParentField");
        assertNotNull(expectedGetter);
        assertEquals(expectedGetter, accessor.getter);

        Method expectedSetter = ChildBeanWithFields.class.getMethod("setPrivateParentField", String.class);
        assertNotNull(expectedSetter);
        assertEquals(expectedSetter, accessor.setter);
    }

    @Test
    public void getFieldAccessorReturnsCorrectResultForBooleanGetterAndSetter() throws NoSuchFieldException, NoSuchMethodException {
        Field field = ChildBeanWithFields.class.getDeclaredField("privateChildFieldBoolean");
        OgressFieldAccessor accessor = OgressUtils.getFieldAccessor(ChildBeanWithFields.class, field);
        assertNotNull(accessor);
        assertNull(accessor.field);

        Method expectedGetter = ChildBeanWithFields.class.getMethod("isPrivateChildFieldBoolean");
        assertNotNull(expectedGetter);
        assertEquals(expectedGetter, accessor.getter);

        Method expectedSetter = ChildBeanWithFields.class.getMethod("setPrivateChildFieldBoolean", Boolean.TYPE);
        assertNotNull(expectedSetter);
        assertEquals(expectedSetter, accessor.setter);
    }

    @Test
    public void getFieldAccessorReturnsCorrectResultForSetterWithNonVoidReturn() throws NoSuchFieldException, NoSuchMethodException {
        Field field = ChildBeanWithFields.class.getDeclaredField("privateChildField2");
        OgressFieldAccessor accessor = OgressUtils.getFieldAccessor(ChildBeanWithFields.class, field);
        assertNotNull(accessor);
        assertNull(accessor.field);

        Method expectedSetter = ChildBeanWithFields.class.getMethod("setPrivateChildField2", String.class);
        assertNotNull(expectedSetter);
        assertEquals(expectedSetter, accessor.setter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldAccessorThrowsExceptionNoPublicGetter() throws NoSuchFieldException {
        Field field = BeanNoPublicGetter.class.getDeclaredField("field");
        OgressUtils.getFieldAccessor(BeanNoPublicGetter.class, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldAccessorThrowsExceptionNoPublicSetter() throws NoSuchFieldException {
        Field field = BeanNoPublicSetter.class.getDeclaredField("field");
        OgressUtils.getFieldAccessor(BeanNoPublicSetter.class, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldAccessorThrowsExceptionIfAccessorIsStatic() throws NoSuchFieldException {
        Field field = BeanStaticAccessor.class.getDeclaredField("field");
        OgressUtils.getFieldAccessor(BeanStaticAccessor.class, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldAccessorThrowsExceptionIfClassDoesNotMatchedField() throws NoSuchFieldException {
        Field field = SimpleBean.class.getField("name");
        OgressUtils.getFieldAccessor(ChildBeanWithFields.class, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldAccessorThrowsExceptionIfSetterArgIsNotCompatible() throws NoSuchFieldException {
        Field field = BeanIncompatibleSetter.class.getDeclaredField("field");
        OgressUtils.getFieldAccessor(BeanIncompatibleSetter.class, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldAccessorThrowsExceptionIfGetterHasMultipleArgs() throws NoSuchFieldException {
        Field field = BeanMultiArgsGetter.class.getDeclaredField("field");
        OgressUtils.getFieldAccessor(BeanMultiArgsGetter.class, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldAccessorThrowsExceptionIfSetterHasMultipleArgs() throws NoSuchFieldException {
        Field field = BeanMultiArgsSetter.class.getDeclaredField("field");
        OgressUtils.getFieldAccessor(BeanMultiArgsSetter.class, field);
    }

    @Test
    public void checkGetOgressFieldKindForValues() throws NoSuchFieldException {
        Class<AllFieldTypesBean> cls = AllFieldTypesBean.class;

        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("booleanField")));
        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("BooleanField")));

        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("byteField")));
        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("ByteField")));

        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("charField")));
        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("CharacterField")));

        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("doubleField")));
        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("DoubleField")));

        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("floatField")));
        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("FloatField")));

        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("intField")));
        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("IntegerField")));

        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("longField")));
        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("LongField")));

        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("shortField")));
        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("ShortField")));

        assertSame(OgressFieldKind.Value, getOgressFieldKind(cls.getField("StringField")));
    }

    @Test
    public void checkGetOgressFieldKindForReferences() throws NoSuchFieldException {
        Class<AllFieldTypesBean> cls = AllFieldTypesBean.class;

        assertSame(OgressFieldKind.Reference, getOgressFieldKind(cls.getField("ParentBeanField")));
        assertSame(OgressFieldKind.Reference, getOgressFieldKind(cls.getField("ChildBeanField")));
        assertSame(OgressFieldKind.Reference, getOgressFieldKind(cls.getField("ObjectField")));
        assertSame(OgressFieldKind.Reference, getOgressFieldKind(cls.getField("SerializableField")));
        assertSame(OgressFieldKind.Reference, getOgressFieldKind(cls.getField("RunnableField")));
    }

    @Test
    public void checkGetOgressFieldKindForArrayOfValues() throws NoSuchFieldException {
        Class<AllFieldTypesBean> cls = AllFieldTypesBean.class;

        assertSame(OgressFieldKind.ArrayOfValues, getOgressFieldKind(cls.getField("booleanArrayField")));
        assertSame(OgressFieldKind.ArrayOfValues, getOgressFieldKind(cls.getField("BooleanArrayField")));
        assertSame(OgressFieldKind.ArrayOfValues, getOgressFieldKind(cls.getField("charArrayField")));
        assertSame(OgressFieldKind.ArrayOfValues, getOgressFieldKind(cls.getField("CharacterArrayField")));
        assertSame(OgressFieldKind.ArrayOfValues, getOgressFieldKind(cls.getField("intArrayField")));
        assertSame(OgressFieldKind.ArrayOfValues, getOgressFieldKind(cls.getField("IntegerArrayField")));
        assertSame(OgressFieldKind.ArrayOfValues, getOgressFieldKind(cls.getField("StringArrayField")));
    }

    @Test
    public void checkGetOgressFieldKindForArrayOfReferences() throws NoSuchFieldException {
        Class<AllFieldTypesBean> cls = AllFieldTypesBean.class;
        assertSame(OgressFieldKind.ArrayOfReferences, getOgressFieldKind(cls.getField("ObjectArrayField")));
        assertSame(OgressFieldKind.ArrayOfReferences, getOgressFieldKind(cls.getField("SerializableArrayField")));
        assertSame(OgressFieldKind.ArrayOfReferences, getOgressFieldKind(cls.getField("RunnableArrayField")));
        assertSame(OgressFieldKind.ArrayOfReferences, getOgressFieldKind(cls.getField("ParentBeanArrayField")));
        assertSame(OgressFieldKind.ArrayOfReferences, getOgressFieldKind(cls.getField("ChildBeanArrayField")));
    }

    @Test
    public void checkGetOgressFieldKindForIterableOfValues() throws NoSuchFieldException {
        Class<AllFieldTypesBean> cls = AllFieldTypesBean.class;
        assertSame(OgressFieldKind.IterableOfValues, getOgressFieldKind(cls.getField("IntegerListField")));
        assertSame(OgressFieldKind.IterableOfValues, getOgressFieldKind(cls.getField("StringIterableField")));
        assertSame(OgressFieldKind.IterableOfValues, getOgressFieldKind(cls.getField("BooleanSetField")));
        assertSame(OgressFieldKind.IterableOfValues, getOgressFieldKind(cls.getField("IntegerArrayListField")));
    }

    @Test
    public void checkGetOgressFieldKindForIterableOfReferences() throws NoSuchFieldException {
        Class<AllFieldTypesBean> cls = AllFieldTypesBean.class;
        assertSame(OgressFieldKind.IterableOfReferences, getOgressFieldKind(cls.getField("ObjectCollection")));
        assertSame(OgressFieldKind.IterableOfReferences, getOgressFieldKind(cls.getField("ObjectSetField")));
        assertSame(OgressFieldKind.IterableOfReferences, getOgressFieldKind(cls.getField("RunnableIterableField")));
        assertSame(OgressFieldKind.IterableOfReferences, getOgressFieldKind(cls.getField("ExtendsRunnableIterableField")));
        assertSame(OgressFieldKind.IterableOfReferences, getOgressFieldKind(cls.getField("StackOfSerializableField")));
        assertSame(OgressFieldKind.IterableOfReferences, getOgressFieldKind(cls.getField("ChildBeanLinkedListField")));
    }


    @Test
    public void checkGetOgressFieldKindForMapOfValues() {
        Class<AllFieldTypesBean> cls = AllFieldTypesBean.class;
        //todo:
    }

    @Test
    public void checkGetOgressFieldKindForMapOfReferences() {
        //todo:
    }

}
