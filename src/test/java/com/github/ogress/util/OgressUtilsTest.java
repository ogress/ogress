package com.github.ogress.util;

import com.github.ogress.model.ChildBean;
import com.github.ogress.model.ComplexChildBean;
import com.github.ogress.model.SimpleBean;
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

}
