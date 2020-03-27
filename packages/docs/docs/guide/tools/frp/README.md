# FRP

## 什么是FRP

frp 是一个可用于内网穿透的高性能的反向代理应用，支持 tcp, udp, http, https 协议。主要他是一个开源的。

[https://github.com/fatedier/frp](https://github.com/fatedier/frp)

具体配置以及使用方法我这里就不过多介绍了，官方文档有详细说明。

> 本篇文章目标：
> - 自定义二级域名做本地端口映射 
> - 要求支持80端来做微信开发调试 
> - 能查看端口的连接状态

## 下载最新版本客户端

- Mac [darwin_amd64.tar.gz
](https://github.com/fatedier/frp/releases)
- Centos [linux_amd64.tar.gz](https://github.com/fatedier/frp/releases)

**这里我贴出我的配置内容如下：**

## 服务端端配置 frps.ini

假如服务端的IP地址为：121.35.99.12

```shell
[common]
bind_port = 7000 
vhost_http_port = 9988 #由于80端口已暂用这里我们使用Nginx做端口映射到80端口来做微信开发的调试，如何映射后文会介绍
#连接池
max_pool_count = 5
#token验证
privilege_token = javen
#自定义二级域名
subdomain_host = frp.javen.com
#控制面板
dashboard_port = 9999
dashboard_user = javen
dashboard_pwd = javen
#日志
log_file = ./frps.log
log_level = info
log_max_days = 3
```

## 客户端配置 frpc.ini

```shell
[common]
server_addr = 121.35.99.12 # 服务器IP
server_port = 7000 # 服务器bind_port
privilege_token = javen

[wx]
type = http
local_port = 8080 # 映射到本地的8080端口
subdomain = wx

# 如果不使用SSH可以将其注释掉
[ssh]
type = tcp
local_ip = 127.0.0.1
local_port = 22
remote_port = 6000
```

## 自定义二级域名

在多人同时使用一个 frps 时，通过自定义二级域名的方式来使用会更加方便。

通过在 frps 的配置文件中配置 subdomain_host，就可以启用该特性。之后在 frpc 的 http、https 类型的代理中可以不配置 custom_domains，而是配置一个 subdomain 参数。

只需要将 *.{subdomain_host} 解析到 frps 所在服务器。之后用户可以通过 subdomain 自行指定自己的 web 服务所需要使用的二级域名，通过 {subdomain}.{subdomain_host} 来访问自己的 web 服务。

**假如域名为：abc.com 去域名的控制面板添加解析 `*.frp` 到 121.35.99.12**

## 客户端访问

http://wx.frp.javen.com:9988/user 映射到本地的 http://localhost:8080/user

## Dashboard

通过浏览器查看 frp 的状态以及代理统计信息展示。

[http://121.35.99.12:9999](http://121.35.99.12:9999/) 登录的用户名以及密码为服务端配置的 dashboard_user = javen dashboard_pwd = javen


<img :src="$withBase('/frp.png')" alt="frp dashboard">


## 端口映射

这里我们**使用Nginx将9988端口映射到80端口**供微信开发调试使用

```shell{63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83}
#user  nobody;
worker_processes  2;
worker_cpu_affinity 01 10;
#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #'$status $body_bytes_sent "$http_referer" '
    #'"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;
	
	upstream wx {
	   ip_hash;
	   server localhost:8080 weight=1 max_fails=3 fail_timeout=60s;
     server localhost:8088 weight=1 max_fails=3 fail_timeout=60s;
	}
	
	server {
		listen       80;
		server_name  localhost;
		access_log  /home/nginxlog/wx_access.log;

		location / {
			proxy_redirect          off;
			proxy_set_header Host $host:$server_port;
			proxy_set_header X-Forwarded-For $remote_addr;
			client_max_body_size      20m;
			client_body_buffer_size 128k;
			proxy_connect_timeout   600;
			proxy_send_timeout      600;
			proxy_read_timeout      900;
			proxy_buffer_size       4k;
			proxy_buffers           4 32k;
			proxy_busy_buffers_size 64k;
			proxy_temp_file_write_size 64k;
			proxy_pass http://wx;
		}

	}

  server {
    listen       80;
    server_name  *.frp.javen.com;
    access_log  /home/nginxlog/frp_access.log;

    location / {
    proxy_redirect          off;
    proxy_set_header Host $host:$server_port;
    proxy_set_header X-Forwarded-For $remote_addr;
    client_max_body_size      20m;
    client_body_buffer_size 128k;
    proxy_connect_timeout   600;
    proxy_send_timeout      600;
    proxy_read_timeout      900;
    proxy_buffer_size       4k;
    proxy_buffers           4 32k;
    proxy_busy_buffers_size 64k;
    proxy_temp_file_write_size 64k;
    proxy_pass http://127.0.0.1:9988/;
  }
}
	
	server {
		listen       8888;
		server_name  localhost;
		access_log   /home/nginxlog/static_access.log;

		location ~ .*\.(gif|jpg|jpeg|bmp|png|ico|txt|js|css|apk)$
		{
			root /home/ftp/private; 
			expires 7d; 
		}
	}
}
```