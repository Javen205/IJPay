### 前端后台以及游戏中使用google-protobuf详解

[TOC]

#### 0、什么是protoBuf

protoBuf是一种灵活高效的独立于语言平台的结构化数据表示方法，与XML相比，protoBuf更小更快更简单。你可以用定义自己protoBuf的数据结构，用ProtoBuf编译器生成特定语言的源代码，如C++，Java，Python等，目前protoBuf对主流的编程语言都提供了支持,非常方便的进行序列化和反序列化。

特点：

- 平台无关、语言无关。
- 二进制、数据自描述。
- 提供了完整详细的操作API。
- 高性能 比xml要快20-100倍
- 尺寸小 比xml要小3-10倍 高可扩展性
- 数据自描述、前后兼容 

#### 1、下载protobuf的编译器

[目前最新版本为Protocol Buffers v3.5.1](https://github.com/google/protobuf/releases) 

#### 2、配置环境变量

解压 `protoc-3.5.1-osx-x86_64.zip`

Mac 配置环境变量 `vi ~/.bash_profile` 使其配置生效`source ~/.bash_profile`

```json
#protobuf
export PROTOBUF_HOME=/Users/Javen/Documents/dev/java/protobuf/protoc-3.5.1-osx-x86_64
export PATH=$PATH:$PROTOBUF_HOME/bin
```

Window  将bin添加到path 即可 例如:`D:\protobuf\protoc-3.5.1-win32\bin`



`本文在Mac环境下编写`  **Mac**与**window**命令唯一的区别就是需要将`protoc`改成`protoc.exe` 前提是需要添加环境变量。



#### 3、编写一个proto文件

文件保存为`chat.proto` 此`proto`文件摘自[**t-io 让天下没有难开发的网络编程**](https://gitee.com/tywo45/t-io)

```protobuf
syntax = "proto3";
package com.im.common.packets;

option java_package = "com.im.common.packets";  //设置java对应的package
option java_multiple_files = true; //建议设置为true，这样会每个对象放在一个文件中，否则所有对象都在一个java文件中

/**
 * 聊天类型
 */
enum ChatType {
    CHAT_TYPE_UNKNOW = 0;//未知
    CHAT_TYPE_PUBLIC = 1;//公聊
    CHAT_TYPE_PRIVATE = 2;//私聊
}
/**
 * 聊天请求
 */
message ChatReqBody {
    int64 time = 1;//消息发送时间
    ChatType type = 2; //聊天类型
    string text = 3; //聊天内容
    string group = 4; //目标组id
    int32 toId = 5; //目标用户id，
    string toNick = 6; //目标用户nick
}

/**
 * 聊天响应
 */
message ChatRespBody {
    int64 time = 1;//消息发送时间
    ChatType type = 2; //聊天类型
    string text = 3; //聊天内容
    int32 fromId = 4; //发送聊天消息的用户id
    string fromNick = 5; //发送聊天消息的用户nick
    int32 toId = 6; //目标用户id
    string toNick = 7; //目标用户nick
    string group = 8; //目标组id
}
```





#### 4、编译器对其进行编译

##### 4.1 编译为Java

进入到项目的根目录执行以下编译命令，`proto文件`存放在`com/im/common/packets`包下，`com/im/common/packets`为`proto文件`中的包名。

```protobuf
protoc  --java_out=./  com/im/common/packets/chat.proto
```



##### 4.2 编译为JS

```protobuf
protoc --js_out=import_style=commonjs,binary:. chat.proto
```

执行后会在当前文件夹中生成`chat_pb.js` 文件，这里面就是`protobuf`的API和一些函数。如果是`Node.js` 就可以直接使用了，如果想在浏览器(前端)中使用`protobuf`还需要做一些处理。



#### 5、前端使用protobuf处理步骤

#####  5.1 npm安装需要的库

在`chat_pb.js`文件的同级目录下安装引用库

```json
npm install -g require
npm install -g browserify
npm install google-protobuf
```

##### 5.2 使用browserify对文件进行编译打包

编写脚本**保存为exports.js**

```javascript
var chatProto = require('./chat_pb');  
module.exports = {  
DataProto: chatProto  
}
```

**执行命令** `browserify exports.js > chat.js `对`chat_pb.js`文件进行编译打包生成`chat.js`后就可以愉快的使用了。



#### 6、protobuf使用示例

##### 6.1 前端(JavaScript)中使用protobuf

```html
<script src="./chat.js"></script>
<script type="text/javascript">
    var chatReqBody = new proto.com.im.common.packets.ChatReqBody();
    chatReqBody.setTime(new Date().getTime());
    chatReqBody.setText("测试");
    chatReqBody.setType(1);
    chatReqBody.setGroup("Javen");
    chatReqBody.setToid(666);
    chatReqBody.setTonick("Javen205");

    var bytes = chatReqBody.serializeBinary();  
    console.log("序列化为字节:"+bytes);
    var data = proto.com.im.common.packets.ChatReqBody.deserializeBinary(bytes); 
    console.log("反序列化为对象:"+data);  
    console.log("从对象中获取指定属性:"+data.getTonick());
    console.log("对象转化为JSON:"+JSON.stringify(data));  

</script>
```

##### 6.2 Java中使用protobuf

java中要用protobuf,protobuf与json相互转换，首先需要引入相关的jar，maven的pom坐标如下

```xml
<dependency>
    <groupId>com.google.protobuf</groupId>
    <artifactId>protobuf-java</artifactId>
    <version>3.5.1</version>
</dependency>
<dependency>
    <groupId>com.googlecode.protobuf-java-format</groupId>
    <artifactId>protobuf-java-format</artifactId>
    <version>1.4</version>
</dependency>
```



```java
public static void test() {
        try {
            JsonFormat jsonFormat = new JsonFormat();
            ChatRespBody.Builder builder = ChatRespBody.newBuilder();
            builder.setType(ChatType.CHAT_TYPE_PUBLIC);
            builder.setText("Javen 测试");
            builder.setFromId(1);
            builder.setFromNick("Javen");
            builder.setToId(110);
            builder.setToNick("Javen.zhou");
            builder.setGroup("Javen");
            builder.setTime(SystemTimer.currentTimeMillis());
            ChatRespBody chatRespBody = builder.build();
            //从protobuf转json
            String asJson = jsonFormat.printToString(chatRespBody);
            System.out.println("Object to json "+asJson);
            
            byte[] bodybyte = chatRespBody.toByteArray();
            //解码是从byte[]转换为java对象
            ChatRespBody parseChatRespBody = ChatRespBody.parseFrom(bodybyte);
            asJson = jsonFormat.printToString(parseChatRespBody);
            System.out.println("bodybyte to json "+asJson);
            
            //从json转protobuf
            ChatRespBody.Builder _builder = ChatRespBody.newBuilder();
            jsonFormat.merge(new ByteArrayInputStream(asJson.getBytes()), _builder);
            ChatRespBody _chatRespBody = _builder.build();
            asJson = jsonFormat.printToString(_chatRespBody);
            System.out.println("json to protobuf "+asJson);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```

##### 6.3 QQ玩一玩中使用protobuf

将`chat.js`中的`var global = Function('return this')();`修改为

```javascript
// var global = Function('return this')();

var global = (function(){
  return this;
})()

```

```javascript

BK.Script.loadlib('GameRes://qqPlayCore.js');
BK.Script.loadlib('GameRes://tio/chat.js');

function test() {
    var ws = new BK.WebSocket("ws://127.0.0.1:9326?group=test&name=Javen");
    ws.onOpen = function(ws) {
        BK.Script.log(1, 0, "onOpen.js");
        BK.Script.log(1, 0, "1.readyState = " + ws.getReadyState());

        var time = 0;
        BK.Director.ticker.add(function(ts, duration) {
            time = time + 1;
            if (time % 100 == 0) {
                // ws.send("phone test" + time);
                var chatReqBody = new proto.com.im.common.packets.ChatReqBody();
                chatReqBody.setTime(new Date().getTime());
                chatReqBody.setText("phone test" + time);
                chatReqBody.setType(1);
                chatReqBody.setGroup("test");
                var bytes = chatReqBody.serializeBinary();
                ws.send(bytes);
            }
        });
    };
    ws.onClose = function(ws) {
        BK.Script.log(1, 0, "onClose.js");
        BK.Script.log(1, 0, "1.readyState = " + ws.getReadyState());
    };
    ws.onError = function(ws) {
        BK.Script.log(1, 0, "onError.js");
        BK.Script.log(1, 0, "1.readyState = " + ws.getReadyState());
        BK.Script.log("onError.js.js getErrorCode:" + ws.getErrorCode());
        BK.Script.log("onError.js getErrorString:" + ws.getErrorString());
    };
    ws.onMessage = function(ws, event) {
        if (!event.isBinary) {
            var str = event.data.readAsString();
            BK.Script.log(1, 0, "text = " + str);
        } else {
            var buf = event.data;
            //将游标pointer重置为0
            buf.rewind();
            var ab = new ArrayBuffer(buf.length);
            var dv = new DataView(ab);
            while (!buf.eof) {
                dv.setUint8(buf.pointer, buf.readUint8Buffer());
            }
            var chatRespBody = proto.com.im.common.packets.ChatRespBody.deserializeBinary(ab);
            var msg = chatRespBody.getFromnick() + " 说: " + chatRespBody.getText();
            BK.Script.log(1, 0, "text = " + msg);
        }
    };
    ws.onSendComplete = function(ws) {
        BK.Script.log(1, 0, "onSendComplete.js");
    };
    ws.connect();
}

test();
```

##### 6.4 Eget中使用protobuf

###### 插件下载

egret有提供将`proto文件`生成JS以及TS的工具

```
npm install protobufjs -g
npm install @egret/protobuf -g
```

###### 操作步骤

1、在白鹭项目的根目录中新建`protobuf`文件夹,再在`protobuf`文件夹中新建`protofile`文件夹

2、将`proto`文件放到`protofile`文件夹中

3、依次执行`pb-egret add`、`pb-egret generate`

将会自动完成以下操作：

1、在`tsconfig.json`中的`include`节点中添加`protobuf/**/*.d.ts` 

2、在`egretProperties.json`中的`modules`节点添加

```json
{
"name": "protobuf-library",
"path": "protobuf/library"
},
{
"name": "protobuf-bundles",
"path": "protobuf/bundles"
}
```

3、在`protobuf`文件夹中自动生成`bundles`以及`library`文件夹里面包含了我们需要的js以及ts



###### 项目中能使用

> 处理发送消息

```typescript
 private sendReq(text:string,group:string){
        var chatReqBody = new com.im.common.packets.ChatReqBody();
        chatReqBody.time = new Date().getTime();
        chatReqBody.text = text;
        chatReqBody.type = com.im.common.packets.ChatType.CHAT_TYPE_PUBLIC;
        chatReqBody.group = group;
        let data = com.im.common.packets.ChatReqBody.encode(chatReqBody).finish();
        this.sendBytesData(data);
    }

    private sendBytesData(data:Uint8Array){
        this.socket.writeBytes(new egret.ByteArray(data));
    }
```

> 处理接收消息

```typescript
 private onReceiveMessage(e:egret.Event):void {

        //创建 ByteArray 对象
        var byte:egret.ByteArray = new egret.ByteArray();
        //读取数据
        this.socket.readBytes(byte);
        let buffer = new Uint8Array(byte.buffer);
        let chatRespBody =  com.im.common.packets.ChatRespBody.decode(buffer);
        
        // this.trace("收到数据:"+JSON.stringify(chatRespBody));
        this.trace(chatRespBody.fromNick+" 说: "+chatRespBody.text);
    }
```

到这里如何使用protobuf就介绍完了，个人能力有限如有错误欢迎指正。你有更好的解决方案或者建议欢迎一起交流讨论，如有疑问欢迎留言。