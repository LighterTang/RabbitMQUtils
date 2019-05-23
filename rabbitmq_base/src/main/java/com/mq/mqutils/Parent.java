package com.mq.mqutils;

/**
 * @Title: Parent
 * @Package: com.mq.mqutils
 * @Description: TODO（添加描述）
 * @Author: tangquanbin
 * @Data: 2019/5/21 0021 22:52
 * @Version: V1.0
 */
public abstract class Parent {
    public void print1(){
        System.out.println("parent1");
    }
    private void print2(){
        System.out.println("parent2");
    }
    public abstract void print3();
}
