package goormcoder.webide.util.javaCompiler;

import java.net.URI;
import javax.tools.SimpleJavaFileObject;

class MemoryJavaFileObject extends SimpleJavaFileObject {
    final String code;

    MemoryJavaFileObject(String name, String code) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}