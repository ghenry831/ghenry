package com.lambda.demo1;

public class LambdaTest {
    public static void main(String[] args) {

        //1.子类实现
        Factory factory = new SubClass(); //多态
        User user1 = (User) factory.getObject();
        System.out.println(user1);


        //2.匿名内部类
        factory = new Factory() {
            @Override
            public Object getObject() {
                return new User("GT",11);
            }
        };
        User user2 = (User)factory.getObject();
        System.out.println(user2);

        //3.lambda
        factory = () -> {
            return new User("aaaaa",25);
        };
        User user3 = (User)factory.getObject();
        System.out.println(user3);


        //lambda作为参数进行传递
        User user4 = getUserFormFactory(() ->{
            return new User("bb",33);
        },"User");
        System.out.println(user4);

        //lambda作为函数的返回值
        factory = getFactory();
        System.out.println(factory.getObject());
    }

    public static User getUserFormFactory(Factory factory,String beanName){
        Object obj = factory.getObject();

        if (obj != null && obj.getClass().getSimpleName().equals(beanName)){
            return (User) obj;
        }
        return null;
    }

    public static Factory getFactory(){
        return () ->{
          return  new User("ggggg",30);
        };
    }
}
