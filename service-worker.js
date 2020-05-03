/**
 * Welcome to your Workbox-powered service worker!
 *
 * You'll need to register this file in your web app and you should
 * disable HTTP caching for this file too.
 * See https://goo.gl/nhQhGp
 *
 * The rest of the code is auto-generated. Please don't update this file
 * directly; instead, make changes to your Workbox build configuration
 * and re-run your build process.
 * See https://goo.gl/2aRDsh
 */

importScripts("https://storage.googleapis.com/workbox-cdn/releases/4.3.1/workbox-sw.js");

self.addEventListener('message', (event) => {
  if (event.data && event.data.type === 'SKIP_WAITING') {
    self.skipWaiting();
  }
});

/**
 * The workboxSW.precacheAndRoute() method efficiently caches and responds to
 * requests for URLs in the manifest.
 * See https://goo.gl/S9QRab
 */
self.__precacheManifest = [
  {
    "url": "404.html",
    "revision": "5847b3f8dcddeb89b43468807bc74e76"
  },
  {
    "url": "assets/css/0.styles.a7c1ccb7.css",
    "revision": "02a0cdff8c18b7d0120be8aee6fcf10b"
  },
  {
    "url": "assets/fonts/element-icons.535877f5.woff",
    "revision": "535877f50039c0cb49a6196a5b7517cd"
  },
  {
    "url": "assets/fonts/element-icons.732389de.ttf",
    "revision": "732389ded34cb9c52dd88271f1345af9"
  },
  {
    "url": "assets/img/search.83621669.svg",
    "revision": "83621669651b9a3d4bf64d1a670ad856"
  },
  {
    "url": "assets/js/10.c0be62b6.js",
    "revision": "f12aa948eeeadf8b8a727d327bc94a2e"
  },
  {
    "url": "assets/js/11.140f3287.js",
    "revision": "f7a10636350cd6913acb9b37a80b00bc"
  },
  {
    "url": "assets/js/12.89e86f55.js",
    "revision": "a3d6c063ffec6c3b93a42bcbd2882333"
  },
  {
    "url": "assets/js/13.022079b1.js",
    "revision": "b51120dd2944126dbf85ab7433e8c655"
  },
  {
    "url": "assets/js/14.02e28452.js",
    "revision": "345422192637b0668cbfe664138922eb"
  },
  {
    "url": "assets/js/15.b961770d.js",
    "revision": "10451125c4b0974ae1f1c171c647966c"
  },
  {
    "url": "assets/js/16.90c975be.js",
    "revision": "bd929ca91327f71976b71e9c0cbcdbf9"
  },
  {
    "url": "assets/js/17.38470b55.js",
    "revision": "c2abc6220e71ea89c684cf9b1a12704d"
  },
  {
    "url": "assets/js/18.3640af31.js",
    "revision": "88c1d0cfdb101cadbe4d7a5da4f9aaae"
  },
  {
    "url": "assets/js/19.ebbc83a7.js",
    "revision": "47a71a0b4312a761a1d12c59692a11dd"
  },
  {
    "url": "assets/js/20.734acbfc.js",
    "revision": "0c15463ba018f95d8ee48750b480d8ed"
  },
  {
    "url": "assets/js/21.3b4e9c88.js",
    "revision": "923e5b0e269d2684fc3058cfe154e41c"
  },
  {
    "url": "assets/js/22.d3dc0ee1.js",
    "revision": "d8661d7241aa77bb16687e42d79d1277"
  },
  {
    "url": "assets/js/23.c093aa3d.js",
    "revision": "8d7ed9215698001139b73237995c955e"
  },
  {
    "url": "assets/js/24.6d9442c9.js",
    "revision": "c3baaf9924dad7e1395e495bb367f7d5"
  },
  {
    "url": "assets/js/25.2ff45f43.js",
    "revision": "0d43f74ce6ff829172773a2d27204f80"
  },
  {
    "url": "assets/js/26.2e19ef88.js",
    "revision": "f2cbb2e4bcb820640cbf36c9f85fb913"
  },
  {
    "url": "assets/js/27.8585814f.js",
    "revision": "114e9fe59e6ca6278de915a6eb583dda"
  },
  {
    "url": "assets/js/28.a87411fc.js",
    "revision": "97dd4faa5dfd5a0c0164397048c0c168"
  },
  {
    "url": "assets/js/29.ca02bc32.js",
    "revision": "656124c85a42aa08b655f2fdf8455f96"
  },
  {
    "url": "assets/js/3.752f6f58.js",
    "revision": "fc45af48fec7ed2cf2d8d4da5638b672"
  },
  {
    "url": "assets/js/30.b059bd2b.js",
    "revision": "00aa5e4df9632c1d9b5f44df414a2e12"
  },
  {
    "url": "assets/js/31.26bf9761.js",
    "revision": "c905316831a2724f3fd89caa6dfbbd9b"
  },
  {
    "url": "assets/js/4.a223725b.js",
    "revision": "3b8176eaabc08d60b2fb5a00ca0bce09"
  },
  {
    "url": "assets/js/5.07ef0910.js",
    "revision": "7c0d8d264d547f3bfd159f80d044bed6"
  },
  {
    "url": "assets/js/6.b87e53f3.js",
    "revision": "ddfd28dfc4475244d6253391bb2aa8c0"
  },
  {
    "url": "assets/js/7.715c919a.js",
    "revision": "0ecc67b0935fea31d3d5077d6f641ad8"
  },
  {
    "url": "assets/js/8.87726df5.js",
    "revision": "b175a159762441d6d69ec5fa399e7b54"
  },
  {
    "url": "assets/js/9.6a21e7ef.js",
    "revision": "c4c6090f1b4f60102041d4c6ebb2945b"
  },
  {
    "url": "assets/js/app.892108df.js",
    "revision": "502c35e636e7b2c15787b84c2e061a33"
  },
  {
    "url": "assets/js/vendors~notification.edc232ee.js",
    "revision": "659ad69b694aee38af7f8307247a0199"
  },
  {
    "url": "guide/alipay/extension.html",
    "revision": "789bd11a3cfd82a8bf1c95d2fde8bda7"
  },
  {
    "url": "guide/alipay/index.html",
    "revision": "8b9581af9daa96c9040cb3eda80e9a24"
  },
  {
    "url": "guide/alipay/init.html",
    "revision": "169de6599c70783e9b86d2519a775fea"
  },
  {
    "url": "guide/client/ios.html",
    "revision": "7c9914dd801d6a6ca89920adefcbb059"
  },
  {
    "url": "guide/client/jpay.html",
    "revision": "55cc98836228400cb987478efcfd287d"
  },
  {
    "url": "guide/config/alipay_config.html",
    "revision": "0209941aa3456c5f4781485d14f131ca"
  },
  {
    "url": "guide/config/weixinpay_config.html",
    "revision": "e1e6254613173f9a995e1826835ac752"
  },
  {
    "url": "guide/donate/index.html",
    "revision": "45f626f8c80757d5a19c6a7bda0da764"
  },
  {
    "url": "guide/http.html",
    "revision": "dbf3f809183393451583e41a57cdd550"
  },
  {
    "url": "guide/index.html",
    "revision": "591b8031ac1491551dee679b713559fe"
  },
  {
    "url": "guide/jdpay/index.html",
    "revision": "627d447323fdfeef9a465cd74607a67a"
  },
  {
    "url": "guide/maven.html",
    "revision": "04e3f1d69aa1f7206674a3fb45fbbd8b"
  },
  {
    "url": "guide/paypal/index.html",
    "revision": "da332d9d837ace665a3b88887b653a22"
  },
  {
    "url": "guide/qqpay/index.html",
    "revision": "13b9a50bcf4b00c5a35460e3c9a4f721"
  },
  {
    "url": "guide/resource.html",
    "revision": "cb14e2cc0b397f9215818cc9d9c47789"
  },
  {
    "url": "guide/tools/frp/index.html",
    "revision": "cbe3683b2e51261cafb27d70dafeb752"
  },
  {
    "url": "guide/unionpay/index.html",
    "revision": "42e96e8841bed13a43b4e3212b06cd41"
  },
  {
    "url": "guide/weixin/tnwx.html",
    "revision": "e060b5ac0478e88d3309e8f03e4fd2bd"
  },
  {
    "url": "guide/weixin/weixin_guide.html",
    "revision": "1d78122b5e8d9b39ac8108ecbbb549ea"
  },
  {
    "url": "guide/wxpay/api-v3.html",
    "revision": "5605bd4b03937f4e4737a342289e075a"
  },
  {
    "url": "guide/wxpay/external.html",
    "revision": "2a1426ffae9b3af393578dbf09e1726a"
  },
  {
    "url": "guide/wxpay/index.html",
    "revision": "a8157654219a8b14c51bfa00b475dc1e"
  },
  {
    "url": "guide/wxpay/question.html",
    "revision": "395ab4ebe315e1e1b80285d1e9801991"
  },
  {
    "url": "index.html",
    "revision": "c516c41f6ed953f12d3b78454b6433e2"
  },
  {
    "url": "wxpay.jpeg",
    "revision": "311a9f9b98f805954aeb9711c2b7959f"
  },
  {
    "url": "wxpay.png",
    "revision": "f8f237b08107f485bcb03b3937ec6a08"
  }
].concat(self.__precacheManifest || []);
workbox.precaching.precacheAndRoute(self.__precacheManifest, {});
addEventListener('message', event => {
  const replyPort = event.ports[0]
  const message = event.data
  if (replyPort && message && message.type === 'skip-waiting') {
    event.waitUntil(
      self.skipWaiting().then(
        () => replyPort.postMessage({ error: null }),
        error => replyPort.postMessage({ error })
      )
    )
  }
})
