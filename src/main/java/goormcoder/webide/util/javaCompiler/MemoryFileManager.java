package goormcoder.webide.util.javaCompiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

class MemoryFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    final Map<String, ByteArrayOutputStream> classBytes = new HashMap<>();

    MemoryFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        if (kind == JavaFileObject.Kind.CLASS) {
            return new SimpleJavaFileObject(URI.create("string:///" + className.replace('.', '/') + kind.extension), kind) {
                @Override
                public OutputStream openOutputStream() {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    classBytes.put(className, bos);
                    return bos;
                }
            };
        }
        return super.getJavaFileForOutput(location, className, kind, sibling);
    }

    public Map<String, byte[]> getClassBytes() {
        Map<String, byte[]> result = new HashMap<>();
        classBytes.forEach((k, v) -> result.put(k, v.toByteArray()));
        return result;
    }
}