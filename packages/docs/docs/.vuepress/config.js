module.exports = ctx => ( {
    base:'/IJPay/',
    locales: {
        '/': {
            lang: 'zh-CN',
            title: 'IJPay',
            description: 'IJPay 让支付触手可及',
        },
        // '/en/': {
        //     lang: 'en-US',
        //     title: 'IJPay',
        //     description: 'IJPay 让支付触手可及'
        // }
    },
    markdown: {
        lineNumbers: true,
    },
    head: [
        ['link', { rel: 'icon', href: `/logo.png` }],
        ['link', { rel: 'manifest', href: '/manifest.json' }],
        ['meta', { name: 'theme-color', content: '#3eaf7c' }],
        ['meta', { name: 'apple-mobile-web-app-capable', content: 'yes' }],
        ['meta', { name: 'apple-mobile-web-app-status-bar-style', content: 'black' }],
        ['link', { rel: 'apple-touch-icon', href: `/icons/apple-touch-icon-152x152.png` }],
        ['link', { rel: 'mask-icon', href: '/icons/safari-pinned-tab.svg', color: '#3eaf7c' }],
        ['meta', { name: 'msapplication-TileImage', content: '/icons/msapplication-icon-144x144.png' }],
        ['meta', { name: 'msapplication-TileColor', content: '#000000' }]
    ],
    themeConfig: {
        repo: 'javen205/IJPay',
        editLinks: true,
        docsDir: 'packages/docs/docs',
        locales: {
            '/': {
                label: '简体中文',
                selectText: '选择语言',
                editLinkText: '在 GitHub 上编辑此页',
                lastUpdated: '上次更新',
                nav: require('./nav/zh'),
                sidebar: {
                    '/guide/': getGuideSidebar('客户端','微信公众号开发脚手架',
                        'IJPay 让支付触手可及','获取支付配置','支付宝支付','微信支付',
                        'QQ 钱包支付','银联支付','京东支付'),
                },
            },
            // '/en/': {
            //     label: 'English',
            //     selectText: 'Languages',
            //     editLinkText: 'Edit this page on GitHub',
            //     lastUpdated: 'Last Updated',
            //     nav: require('./nav/en'),
            //     sidebar: {
            //         '/en/guide/': getGuideSidebar('指南', '深入'),
            //     },
            // }
        },
    },
    plugins: [
        ['@vuepress/back-to-top', true],
        ['@vuepress/pwa', {
            serviceWorker: true,
            updatePopup: true
        }],
    ],
    extraWatchFiles: [
        '.vuepress/nav/en.js',
        '.vuepress/nav/zh.js',
    ]
})

function getGuideSidebar (groupA, groupB,groupC,groupD,groupE,groupF,groupG,groupH,groupI) {
    return [
        {
            title: groupA,
            collapsable: false,
            children: [
                'client/jpay',
                'client/ios',
            ]
        },
        {
            title: groupB,
            collapsable: false,
            children: [
                'weixin/tnw',
                'weixin/weixin_guide',
            ]
        },
        {
            title: groupC,
            collapsable: false,
            children: [
                '',
                'maven',
            ]
        },
        {
            title: groupD,
            collapsable: false,
            children: [
                'config/alipay_config',
                'config/weixinpay_config',
            ]
        },
        {
            title: groupE,
            collapsable: false,
            children: [
                'alipay/',
                'alipay/init',
                // 'alipay/h5',
                // 'alipay/pc',
            ]
        },
        {
            title: groupF,
            collapsable: false,
            children: [
                'wxpay/',
            ]
        },
    ]
}





