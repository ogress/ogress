package com.github.ogress.util;

import com.github.ogress.OgressFieldAccessor;
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

public class OgressUtilsTest extends Assert {

    @Test
    public void getObjectTypeNameSimpleBean() {
        String typeName = OgressUtils.getObjectTypeName(SimpleBean.class);
        assertEquals("simple_bean", typeName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getObjectTypeOfNoBeanThrowsIllegalArgumentException() {
        OgressUtils.getObjectTypeName(Object.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTypeOfBeanWithIllegalTypeThrowsIllegalArgumentException() {
        OgressUtils.getObjectTypeName(BeanWithIllegalTypeName.class);
    }

    @Test
    public void getObjectTypeNameChildBean() {
        String typeName = OgressUtils.getObjectTypeName(ChildBean.class);
        assertEquals("child_bean", typeName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getObjectTypeNameChildIsNoBeanThrowsIllegalArgumentException() {
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
    public void getFieldWithNoNameThrowsIllegalArgumentException() {
        OgressUtils.getOgressFields(BeanWithIllegalFieldName.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldChecksStaticsAndThrowsIllegalArgumentException() {
        OgressUtils.getOgressFields(BeanWithStaticField.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldChecksFinalsAndThrowsIllegalArgumentException() {
        OgressUtils.getOgressFields(BeanWithFinalField.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldChecksForDuplicatesAndThrowsIllegalArgumentException() {
        OgressUtils.getOgressFields(BeanWithDuplicateFieldName.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldChecksForDuplicatesInParentsAndThrowsIllegalArgumentException() {
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
    public void getFieldAccessorReturnsCorrectResultForSetterWithReturn() throws NoSuchFieldException, NoSuchMethodException {
        Field field = ChildBeanWithFields.class.getDeclaredField("privateChildField2");
        OgressFieldAccessor accessor = OgressUtils.getFieldAccessor(ChildBeanWithFields.class, field);
        assertNotNull(accessor);
        assertNull(accessor.field);

        Method expectedSetter = ChildBeanWithFields.class.getMethod("setPrivateChildField2", String.class);
        assertNotNull(expectedSetter);
        assertEquals(expectedSetter, accessor.setter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldAccessorThrowsIllegalArgumentExceptionNoPublicGetter() throws NoSuchFieldException {
        Field field = BeanNoPublicGetter.class.getDeclaredField("field");
        OgressUtils.getFieldAccessor(BeanNoPublicGetter.class, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldAccessorThrowsIllegalArgumentExceptionNoPublicSetter() throws NoSuchFieldException {
        Field field = BeanNoPublicSetter.class.getDeclaredField("field");
        OgressUtils.getFieldAccessor(BeanNoPublicSetter.class, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldAccessorThrowsIllegalArgumentExceptionIfAccessorIsStatic() throws NoSuchFieldException {
        Field field = BeanStaticAccessor.class.getDeclaredField("field");
        OgressUtils.getFieldAccessor(BeanStaticAccessor.class, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldAccessorThrowsIllegalArgumentExceptionIfClassDoesNotMatchedField() throws NoSuchFieldException {
        Field field = SimpleBean.class.getField("name");
        OgressUtils.getFieldAccessor(ChildBeanWithFields.class, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldAccessorThrowsIllegalArgumentExceptionIfSetterArgIsNotCompatible() throws NoSuchFieldException {
        Field field = BeanIncompatibleSetter.class.getDeclaredField("field");
        OgressUtils.getFieldAccessor(BeanIncompatibleSetter.class, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldAccessorThrowsIllegalArgumentExceptionIfGetterHasMultipleArgs() throws NoSuchFieldException {
        Field field = BeanMultiArgsGetter.class.getDeclaredField("field");
        OgressUtils.getFieldAccessor(BeanMultiArgsGetter.class, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFieldAccessorThrowsIllegalArgumentExceptionIfSetterHasMultipleArgs() throws NoSuchFieldException {
        Field field = BeanMultiArgsSetter.class.getDeclaredField("field");
        OgressUtils.getFieldAccessor(BeanMultiArgsSetter.class, field);
    }

}
