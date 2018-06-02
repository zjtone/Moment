# Moment
一个基于Java的服务器，支持自定义协议。

做了这么久开发，也没造出几个轮子。现在又再次重新造轮子，修改了之前的写法和架构，重新造了一个功能能加丰富的轮子。

这个服务器是基于Java ServerSocket的，BIO的缺点比较多，现在还使用BIO的也少见了，不过还是想从BIO入手进行编写，BIO还是可以做的很强大的，后面再使用NIO、AIO来修改。

### Moment的使用

首先是怎么用的问题，使用如下：
```
        try {
            int[] ports = new int[]{8080, 8888, 8880, 8088};
            Moment moment = new Moment(ports);
            HTTPHandler httpHandler = new HTTPHandler();
            httpHandler.defaultApi(new Api() {
                @Override
                public void response(HTTPRequest httpRequest, HTTPResponse httpResponse) throws IOException {
                }
            }).get("/", new Api() {
                @Override
                public void response(HTTPRequest httpRequest, HTTPResponse httpResponse) throws IOException {
                }
            });
            moment.handler(httpHandler).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
```

给Moment提供端口列表，会自动选择其中的第一个没有被绑定的进行绑定。
通过给Moment传入不用的Handler（继承于IHandler）就可以支持不同的协议。里面现在放了一个HTTP协议的Handler，可以支持HTTP协议。

本文到此可以结束了，后面是稍微介绍一下HTTPHandler的使用。

### HTTPHandler的使用

HTTPHandler采用了RestFulApi的写法，只要通过get或者post向里面传入api和对应的处理方法，在接受到相应的api时就会自动调用处理方法来进行处理，处理方法是基于接口来实现的，继承Api接口，在高版本的java可以使用函数式编程的方法传递参数。

为了处理复杂的api情况，则可以通过HTTPHandler的defaultApi方法传入默认使用的Api方法，由程序员自己定义使用的方法。

response内使用两个参数：HTTPRequest和HTTPResponse，分别对应请求和响应。

### 总结

总体而言，这个库的使用上是非常方便的，希望能通过不断的维护升级，来使得这个库更加强大。
