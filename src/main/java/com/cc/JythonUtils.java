package com.cc;

import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class JythonUtils {

    private static final PythonInterpreter interpreter = new PythonInterpreter();


    // 执行一段Python代码
    public static void execCode(String code) {
        // 中文及中文符号显示为?问题，解决
        PyString codeUtf8 = Py.newStringUTF8(code);
        // 执行一段Python代码
        interpreter.exec(codeUtf8);
        interpreter.close();
    }


    // 执行Python文件脚本
    // IDEA控制台会显示Python脚本中的所有打印
    public static void execFile(String file) {
        // 执行一个.py文件（支持Python内置模块）
        interpreter.execfile(file);
        interpreter.close();
    }


    // 执行Python文件中的指定方法
    // IDEA控制台会显示Python脚本中的所有打印
    // 每执行文件中的一个方法，都会重新加载执行一次Python文件
    public static Object execFunc(String file, String method, Object... params) {
        interpreter.execfile(file);
        // 报错解决：UnicodeDecodeError: 'ascii' codec can't decode byte 0xef in position 5: ordinal not in range(128)
        interpreter.exec("import sys");
        interpreter.exec("reload(sys)");
        interpreter.exec("sys.setdefaultencoding('utf-8')");
        PyObject func = interpreter.get(method, PyFunction.class);
        // Java类型转换为Python类型：Py.java2py()、Py.javas2pys()
        PyObject[] pyObjs = Py.javas2pys(params);
        // 输入中文输出乱码解决：Py.newStringUTF8() => Py.newStringOrUnicode()
        // func.__call__(Py.newStringOrUnicode("中文"))
        PyObject result = func.__call__(pyObjs);
        // Python类型转换为Java类型（2种方式）
        // System.out.println(result.__tojava__(Object.class).getClass().getName());
        interpreter.close();
        return Py.tojava(result, Object.class);
    }


    // 自定义python.exe（可指定虚拟环境）执行含有第三方库的Python文件
    public static String execCmd(String exe, String file, Object... args) throws IOException, InterruptedException {
        List<Object> argv = new ArrayList<>(Arrays.asList(exe, file));
        argv.addAll(Arrays.asList(args));
        String[] argvs = new String[argv.size()];
        for (int i = 0; i < argv.size(); i++) {
            argvs[i] = String.valueOf(argv.get(i));
        }
        return execCmd(argvs);
    }


    // 自定义python.exe（可指定虚拟环境）执行含有第三方库的Python文件
    private static String execCmd(String... argv) throws IOException, InterruptedException {
        Process proc = Runtime.getRuntime().exec(argv);
        // 使用输入缓冲流截取结果
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "GBK"));
        StringBuilder builder = new StringBuilder();
        String line = in.readLine();
        while (line != null){
            builder.append(line);
            line = in.readLine();
            if (line != null) builder.append("\n");
        }
        in.close();
        proc.waitFor();
        return builder.toString();
    }


}





