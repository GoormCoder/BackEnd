package goormcoder.webide.util.javaCompiler;

import java.util.Map;

class MemoryClassLoader extends ClassLoader {
    private final Map<String, byte[]> classBytes;

    public MemoryClassLoader(Map<String, byte[]> classBytes) {
        this.classBytes = classBytes;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] buf = classBytes.get(name);
        if (buf == null) {
            throw new ClassNotFoundException(name);
        }
        return defineClass(name, buf, 0, buf.length);
    }
}