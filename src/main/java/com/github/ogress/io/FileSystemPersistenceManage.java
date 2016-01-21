package com.github.ogress.io;

import com.github.ogress.OgressDb;
import com.sun.xml.internal.fastinfoset.sax.Properties;
import org.jetbrains.annotations.NotNull;

public class FileSystemPersistenceManage implements OgressDb {

    /**
     * Creates new instance of OgressDb. Throws exception if instantiation fails.
     *
     * @param options set of options, see below
     *                <ul>
     *                <li><i>dir</i> - root dir to store database.
     *                Dir will be created if not exists or root object is tried to be loaded if the dir exists.
     *                [Required].</li>
     *                <li><i>ioClass</i> - object loader implementation. Object loaders used to load/store raw object data.
     *                [Optional]</li>
     *                <li><i>serializerClass</i> - object serializer class name. Used to serialize/de-serialize objects from raw data.</li>
     *                </ul>
     * @return instance of OgressDb
     */
    @NotNull
    public static OgressDb create(@NotNull Properties options) {
        return null;
    }

    @NotNull
    @Override
    public OgressObject getRoot() {
        //TODO:
        return null;
    }

    @Override
    public void setRoot(@NotNull OgressObject newRoot) {
        //TODO:
    }

    @Override
    public void flush() {
        //TODO:
    }

    @Override
    public void cleanup() {
        //TODO:
    }

    @Override
    public void close() {
        //TODO:
    }
}
