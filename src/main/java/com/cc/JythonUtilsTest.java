package com.cc;

import org.junit.Test;

import java.io.IOException;

public class JythonUtilsTest {

    @Test
    public void execCodeTest(){
        JythonUtils.execCode("print('Hello Jython！')");
    }

    @Test
    public void execFileTest(){
        JythonUtils.execFile("C:\\Users\\cc\\Desktop\\java2python.py");
    }

    @Test
    public void execFuncTest(){
        String file = "C:\\Users\\cc\\Desktop\\java2python.py";
        // 执行hello()方法
        Object helloRes = JythonUtils.execFunc(file, "hello", "中文");
        System.out.println(helloRes);
        // 执行add()方法
        Object addRes = JythonUtils.execFunc(file, "add", 2, 3);
        System.out.println(addRes);
        // 执行无参数无返回值方法（结果为null）
        System.out.println(JythonUtils.execFunc(file, "no"));
    }

    @Test
    public void execCmdTest() throws IOException, InterruptedException {
        String exe = "D:\\Program Files\\Python\\python.exe";
        String file = "C:\\Users\\cc\\Desktop\\java2python1.py";
        String res = JythonUtils.execCmd(exe, file, "中文", 18);
        System.out.println(res);
    }


}
