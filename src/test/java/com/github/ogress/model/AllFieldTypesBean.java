package com.github.ogress.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collector;

public class AllFieldTypesBean {
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

    public int integerField;
    public Integer IntegerField;

    public long longField;
    public Long LongField;

    public short shotField;
    public Short ShortField;

    public String StringField;

    public ParentBean ParentBeanField;
    public ChildBean ChildBeanField;
    public Object ObjectField;
    public Serializable SerializableField;
    public Runnable RunnableField;


    public boolean[] booleanArrayField;
    public Boolean[] BooleanArrayField;

    public char[] charArrayField;
    public Character[] CharacterArrayField;

    public int[] intArrayField;
    public Integer[] IntegerArrayField;

    public String[] StringArrayField;


    public Object[] ObjectArrayField;
    public Serializable[] SerializableArrayField;
    public Runnable[] RunnableArrayField;
    public ParentBean[] ParentBeanArrayField;
    public ChildBean[] ChildBeanArrayField;

    public List<Integer> IntegerListField;
    public Iterable<String> StringIterableField;
    public Set<Boolean> BooleanSetField;
    public ArrayList<Integer> IntegerArrayListField;

    public Collection ObjectCollection;
    public Set<Object> ObjectSetField;
    public Iterable<Runnable> RunnableIterableField;
    public Iterable<? extends Runnable> ExtendsRunnableIterableField;
    public Stack<Serializable> StackOfSerializableField;
    public LinkedList<ChildBean> ChildBeanLinkedListField;
}
