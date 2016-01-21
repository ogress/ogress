package com.github.ogress.util;

import com.github.ogress.model.BeanWithIllegalType;
import com.github.ogress.model.ChildBean;
import com.github.ogress.model.ChildOfAbstractParentBean;
import com.github.ogress.model.ChildOfAbstractParentNoBean;
import com.github.ogress.model.ChildOfTypedInterfaceBean;
import com.github.ogress.model.SimpleBean;
import org.junit.Assert;
import org.junit.Test;

public class OgressUtilsTest extends Assert {

    @Test
    public void getObjectTypeNameSimpleBean() {
        String typeName = OgressUtils.getObjectTypeName(new SimpleBean());
        assertEquals("simple_bean", typeName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getObjectTypeOfNoBeanThrowsIllegalArgumentException() {
        OgressUtils.getObjectTypeName(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTypeOfBeanWithIllegalTypeThrowsIllegalArgumentException() {
        OgressUtils.getObjectTypeName(new BeanWithIllegalType());
    }

    @Test
    public void getObjectTypeNameChildBean() {
        String typeName = OgressUtils.getObjectTypeName(new ChildBean());
        assertEquals("child_bean", typeName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getObjectTypeNameChildIsNoBeanThrowsIllegalArgumentException() {
        OgressUtils.getObjectTypeName(new ChildOfAbstractParentNoBean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getObjectTypeNameChildIsAbstractAndMarkedAsTypeThrowsException() {
        OgressUtils.getObjectTypeName(new ChildOfAbstractParentBean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getObjectTypeNameChildIsInterfacesAndMarkedAsTypeThrowsException() {
        OgressUtils.getObjectTypeName(new ChildOfTypedInterfaceBean());
    }

}
