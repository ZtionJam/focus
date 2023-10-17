'use strict'

import { app, protocol, BrowserWindow, ipcMain, Tray, Menu, nativeImage, Notification } from 'electron'
import { createProtocol } from 'vue-cli-plugin-electron-builder/lib'
const path = require('path')

const isDevelopment = process.env.NODE_ENV !== 'production'
app.dock.hide()
protocol.registerSchemesAsPrivileged([
  { scheme: 'app', privileges: { secure: true, standard: true } }
])
let win;
let sureClose = false;
async function createWindow() {
  win = new BrowserWindow({
    width: 400,
    height: 600,
    backgroundColor: '#00000000',
    visible: false,
    resizable: false,
    webPreferences: {
      nodeIntegration: process.env.ELECTRON_NODE_INTEGRATION,
      contextIsolation: !process.env.ELECTRON_NODE_INTEGRATION
    }
  })
  //开发配置
  if (process.env.WEBPACK_DEV_SERVER_URL) {
    await win.loadURL(process.env.WEBPACK_DEV_SERVER_URL)
    if (!process.env.IS_TEST) win.webContents.openDevTools()
  } else {
    createProtocol('app')
    win.hide()
    win.loadURL('app://./index.html')
  }
  win.once('ready-to-show', () => {
    win.hide()
  })
  win.on('close', (e) => {
    if (!sureClose) {
      e.preventDefault();
    }
    win.hide()
  })
}
ipcMain.on('notice', (e,msg) => {
  if (Notification.isSupported()) {
    const notification = new Notification({
      title: msg.title, subtitle: msg.subtitle,
      body: msg.body
    });
    notification.show()
    notification.on('click', () => {
      win.show()
    })
  }
})
app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit()
  }
})

app.on('activate', () => {
  if (BrowserWindow.getAllWindows().length === 0) createWindow()
})
app.on('ready', async () => {
  createWindow();
  //菜单栏图标
  if (process.platform === 'darwin') {
    const icon = nativeImage.createFromDataURL(logo);
    let tray = new Tray(icon);
    const contextMenu = Menu.buildFromTemplate([
      {
        label: '打开主页面', click: () => {
          win.show()
        }
      },
      {
        label: '配置', click: () => {
        }
      },
      { type: 'separator' },
      {
        label: '关闭应用', click: () => {
          sureClose = true;
          app.quit()
        }
      }
    ])
    tray.setToolTip('焦距'); // 鼠标悬停时显示的提示文字
    tray.setContextMenu(contextMenu);
  }
})

if (isDevelopment) {
  if (process.platform === 'win32') {
    process.on('message', (data) => {
      if (data === 'graceful-exit') {
        app.quit()
      }
    })
  } else {
    process.on('SIGTERM', () => {
      app.quit()
    })
  }
}
let logo = `data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAKpJREFUOE9jZMACXO6c/I9NfI+KOSO6OIoALo3ompANghtArGaYYTBDwAZg07xb2QzFYte7pzB8BTIEw4AdSqYMzBBhDNDy4g7Dwa/v4OJgA9BtR7cZ3RR0l6AYANN870c/VhcocRSCxZENwWoAVt1IggQNGDgXgFwJCgeSXICeDgjFAnIgYk0HsLDClpBgaQQWiHADcKVGfLGBkpRhConND1gzE7JtpGRnALWuZoGX/RVSAAAAAElFTkSuQmCC`;