const { defineConfig } = require('@vue/cli-service')
const NodePolyfillPlugin = require('node-polyfill-webpack-plugin')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    plugins: [new NodePolyfillPlugin()]
  },
  pluginOptions: {
    electronBuilder: {
      nodeIntegration: true,
      builderOptions: {
        productName: "焦距",
        appId: "cn.ztion.focus",
        copyright: "ZtionJam",
        nsis: {
          oneClick: false,
          allowElevation: true,
          allowToChangeInstallationDirectory: true,
          createDesktopShortcut: true,
          createStartMenuShortcut: true,
          shortcutName: "焦距"
        },
        publish: {
          provider: "generic",
          url: "http://ztion.cn/update"
        },
        win: {
          icon: './public/icon.ico'
        },
        mac: {
          icon: './public/icon_512x512.ico'
        }
      }
    }
  }
})
