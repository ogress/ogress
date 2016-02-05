package com.github.ogress.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

@SuppressWarnings("unused")
public class AllFieldTypesBean {
    // Values
    public boolean booleanField;
    public Boolean BooleanField;

    public byte byteField;
    public Byte ByteField;

    public char charField;
    public Character CharacterField;

    public double doubleField;
    public Double DoubleField;

    public float floatField;
    public Float FloatField;

    public int intField;
    public Integer IntegerField;

    public long longField;
    public Long LongField;

    public short shortField;
    public Short ShortField;

    public String StringField;


    // References
    public ParentBean ParentBeanField;
    public ChildBean ChildBeanField;
    public Object ObjectField;
    public Serializable SerializableField;
    public Runnable RunnableField;


    // Arrays of values
    public boolean[] booleanArrayField;
    public Boolean[] BooleanArrayField;
    public char[] charArrayField;
    public Character[] CharacterArrayField;
    public int[] intArrayField;
    public Integer[] IntegerArrayField;
    public String[] StringArrayField;


    // Arrays of references
    public Object[] ObjectArrayField;
    public Serializable[] SerializableArrayField;
    public Runnable[] RunnableArrayField;
    public ParentBean[] ParentBeanArrayField;
    public ChildBean[] ChildBeanArrayField;


    // Collection of values
    public List<Integer> IntegerListField;
    public Iterable<String> StringIterableField;
    public Set<Boolean> BooleanSetField;
    public ArrayList<Integer> IntegerArrayListField;


    // Collection of references
    public Collection ObjectCollectionField;
    public Set<Object> ObjectSetField;
    public Iterable<Runnable> RunnableIterableField;
    public Iterable<? extends Runnable> ExtendsRunnableIterableField;
    public Stack<Serializable> StackOfSerializableField;
    public LinkedList<ChildBean> ChildBeanLinkedListField;


    // Map of values
    public Map<Integer, String> IntegerStringMapField;
    public Map<String, Boolean> StringBooleanMap;


    // Map of references
    public Map ObjectMapField;
    public Map<String, Object> StringObjectMapField;
    public Map<String, Runnable> StringRunnableMapField;
    public Map<Boolean, Serializable> BooleanSerializableMapField;
    public Map<Boolean, List> BooleanListMapField;
    public Map<Runnable, String> RunnableStringMapField;
}
